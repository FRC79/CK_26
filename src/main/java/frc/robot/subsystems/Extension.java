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
    extensionMotor.set(power);
  }

  public double getExtensionDistance() {
    return extendPot.get(); //extendPot.get() * ExtensionConstants.POT_ANGLE_TO_EXTENSION_DISTANCE_CM;
  }
}
