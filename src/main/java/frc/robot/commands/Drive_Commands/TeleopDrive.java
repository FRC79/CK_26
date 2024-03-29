// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive_Commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.Timer;
import frc.robot.Constants.*;

public class TeleopDrive extends CommandBase {

  private Drivetrain m_MecanumDrive;
  private Joystick m_translater;
  private Joystick m_rotater;
  private Timer endgameTimer;

  /** Creates a new TeleopDrive. */
  public TeleopDrive(Drivetrain subsystem, Joystick translator, Joystick rotater) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_MecanumDrive = subsystem;
    m_translater = translator;
    m_rotater = rotater;
    addRequirements(m_MecanumDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_MecanumDrive.resetEncoders();
    m_MecanumDrive.resetGyro();
    endgameTimer = new Timer(DeployableWheelsConstants.WAIT_TIME_ENDGAME_MS);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_translater.getRawButton(OperatorConstants.BRAKE_DRIVETRAIN_BUTTON) && endgameTimer.isReady()) {
      m_MecanumDrive.cartesianDrive(0, 0, 0);
      return;
    }

    double yStick = -1 * m_translater.getY();
    double xStick = m_translater.getX();
    double zStick = m_rotater.getX();

    m_MecanumDrive.cartesianDrive(yStick, xStick, zStick);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
