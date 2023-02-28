// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Pivot;

public class ToggleHighSolenoid extends CommandBase {

  private final Pivot m_pivot;

  /** Creates a new EnableClamp. */
  public ToggleHighSolenoid(Pivot subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
    addRequirements(m_pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_pivot.getHighSolenoidState().equals("Forward")) {
        m_pivot.setHighSolenoidState("Reverse");
    } else if (m_pivot.getHighSolenoidState().equals("Reverse")) {
        m_pivot.setHighSolenoidState("Forward");
    } else {
        m_pivot.setHighSolenoidState("Reverse");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
