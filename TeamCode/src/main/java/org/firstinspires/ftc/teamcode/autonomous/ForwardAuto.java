package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Forward Auto Blue", group = "Auto")
public class ForwardAuto extends ForwardAutoPowerBased {

@Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions - btw, it works way better than when at FASTER SPEEDS


        sleep(29000);

        strafeLeft(1000);






    }
}