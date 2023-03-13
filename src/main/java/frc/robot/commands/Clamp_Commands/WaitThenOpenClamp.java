// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Clamp_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Timer;
import frc.robot.subsystems.Clamp;

public class WaitThenOpenClamp extends CommandBase {

  private final Clamp m_clamp;

  private Timer m_warmup_timer;

  /** Creates a new EnableClamp. */
  public WaitThenOpenClamp(Clamp subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_clamp = subsystem;
    addRequirements(m_clamp);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_warmup_timer = new Timer(1000);
    m_warmup_timer.clear();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_warmup_timer.isReady()) {
        m_clamp.setClampSolenoidState("Forward");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_warmup_timer.isReady();
  }
}
