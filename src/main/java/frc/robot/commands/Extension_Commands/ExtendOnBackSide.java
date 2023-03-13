// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Extension_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.AutonState;
import frc.robot.Timer;
import frc.robot.Constants.ExtensionConstants;
import frc.robot.physics.PivotController;
import frc.robot.subsystems.Extension;
import frc.robot.subsystems.Pivot;

public class ExtendOnBackSide extends CommandBase {

  private final Extension m_Extension;
  private final Pivot m_Pivot;
  private Timer m_warmup_timer;
  private boolean first_stage_done = false;
  private boolean second_stage_done = false;
  private AutonState m_auton_state;

  /** Creates a new Extend. */
  public ExtendOnBackSide(Pivot pivot, Extension extension, AutonState auton_state) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Pivot = pivot;
    m_Extension = extension;
    m_auton_state = auton_state;
    addRequirements(m_Extension);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_warmup_timer = new Timer(500);
    m_Extension.resetEncoder();
    first_stage_done = false;
    second_stage_done = false;
  }

  private boolean onBackSide() {
    double revs_pivot = m_Pivot.getEncoder().getPosition();
    return revs_pivot > PivotController.UPRIGHT_PIVOT_TOLERANCE_BACK_SIDE + PivotController.UPRIGHT_PIVOT_VALUE;
  }

  private boolean moreOnBackSide() {
    double revs_pivot = m_Pivot.getEncoder().getPosition();
    return revs_pivot > PivotController.MAX_ENCODER_VALUE - 3.0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (onBackSide() && m_Extension.stillSpooledIn()) {
      m_Extension.setMotorPower(-ExtensionConstants.EXTEND_POWER);
    } else if (moreOnBackSide()) {
      if (!first_stage_done) {
        first_stage_done = true;
        m_Extension.resetEncoder();
      }
      if (m_Extension.stillSpooledInLess()) {
        m_Extension.setMotorPower(-ExtensionConstants.EXTEND_POWER);
      } else {
        second_stage_done = true;
        m_auton_state.setInStageTwo(true);
        m_Extension.setMotorPower(0.0);
      }
    } else if (m_warmup_timer.isReady()) {
      m_Extension.setMotorPower(0);
    } else {
      m_Extension.setMotorPower(-ExtensionConstants.RETRACT_POWER);
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
    return second_stage_done;
  }
}
