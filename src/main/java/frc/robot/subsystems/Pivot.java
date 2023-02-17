// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.Constants.*;

public class Pivot extends PIDSubsystem {

  private final WPI_VictorSPX pivotMotor = new WPI_VictorSPX(PivotConstants.PIVOT_MOTOR_PORT);
  private final AnalogPotentiometer pivotPot = new AnalogPotentiometer(
  PivotConstants.PIVOT_POT_PORT, PivotConstants.POT_RANGE_DEGREES);
  private final DigitalInput maxAngleLimitSwitch = new DigitalInput(PivotConstants.MAX_ANGLE_LIMIT_PORT);
  private final DigitalInput minAngleLimitSwitch = new DigitalInput(PivotConstants.MIN_ANGLE_LIMIT_PORT);
  double angleEstimate;
  private int setpointID = 0;
  
  public Pivot() {
    super(
      // The PIDController used by the subsystem
      new PIDController(PivotConstants.P, PivotConstants.I, PivotConstants.D));
    getController().enableContinuousInput(PivotConstants.MIN_ANGLE_PIVOT, PivotConstants.MAX_ANGLE_PIVOT);
    getController().setTolerance(PivotConstants.PIVOT_SETPOINT_ANGLE_TOLERANCE,PivotConstants.PIVOT_SETPOINT_SPEED_TOLERANCE);
    //getController().setIntegratorRange(-0.5, 0.5);
  }


  public void setMotorPower(double power) {
    if (power > 0){
      if (maxAngleLimitSwitch.get()) {
        pivotMotor.set(ControlMode.PercentOutput, 0);
      } else{
        pivotMotor.set(ControlMode.PercentOutput, power);
      }
    } else {
      if (minAngleLimitSwitch.get()) {
        pivotMotor.set(ControlMode.PercentOutput, 0);
      } else {
        pivotMotor.set(ControlMode.PercentOutput, power);
      }
    }
  }

  public boolean getMaxLimitState(){
    return maxAngleLimitSwitch.get();
  }

  public boolean getMinLimitState(){
    return minAngleLimitSwitch.get();
  }

  public void newSetpoint(double desiredAngle){
    getController().reset();
    getController().setSetpoint(desiredAngle);
    setpointID += 1;
  }

  public int getSetpointID(){
    return setpointID;
  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
  }

  public double getCommand(){
    return MathUtil.clamp(getController().calculate(getMeasurement()),PivotConstants.MIN_PID_POWER,PivotConstants.MAX_PID_POWER);
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    angleEstimate = pivotPot.get();
    return angleEstimate;
  }
}
