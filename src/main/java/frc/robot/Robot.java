// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.Drive_Commands.*;
import frc.robot.commands.Pivot_Commands.PivotTeleop;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  private TeleopDrive m_TeleopDrive;

  private PivotTeleop m_pivotTeleop;

  private UsbCamera camera1;
  private UsbCamera camera2;
  private VideoSink server;

  private Timer m_timer;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    camera1 = CameraServer.startAutomaticCapture(0);
    camera2 = CameraServer.startAutomaticCapture(1);
    server = CameraServer.getServer();

    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() { if (m_pivotTeleop != null) {m_pivotTeleop.cancel();} }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    m_TeleopDrive = new TeleopDrive(m_robotContainer.getDrivetrain());
    m_pivotTeleop = new PivotTeleop(m_robotContainer.getPivot());

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_TeleopDrive.schedule();

    m_pivotTeleop.init(m_robotContainer.getOperatorJoystick());
    m_pivotTeleop.schedule();

    m_timer = new Timer(100);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Switch camera source as we move the pivot.
    if (m_robotContainer.getPivot().getEncoder().getPosition() > m_pivotTeleop.UPRIGHT_PIVOT_VALUE) {
      server.setSource(camera2);
    } else {
      server.setSource(camera1);
    }

    // DEBUG INFO
    // if (m_timer.isReady()) {
    //   SmartDashboard.putNumber("Position (Revs)", m_robotContainer.getPivot().getEncoder().getPosition());
    //   SmartDashboard.putNumber("Velocity (RPM)", m_robotContainer.getPivot().getEncoder().getVelocity());
    //   SmartDashboard.putNumber("Current output", m_robotContainer.getPivot().getMotorOutput());
    //   SmartDashboard.putNumber("Extension Distance", m_robotContainer.getExtension().getExtensionDistance());
    //   SmartDashboard.putNumber("FrontLeftEncoderDistanceMeters",
    //       m_robotContainer.getDrivetrain().getFrontLeftDistanceMeters());
    //   SmartDashboard.putNumber("BackRightEncoderDistanceMeters",
    //       m_robotContainer.getDrivetrain().getBackRightDistanceMeters());
    //   SmartDashboard.putNumber("GyroPitchDegrees", m_robotContainer.getDrivetrain().getGyroPitchAngleDegrees());
    //   SmartDashboard.putNumber("CommandedPivotGravityAssist", m_pivotTeleop.getCommandedValue());
    //   SmartDashboard.putBoolean("Faulted", m_pivotTeleop.getFaulted());
    //   SmartDashboard.putNumber("CushionMotorValue", m_pivotTeleop.getCushionMotorPower());
    //   SmartDashboard.putNumber("JoystickPivotTotalValue", m_pivotTeleop.getJoystickTotalPower());
    //   m_timer.clear();
    // }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
