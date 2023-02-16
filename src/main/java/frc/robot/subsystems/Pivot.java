// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class Pivot extends SubsystemBase {

  private final WPI_VictorSPX pivotMotor = new WPI_VictorSPX(ArmConstants.PIVOT_MOTOR_PORT);
  private final AnalogPotentiometer pot = new AnalogPotentiometer(
  PivotConstants.POT_PORT, PivotConstants.RANGE_DEGREES, PivotConstants.OFFSET_DEGREES);
  private final DigitalInput topLimitSwitch = new DigitalInput(PivotConstants.TOP_LIMIT_PORT)
  /** Creates a new Pivot. */
  public Pivot() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
