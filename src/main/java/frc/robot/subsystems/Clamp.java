// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClampConstants;

public class Clamp extends SubsystemBase {

  private final DoubleSolenoid clampSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
      ClampConstants.CLAMP_SOLENOID_PORT[0], ClampConstants.CLAMP_SOLENOID_PORT[1]);

  private String m_clampState = "Off";

  /** Creates a new Clamp. */
  public Clamp() {
    clampSolenoid.set(Value.kReverse);
    m_clampState = "Reverse";
  }

  public void setClampSolenoidState(String state) {
    switch (state) {
      case ("Forward"):
        clampSolenoid.set(Value.kForward);
        m_clampState = state;
        break;
      case ("Reverse"):
        clampSolenoid.set(Value.kReverse);
        m_clampState = state;
        break;
      case ("Off"):
        clampSolenoid.set(Value.kOff);
        m_clampState = state;
        break;
      default:
        clampSolenoid.set(Value.kOff);
        m_clampState = "Off";
        break;
    }
  }

  public String getState() {
    return m_clampState;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
