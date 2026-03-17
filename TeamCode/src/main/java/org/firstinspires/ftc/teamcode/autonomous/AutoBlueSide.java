package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue Side", group = "Auto")
public class AutoBlueSide extends AutoBaseFile {

    @Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions - btw, it works way better than when at FASTER SPEEDS,

        //backward(1,150,3);
        forward(1,100, .8);
        launch(LAUNCH_POSTION.BLUE_PILLAR_FAR);

        sleep(3000);


        rotateLeft(0.5, 45, 1);

        toggleIntake();
        forward(1,95,1); //  pushes the wall on purpose
        backward(1, 90,1);
        toggleIntake();



        rotateRight(0.5, 45, 1);
        launch(LAUNCH_POSTION.BLUE_PILLAR_FAR);

        /*

        rotateLeft(0.5, 45, 1);
        move_left(1,60,1);
        toggleIntake();
        forward(1,160,1);
        backward(1, 50,1);

        move_right(1,30,1);
        rotateRight(0.5, 90, 1);
        move_left(1,20,1);
        sleep(3500);

        move_right(1,130,1);
        rotateLeft(0.5, 45, 1);
        launch(LAUNCH_POSTION.BLUE_PILLAR_FAR);


        sleep(20000);


        */





    }
}