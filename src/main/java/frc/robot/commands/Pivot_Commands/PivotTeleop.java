// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.*;
import frc.robot.subsystems.Pivot;

public class PivotTeleop extends CommandBase {

  private Pivot m_pivot;
  private GenericHID m_gamepad;
  
  private static final double MAX_ENCODER_VALUE = 43.7; // revolutions of the drive axel
  private static final double MIN_ENCODER_VALUE = 0.0; // starting location when the pivot is stored in the robot.
  private static final double MIN_ABS_RPM_CUSHION = 10.0; // RPM
  public static final double UPRIGHT_PIVOT_VALUE = 24.547; // the value of the encoder when the pivot is at the top of rotation.
  private static final double UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE = 14.547;
  private static final double UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE = 3.0;
  private static final double MAX_MOTOR_VALUE = 0.3;
  private static final double DAMPENER_CONSTANT = 0.00001 * 0.2;
  private static final double MAX_CUSHION_OUTPUT_VALUE = 0.1;
  private static final double FRONT_CUSHION_OUTPUT_SCALING = 0.5;
  private static final double MAX_TOTAL_MOTOR_VALUE = 0.3;
  private static final double RPM_CUSHION_TOLERANCE = 10.0; // RPM
  private boolean faulted = false;
  private double commanded_value = 0.0;
  private double joystick_total_value = 0.0;
  private double cushion_value = 0.0;

// Case 1) 

  /** Creates a new PivotTeleop. */
  public PivotTeleop(Pivot subsystem, GenericHID operator_joystick) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
    m_gamepad = operator_joystick;
    addRequirements(m_pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() { 
    double revs = m_pivot.getEncoder().getPosition();
    double rpm = m_pivot.getEncoder().getVelocity();

    if (m_gamepad == null) {
        System.out.println("Should have gamepad");
        faulted = true;
        return;
    }

    // if (!(-5 < revs && revs <= 5)) {
    //     // Must have a proper starting position towards low end of the robot.
    //     System.out.println("FAULTED, POSITION INVALID");
    //     faulted = true;
    //     return;
    // }

    if (!(-0.01 < rpm && rpm <= 0.01)) {
        // Should be fairly stationary at start.
        System.out.println("FAULTED, VELOCITY INVALID");
        faulted = true;
        return;
    }
   }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (faulted) {
        return;
    }

    double revs = m_pivot.getEncoder().getPosition();
    double rpm = m_pivot.getEncoder().getVelocity();

    double cushion_motor_power = 0.0;
    double joystick_motor_power_towards_back = 0.0;
    double joystick_motor_power_towards_front = 0.0;

    if (revs < UPRIGHT_PIVOT_VALUE && rpm < -RPM_CUSHION_TOLERANCE) {
        // Case 1: Front of robot, falling towards front.
        if (m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON)) {
            cushion_motor_power = 0.0;
            joystick_motor_power_towards_back = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON) ? MAX_MOTOR_VALUE : 0.0;
            joystick_motor_power_towards_front = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON) ? -MAX_MOTOR_VALUE : 0.0;
        } else {
            joystick_motor_power_towards_back = 0.0;
            joystick_motor_power_towards_front = 0.0;
            cushion_motor_power = MathUtil.clamp(-DAMPENER_CONSTANT * rpm, -MAX_CUSHION_OUTPUT_VALUE * FRONT_CUSHION_OUTPUT_SCALING, MAX_CUSHION_OUTPUT_VALUE * FRONT_CUSHION_OUTPUT_SCALING);
        }
    } else if (revs < UPRIGHT_PIVOT_VALUE && rpm >= -RPM_CUSHION_TOLERANCE) {
        // Case 2: Front of robot, moving upward toward back.
        cushion_motor_power = 0.0;
        joystick_motor_power_towards_back = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON) ? MAX_MOTOR_VALUE : 0.0;
        joystick_motor_power_towards_front = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON) ? -MAX_MOTOR_VALUE : 0.0;
    } else if (revs >= UPRIGHT_PIVOT_VALUE && rpm > RPM_CUSHION_TOLERANCE) {
        // Case 3: Back of robot, falling towards back.
        if (m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON)) {
          cushion_motor_power = 0.0;
          joystick_motor_power_towards_back = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON) ? MAX_MOTOR_VALUE : 0.0;
          joystick_motor_power_towards_front = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON) ? -MAX_MOTOR_VALUE : 0.0;
        } else {
          joystick_motor_power_towards_back = 0.0;
          joystick_motor_power_towards_front = 0.0;
          cushion_motor_power = MathUtil.clamp(-DAMPENER_CONSTANT * rpm, -MAX_CUSHION_OUTPUT_VALUE, MAX_CUSHION_OUTPUT_VALUE);
        }
    } else {
        // Case 4: Back of robot, moving up towards front.
        cushion_motor_power = 0.0;
        joystick_motor_power_towards_back = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON) ? MAX_MOTOR_VALUE : 0.0;
        joystick_motor_power_towards_front = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON) ? -MAX_MOTOR_VALUE : 0.0;
    }

    // If we're near the top, just let joystick motor commands override.
    double deadzone_limit_back = UPRIGHT_PIVOT_VALUE + UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE;
    double deadzone_limit_front = UPRIGHT_PIVOT_VALUE - UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE;
    if (deadzone_limit_front <= revs && revs <= deadzone_limit_back) {
      cushion_motor_power = 0.0;
      joystick_motor_power_towards_back = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON) ? MAX_MOTOR_VALUE : 0.0;
      joystick_motor_power_towards_front = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON) ? -MAX_MOTOR_VALUE : 0.0;
    }

    cushion_value = cushion_motor_power;
    joystick_total_value = joystick_motor_power_towards_back + joystick_motor_power_towards_front;

    double total_power = cushion_motor_power + joystick_motor_power_towards_back + joystick_motor_power_towards_front;
    double clamped_total_power = MathUtil.clamp(total_power, -MAX_TOTAL_MOTOR_VALUE, MAX_TOTAL_MOTOR_VALUE);
    m_pivot.setMotorPower(clamped_total_power);
    // m_pivot.setMotorPower(0);
    commanded_value = clamped_total_power;
  }

  public boolean getFaulted() {
    return faulted;
  }

  public double getCommandedValue() {
    return commanded_value;
  }

  public double getCushionMotorPower() {
    return cushion_value;
  }

  public double getJoystickTotalPower() {
    return joystick_total_value;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_pivot.setMotorPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return faulted;
  }
}
