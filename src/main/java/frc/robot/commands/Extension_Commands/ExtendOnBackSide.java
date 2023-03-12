// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Extension_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ExtensionConstants;
import frc.robot.physics.PivotController;
import frc.robot.subsystems.Extension;
import frc.robot.subsystems.Pivot;

public class ExtendOnBackSide extends CommandBase {

  private final Extension m_Extension;
  private final Pivot m_Pivot;
  /** Creates a new Extend. */
  public ExtendOnBackSide(Pivot pivot, Extension extension) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Pivot = pivot;
    m_Extension = extension;
    addRequirements(m_Extension);
    addRequirements(m_Pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  private boolean onBackSide() {
    double revs_extension = m_Extension.getEncoder().getPosition();
    return revs_extension > PivotController.UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE + PivotController.UPRIGHT_PIVOT_VALUE;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (onBackSide()) {
        m_Extension.setMotorPower(ExtensionConstants.EXTEND_POWER);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Extension.setMotorPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return onBackSide() && m_Extension.isFullyExtended();
  }
}
