// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClampConstants;

public class Clamp extends SubsystemBase {

  private final Solenoid clampSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, ClampConstants.SOLENOID_PORT);

  /** Creates a new Clamp. */
  public Clamp() {}

  public void enableSolenoid(boolean Enabled){
    clampSolenoid.set(Enabled);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
