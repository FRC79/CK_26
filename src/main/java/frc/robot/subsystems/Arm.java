// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;

import frc.robot.Constants.ArmConstants;

public class Arm extends ProfiledPIDSubsystem {

  private final WPI_VictorSPX pivotMotor = new WPI_VictorSPX(ArmConstants.PIVOT_MOTOR_PORT);
  private final Encoder m_encoder = 
    new Encoder(
      ArmConstants.ENCODER_PORTS[0], ArmConstants.ENCODER_PORTS[1]);
  private final ArmFeedforward m_feedForward = 
    new ArmFeedforward(
      ArmConstants.S_VOLTS, ArmConstants.G_VOLTS, 
      ArmConstants.V_VOLT_SECOND_PER_RAD, ArmConstants.A_VOLT_SECOND_SQUARED_PER_RAD);

  /** Creates a new Arm. */
  public Arm() {
    super(
        // The ProfiledPIDController used by the subsystem
        new ProfiledPIDController(
            ArmConstants.Kp,
            ArmConstants.Ki,
            ArmConstants.Kd,
            // The motion profile constraints
            new TrapezoidProfile.Constraints(
              ArmConstants.MAX_VELOCITY_RAD_PER_SECOND, 
              ArmConstants.MAX_ACCELERATION_RAD_PER_SECOND_SQAURED)), 
        0);

    m_encoder.setDistancePerPulse(ArmConstants.ENCODER_DISTANCE_PER_PULSE);
    //start arm on neutral position 
    setGoal(ArmConstants.ARM_OFFSET_NUETRAL_RADS);
  }

  @Override
  public void useOutput(double output, TrapezoidProfile.State setpoint) {
    // Calculate the feedforward from the setpoint
    double feedforward = m_feedForward.calculate(setpoint.position, setpoint.velocity);
    // Add the feedforward to the PID output to get the motor output
    pivotMotor.setVoltage(output + feedforward);

  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    return m_encoder.getDistance() + ArmConstants.ARM_OFFSET_NUETRAL_RADS;
  }
}
