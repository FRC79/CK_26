// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

  /* Drivetrain constants */
  public static final class DriveConstants {
    /* Motor ports */
    public static final int FL_MOTOR_PORT = 0;
    public static final int BL_MOTOR_PORT = 1;
    public static final int FR_MOTOR_PORT = 2;
    public static final int BR_MOTOR_PORT = 3;

    /* Drive mode powers */
    public static final double FAST_MODE_DRIVE_POWER = 1.0;
    public static final double SLOW_MODE_DRIVE_POWER = 0.5;

    // Encoders
    public static final int[] FL_ENCODER_PORTS = new int[] {1, 0};
    public static final int[] BR_ENCODER_PORTS = new int[] {9, 8};

    public static final boolean FL_ENCODER_REVERSED = false;
    public static final boolean BR_ENCODER_REVERSED = true;

    // therefore pi * 0.1524 == 0.478
    public static final double DRIVE_ENCODER_CPR = 1024; // Clicks per rev, 2^10
    public static final double WHEEL_DIAMETER_METERS = 0.1524; // 6" wheels
    public static final double ENCODER_DISTANCE_PER_PULSE_METERS = (WHEEL_DIAMETER_METERS * Math.PI)/ DRIVE_ENCODER_CPR;
  }

  public static final class PivotConstants {
    /* Motor ports */
    public static final int PIVOT_MOTOR_PORT = 4;

    public static final int[] HIGH_SOLENOID_PORT = new int[] { 0, 1 };
    public static final int[] LOW_SOLENOID_PORT = new int[] { 4, 5 };

    public static final double kMAX_OUTPUT_MAG_VELOCITY = 0.2;
  }

  public static final class ExtensionConstants {
    /* Motor ports */
    public static final int EXTENSION_MOTOR_PORT = 5;

    /* Potentiometer Values */
    public static final int EXTENSION_POT_PORT = 0;
    // // 360 degrees x 10 turns, scaling factor for the analog voltage
    public static final double RANGE_DEGREES = 3600;
    public static final double POT_ANGLE_TO_EXTENSION_DISTANCE_CM = 76.2 / RANGE_DEGREES; // CM / DEG
  }

  public static final class ClampConstants {
    /* Solenoid port */
    public static final int[] CLAMP_SOLENOID_PORT = new int[] { 6, 7 };
  }

  public static class OperatorConstants {
    public static final int DRIVER_TRANSLATER = 0;
    public static final int DRIVER_ROTATER = 1;
    public static final int OPERATOR = 2;

    /* Controller Buttons */
    // Extension buttons
    public static final int EXTENSION_BUTTON = 5;
    public static final int RETRACTION_BUTTON = 7;
 
    // Pivot rotation buttons
    public static final int PIVOT_TOWARDS_ROBOT_FRONT_BUTTON = 8;
    public static final int PIVOT_TOWARDS_ROBOT_BACK_BUTTON = 6;

    // Clamp buttons
    public static final int CLAMP_OPEN_BUTTON = 1;
    public static final int CLAMP_CLOSE_BUTTON = 4;

    // Height pistons for pivot
    public static final int HARD_STOP_HIGH_TOGGLE_BUTTON = 10;
    public static final int HARD_STOP_LOW_TOGGLE_BUTTON = 9;

    // Slow mode for driving the wheels
    public static final int SLOW_MODE_BUTTON = 11;
  }
}
