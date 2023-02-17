// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  /*Drivetrain constants */
  public static final class DriveConstants {
    /*Motor ports */
    public static final int FL_MOTOR_PORT = 0;
    public static final int BL_MOTOR_PORT = 1;
    public static final int FR_MOTOR_PORT = 2;
    public static final int BR_MOTOR_PORT = 3;
  }
public static final class PivotConstants {
  /*Motor ports */
  public static final int PIVOT_MOTOR_PORT = 4;
  /*Potentiometer Values*/
  public static final int PIVOT_POT_PORT = 0;
  //360 degrees x 10 turns, scaling factor for the analog voltage
  public static final double POT_RANGE_DEGREES = 3600;

  public static final int MIN_ANGLE_LIMIT_PORT = 0;
  public static final int MAX_ANGLE_LIMIT_PORT = 1;

  public static final double MAX_PID_POWER = 0.5;
  public static final double MIN_PID_POWER = -0.5;

  public static final double MIN_ANGLE_PIVOT = 0;
  public static final double MAX_ANGLE_PIVOT = 360;

  public static final double PIVOT_SETPOINT_ANGLE_TOLERANCE = 1;
  public static final double PIVOT_SETPOINT_SPEED_TOLERANCE = 0.5;

  public static final double P = 0.01;
  public static final double I = 0.0;
  public static final double D = 0.0;

}


public static final class ExtensionConstants {
  /*Motor ports */
  public static final int EXTENSION_MOTOR_PORT = 5;
  /*Potentiometer Values*/
  public static final int EXTENSION_POT_PORT = 1;
  //360 degrees x 10 turns, scaling factor for the analog voltage
  public static final double RANGE_DEGREES = 3600;

  public static final int MIN_EXTENSION_LIMIT_PORT = 0;
  public static final int MAX_EXTENSION_LIMIT_PORT = 1;

  public static final double POT_ANGLE_TO_EXTENSION_DISTANCE_CM = 76.2 /*cm*/ / 3600 /*degs*/;
}

public static final class ClampConstants {
  /* Solenoid port */
  public static final int SOLENOID_PORT = 0;
}

  public static class OperatorConstants {
    public static final int DRIVER = 0;
    public static final int OPERATOR = 1;
    //public static final int ROTATER = 1;


    /* Controller Buttons */
    public static final int HIGH_GOAL_BUTTON = 0;
    public static final int MED_GOAL_BUTTON = 1;
    public static final int LOW_GOAL_BUTTON = 2;
    public static final int PLAYSTATION_BUTTON = 3;
    public static final int GROUND_BUTTON = 4;
    public static final int GRIP_BUTTON = 5;
    public static final int SLOW_MODE_BUTTON = 6;
    public static final int DISABLE_ARM_BUTTON = 7;

    public static final int PIVOT_UP_BUTTON = 9;
    public static final int PIVOT_DOWN_BUTTON = 10;
    public static final int EXTEND_BUTTON = 11;
    public static final int RETRACT_BUTTON = 12;
  }
}
