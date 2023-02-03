package frc.robot.commands.Drive_Commands;

import java.time.Instant;

import frc.robot.Timer;
public class BridgeBalancer {

    private float m_angle_degrees;
    private float m_deriv_angle_degrees;
    private float angle_last;
    private float m_acceleration_mps2;
    private int s_last;
    private float y_power;
    private boolean boarded = false;
    private float ERROR_CONSTANT_DEGREES = 3;
    private float MAX_ANGLE_CONSTANT = 90;

    private Timer timer;

    public String state;
    
    // Fully Carpet(FC), Boarding(BD), Falling Backwards(FB), Bridge Falling(BF),
    // Engaging(E), Balance(B)
    // Plus: Front of ramp
    // Minus: Back of ramp
    private final int STATE_FC_PLUS = 0;
    private final int STATE_BD_PLUS = 1;
    private final int STATE_FB_PLUS = 2;
    private final int STATE_BF_PLUS = 3;
    private final int STATE_E = 4;
    private final int STATE_B = 5;
    private final int STATE_FC_MINUS = 6;
    private final int STATE_BD_MINUS = 7;
    private final int STATE_FB_MINUS = 8;
    private final int STATE_BF_MINUS = 9;

    // constructor
    public BridgeBalancer() {
        s_last = STATE_FC_PLUS;
        timer = new Timer(500);
    }

    // State functions
    private void NextStateFCPLUS() throws Exception{
        if (IsWithin(ERROR_CONSTANT_DEGREES, Math.abs(m_angle_degrees), MAX_ANGLE_CONSTANT)) {
            s_last = STATE_BD_PLUS;
        } 
        else if (Math.abs(m_angle_degrees) < ERROR_CONSTANT_DEGREES) {
            s_last = STATE_FC_PLUS;
        }
        else {
            throw new Exception("We reached an invalid angle: " + m_angle_degrees);
        }
    }

    private void NextStateBDPLUS() throws Exception{

        if (m_acceleration_mps2 >= 3 && m_deriv_angle_degrees < 0) {
            s_last = STATE_BF_PLUS;
        } else if (m_acceleration_mps2 < 0) {
            s_last = STATE_FB_PLUS;
        } 
        else if (m_acceleration_mps2 < 0 && m_deriv_angle_degrees > 0){
            s_last = STATE_BD_PLUS;
        }
        else {
            throw new Exception("We reached an invalid acceleration: " + m_angle_degrees + " or change in angle: " + m_deriv_angle_degrees);
        }
    }

    private void NextStateFBPLUS() throws Exception{
        if (Math.abs(m_angle_degrees) < ERROR_CONSTANT_DEGREES) {
            s_last = STATE_FC_PLUS;
        } else if (m_acceleration_mps2 > 0) {
            s_last = STATE_BD_PLUS;
        } else if (m_acceleration_mps2 < 0) {
            s_last = STATE_FB_PLUS;
        }
        else {
            throw new Exception("We reached an invalid acceleration: " + m_angle_degrees + " or angle: " + m_angle_degrees);
        }
    }

    private void NextStateBFPLUS() {
        if (Math.abs(m_angle_degrees) <= ERROR_CONSTANT_DEGREES) {
            s_last = STATE_E;
        } else if (m_acceleration_mps2 >= 3 && Math.abs(m_angle_degrees) >= ERROR_CONSTANT_DEGREES){
            s_last = STATE_BF_PLUS;
        }
    }

    private void NextStateE() {
        if (Math.abs(m_angle_degrees) <= ERROR_CONSTANT_DEGREES && Math.abs(m_acceleration_mps2) <= 1) {
            s_last = STATE_B;
        } else if (m_acceleration_mps2 < 0 && m_angle_degrees > 0) {
            s_last = STATE_FB_PLUS;
        } else if (m_acceleration_mps2 > 0 && m_angle_degrees < 0) {
            s_last = STATE_FB_MINUS;
        } else {
            s_last = STATE_E;
        }
    }

    private void NextStateB() {
        boarded = true;
    }

    private void NextStateBFMINUS() {
        if (Math.abs(m_angle_degrees - 0) <= 1) {
            s_last = STATE_E;
        } else {
            s_last = STATE_BF_MINUS;
        }
    }

    private void NextStateFBMINUS() {
        if (Math.abs(m_angle_degrees - 0) <= 1) {
            s_last = STATE_FC_MINUS;
        } else if (m_acceleration_mps2 < 0) {
            s_last = STATE_BD_MINUS;
        } else {
            s_last = STATE_FB_MINUS;
        }
    }

    private void NextStateBDMINUS() {
        if (m_acceleration_mps2 < 0 && m_deriv_angle_degrees > 0) {
            s_last = STATE_BF_MINUS;
        } else if (m_acceleration_mps2 > 0) {
            s_last = STATE_FB_MINUS;
        } else {
            s_last = STATE_BD_MINUS;
        }
    }

    private void NextStateFCMINUS() {
        if (Math.abs(m_angle_degrees - angle_last) >= 1) {
            s_last = STATE_BD_MINUS;
        } else {
            s_last = STATE_FC_MINUS;
        }
    }

    public float GetAction() throws Exception{
        switch (s_last) {
            case (STATE_FC_PLUS):
                y_power = 1.0f;
                state = "Fully Carpeted +";
                break;
            case (STATE_BD_PLUS):
                y_power = 1.0f;
                state = "Boarding +";
                break;
            case (STATE_FB_PLUS):
                y_power = 1.0f;
                state = "Falling Backwards +";
                break;
            case (STATE_BF_PLUS):
                y_power = 0.2f;
                state = "Bridge Falling +";
                break;
            case (STATE_E):
                //PID might go here
                y_power = 0;
                state = "Engaging";
                break;
            case (STATE_B):
                y_power = 0;
                state = "Balance";
                break;
            case (STATE_FC_MINUS):
                y_power = -1.0f;
                state = "Fully Carpeted -";
                break;
            case (STATE_BD_MINUS):
                y_power = -1.0f;
                state = "Boarding -";
                break;
            case (STATE_FB_MINUS):
                y_power = -1.0f;
                state = "Falling Backwards -";
                break;
            case (STATE_BF_MINUS):
                y_power = -0.2f;
                state = "Bridge Falling -";
                break;
            default:
                throw new Exception("We reached an invalid state: " + s_last);
        }
        return y_power;
    }

    public boolean IsWithin (float lowerAngle, float currentAngle, float upperAngle) {
        return (lowerAngle <= currentAngle && currentAngle <= upperAngle);
    }

    public float GetAngleDeriv (){
        if (timer.TimeElasped() == 0) {
            m_deriv_angle_degrees = 0;
        }
        else{
            m_deriv_angle_degrees = ((m_angle_degrees-angle_last)/timer.TimeElasped());
        }
        timer.clear();
        return m_deriv_angle_degrees;
    }

    public String getState(){
        return state;
    }

    // Update State functions
    public void UpdateState(float angle, float acceleration) throws Exception {
        angle_last = m_angle_degrees;
        m_angle_degrees = angle;
        m_deriv_angle_degrees = GetAngleDeriv();
        m_acceleration_mps2 = acceleration;
        switch (s_last) {
            case (STATE_FC_PLUS):
                NextStateFCPLUS();
                break;
            case (STATE_BD_PLUS):
                NextStateBDPLUS();
                break;
            case (STATE_FB_PLUS):
                NextStateFBPLUS();
                break;
            case (STATE_BF_PLUS):
                NextStateBFPLUS();
                break;
            case (STATE_E):
                NextStateE();
                break;
            case (STATE_B):
                NextStateB();
                break;
            case (STATE_FC_MINUS):
                NextStateFCMINUS();
                break;
            case (STATE_BD_MINUS):
                NextStateBDMINUS();
                break;
            case (STATE_FB_MINUS):
                NextStateFBMINUS();
                break;
            case (STATE_BF_MINUS):
                NextStateBFMINUS();
                break;
            default:
                throw new Exception("We reached an invalid state : " + s_last);
        }
    }
}
