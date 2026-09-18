package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue Side", group = "Auto")
public class AutoBlueSide extends AutoBaseFile {



    private void forward(double speed, long time){

        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        backRight.setPower(speed);
        backLeft.setPower(speed);
        sleep(time);
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

    }

    private void roatate(double speed, long time){
        backLeft.setPower(-speed);
        backRight.setPower(speed);
        frontLeft.setPower(-speed);
        frontRight.setPower(speed);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);

    }
    @Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions - btw, it works way better than when at FASTER SPEEDS,

        //backward(1,150,3);
        //forward(1,75, 1.6);

        //1st Cycle............

        forward(.8,1000);
        launch(LAUNCH_POSITION.BLUE_PILLAR_FAR);
        sleep(100);

        //2cd Cycle............
        toggleIntake();
        toggleMidtake();
        roatate(.5,1700);
        toggleIntake();
        toggleMidtake();
        forward(.9,200);

        //Reset to Launch position
        sleep(100);
        forward(-.9,200);
        roatate(-.5,1700);
        sleep(100);

        launch(LAUNCH_POSITION.BLUE_PILLAR_FAR);

        //3rd Cycle





        /*
        rotateLeft(0.5, 45, 1);

        toggleIntake();
        forward(1,95,1); //  pushes the wall on purpose
        backward(1, 90,1);
        toggleIntake();



        rotateRight(0.5, 45, 1);
        launch(LAUNCH_POSTION.BLUE_PILLAR_FAR);
        */


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