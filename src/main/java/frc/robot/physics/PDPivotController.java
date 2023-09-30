package frc.robot.physics;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.math.MathUtil;
import frc.robot.Timer;
import frc.robot.Constants.*;
import frc.robot.subsystems.Pivot;
import java.time.Instant;
import java.lang.Math;

public class PDPivotController {

  private Pivot m_pivot;

  public static final double MAX_ENCODER_VALUE = 34.85; // revolutions of the drive axel
  private static final double MIN_ENCODER_VALUE = 0.0; // starting location when the pivot is stored in the robot.
  private static final double MIN_ABS_RPM_CUSHION = 10.0; // RPM
  public static final double UPRIGHT_PIVOT_VALUE = 23.64; // the value of the encoder when the pivot is at the top of
                                                          // rotation.
  public static final double UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE = 14.547;
  public static final double UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE = 3.0;
  public static final double MAX_MOTOR_VALUE = 0.3;
  private static final double DAMPENER_CONSTANT = 0.00001 * 0.2;
  private static final double MAX_CUSHION_OUTPUT_VALUE = 0.1;
  private static final double FRONT_CUSHION_OUTPUT_SCALING = 0.5;
  private static final double MAX_TOTAL_MOTOR_VALUE = 0.3;
  private static final double RPM_CUSHION_TOLERANCE = 10.0; // RPM
  private static final double KP = 0.0001;
  private static final double KD = 0.00002 * 0;
  private static final double KF = 0.15;
  private static final double KDAMP = 0.00001 * 0.0;
  private static final double MIN_PIVOT_ANGLE_DEGREES = 17.745;
  private static final double DEGREES_PER_REV = 7.258065;
  private boolean faulted = false;
  private double commanded_value = 0.0;
  private double total_motor_commanded = 0.0;
  private double cushion_value = 0.0;
  private double rpm_setpoint = 0.0;
  private double prev_error = 0.0;
  private double error = 0.0;
  private double dt = 0.0;
  private Instant lastTime = Instant.now();
  private double recentpidCommand = 0.0;
  private double recentfeedfoward = 0.0;
  private double recentDeriv = 0.0;
  private double lastLawValue = 0.0;
  private Timer timer;

  public PDPivotController(Pivot subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
    timer = new Timer(20);
    timer.clear();
  }

  // public bool isStartStatusOk() {
  // double revs = m_pivot.getEncoder().getPosition();
  // double rpm = m_pivot.getEncoder().getVelocity();

  // // if (!(-5 < revs && revs <= 5)) {
  // // // Must have a proper starting position towards low end of the robot.
  // // System.out.println("FAULTED, POSITION INVALID");
  // // faulted = true;
  // // return false;
  // // }

  // // if (!(-0.01 < rpm && rpm <= 0.01)) {
  // // // Should be fairly stationary at start.
  // // System.out.println("FAULTED, VELOCITY INVALID");
  // // faulted = true;
  // // return false;
  // // }

  // return true;
  // }

  public double getError() {
    return error;
  }

  public double getPreviousError() {
    return prev_error;
  }

  public double getRecentDerivative() {
    return recentDeriv;
  }

  public double getErrorDerivative() {
    if (dt <= 1e-6) {
      return 0.0;
    }
    return (error - prev_error) / dt;
  }

  public double getRPMSetpoint() {
    return rpm_setpoint;
  }

  public double getRecentFeedforward() {
    return recentfeedfoward;
  }

  public double getRecentPIDCommand() {
    return recentpidCommand;
  }

  public double getDt() {
    return dt;
  }

  public void resetPDController() {
    commanded_value = 0.0;
    total_motor_commanded = 0.0;
    cushion_value = 0.0;
    rpm_setpoint = 0.0;
    prev_error = 0.0;
    error = 0.0;
    dt = 0.0;
    lastTime = Instant.now();
    recentpidCommand = 0.0;
    recentfeedfoward = 0.0;
    recentDeriv = 0.0;
    lastLawValue = 0.0;
    timer.clear();
  }

  public double degreesFromZAxis(double encoder_revs) {
    return encoder_revs * DEGREES_PER_REV + MIN_PIVOT_ANGLE_DEGREES;
  }

  public double controlLawPD(double rpm_setpoint, double rpm_actual, double encoder_revs) {
    Instant now = Instant.now();
    dt = (now.toEpochMilli() - lastTime.toEpochMilli()) / 1000.0;

    error = rpm_setpoint - rpm_actual;
    double radians_from_z_axis = degreesFromZAxis(encoder_revs) * Math.PI / 180.0;
    double feedforward = Math.sin(radians_from_z_axis);
    double feedforward2 = rpm_actual;
    recentDeriv = getErrorDerivative();
    double motorCommand = -KP * error - KD * recentDeriv + KF * feedforward - Math.min(0, KDAMP * feedforward2);

    recentpidCommand = motorCommand;
    recentfeedfoward = feedforward;

    prev_error = error;
    lastTime = now;

    return motorCommand;
  }

  public double controlLaw(double forward_motor_request, double backward_motor_request) {
    if (faulted) {
      return 0.0;
    }

    double revs = m_pivot.getEncoder().getPosition();
    double rpm = m_pivot.getEncoder().getVelocity();

    double cushion_motor_power = 0.0;
    double back_motor_commanded = 0.0;
    double front_motor_commanded = 0.0;

    if (timer.isReady()) {
      lastLawValue = controlLawPD(0.0, rpm, revs);
    }

    if (revs < UPRIGHT_PIVOT_VALUE && rpm < -RPM_CUSHION_TOLERANCE) {
      // Case 1: Front of robot, falling towards front.
      if (Math.abs(backward_motor_request) > 0) {
        cushion_motor_power = 0.0;
        back_motor_commanded = backward_motor_request;
        front_motor_commanded = forward_motor_request;
      } else {
        back_motor_commanded = 0.0;
        front_motor_commanded = 0.0;
        cushion_motor_power = MathUtil.clamp(-DAMPENER_CONSTANT * rpm,
            -MAX_CUSHION_OUTPUT_VALUE * FRONT_CUSHION_OUTPUT_SCALING,
            MAX_CUSHION_OUTPUT_VALUE * FRONT_CUSHION_OUTPUT_SCALING);
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
        cushion_motor_power = MathUtil.clamp(-DAMPENER_CONSTANT * rpm, -MAX_CUSHION_OUTPUT_VALUE,
            MAX_CUSHION_OUTPUT_VALUE);
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

    // For keeping track.
    cushion_value = cushion_motor_power;
    total_motor_commanded = back_motor_commanded + front_motor_commanded;

    boolean anyRequest = Math.abs(forward_motor_request) > 0 || Math.abs(backward_motor_request) > 0;
    if (!anyRequest && 1.5 <= revs && revs <= 3.2) {
      return MathUtil.clamp(lastLawValue, 0.0, 0.6);
    }

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
