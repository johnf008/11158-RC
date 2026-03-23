package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Strafe Left Auto", group = "Auto")
public class StrafeLeftAuto extends AutoBaseFile {

    @Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();
        sleep(29000);


        // Autonomous actions
        strafeLeft(1.0, 1 );


    }
}