package frc.robot.physics;

public class VirtualAngularDampener {
    // A VirtualAngularDampener simulates the physics of a dampener being
    // engaged via shaft travel through the dampener.
    // A dampener can be thought of as a section of the angular travel which when engaged / passed over
    // counter-acts the direction of travel with a torque. Air-resistance can be thought of as a type of
    // dampener for instance (the faster you go, the more resistence you encounter).
    // Dampeners are common in kitchen drawers in order to slow down fast moving shafts to avoid damage.
    // In this case, the shaft is angular, i.e. rotational speed is to be dampened.
    // @param dampener_constant - Constant which implies the amount of dampening to apply.
    // @param min_angular_position_deg - The minimum angle in the robot coordinate system for which the shaft is dampened.
    // @param max_angular_position_deg - The maximum angle in the robot coordinate system for which the shaft is dampened.
    
    private final double m_dampener_constant;
    private double m_min_angular_position_deg;
    private double m_max_angular_position_deg;

    public VirtualAngularDampener(double dampener_constant, double min_angular_position_deg, double max_angular_position_deg) { 
        assert dampener_constant > 0.0 : "Dampener constant is not positive!";
        m_dampener_constant = dampener_constant;
        m_min_angular_position_deg = min_angular_position_deg;
        m_max_angular_position_deg = max_angular_position_deg;

        // To run the asserts. Should run last so member vars are populated for the asserts.
        setMinAngularPositionDeg(min_angular_position_deg);
        setMaxAngularPositionDeg(max_angular_position_deg);
    }

    public double getMinAngularPositionDeg() {
        return m_min_angular_position_deg;
    }

    public void setMinAngularPositionDeg(double min_angular_position_deg) {
        assert (0.0 <= min_angular_position_deg && min_angular_position_deg < m_max_angular_position_deg) : "Invalid min angular position passed.";
        m_min_angular_position_deg = min_angular_position_deg;
    }

    public double getMaxAngularPositionDeg() {
        return m_max_angular_position_deg;
    }

    public void setMaxAngularPositionDeg(double max_angular_position_deg) {
        assert (m_min_angular_position_deg < max_angular_position_deg && max_angular_position_deg <= 360.0) : "Invalid max angular position.";
        m_max_angular_position_deg = max_angular_position_deg;
    }

    // Given the angle of the shaft with respect to the angular coordinate system of
    // the robot and the angular velocity of the shaft,
    // returns the torque applied by the dampener. The dampener torque is always
    // resistive, meaning it
    // is applied in the opposite direction of shaft travel and only if passing over the dampener.
    // Torque is measured in N*m, hence it is not bounded from [-1, 1].
    // The user needs to bound this manually.
    public double getTorque(double shaft_angle_deg, double shaft_angular_speed_deg_per_sec) {
        double dampener_torque = 0.0;

        if (m_min_angular_position_deg <= shaft_angle_deg && shaft_angle_deg <= m_max_angular_position_deg) {
            // Dampener is being engaged.
            dampener_torque = -m_dampener_constant * shaft_angular_speed_deg_per_sec;
        }

        return dampener_torque;
    }
}
