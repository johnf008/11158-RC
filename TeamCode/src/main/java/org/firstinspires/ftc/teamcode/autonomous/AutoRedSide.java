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

        forward(1,100, 1.6);
        launch(LAUNCH_POSTION.RED_PILLAR_FAR);

        sleep(2000);


        strafeRight(1,1);





    }
}