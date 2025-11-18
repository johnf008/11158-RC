package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Forward Auto Blue", group = "Auto")
public class ForwardAuto extends AutoBaseFile {

@Override
    public void runOpMode(){

    // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        sleep(5000);

        forward(1000);

        sleep(3000);

    }
}