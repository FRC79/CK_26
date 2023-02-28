// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Clamp_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Clamp;

public class OpenClamp extends CommandBase {

  private final Clamp m_clamp;

  /** Creates a new EnableClamp. */
  public OpenClamp(Clamp subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_clamp = subsystem;
    addRequirements(m_clamp);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_clamp.setClampSolenoidState("Forward");
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
