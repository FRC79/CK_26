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

  public static final class ArmConstants {
    /*Motor ports */
    public static final int PIVOT_MOTOR_PORT = 10;

    // PID values
    public static final double Kp = 1;
    public static final double Ki = 0;
    public static final double Kd = 0;

    /*Encoder constants */
    public static final int[] ENCODER_PORTS = new int[] {0,1};

    //Pulses per revolution
    public static final int ENCODER_PPR = 256;
    public static final double ENCODER_DISTANCE_PER_PULSE = 2.0 *Math.PI / ENCODER_PPR;

    //values for armfeedforward
    public static final double S_VOLTS = 1;
    public static final double G_VOLTS = 1;
    public static final double V_VOLT_SECOND_PER_RAD = 0.5;
    public static final double A_VOLT_SECOND_SQUARED_PER_RAD = 0.1;

    //Max velocity and acceleration for arms
    public static final double MAX_VELOCITY_RAD_PER_SECOND = 3;
    public static final double MAX_ACCELERATION_RAD_PER_SECOND_SQAURED = 10;

    //The offset of the arm from the horizontal in its neutral position
    // measured from the horizontal in radians
    public static final double ARM_OFFSET_NUETRAL_RADS = 0.5;
    public static final double ARM_OFFSET_HIGH_RADS = 0.5;
    public static final double ARM_OFFSET_MED_RADS = 0.5;
    public static final double ARM_OFFSET_LOW_RADS = 0.5;
    public static final double ARM_OFFSET_PLAYSTATION_RADS = 0.5;
  }

public static final class PivotConstants {
  /*Motor ports */
  public static final int PIVOT_MOTOR_PORT = 10;
  /*Potentiometer Values*/
  public static final int POT_PORT = 0;
  public static final double RANGE_DEGREES = 30;
  public static final double OFFSET_DEGREES = 180;
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
  }
}
