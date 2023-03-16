// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

public class Extension extends SubsystemBase {

  private final CANSparkMax extensionMotor = new CANSparkMax(ExtensionConstants.EXTENSION_MOTOR_PORT,
      CANSparkMaxLowLevel.MotorType.kBrushless);

  private final AnalogPotentiometer extendPot = new AnalogPotentiometer(
      ExtensionConstants.EXTENSION_POT_PORT, ExtensionConstants.RANGE_DEGREES);

  private double encoderOffset = 0;

  public Extension() {
    extensionMotor.set(0.0);
  }

  public void setCoastMode() {
    extensionMotor.setIdleMode(IdleMode.kCoast);
  }

  public void setBrakeMode() {
    extensionMotor.setIdleMode(IdleMode.kBrake);
  }

  public RelativeEncoder getEncoder() {
    return extensionMotor.getEncoder();
  }

  public void setMotorPower(double power) {
    extensionMotor.set(power);
  }

  public double getPot() {
    return extendPot.get();
  }

  public boolean isFullyExtended() {
    return Math.abs(getClampedPot() - ExtensionConstants.FULL_EXTEND_POT_VALUE) < 1e-1;
  }

  public boolean isFullyRetracted() {
    return Math.abs(getClampedPot() - ExtensionConstants.FULL_RETRACT_POT_VALUE) < 1e-1;
  }

  public boolean isMostlyRetracted() {
    return getClampedPot() >= ExtensionConstants.MOSTLY_RETRACT_POT_VALUE;
  }

  public double getClampedPot() {
    return MathUtil.clamp(getPot(), ExtensionConstants.FULL_EXTEND_POT_VALUE,
        ExtensionConstants.FULL_RETRACT_POT_VALUE);
  }

  public void resetEncoder() {
    encoderOffset = getEncoder().getPosition();
  }

  public double getCalibratedEncoderPosition() {
    return getEncoder().getPosition() - encoderOffset;
  }

  public boolean stillSpooledIn() {
    double SPOOL_OUT_TURNS = 46.0;
    return Math.abs(getCalibratedEncoderPosition()) <= SPOOL_OUT_TURNS;
  }

  public boolean stillSpooledInLess() {
    double SPOOL_OUT_TURNS = 13.0;
    return Math.abs(getCalibratedEncoderPosition()) <= SPOOL_OUT_TURNS;
  }
}
