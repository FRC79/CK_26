// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Pivot;

public class ToggleLowSolenoid extends CommandBase {

  private final Pivot m_pivot;

  /** Creates a new EnableClamp. */
  public ToggleLowSolenoid(Pivot subsystem) {
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
    if (m_pivot.getLowSolenoidState().equals("Forward")) {
        m_pivot.setLowSolenoidState("Reverse");
    } else if (m_pivot.getLowSolenoidState().equals("Reverse")) {
        m_pivot.setLowSolenoidState("Forward");
    } else {
        m_pivot.setLowSolenoidState("Reverse");
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
