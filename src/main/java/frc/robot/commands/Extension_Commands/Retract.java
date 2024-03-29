// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Extension_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ExtensionConstants;
import frc.robot.subsystems.Extension;

public class Retract extends CommandBase {

  private final Extension m_Extension;
  /** Creates a new Retract. */
  public Retract(Extension subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Extension = subsystem;
    addRequirements(m_Extension);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_Extension.setMotorPower(ExtensionConstants.RETRACT_POWER);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Extension.setMotorPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
