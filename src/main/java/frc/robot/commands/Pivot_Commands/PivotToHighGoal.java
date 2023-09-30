// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.AutonState;
import frc.robot.Timer;
import frc.robot.physics.PDPivotController;
import frc.robot.subsystems.Extension;
import frc.robot.subsystems.Pivot;

public class PivotToHighGoal extends CommandBase {
  private Pivot m_pivot;
  private PDPivotController m_PDPivotController;
  private Extension m_Extension;
  private Timer m_warmup_timer;
  private AutonState m_auton_state;

  /** Creates a new PivotToHighGoal. */
  public PivotToHighGoal(Pivot subsystem, PDPivotController controller, Extension extension, AutonState auton_state) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
    m_PDPivotController = controller;
    m_Extension = extension;
    m_auton_state = auton_state;
    addRequirements(m_pivot);
  }

  public double getRequestCommand() {
    double revs = m_pivot.getEncoder().getPosition();

    // If we are not on the back side with the pivot, keep going.
    if (!m_auton_state.inStageTwo() && revs < PDPivotController.UPRIGHT_PIVOT_VALUE + PDPivotController.UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE) {
      return PDPivotController.MAX_MOTOR_VALUE;
    }

    if (m_auton_state.inStageTwo() && m_Extension.isFullyRetracted() && revs > PDPivotController.UPRIGHT_PIVOT_VALUE - PDPivotController.UPRIGHT_PIVOT_TOLERANCE_FRONT_SIDE) {
      return -PDPivotController.MAX_MOTOR_VALUE;
    }

    // Otherwise zero motor request
    return 0.0;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_warmup_timer = new Timer(600);
    m_PDPivotController.resetPDController();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_PDPivotController == null) {
      return;
    }

    // Need to wait for the arm to retract before we can pivot up.
    if (!m_warmup_timer.isReady()) {
      m_pivot.setMotorPower(0.0);
      return;
    }

    double motorCommand = 0.0;
    if (getRequestCommand() >= 0) {
      motorCommand = m_PDPivotController.controlLaw(0.0, getRequestCommand());
    } else {
      motorCommand = m_PDPivotController.controlLaw(getRequestCommand(), 0.0);
    }
    m_pivot.setMotorPower(motorCommand);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_pivot.setMotorPower(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_PDPivotController == null) {
      return true;
    }

    return false;
  }
}
