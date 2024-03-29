// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.Constants.DriveConstants;
import frc.robot.commands.Drive_Commands.BridgeBalancer;
import frc.robot.Constants;
import frc.robot.Logger;

// gyro lib
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
// SP interface for gyro
import edu.wpi.first.wpilibj.SPI;

public class Drivetrain extends SubsystemBase {

  /* Left Motors */
  private final WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(DriveConstants.FL_MOTOR_PORT);
  private final WPI_VictorSPX backLeftMotor = new WPI_VictorSPX(DriveConstants.BL_MOTOR_PORT);

  /* Right Motors */
  private final WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(DriveConstants.FR_MOTOR_PORT);
  private final WPI_VictorSPX backRightMotor = new WPI_VictorSPX(DriveConstants.BR_MOTOR_PORT);

  private final MecanumDrive m_robotDrive = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor,
      backRightMotor);

  /* Encoders */
  private final Encoder frontLeftEncoder = new Encoder(
      DriveConstants.FL_ENCODER_PORTS[0],
      DriveConstants.FL_ENCODER_PORTS[1],
      DriveConstants.FL_ENCODER_REVERSED);

  private final Encoder backRightEncoder = new Encoder(
      DriveConstants.BR_ENCODER_PORTS[0],
      DriveConstants.BR_ENCODER_PORTS[1],
      DriveConstants.BR_ENCODER_REVERSED);

  /* gyro */
  private final AHRS gyro = new AHRS(SPI.Port.kMXP);

  /* Slow mode toggle state */
  private boolean slow_mode_enabled = false;

  /** Creates a new MecanumDrive. */
  public Drivetrain() {
    frontRightMotor.setInverted(true);
    backRightMotor.setInverted(true);
    m_robotDrive.setDeadband(0.05);
    setMaxOutput(DriveConstants.FAST_MODE_DRIVE_POWER);

    frontLeftEncoder.setDistancePerPulse(DriveConstants.ENCODER_DISTANCE_PER_PULSE_METERS);
    backRightEncoder.setDistancePerPulse(DriveConstants.ENCODER_DISTANCE_PER_PULSE_METERS);
  }

  public void cartesianDrive(double y, double x, double z) {
    m_robotDrive.driveCartesian(y, x, z);
  }

  public void setMaxOutput(double maxOutput) {
    m_robotDrive.setMaxOutput(maxOutput);
  }

  public void setSlowModeEnabled(boolean state) {
    slow_mode_enabled = state;
    setMaxOutput(isSlowModeEnabled() ? DriveConstants.SLOW_MODE_DRIVE_POWER : DriveConstants.FAST_MODE_DRIVE_POWER);
  }

  public boolean isSlowModeEnabled() {
    return slow_mode_enabled;
  }

  public double getFrontLeftDistanceMeters() {
    return frontLeftEncoder.getDistance();
  }

  public double getBackRightDistanceMeters() {
    return backRightEncoder.getDistance();
  }

  public double getGyroPitchAngleDegrees() {
    return gyro.getPitch();
  }

  public void resetGyro() {
    gyro.reset();
  }

  public void resetEncoders() {
    frontLeftEncoder.reset();
    backRightEncoder.reset();
  }

  @Override
  public void periodic() {
  }
}
