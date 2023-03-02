// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

public class Extension extends SubsystemBase {

  private final CANSparkMax extensionMotor = new CANSparkMax(ExtensionConstants.EXTENSION_MOTOR_PORT,
      CANSparkMaxLowLevel.MotorType.kBrushless);

  private final AnalogPotentiometer extendPot = new AnalogPotentiometer(
      ExtensionConstants.EXTENSION_POT_PORT, ExtensionConstants.RANGE_DEGREES);

  public Extension() {
    extensionMotor.set(0.0);
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

    extensionMotor.set(power_to_apply);
  }

  public double getExtensionDistance() {
    return extendPot.get() * ExtensionConstants.POT_ANGLE_TO_EXTENSION_DISTANCE_CM;
  }
}
