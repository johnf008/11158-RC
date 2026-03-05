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

        backward(1,150,3);
        launch();

        sleep(3000);

       // move_left(1,100,3);

        /*

        rotateLeft(0.5, 45, 3);

        toggleIntake();
        forward(1,95,5); //  pushes the wall on purpose
        backward(1, 90,5);
        toggleIntake();

        rotateRight(0.5, 45, 3);
        launch();

        rotateLeft(0.5, 45, 3);
        move_left(1,60,5);
        toggleIntake();
        forward(1,160,5);
        backward(1, 50,5);

        move_right(1,30,5);
        rotateRight(0.5, 90, 3);
        move_left(1,20,5);
        sleep(3500);

        move_right(1,130,5);
        rotateLeft(0.5, 45, 3);
        launch();

         */






        sleep(20000);


        //





    }
}