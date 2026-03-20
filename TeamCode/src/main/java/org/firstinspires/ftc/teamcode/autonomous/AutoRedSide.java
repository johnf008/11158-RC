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

        encoderDrive(1,100,97,100,97,.8);
        sleep(2000);

        //rotateRight(1,2,1.25);
        rotateRightQuarter(0.25, 2, 1.25);
        sleep(2000);

        encoderDrive(1,75,73,75,73,2);
        toggleIntake();
        toggleMidtake();
        sleep(3000);

        move_left(0.5,15, 1 );
        sleep(2000);





    }
}