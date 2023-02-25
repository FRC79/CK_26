// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class Pivot extends SubsystemBase {

  private final WPI_VictorSPX pivotMotor = new WPI_VictorSPX(PivotConstants.PIVOT_MOTOR_PORT);
  private final AnalogPotentiometer pivotPot = new AnalogPotentiometer(
  PivotConstants.PIVOT_POT_PORT, PivotConstants.RANGE_DEGREES);
  private final DigitalInput maxAngleLimitSwitch = new DigitalInput(PivotConstants.MAX_ANGLE_LIMIT_PORT);
  private final DigitalInput minAngleLimitSwitch = new DigitalInput(PivotConstants.MIN_ANGLE_LIMIT_PORT);
  double angleEstimate;

  private final DoubleSolenoid highSolenoid = new DoubleSolenoid(
  PneumaticsModuleType.CTREPCM, PivotConstants.HIGH_SOLENOID_PORT[0], PivotConstants.HIGH_SOLENOID_PORT[1]);
  private final DoubleSolenoid lowSolenoid = new DoubleSolenoid(
  PneumaticsModuleType.CTREPCM, PivotConstants.LOW_SOLENOID_PORT[0], PivotConstants.LOW_SOLENOID_PORT[1]);


  public Pivot() {}


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

  public void setHighSolenoidState(String state){
    switch (state){
      case ("Forward"):
        highSolenoid.set(Value.kForward);
        break;
      case ("Reverse"):
        highSolenoid.set(Value.kReverse);
        break;
      case ("Off"):
        highSolenoid.set(Value.kOff);
        break;
      default:
        highSolenoid.set(Value.kOff);
        break;
    }
  }

  public void setLowSolenoidState(String state){
    switch (state){
      case ("Forward"):
        lowSolenoid.set(Value.kForward);
        break;
      case ("Reverse"):
        lowSolenoid.set(Value.kReverse);
        break;
      case ("Off"):
        lowSolenoid.set(Value.kOff);
        break;
      default:
        lowSolenoid.set(Value.kOff);
        break;
    }
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    angleEstimate = pivotPot.get();
  }
}
