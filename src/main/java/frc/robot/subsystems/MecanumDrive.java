// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.DemandType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.*;

public class MecanumDrive extends SubsystemBase {

  /*Left Motors*/
  private final VictorSPX frontLeftMotor = new VictorSPX(DriveConstants.FL_MOTOR_PORT);
  private final TalonSRX backLeftMotor = new TalonSRX(DriveConstants.BL_MOTOR_PORT);

  /*Rightt Motors*/
  private final VictorSPX frontRightMotor = new VictorSPX(DriveConstants.FR_MOTOR_PORT);
  private final TalonSRX backRightMotor = new TalonSRX(DriveConstants.BR_MOTOR_PORT);

  frontRightMotor.setInverted(true);


  /** Creates a new MecanumDrive. */
  public MecanumDrive() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
