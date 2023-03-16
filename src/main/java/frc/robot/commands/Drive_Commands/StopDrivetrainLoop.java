// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drive_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.AutonState;
import frc.robot.Timer;
import frc.robot.physics.PivotController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Pivot;

public class StopDrivetrainLoop extends CommandBase {

  private final Drivetrain m_MecanumDrive;
  private AutonState m_auto_state;
  private final Pivot m_Pivot;
  private Timer m_move_forward_timer;
  private boolean stage_three_started = false;
  private boolean m_should_drive_forward = false;

  /** Creates a new DriveRight. */
  public StopDrivetrainLoop(Drivetrain subsystem, Pivot pivot, AutonState auto_state, boolean should_drive_forward) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_MecanumDrive = subsystem;
    m_auto_state = auto_state;
    m_Pivot = pivot;
    m_should_drive_forward = should_drive_forward;
    addRequirements(m_MecanumDrive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    stage_three_started = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double revs = m_Pivot.getEncoder().getPosition();
    if (!stage_three_started && m_auto_state.inStageTwo() && revs < PivotController.UPRIGHT_PIVOT_VALUE - PivotController.UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE) {
        stage_three_started = true;
        m_move_forward_timer = new Timer(2000);
        m_move_forward_timer.clear();
    }

    if (stage_three_started && !m_move_forward_timer.isReady() && m_should_drive_forward) {
        m_MecanumDrive.cartesianDrive(0.5, 0, 0);
        return;
    }

    m_MecanumDrive.cartesianDrive(0, 0, 0);
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
