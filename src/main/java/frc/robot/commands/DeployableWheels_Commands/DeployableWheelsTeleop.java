// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.DeployableWheels_Commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Timer;
import frc.robot.Constants.*;
import frc.robot.subsystems.DeployableWheels;

public class DeployableWheelsTeleop extends CommandBase {
  
  private DeployableWheels m_deployableWheels;
  private Timer endgameTimer;
  private Joystick m_translatorJoystick;

  public DeployableWheelsTeleop(DeployableWheels subsystem, Joystick translator) {
    m_deployableWheels = subsystem;
    m_translatorJoystick = translator;
    addRequirements(m_deployableWheels);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    endgameTimer = new Timer(DeployableWheelsConstants.WAIT_TIME_ENDGAME_MS);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!endgameTimer.isReady()) {
        m_deployableWheels.setMotorPower(0.0);
        return;
    }

    double yStick = -1 * m_translatorJoystick.getY();
    m_deployableWheels.setMotorPower(yStick); // TODO may need to throttle down.
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_deployableWheels.setMotorPower(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
