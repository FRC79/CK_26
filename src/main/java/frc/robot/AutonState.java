// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class AutonState {
    private boolean in_stage_two = false;
    
    public AutonState() {
        
    }

    public void setInStageTwo(boolean state) {
        in_stage_two = state;
    }

    public boolean inStageTwo() {
        return in_stage_two;
    }
}
