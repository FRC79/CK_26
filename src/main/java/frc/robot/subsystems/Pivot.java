// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;
// import com.revrobotics.CANSparkMax;


public class Pivot extends SubsystemBase {

  private final WPI_VictorSPX pivotMotor = new WPI_VictorSPX(PivotConstants.PIVOT_MOTOR_PORT);
  private final DoubleSolenoid highSolenoid = new DoubleSolenoid(
  PneumaticsModuleType.CTREPCM, PivotConstants.HIGH_SOLENOID_PORT[0], PivotConstants.HIGH_SOLENOID_PORT[1]);
  private final DoubleSolenoid lowSolenoid = new DoubleSolenoid(
  PneumaticsModuleType.CTREPCM, PivotConstants.LOW_SOLENOID_PORT[0], PivotConstants.LOW_SOLENOID_PORT[1]);

  
  public Pivot() {
    
  }

  public void setMotorPower(double power) {
    double MAX_POWER_MAG = 0.2;
    double power_to_apply = 0.0;
    if (power > MAX_POWER_MAG) {
      power_to_apply = MAX_POWER_MAG;
    } else if (power < -MAX_POWER_MAG) {
      power_to_apply = -MAX_POWER_MAG;
    } else {
      power_to_apply = power;
    }
      
    pivotMotor.set(ControlMode.PercentOutput, power_to_apply);
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
}
