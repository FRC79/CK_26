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
import frc.robot.Logger;

// gyro lib
import com.kauailabs.navx.frc.AHRS;

// SP interface for gyro
import edu.wpi.first.wpilibj.SPI;

import frc.robot.Timer;

public class Drivetrain extends SubsystemBase {

  /* Left Motors */
  private final WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(DriveConstants.FL_MOTOR_PORT);
  private final WPI_VictorSPX backLeftMotor = new WPI_VictorSPX(DriveConstants.BL_MOTOR_PORT);

  /* Right Motors */
  private final WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(DriveConstants.FR_MOTOR_PORT);
  private final WPI_VictorSPX backRightMotor = new WPI_VictorSPX(DriveConstants.BR_MOTOR_PORT);

  private final MecanumDrive m_robotDrive = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor,
      backRightMotor);

  /* gyro */
  public final AHRS gyro = new AHRS(SPI.Port.kMXP);

  private Timer timer;

  // values for testing gyro
  float pitch_angle;
  float roll_angle;
  float yaw_angle;
  float rawaccelX;
  float rawaccelY;
  float rawaccelZ;
  float accelX;
  float accelY;
  float accelZ;
  float grav = 9.81f;

  private Logger logger;
  private Map<String, Float> floatColumns;

  private BridgeBalancer bridgeBalancer;


  /** Creates a new MecanumDrive. */
  public Drivetrain() {
    frontRightMotor.setInverted(true);
    backRightMotor.setInverted(true);
    m_robotDrive.setDeadband(0.05);

    logger = new Logger("gyrotest.csv",
        Arrays.asList("Pitch", "Roll", "Yaw", "AccelX", "AccelY", "AccelZ", "RawAccelX", "RawAccelY", "RayAccelZ"));
    floatColumns = new HashMap<>();
    timer = new Timer(500);
    bridgeBalancer = new BridgeBalancer();
  }

  public void cartesianDrive(double y, double x, double z) {
    m_robotDrive.driveCartesian(y, x, z);
  }

  public void setMaxOutput(double maxOutput){
    m_robotDrive.setMaxOutput(maxOutput);
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
    /* 
    try {
      bridgeBalancer.UpdateState(pitch_angle, accelX);
    } catch (Exception e) {
      System.out.println("Woops, bridge code broke");
    }
    */

    if (timer.isReady()) {
      floatColumns.put("Pitch", pitch_angle);
      floatColumns.put("Roll", roll_angle);
      floatColumns.put("Yaw", yaw_angle);
      floatColumns.put("AccelX", accelX);
      floatColumns.put("AccelY", accelY);
      floatColumns.put("AccelZ", accelZ);
      floatColumns.put("RawAccelX", rawaccelX);
      floatColumns.put("RawAccelY", rawaccelY);
      floatColumns.put("RawAccelZ", rawaccelZ);
      logger.logFloat(floatColumns);
      floatColumns.clear();

      String gyroData = String.format("Pitch: %f, Roll: %f, Yaw: %f", pitch_angle, roll_angle, yaw_angle);
      String accelData = String.format("AccelX: %f, AccelY: %f, AccelZ: %f", accelX, accelY, accelZ);
      String rawaccelData = String.format("RawAccelX: %f, RawAccelY: %f, RawAccelZ: %f", rawaccelX, rawaccelY, rawaccelZ);
      System.out.println(gyroData);
      System.out.println(accelData);
      System.out.println(rawaccelData);

      timer.clear();
    }
  }
}
