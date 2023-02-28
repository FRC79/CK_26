// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Pivot;

public class PivotUp extends CommandBase {

  private final Pivot m_Pivot;
  /** Creates a new PivotUp. */
  public PivotUp(Pivot subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Pivot = subsystem;
    addRequirements(m_Pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // m_Pivot.setMotorPower(0.2);
    m_Pivot.engageMotorPower();
    m_Pivot.setVelocitySetpoint(1.6);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // m_Pivot.setMotorPower(0);
    // m_Pivot.setVelocitySetpoint(0.0);
    m_Pivot.cutMotorPower();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
