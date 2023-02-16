// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;
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
  private final Arm m_arm = new Arm();

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

    //Arm Buttons
    new JoystickButton(operator, OperatorConstants.HIGH_GOAL_BUTTON).onTrue(Commands.runOnce(
      () -> {
        m_arm.setGoal(Constants.ArmConstants.ARM_OFFSET_HIGH_RADS);
        m_arm.enable();
      }, 
      m_arm));

    new JoystickButton(operator, OperatorConstants.MED_GOAL_BUTTON).onTrue(Commands.runOnce(
      () -> {
        m_arm.setGoal(Constants.ArmConstants.ARM_OFFSET_MED_RADS);
        m_arm.enable();
      }, 
      m_arm));

    new JoystickButton(operator, OperatorConstants.LOW_GOAL_BUTTON).onTrue(Commands.runOnce(
      () -> {
        m_arm.setGoal(Constants.ArmConstants.ARM_OFFSET_LOW_RADS);
        m_arm.enable();
      }, 
      m_arm));
  
    new JoystickButton(operator, OperatorConstants.PLAYSTATION_BUTTON).onTrue(Commands.runOnce(
    () -> {
      m_arm.setGoal(Constants.ArmConstants.ARM_OFFSET_PLAYSTATION_RADS);
      m_arm.enable();
    }, 
    m_arm));
    
    new JoystickButton(operator, OperatorConstants.GROUND_BUTTON).onTrue(Commands.runOnce(
      () -> {
        m_arm.setGoal(Constants.ArmConstants.ARM_OFFSET_NUETRAL_RADS);
        m_arm.enable();
      }, 
      m_arm));
    
    new JoystickButton(operator, OperatorConstants.DISABLE_ARM_BUTTON).onTrue(Commands.runOnce(m_arm::disable));

    new JoystickButton(operator, OperatorConstants.SLOW_MODE_BUTTON)
    .onTrue(Commands.runOnce(() -> m_Drivetrain.setMaxOutput(0.5)))
    .onFalse(Commands.runOnce(() -> m_Drivetrain.setMaxOutput(1.0)));
  }

  public void disablePIDSubsystems() {
    m_arm.disable();
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
