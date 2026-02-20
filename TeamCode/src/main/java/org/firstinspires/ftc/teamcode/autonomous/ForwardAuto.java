package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Forward Auto Blue", group = "Auto")
public class ForwardAuto extends AutoBaseFile {

@Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions - btw, it works way better than when at FASTER SPEEDS
        //JOhn remember to change CM_REDUCTION_MULTIPLIER

        //move backwards until at a middle length from the goal

        //initiate launch sequence

        //strafe off the launching zone

        forward(1,10,5);

        rotateRight(1,360, 20);
        sleep(200);





    }
}