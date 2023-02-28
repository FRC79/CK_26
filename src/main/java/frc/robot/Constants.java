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
  }

  public static final class PivotConstants {
    /* Motor ports */
    public static final int PIVOT_MOTOR_PORT = 4;

    public static final int[] HIGH_SOLENOID_PORT = new int[] { 0, 1 };
    public static final int[] LOW_SOLENOID_PORT = new int[] { 2, 3 };

  }

  public static final class ExtensionConstants {
    /* Motor ports */
    public static final int EXTENSION_MOTOR_PORT = 5;

  }

  public static final class ClampConstants {
    /* Solenoid port */
    public static final int[] CLAMP_SOLENOID_PORT = new int[] { 0, 1 };
  }

  public static class OperatorConstants {
    public static final int DRIVER = 0;
    public static final int OPERATOR = 1;

    /* Controller Buttons */
    public static final int TOGGLE_HIGH_PISTON_BUTTON = 3;
    public static final int TOGGLE_LOW_PISTON_BUTTON = 4;
    public static final int GRIP_BUTTON = 5;
    public static final int PIVOT_UP_BUTTON = 9;
    public static final int PIVOT_DOWN_BUTTON = 10;
    public static final int EXTEND_BUTTON = 11;
    public static final int RETRACT_BUTTON = 12;
  }
}
