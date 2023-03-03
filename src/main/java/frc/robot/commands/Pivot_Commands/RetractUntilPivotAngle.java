// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Extension;
import frc.robot.subsystems.Pivot;

public class RetractUntilPivotAngle extends CommandBase {
  /** Creates a new RetractUntilPivotAngle. */
  private Pivot m_pivot;
  private Extension m_extension;
  private static final double ANGLE_CUTOFF_POSITION_REVS = 8.0;

  public RetractUntilPivotAngle(Pivot pivot, Extension extension) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = pivot;
    m_extension = extension;
    addRequirements(m_pivot);
    addRequirements(m_extension);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_extension.setMotorPower(-0.5);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_extension.setMotorPower(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double revs = m_pivot.getEncoder().getPosition();

    if (revs > ANGLE_CUTOFF_POSITION_REVS) {
      return true;
    }

    return false;
  }
}
