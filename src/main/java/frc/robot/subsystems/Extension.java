// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class Extension extends SubsystemBase {

  private final WPI_VictorSPX extensionMotor = new WPI_VictorSPX(ExtensionConstants.EXTENSION_MOTOR_PORT);
  
  public Extension() {}


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
      
    extensionMotor.set(ControlMode.PercentOutput, power_to_apply);
  }
}
