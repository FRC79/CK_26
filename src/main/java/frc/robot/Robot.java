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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain;
import frc.robot.commands.Drive_Commands.TeleopDrive;
import frc.robot.commands.Extension_Commands.ExtendOnBackSide;
import frc.robot.commands.Extension_Commands.RetractForTime;
import frc.robot.commands.Extension_Commands.RetractFully;
import frc.robot.commands.Extension_Commands.RetractUntilPivotAngle;
import frc.robot.commands.Clamp_Commands.ClampTeleop;
import frc.robot.commands.Clamp_Commands.OpenClamp;
import frc.robot.commands.Clamp_Commands.WaitThenOpenClamp;
import frc.robot.commands.DeployableWheels_Commands.DeployableWheelsTeleop;
import frc.robot.commands.DeployableWheels_Commands.StopDeployableWheelsLoop;
import frc.robot.commands.Drive_Commands.DriveForwardForTime;
import frc.robot.commands.Drive_Commands.StopDrivetrainLoop;
import frc.robot.commands.Pivot_Commands.PivotTeleop;
import frc.robot.commands.Pivot_Commands.PivotToHighGoal;
import frc.robot.physics.PivotController;
import edu.wpi.first.wpilibj2.command.Commands;

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
  private DeployableWheelsTeleop m_DeployableWheelsTeleop;
  private ClampTeleop m_clampTeleop;

  private UsbCamera camera1;
  private UsbCamera camera2;
  private VideoSink server;

  private AutonState auton_state;

  private Timer m_timer;

  private static final String kDoNothingAuto = "Do Nothing";
  private static final String kScoreHighGoal = "Score High Goal Then Stop";
  private static final String kScoreHighGoalAndDrive = "Score High Goal Then Drive";

  private String m_autoSelected;
  private final SendableChooser<String> m_auto_chooser = new SendableChooser<>();

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

    m_auto_chooser.setDefaultOption(kDoNothingAuto, kDoNothingAuto);
    m_auto_chooser.addOption(kScoreHighGoal, kScoreHighGoal);
    m_auto_chooser.addOption(kScoreHighGoalAndDrive, kScoreHighGoalAndDrive);
    SmartDashboard.putData("Autonomous Mode Chooser", m_auto_chooser);

    camera1 = CameraServer.startAutomaticCapture(0);
    // camera2 = CameraServer.startAutomaticCapture(1);
    camera1.setResolution(640, 480);
    // camera2.setResolution(320, 240);
    camera1.setFPS(15);
    // camera2.setFPS(15);
    server = CameraServer.getServer();

    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    // camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

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
  public void disabledInit() {
    if (m_pivotTeleop != null) {
      m_pivotTeleop.cancel();
      m_pivotTeleop = null;
    }

    if (m_DeployableWheelsTeleop != null) {
      m_DeployableWheelsTeleop.cancel();
      m_DeployableWheelsTeleop = null;
    }

    if (m_TeleopDrive != null) {
      m_TeleopDrive.cancel();
      m_TeleopDrive = null;
    }

    if (m_clampTeleop != null) {
      m_clampTeleop.cancel();
      m_clampTeleop = null;
    }

    // For easy access to robot.
    m_robotContainer.getExtension().setCoastMode();
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_robotContainer.getExtension().setBrakeMode();
    auton_state = new AutonState();

    m_autoSelected = m_auto_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    Command pivot_to_high_goal_command = new PivotToHighGoal(m_robotContainer.getPivot(),
        m_robotContainer.getPivotController(), m_robotContainer.getExtension(), auton_state);

    Command extend_on_back_side_command = new ExtendOnBackSide(m_robotContainer.getPivot(),
        m_robotContainer.getExtension(), auton_state);

    Command open_clamp_command = new WaitThenOpenClamp(m_robotContainer.getClamp());

    Command fully_retract_command = new RetractFully(m_robotContainer.getExtension());

    Command extendAndReleaseCommand = extend_on_back_side_command.andThen(open_clamp_command)
        .andThen(fully_retract_command);

    Command do_nothing_deployable_wheels_command = new StopDeployableWheelsLoop(
        m_robotContainer.getDeployableWheels());

    if (m_autoSelected.equals(kScoreHighGoal)) {
      Command do_nothing_drive_command = new StopDrivetrainLoop(m_robotContainer.getDrivetrain(),
          m_robotContainer.getPivot(), auton_state, false);

      m_autonomousCommand = Commands.parallel(pivot_to_high_goal_command,
          extendAndReleaseCommand, do_nothing_drive_command, do_nothing_deployable_wheels_command);

    } else if (m_autoSelected.equals(kScoreHighGoalAndDrive)) {
      Command wait_then_drive_command = new StopDrivetrainLoop(m_robotContainer.getDrivetrain(),
          m_robotContainer.getPivot(), auton_state, true);

      m_autonomousCommand = Commands.parallel(pivot_to_high_goal_command,
          extendAndReleaseCommand, wait_then_drive_command, do_nothing_deployable_wheels_command);
    } else {
      m_autonomousCommand = Commands.none();
    }

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

    m_timer = new Timer(100);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (m_timer.isReady()) {
      SmartDashboard.putString("RunningAutonomous", m_autoSelected);
      m_timer.clear();
    }
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    m_robotContainer.getExtension().setBrakeMode();
    m_robotContainer.getExtension().resetEncoder();

    m_TeleopDrive = new TeleopDrive(m_robotContainer.getDrivetrain(), m_robotContainer.getTranslatorJoystick(),
        m_robotContainer.getRotaterJoystick());
    m_pivotTeleop = new PivotTeleop(m_robotContainer.getPivot(), m_robotContainer.getPivotController(),
        m_robotContainer.getOperatorJoystick());
    m_DeployableWheelsTeleop = new DeployableWheelsTeleop(m_robotContainer.getDeployableWheels(),
        m_robotContainer.getTranslatorJoystick());
    m_clampTeleop = new ClampTeleop(m_robotContainer.getClamp(), m_robotContainer.getOperatorJoystick());

    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_TeleopDrive.schedule();
    m_pivotTeleop.schedule();
    m_DeployableWheelsTeleop.schedule();
    m_clampTeleop.schedule();

    m_timer = new Timer(100);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // DEBUG INFO
    // if (m_timer.isReady()) {
    //   SmartDashboard.putNumber("PotRaw", m_robotContainer.getExtension().getPot());
    //   SmartDashboard.putNumber("PotClamped",
    //       m_robotContainer.getExtension().getClampedPot());
    //   SmartDashboard.putBoolean("IsFullyExtended",
    //       m_robotContainer.getExtension().isFullyExtended());
    //   SmartDashboard.putBoolean("IsFullyRetracted",
    //       m_robotContainer.getExtension().isFullyRetracted());
    //   SmartDashboard.putNumber("ExtensionRevs",
    //       m_robotContainer.getExtension().getEncoder().getPosition());
    //   SmartDashboard.putBoolean("StillSpooledIn",
    //       m_robotContainer.getExtension().stillSpooledIn());
    //   SmartDashboard.putNumber("CalibratedEncoderExtension",
    //       m_robotContainer.getExtension().getCalibratedEncoderPosition());
    //   SmartDashboard.putNumber("Position (Revs)",
    //       m_robotContainer.getPivot().getEncoder().getPosition());
    //   SmartDashboard.putNumber("Velocity (RPM)",
    //       m_robotContainer.getPivot().getEncoder().getVelocity());
    //   SmartDashboard.putNumber("Current output",
    //       m_robotContainer.getPivot().getMotorOutput());
    //   SmartDashboard.putNumber("FrontLeftEncoderDistanceMeters",
    //       m_robotContainer.getDrivetrain().getFrontLeftDistanceMeters());
    //   SmartDashboard.putNumber("BackRightEncoderDistanceMeters",
    //       m_robotContainer.getDrivetrain().getBackRightDistanceMeters());
    //   SmartDashboard.putNumber("GyroPitchDegrees",
    //       m_robotContainer.getDrivetrain().getGyroPitchAngleDegrees());
    //   SmartDashboard.putNumber("CommandedPivotGravityAssist",
    //       m_pivotTeleop.getCommandedValue());
    //   SmartDashboard.putBoolean("Faulted", m_pivotTeleop.getFaulted());
    //   SmartDashboard.putNumber("CushionMotorValue",
    //       m_pivotTeleop.getCushionMotorPower());
    //   SmartDashboard.putNumber("JoystickPivotTotalValue",
    //       m_pivotTeleop.getTotalMotorCommanded());
    //   m_timer.clear();
    // }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
    m_timer = new Timer(100);
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if (m_timer.isReady()) {
      for (int i = 1; i < 12; i++) {
        SmartDashboard.putBoolean("KrunchButton" + Integer.toString(i),
            m_robotContainer.getTranslatorJoystick().getRawButton(i));
      }
      m_timer.clear();
    }
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
