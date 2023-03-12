// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DeployableWheelsConstants;

public class DeployableWheels extends SubsystemBase {
  /** Creates a new DeployableWheels. */
  private final WPI_VictorSPX tractionWheelMotor = new WPI_VictorSPX(
      DeployableWheelsConstants.TRACTION_WHEEL_MOTOR_PORT);

  public DeployableWheels() {
    tractionWheelMotor.set(0.0);
  }

  public void setMotorPower(double power) {
    tractionWheelMotor.set(power);
  }

  public void teleopInit() {

  }

  @Override
  public void periodic() {
  }
}
