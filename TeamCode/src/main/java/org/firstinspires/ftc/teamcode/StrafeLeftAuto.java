package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Strafe Left Auto", group = "Auto")
public class StrafeLeftAuto extends AutoBaseFile {
    @Override
    public void runOpMode() {
        // Initialize hardware and wait for start
        super.runOpMode();


        waitForStart();
        sleep(5000);

        strafeLeft(1000);

        sleep(3000);

        toggleIntake();

        sleep(5000);

    }
}