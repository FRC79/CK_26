// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Extension_Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Timer;
import frc.robot.subsystems.Extension;
import frc.robot.subsystems.Pivot;

public class ExtendOnTopLimit extends CommandBase {

  private final Extension m_Extension;
  private final Pivot m_Pivot;
  private Timer m_timer;
  private boolean m_limitPressedOnce = false;
  private static final double IS_MOVING_TOLERANCE_RPM = 0.5;
  private static final int WARMUP_TIME_MS = 200;
  /** Creates a new Extend. */
  public ExtendOnTopLimit(Pivot pivot, Extension extension) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Pivot = pivot;
    m_Extension = extension;
    addRequirements(m_Extension);
    addRequirements(m_Pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_Pivot.highGoalTopLimitPressed()) {
        if (!m_limitPressedOnce) {
            // Timer to make sure we extend a bit before looking at zero velocity as our indicator of extension
            m_timer = new Timer(WARMUP_TIME_MS);
            m_timer.clear();
        }
        m_limitPressedOnce = true;
    }

    if (m_limitPressedOnce) {
        m_Extension.setMotorPower(0.5);
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
    if (m_timer == null) {
        return false;
    }

    if (!m_timer.isReady()) {
        return false;
    }

    double rpm = m_Extension.getEncoder().getVelocity();

    // It's still moving.
    if (Math.abs(rpm) > IS_MOVING_TOLERANCE_RPM) {
        return false;
    }

    // It's gone all it can.
    return true;
  }
}
