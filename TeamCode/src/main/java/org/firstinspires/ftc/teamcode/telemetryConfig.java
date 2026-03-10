package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="config", group="Testing")

public class telemetryConfig extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, midtake, midtake_two, outtake;

    @Override
    public void init(){
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");

        intake = hardwareMap.dcMotor.get("intake");
        outtake = hardwareMap.dcMotor.get("outtake");
        midtake = hardwareMap.dcMotor.get("midtake");
        midtake_two = hardwareMap.dcMotor.get("secretMotor");

    }

    @Override
    public void loop(){
        telemetry.addData("leftFront: ", frontLeft.getPortNumber());
        telemetry.addData("rightFront: ", frontRight.getPortNumber());
        telemetry.addData("backLeft: ", backLeft.getPortNumber());
        telemetry.addData("backRight: ", backRight.getPortNumber());
        telemetry.addLine(" ");

        telemetry.addData("intake", intake.getPortNumber());
        telemetry.addData("midtake", midtake.getPortNumber());
        telemetry.addData("midtake_two", midtake_two.getPortNumber());
        telemetry.addData("outtake", outtake.getPortNumber());

        telemetry.update();
    }
}
