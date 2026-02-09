package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Middle Auto Blue", group = "Auto")
public class BlueMiddleAuto extends AutoBaseFile {

@Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions

        forward(.4, 80, 5 );







    }
}