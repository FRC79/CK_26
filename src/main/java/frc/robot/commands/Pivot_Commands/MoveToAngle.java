// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Pivot;

public class MoveToAngle extends CommandBase {

  private final Pivot m_Pivot;
  double desiredAngle;
  private int setpointID = 0;

  /** Creates a new MoveToAngle. */
  public MoveToAngle(Pivot pivot, double setpoint) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Pivot = pivot;
    desiredAngle = setpoint;
    addRequirements(m_Pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_Pivot.newSetpoint(desiredAngle);
    setpointID = m_Pivot.getSetpointID();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Pivot.setMotorPower(m_Pivot.getCommand());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Pivot.setMotorPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (setpointID != m_Pivot.getSetpointID()) || m_Pivot.getMaxLimitState() || m_Pivot.getMinLimitState();
  }
}
