// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Extension_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Timer;
import frc.robot.Constants.ExtensionConstants;
import frc.robot.subsystems.Extension;

public class RetractForTime extends CommandBase {
  private Extension m_extension;
  private Timer m_timer;
  
  public RetractForTime(Extension extension) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_extension = extension;
    addRequirements(m_extension);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer = new Timer(500);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_extension.setMotorPower(ExtensionConstants.RETRACT_POWER);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_extension.setMotorPower(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_timer.isReady();
  }
}
