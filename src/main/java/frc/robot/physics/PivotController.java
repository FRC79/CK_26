package frc.robot.physics;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.*;
import frc.robot.subsystems.Pivot;

public class PivotController {

  private Pivot m_pivot;
  
  private static final double MAX_ENCODER_VALUE = 43.7; // revolutions of the drive axel
  private static final double MIN_ENCODER_VALUE = 0.0; // starting location when the pivot is stored in the robot.
  private static final double MIN_ABS_RPM_CUSHION = 10.0; // RPM
  public static final double UPRIGHT_PIVOT_VALUE = 24.547; // the value of the encoder when the pivot is at the top of rotation.
  private static final double UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE = 7.547;
  private static final double UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE = 1.0;
  public static final double MAX_MOTOR_VALUE = 0.2;
  private static final double DAMPENER_CONSTANT = 0.00001;
  private static final double MAX_CUSHION_OUTPUT_VALUE = 0.2;
  private static final double FRONT_CUSHION_OUTPUT_SCALING = 0.75;
  private static final double MAX_TOTAL_MOTOR_VALUE = MAX_CUSHION_OUTPUT_VALUE;
  private static final double RPM_CUSHION_TOLERANCE = 10.0; // RPM
  private boolean faulted = false;
  private double commanded_value = 0.0;
  private double total_motor_commanded = 0.0;
  private double cushion_value = 0.0;

  /** Creates a new PivotController. */
  public PivotController(Pivot subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
  }

//   public bool isStartStatusOk() { 
//     double revs = m_pivot.getEncoder().getPosition();
//     double rpm = m_pivot.getEncoder().getVelocity();

//     // if (!(-5 < revs && revs <= 5)) {
//     //     // Must have a proper starting position towards low end of the robot.
//     //     System.out.println("FAULTED, POSITION INVALID");
//     //     faulted = true;
//     //     return false;
//     // }

//     // if (!(-0.01 < rpm && rpm <= 0.01)) {
//     //     // Should be fairly stationary at start.
//     //     System.out.println("FAULTED, VELOCITY INVALID");
//     //     faulted = true;
//     //     return false;
//     // }

//     return true;
//    }

  public double controlLaw(double forward_motor_request, double backward_motor_request) {
    if (faulted) {
        return 0.0;
    }

    double revs = m_pivot.getEncoder().getPosition();
    double rpm = m_pivot.getEncoder().getVelocity();

    double cushion_motor_power = 0.0;
    double back_motor_commanded = 0.0;
    double front_motor_commanded = 0.0;

    if (revs < UPRIGHT_PIVOT_VALUE && rpm < -RPM_CUSHION_TOLERANCE) {
        // Case 1: Front of robot, falling towards front.
        if (Math.abs(backward_motor_request) > 0) {
            cushion_motor_power = 0.0;
            back_motor_commanded = backward_motor_request;
            front_motor_commanded = forward_motor_request;
        } else {
            back_motor_commanded = 0.0;
            front_motor_commanded = 0.0;
            cushion_motor_power = MathUtil.clamp(-DAMPENER_CONSTANT * rpm, -MAX_CUSHION_OUTPUT_VALUE * FRONT_CUSHION_OUTPUT_SCALING, MAX_CUSHION_OUTPUT_VALUE * FRONT_CUSHION_OUTPUT_SCALING);
        }
    } else if (revs < UPRIGHT_PIVOT_VALUE && rpm >= -RPM_CUSHION_TOLERANCE) {
        // Case 2: Front of robot, moving upward toward back.
        cushion_motor_power = 0.0;
        back_motor_commanded = backward_motor_request;
        front_motor_commanded = forward_motor_request;
    } else if (revs >= UPRIGHT_PIVOT_VALUE && rpm > RPM_CUSHION_TOLERANCE) {
        // Case 3: Back of robot, falling towards back.
        if (Math.abs(forward_motor_request) > 0) {
          cushion_motor_power = 0.0;
          back_motor_commanded = backward_motor_request;
          front_motor_commanded = forward_motor_request;
        } else {
          back_motor_commanded = 0.0;
          front_motor_commanded = 0.0;
          cushion_motor_power = MathUtil.clamp(-DAMPENER_CONSTANT * rpm, -MAX_CUSHION_OUTPUT_VALUE, MAX_CUSHION_OUTPUT_VALUE);
        }
    } else {
        // Case 4: Back of robot, moving up towards front.
        cushion_motor_power = 0.0;
        back_motor_commanded = backward_motor_request;
        front_motor_commanded = forward_motor_request;
    }

    // If we're near the top, just let joystick motor commands override.
    double deadzone_limit_back = UPRIGHT_PIVOT_VALUE + UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE;
    double deadzone_limit_front = UPRIGHT_PIVOT_VALUE - UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE;
    if (deadzone_limit_front <= revs && revs <= deadzone_limit_back) {
      cushion_motor_power = 0.0;
      back_motor_commanded = backward_motor_request;
      front_motor_commanded = forward_motor_request;
    }

    cushion_value = cushion_motor_power;
    total_motor_commanded = back_motor_commanded + front_motor_commanded;

    double total_power = cushion_motor_power + back_motor_commanded + front_motor_commanded;
    double clamped_total_power = MathUtil.clamp(total_power, -MAX_TOTAL_MOTOR_VALUE, MAX_TOTAL_MOTOR_VALUE);
    commanded_value = clamped_total_power;
    return clamped_total_power;
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

  public double getTotalMotorCommanded() {
    return total_motor_commanded;
  }

}
