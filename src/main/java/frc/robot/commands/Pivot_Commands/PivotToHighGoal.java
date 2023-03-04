// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Pivot_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.physics.PivotController;
import frc.robot.subsystems.Pivot;

public class PivotToHighGoal extends CommandBase {
  private Pivot m_pivot;
  private PivotController m_PivotController;
  private static final double BACKWARD_MOTOR_REQUEST = PivotController.MAX_MOTOR_VALUE;

  /** Creates a new PivotToHighGoal. */
  public PivotToHighGoal(Pivot subsystem, PivotController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_pivot = subsystem;
    m_PivotController = controller;
    addRequirements(m_pivot);
  }

  public double getRequestCommand() {
    double revs = m_pivot.getEncoder().getPosition();

    // If we are not on the back side with the pivot, keep going.
    if (revs < PivotController.UPRIGHT_PIVOT_VALUE + PivotController.UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE) {
      return BACKWARD_MOTOR_REQUEST;
    }

    // Otherwise zero motor request
    return 0.0;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_PivotController == null) {
      return;
    }

    double motorCommand = m_PivotController.controlLaw(0.0, getRequestCommand());
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
    if (m_PivotController == null) {
      return true;
    }

    return false;
  }
}
