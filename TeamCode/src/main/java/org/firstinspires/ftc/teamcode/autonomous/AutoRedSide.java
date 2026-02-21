package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Red Side", group = "Auto")
public class AutoRedSide extends AutoBaseFile {

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

        backward(1,100,5);


        launch();

        move_right(0.5, 25, 3);

        sleep(200);





    }
}