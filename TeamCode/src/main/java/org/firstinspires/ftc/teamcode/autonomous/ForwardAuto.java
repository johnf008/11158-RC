package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Forward Auto Blue", group = "Auto")
public class ForwardAuto extends AutoBaseFile {

@Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions

        forward(.5, 25,10);
        sleep(8000);

    }
}