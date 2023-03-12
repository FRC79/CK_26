// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.*;
import frc.robot.physics.PivotController;
import frc.robot.subsystems.Pivot;

public class PivotTeleop extends CommandBase {

  private Pivot m_pivot;
  private GenericHID m_gamepad;
  private PivotController m_controller;

  private boolean faulted = false;
  
  /** Creates a new PivotTeleop. */
  public PivotTeleop(Pivot subsystem, PivotController controller, GenericHID operator_joystick) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
    m_controller = controller;
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

    if (m_controller == null) {
      System.out.println("Should have controller");
    }

    // if (!(-5 < revs && revs <= 5)) {
    //     // Must have a proper starting position towards low end of the robot.
    //     System.out.println("FAULTED, POSITION INVALID");
    //     faulted = true;
    //     return;
    // }

    // if (!(-0.01 < rpm && rpm <= 0.01)) {
    //     // Should be fairly stationary at start.
    //     System.out.println("FAULTED, VELOCITY INVALID");
    //     faulted = true;
    //     return;
    // }
   }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (faulted) {
        return;
    }

    double joystick_motor_power_towards_back = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON) ? PivotController.MAX_MOTOR_VALUE : 0.0;
    double joystick_motor_power_towards_front = m_gamepad.getRawButton(OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON) ? -PivotController.MAX_MOTOR_VALUE : 0.0;
    
    // Calculate the control law and apply it to the motor.
    double motor_power = m_controller.controlLaw(joystick_motor_power_towards_front, joystick_motor_power_towards_back);
    m_pivot.setMotorPower(motor_power);
  }

  public boolean getFaulted() {
    return faulted;
  }

  public double getCommandedValue() {
    if (m_controller == null) {
      return 0.1234;
    }
    return m_controller.getCommandedValue();
  }

  public double getCushionMotorPower() {
    if (m_controller == null) {
      return 0.1234;
    }
    return m_controller.getCushionMotorPower();
  }

  public double getTotalMotorCommanded() {
    return m_controller.getTotalMotorCommanded();
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
