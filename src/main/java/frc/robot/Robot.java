// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.Drive_Commands.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private Drivetrain m_Drivetrain;

  private TeleopDrive m_TeleopDrive;

  public static final String kDefaultAuto1 = "Autonomous 1";
  public static final String kAuto2 = "Autonomous 2";
  public static final String kAuto3 = "Autonomous 3";
  public String m_autoSelected;
  public final SendableChooser<String> m_chooser = new SendableChooser<>();

  public int autonomousMode = 0;
  public String autonomousModestr = "null";

  double[] stickArray;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_Drivetrain = new Drivetrain();
    m_robotContainer = new RobotContainer();
    m_chooser.setDefaultOption("Autonomous 1", kDefaultAuto1);
    m_chooser.addOption("Autonomous 2", kAuto2);
    m_chooser.addOption("Autonomous 3", kAuto3);
    SmartDashboard.putData("Autonomous Modes", m_chooser);
    SmartDashboard.putData("Robot Drive Train", m_Drivetrain.m_robotDrive);
    CameraServer.startAutomaticCapture();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("Stick Y", m_Drivetrain.yStick);
    SmartDashboard.putNumber("Stick X", m_Drivetrain.xStick);
    SmartDashboard.putNumber("Stick Z", m_Drivetrain.zStick);
    SmartDashboard.putNumber("Pitch Value", m_Drivetrain.pitch_angle);
    SmartDashboard.putNumber("Roll Value", m_Drivetrain.roll_angle);
    SmartDashboard.putNumber("Yaw Value", m_Drivetrain.yaw_angle);
    SmartDashboard.putNumber("Y Accel Value", m_Drivetrain.accelY);
    SmartDashboard.putNumber("X Accel Value", m_Drivetrain.accelX);
    SmartDashboard.putNumber("Z Accel Value", m_Drivetrain.accelZ);
    SmartDashboard.putString("State", m_Drivetrain.m_BridgeBalancer.getState());
    


    m_autoSelected = m_chooser.getSelected();
    SmartDashboard.putString("Selected Autonomous", m_autoSelected);
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    SmartDashboard.putString("Mode", "Disabled");
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    SmartDashboard.putString("Mode", "Autonomous");
    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.

    m_TeleopDrive = new TeleopDrive(m_Drivetrain);

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    SmartDashboard.putString("Mode", "Teleoperated");
    m_TeleopDrive.schedule();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    SmartDashboard.putString("Mode", "Test");
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
