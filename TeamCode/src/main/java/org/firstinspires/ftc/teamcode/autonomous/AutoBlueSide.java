package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue Side", group = "Auto")
public class AutoBlueSide extends AutoBaseFile {

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

        move_left(0.5, 100, 3);

        sleep(200);





    }
}