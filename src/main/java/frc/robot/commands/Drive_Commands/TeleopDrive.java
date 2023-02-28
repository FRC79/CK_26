// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive_Commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

import frc.robot.Constants.*;

public class TeleopDrive extends CommandBase {

  private Drivetrain m_MecanumDrive;
  private Joystick m_stick;
  //private Joystick m_stickr;
  
  
  /** Creates a new TeleopDrive. */
  public TeleopDrive(Drivetrain subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_MecanumDrive = subsystem;
    addRequirements(m_MecanumDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_stick = new Joystick(OperatorConstants.DRIVER);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double yStick = -1 * m_stick.getY();
    double xStick = m_stick.getX();
    double zStick = m_stick.getZ();
    m_MecanumDrive.cartesianDrive(yStick, xStick, zStick);
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
