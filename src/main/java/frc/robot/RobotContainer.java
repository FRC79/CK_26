// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Pivot_Commands.*;
import frc.robot.commands.Clamp_Commands.*;
import frc.robot.commands.Drive_Commands.ToggleSlowMode;
import frc.robot.commands.Extension_Commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  // The robot's subsystems
  private final Drivetrain m_Drivetrain = new Drivetrain();
  private final Pivot m_Pivot = new Pivot();
  private final Extension m_Extension = new Extension();
  private final Clamp m_Clamp = new Clamp();

  // The operator's controller 
  public GenericHID operator = new Joystick(Constants.OperatorConstants.OPERATOR);

  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  public Drivetrain getDrivetrain() {
    return m_Drivetrain;
  }

  public Pivot getPivot() {
    return m_Pivot;
  }

  public Extension getExtension() {
    return m_Extension;
  }

  public Clamp getClamp() {
    return m_Clamp;
  }

  public GenericHID getOperatorJoystick() {
    return operator;
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    // NOTE: DO NOT USE THESE
    // new JoystickButton(operator, OperatorConstants.PIVOT_TOWARDS_ROBOT_FRONT_BUTTON).whileTrue(new PivotTowardsRobotFront(m_Pivot));
    // new JoystickButton(operator, OperatorConstants.PIVOT_TOWARDS_ROBOT_BACK_BUTTON).whileTrue(new PivotTowardsRobotBack(m_Pivot));

    new JoystickButton(operator, OperatorConstants.EXTENSION_BUTTON).whileTrue(new Extend(m_Extension));

    new JoystickButton(operator, OperatorConstants.RETRACTION_BUTTON).whileTrue(new Retract(m_Extension));

    new JoystickButton(operator, OperatorConstants.CLAMP_OPEN_BUTTON).whileTrue(new OpenClamp(m_Clamp));

    new JoystickButton(operator, OperatorConstants.CLAMP_CLOSE_BUTTON).whileTrue(new CloseClamp(m_Clamp));

    // new JoystickButton(operator, OperatorConstants.HARD_STOP_HIGH_TOGGLE_BUTTON).onTrue(new ToggleHighSolenoid(m_Pivot));

    // new JoystickButton(operator, OperatorConstants.HARD_STOP_LOW_TOGGLE_BUTTON).onTrue(new ToggleLowSolenoid(m_Pivot));

    // new JoystickButton(operator, OperatorConstants.SLOW_MODE_BUTTON).onTrue(new ToggleSlowMode(m_Drivetrain));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

   public Command getAutonomousCommand() {
    return Commands.none();
   }
}
