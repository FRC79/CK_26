package frc.robot.physics;

public class VirtualAngularCushionSpring {
    // A VirtualAngularCushionSpring simulates the physics of a spring being
    // compressed via an object not attached to the spring.
    // As the object falls onto the spring, the spring compresses and produces an
    // opposite torque proportional to the angular displacement
    // of the spring from its initial "at-rest" length. The spring naturally is able
    // to compress from Length=rest_length_deg to
    // Length=min_length_deg, but not more than that (otherwise the spring would be
    // flattened).
    // The spring is not allowed to extend pass L=rest_length_deg as the object
    // would in this case be leaving contact with the spring.
    //
    // @param hooke_constant - The spring constant, implies stiffness of the spring
    // and how hard it is to compress (see
    // https://en.wikipedia.org/wiki/Hooke%27s_law).
    // @param rest_length_deg - The length of the spring when no objects sit upon
    // it. Measured in degrees since this is an angular spring.
    // @param min_length_deg - The minimum length the spring can be compressed to,
    // or else it will flatten. Measured in degrees since this is an angular spring.
    // @param anchor_position_deg - The angular position in the coordinate system
    // the spring is attached to the robot frame (virtually).
    // For instance, if the spring is attached to the bottom of the robot, the
    // anchor_position_deg would likely be 0.
    // @param anchor_side - The side of the spring which is anchored to the robot
    // frame (virtually). Assumes vertical mounting.

    enum AnchorSide {
        TOP_OF_SPRING,
        BOTTOM_OF_SPRING
    }

    private final double m_hooke_constant;
    private final double m_rest_length_deg;
    private final double m_min_length_deg;
    private double m_anchor_position_deg;
    private final AnchorSide m_anchor_side;

    public VirtualAngularCushionSpring(double hooke_constant, double rest_length_deg, double min_length_deg,
            double anchor_position_deg, AnchorSide anchor_side) {

        assert hooke_constant > 0.0 : "Hooke constant is not positive!";
        assert (0.0 < min_length_deg && min_length_deg < rest_length_deg && rest_length_deg < 360.0)
                : "0 < min_length_deg < rest_length_deg < 360 does not hold!";

        m_hooke_constant = hooke_constant;
        m_rest_length_deg = rest_length_deg;
        m_min_length_deg = min_length_deg;
        m_anchor_side = anchor_side;

        // Needs to be set last.
        setAnchorPositionDeg(anchor_position_deg);
    }

    public void setAnchorPositionDeg(double anchor_position_deg) {
        if (m_anchor_side == AnchorSide.BOTTOM_OF_SPRING) {
            double max_possible_position_deg = 360.0 - m_rest_length_deg;
            assert (0.0 < anchor_position_deg && anchor_position_deg < max_possible_position_deg)
                    : "0 < anchor_position_deg < max_possible_position_deg does not hold!";
        } else {
            double min_possible_position_deg = m_rest_length_deg;
            assert (min_possible_position_deg < anchor_position_deg && anchor_position_deg < 360.0)
                    : "min_possible_position_deg < anchor_position_deg < 360 does not hold!";
        }
        m_anchor_position_deg = anchor_position_deg;
    }

    public double getAnchorPositionDeg() {
        return m_anchor_position_deg;
    }

    // Given the angle of the shaft with respect to the angular coordinate system of
    // the robot,
    // returns the angular displacement of the spring in degrees.
    // abs(displacement_deg) should be in the
    // range delta_theta in [0 deg, rest_length_deg - min_length_deg].
    public double getDisplacement(double shaft_angle_deg) {
        double displacement_deg = 0.0;

        if (m_anchor_side == AnchorSide.BOTTOM_OF_SPRING) {
            // Shaft is compressing the spring.
            // For virtual spring, consider all compressions beyond flattening to be
            // constant.
            if (m_anchor_position_deg < shaft_angle_deg
                    && shaft_angle_deg < (m_anchor_position_deg + m_rest_length_deg)) {
                displacement_deg = Math.min(shaft_angle_deg - (m_anchor_position_deg + m_rest_length_deg),
                        m_rest_length_deg - m_min_length_deg);
            }
        } else {
            // Shaft is compressing the spring.
            // For virtual spring, consider all compressions beyond flattening to be
            // constant.
            if ((m_anchor_position_deg - m_rest_length_deg) < shaft_angle_deg
                    && shaft_angle_deg < m_anchor_position_deg) {
                displacement_deg = Math.min(shaft_angle_deg - (m_anchor_position_deg - m_rest_length_deg),
                        m_rest_length_deg - m_min_length_deg);
            }
        }

        assert (0.0 <= Math.abs(displacement_deg)
                && Math.abs(displacement_deg) <= (m_rest_length_deg - m_min_length_deg))
                : "delta_theta not in [0 deg, rest_length_deg - min_length_deg]";
        return displacement_deg;
    }

    // Given the angle of the shaft with respect to the angular coordinate system of
    // the robot,
    // returns the torque applied by the spring. The spring torque is always
    // resistive, meaning it
    // is applied in the opposite direction of compression and only if compression
    // occurs.
    // Torque is measured in N*m, hence it is not bounded from [-1, 1].
    // The user needs to bound this manually.
    public double getTorque(double shaft_angle_deg) {
        double spring_torque = -m_hooke_constant * getDisplacement(shaft_angle_deg);

        // Ensure spring torque is always resistive, i.e. pushing against compression.
        if (m_anchor_side == AnchorSide.BOTTOM_OF_SPRING) {
            assert spring_torque >= 0.0 : "Spring torque is applied in an invalid direction.";
        } else {
            assert spring_torque <= 0.0 : "Spring torque is applied in an invalid direction.";
        }
        return spring_torque;
    }
}
