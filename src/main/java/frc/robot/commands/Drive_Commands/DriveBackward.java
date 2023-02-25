// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveBackward extends CommandBase {

  private final Drivetrain m_MecanumDrive;

  /** Creates a new DriveBackward. */
  public DriveBackward(Drivetrain subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_MecanumDrive = subsystem;
    addRequirements(m_MecanumDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_MecanumDrive.cartesianDrive(-0.5, 0, 0);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_MecanumDrive.cartesianDrive(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
