package frc.robot.commands.Drive_Commands;

public class BridgeBalancer {

    private float m_angle_radians;
    private float angle_last;
    private float m_acceleration_mps2;
    private int s_last;
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
    }

    // State functions
    public void NextStateFCPLUS(){
        if (Math.abs(m_angle_radians - angle_last) >= 1){
            s_last = STATE_BD_PLUS;
        }
        else {
            s_last = STATE_FC_PLUS;
        }
    }

    public void NextStateBDPLUS(){
        if (m_acceleration_mps2 > 0 &&  deriv m_angle_radians < 0 ){
            s_last = STATE_BF_PLUS;
        }
        else if (m_acceleration_mps2 < 0){
            s_last = STATE_FB_PLUS;
        }
    }

    public NextStateFBPLUS(){
        
    }

    public NextStateBFPLUS(){
        
    }

    public NextStateE(){
        
    }

    public NextStateB(){
        
    }

    public NextStateFCMINUS(){

    }

    public NextStateBDMINUS(){
        
    }

    public NextStateFBMINUS(){
        
    }

    public NextStateBFMINUS(){
        
    }

    // Update State functions
    public void UpdateState(float angle, float acceleration) throws Exception {
        m_angle_radians = angle;
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
                break;
        }
    }
}
