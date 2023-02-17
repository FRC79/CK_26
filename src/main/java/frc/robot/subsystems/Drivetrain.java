// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import frc.robot.Constants.DriveConstants;
import frc.robot.commands.Drive_Commands.BridgeBalancer;


// gyro lib
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.trajectory.Trajectory.State;
// SP interface for gyro
import edu.wpi.first.wpilibj.SPI;

public class Drivetrain extends SubsystemBase {

  /* Left Motors */
  private final WPI_VictorSPX frontLeftMotor = new WPI_VictorSPX(DriveConstants.FL_MOTOR_PORT);
  private final WPI_TalonSRX backLeftMotor = new WPI_TalonSRX(DriveConstants.BL_MOTOR_PORT);

  /* Right Motors */
  private final WPI_VictorSPX frontRightMotor = new WPI_VictorSPX(DriveConstants.FR_MOTOR_PORT);
  private final WPI_TalonSRX backRightMotor = new WPI_TalonSRX(DriveConstants.BR_MOTOR_PORT);

  public final MecanumDrive m_robotDrive = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor,
      backRightMotor);

  /* gyro */
  public final AHRS gyro = new AHRS(SPI.Port.kMXP);

  public final BridgeBalancer m_BridgeBalancer = new BridgeBalancer();

  // values for testing gyro
  public float pitch_angle;
  public float roll_angle;
  public float yaw_angle;
  public float rawaccelX;
  public float rawaccelY;
  public float rawaccelZ;
  public float accelX;
  public float accelY;
  public float accelZ;
  public float grav = 9.81f;
  public double yStick;
  public double xStick;
  public double zStick;


  /** Creates a new MecanumDrive. */
  public Drivetrain() {
    frontRightMotor.setInverted(true);
    backRightMotor.setInverted(true);
  }

  public void cartesianDrive(double y, double x, double z) {
    yStick = y;
    xStick = x;
    zStick = z;
    m_robotDrive.driveCartesian(y, x, z);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    pitch_angle = gyro.getPitch();
    roll_angle = gyro.getRoll();
    yaw_angle = gyro.getYaw();
    rawaccelX = gyro.getRawAccelX() * grav;
    rawaccelY = gyro.getRawAccelY() * grav;
    rawaccelZ = gyro.getRawAccelZ() * grav;
    accelX = gyro.getWorldLinearAccelX() * grav;
    accelY = gyro.getWorldLinearAccelY() * grav;
    accelZ = gyro.getWorldLinearAccelZ() * grav;
  }
}
