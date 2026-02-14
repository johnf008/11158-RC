package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name="ProgrammingSeminarCode", group="Controlled")
public class programmingseminar extends OpMode {
    private DcMotor motorOne;

    @Override
    public void init() {
        // Get the motor from the configuration
        motorOne = hardwareMap.dcMotor.get("motor1");

        // Set motor direction
        motorOne.setDirection(DcMotor.Direction.FORWARD);

        // Set motor modes
        motorOne.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Set ZeroPowerBehavior
        motorOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void loop() {

        //Iteration #1: Setting the motor power through buttons
        if (gamepad1.xWasPressed()){
            motorOne.setPower(1);
        }
        if (gamepad1.yWasPressed()){
            motorOne.setPower(0);
        }

        //Iteration #2: Setting the motor power through the joysticks
        //motorOne.setPower(gamepad1.right_stick_y * 0.5);

        telemetry.addLine("Hello Driver Hub!!");
        telemetry.addData("My motor's port number: ", motorOne.getPortNumber());

        telemetry.update();
    }


}