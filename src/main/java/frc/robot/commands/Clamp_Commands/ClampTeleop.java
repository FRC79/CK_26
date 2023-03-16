// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Clamp_Commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Clamp;

import frc.robot.Constants.*;

public class ClampTeleop extends CommandBase {

  private Clamp m_Clamp;
  private GenericHID m_operatorJoystick;
  
  
  public ClampTeleop(Clamp subsystem, GenericHID operatorJoystick) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Clamp = subsystem;
    m_operatorJoystick = operatorJoystick;
    assert operatorJoystick != null;
    addRequirements(m_Clamp);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_operatorJoystick.getRawButtonPressed(OperatorConstants.CLAMP_TOGGLE_BUTTON)) {
        if (m_Clamp.getState().equals("Forward")) {
            m_Clamp.setClampSolenoidState("Reverse");
        } else if (m_Clamp.getState().equals("Reverse")) {
            m_Clamp.setClampSolenoidState("Forward");
        } else {
            m_Clamp.setClampSolenoidState("Reverse");
        }
    }
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
