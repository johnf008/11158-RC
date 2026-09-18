package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="Auto Red Side", group = "Auto")
public class AutoRedSide extends AutoBaseFile {

    @Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();


        frontRight.setPower(.8);
        frontLeft.setPower(.8);
        backRight.setPower(.8);
        backLeft.setPower(.8);
        sleep(1000);
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        launch(LAUNCH_POSTION.BLUE_PILLAR_FAR);



        sleep(20000);



/*
        forward(1,100, 4);
        launch(LAUNCH_POSTION.RED_PILLAR_FAR);

        sleep(2000);


        strafeRight(1,1);


*/


    }
}