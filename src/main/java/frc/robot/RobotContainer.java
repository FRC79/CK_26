// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Pivot_Commands.*;
import frc.robot.commands.Clamp_Commands.*;
import frc.robot.commands.Drive_Commands.DriveForward;
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

    new JoystickButton(operator, OperatorConstants.PIVOT_UP_BUTTON).whileTrue(new PivotUp(m_Pivot)).whileFalse(new PivotStop(m_Pivot));

    new JoystickButton(operator, OperatorConstants.PIVOT_UP_BUTTON).whileTrue(new PivotDown(m_Pivot)).whileFalse(new PivotStop(m_Pivot));

    new JoystickButton(operator, OperatorConstants.EXTEND_BUTTON).whileTrue(new Extend(m_Extension)).whileFalse(new ExtensionStop(m_Extension));

    new JoystickButton(operator, OperatorConstants.RETRACT_BUTTON).whileTrue(new Retract(m_Extension)).whileFalse(new ExtensionStop(m_Extension));

    new JoystickButton(operator, OperatorConstants.GRIP_BUTTON).toggleOnTrue(new EnableClamp(m_Clamp)).toggleOnFalse(new DisableClamp(m_Clamp));

    //new JoystickButton(operator, OperatorConstants.AUTODRIVE_BUTTON).onTrue(new DriveForward(m_Drivetrain).withTimeout(5));
    
    new JoystickButton(operator, OperatorConstants.SLOW_MODE_BUTTON)
    .onTrue(Commands.runOnce(() -> m_Drivetrain.setMaxOutput(0.5)))
    .onFalse(Commands.runOnce(() -> m_Drivetrain.setMaxOutput(1.0)));
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
