// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

public class Pivot extends SubsystemBase {

  private final CANSparkMax pivotMotor = new CANSparkMax(PivotConstants.PIVOT_MOTOR_PORT,
      CANSparkMaxLowLevel.MotorType.kBrushless);
  private final DoubleSolenoid highSolenoid = new DoubleSolenoid(
      PneumaticsModuleType.CTREPCM, PivotConstants.HIGH_SOLENOID_PORT[0], PivotConstants.HIGH_SOLENOID_PORT[1]);
  private final DoubleSolenoid lowSolenoid = new DoubleSolenoid(
      PneumaticsModuleType.CTREPCM, PivotConstants.LOW_SOLENOID_PORT[0], PivotConstants.LOW_SOLENOID_PORT[1]);

  private String m_highSolenoidState = "Reverse";
  private String m_lowSolenoidState = "Reverse";

  private double m_velocitySetpoint = 0;

  public Pivot() {
    pivotMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
    pivotMotor.set(0.0);

    // TODO: Set soft limits after looking at values on smart dash.
    cutMotorPower();
    getPIDController().setP(PivotConstants.kP_VELOCITY);
    getPIDController().setI(PivotConstants.kI_VELOCITY);
    getPIDController().setD(PivotConstants.kD_VELOCITY);
    getPIDController().setIZone(PivotConstants.KIz_VELOCITY);
    getPIDController().setFF(PivotConstants.kFF_VELOCITY);

    highSolenoid.set(Value.kReverse);
    m_highSolenoidState = "Reverse";
    lowSolenoid.set(Value.kReverse);
    m_lowSolenoidState = "Reverse";
  }

  public double getMotorOutput() {
    return pivotMotor.get();
  }

  public void setMotorPower(double power) {
    double power_to_apply = MathUtil.clamp(power, -PivotConstants.kMAX_OUTPUT_MAG_VELOCITY,
        PivotConstants.kMAX_OUTPUT_MAG_VELOCITY);
    pivotMotor.set(power_to_apply);
  }

  public void setVelocitySetpoint(double rpm_setpoint) {
    double clamped_setpoint = MathUtil.clamp(rpm_setpoint, -PivotConstants.kMAX_SETPOINT_RPM,
        PivotConstants.kMAX_SETPOINT_RPM);
    getPIDController().setReference(rpm_setpoint, CANSparkMax.ControlType.kVelocity);
    m_velocitySetpoint = rpm_setpoint;
  }

  public double getVelocitySetpoint() {
    return m_velocitySetpoint;
  }

  public void cutMotorPower() {
    getPIDController().setOutputRange(0, 0);
    setVelocitySetpoint(0);
    pivotMotor.set(0.0);
  }

  public void engageMotorPower() {
    getPIDController().setOutputRange(-PivotConstants.kMAX_OUTPUT_MAG_VELOCITY,
        PivotConstants.kMAX_OUTPUT_MAG_VELOCITY);
  }

  public RelativeEncoder getEncoder() {
    return pivotMotor.getEncoder();
  }

  public SparkMaxPIDController getPIDController() {
    return pivotMotor.getPIDController();
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

  public void setLowSolenoidState(String state) {
    switch (state) {
      case ("Forward"):
        lowSolenoid.set(Value.kForward);
        m_lowSolenoidState = state;
        break;
      case ("Reverse"):
        lowSolenoid.set(Value.kReverse);
        m_lowSolenoidState = state;
        break;
      case ("Off"):
        lowSolenoid.set(Value.kOff);
        m_lowSolenoidState = state;
        break;
      default:
        lowSolenoid.set(Value.kOff);
        m_lowSolenoidState = "Off";
        break;
    }
  }

  public String getLowSolenoidState() {
    return m_lowSolenoidState;
  }

  public String getHighSolenoidState() {
    return m_highSolenoidState;
  }
}
