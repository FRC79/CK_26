// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class Extension extends SubsystemBase {

  private final CANSparkMax extensionMotor = new CANSparkMax(ExtensionConstants.EXTENSION_MOTOR_PORT, MotorType.kBrushless);
  private final AnalogPotentiometer extendPot = new AnalogPotentiometer(
  ExtensionConstants.EXTENSION_POT_PORT, ExtensionConstants.RANGE_DEGREES);
  private final DigitalInput maxExtensionLimitSwitch = new DigitalInput(ExtensionConstants.MAX_EXTENSION_LIMIT_PORT);
  private final DigitalInput minExtensionLimitSwitch = new DigitalInput(ExtensionConstants.MIN_EXTENSION_LIMIT_PORT);
  double distanceEstimate;
  
  public Extension() {}


  public void setMotorPower(double power) {
    if (power > 0){
      if (maxExtensionLimitSwitch.get()) {
        extensionMotor.set(0);
      } else{
        extensionMotor.set(power);
      }
    } else {
      if (minExtensionLimitSwitch.get()) {
        extensionMotor.set(0);
      } else {
        extensionMotor.set(power);
      }
    }
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    distanceEstimate = extendPot.get() * ExtensionConstants.POT_ANGLE_TO_EXTENSION_DISTANCE_CM;
  }
}
