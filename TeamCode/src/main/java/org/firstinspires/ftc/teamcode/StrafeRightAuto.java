package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Strafe Right Auto", group = "Auto")
public class StrafeRightAuto extends AutoBaseFile {

    @Override
    public void runOpMode(){
        // Initialize hardware and wait for start
        super.runOpMode();
        waitForStart();

        // Autonomous actions
        sleep(5000);

        strafeLeft(1000);

    }
}