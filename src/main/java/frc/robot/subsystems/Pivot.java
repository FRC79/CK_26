// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;

public class Pivot extends SubsystemBase {

  private CANSparkMax pivotMotor = new CANSparkMax(PivotConstants.PIVOT_MOTOR_PORT,
      CANSparkMaxLowLevel.MotorType.kBrushless);
  private final DoubleSolenoid highSolenoid = new DoubleSolenoid(
      PneumaticsModuleType.CTREPCM, PivotConstants.HIGH_SOLENOID_PORT[0], PivotConstants.HIGH_SOLENOID_PORT[1]);

  private final DigitalInput highGoalLimit = new DigitalInput(PivotConstants.HIGH_GOAL_LIMIT_PORT);

  private String m_highSolenoidState = "Reverse";

  public Pivot() {
    pivotMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
    pivotMotor.set(0.0);

    highSolenoid.set(Value.kReverse);
    m_highSolenoidState = "Reverse";
  }

  public double getMotorOutput() {
    return pivotMotor.get();
  }

  public void setMotorPower(double power) {
    pivotMotor.set(power);
  }

  public RelativeEncoder getEncoder() {
    return pivotMotor.getEncoder();
  }

  public boolean highGoalTopLimitPressed() {
    return highGoalLimit.get();
  }

  public void setHighSolenoidState(String state) {
    switch (state) {
      case ("Forward"):
        highSolenoid.set(Value.kForward);
        m_highSolenoidState = state;
        break;
      case ("Reverse"):
        highSolenoid.set(Value.kReverse);
        m_highSolenoidState = state;
        break;
      case ("Off"):
        highSolenoid.set(Value.kOff);
        m_highSolenoidState = state;
        break;
      default:
        highSolenoid.set(Value.kOff);
        m_highSolenoidState = "Off";
        break;
    }
  }

  public String getHighSolenoidState() {
    return m_highSolenoidState;
  }
}
