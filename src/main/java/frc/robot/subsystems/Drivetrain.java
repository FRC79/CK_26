// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {

  /*Left Motors*/
  private final WPI_VictorSPX frontLeftMotor = new WPI_VictorSPX(DriveConstants.FL_MOTOR_PORT);
  private final WPI_TalonSRX backLeftMotor = new WPI_TalonSRX(DriveConstants.BL_MOTOR_PORT);

  /*Rightt Motors*/
  private final WPI_VictorSPX frontRightMotor = new WPI_VictorSPX(DriveConstants.FR_MOTOR_PORT);
  private final WPI_TalonSRX backRightMotor = new WPI_TalonSRX(DriveConstants.BR_MOTOR_PORT);


  private final MecanumDrive m_robotDrive = new MecanumDrive(frontLeftMotor,backLeftMotor,frontRightMotor,backRightMotor);

  /** Creates a new MecanumDrive. */
  public Drivetrain() {
    frontRightMotor.setInverted(true);
    backRightMotor.setInverted(true);
  }

  public void cartesianDrive(double y, double x, double z) {
    m_robotDrive.driveCartesian(y, x, z);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
