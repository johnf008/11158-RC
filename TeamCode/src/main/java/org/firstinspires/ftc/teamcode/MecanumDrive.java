//This file shall hold the official code our team will use in matches.
//This means that everything in here should EXACTLY correlate with the hardware connected to the robot (ie. intake, outtake, etc.)

//Anything experimental shall be in the other file named "TestingFile.java" with the name on the driver hub being "11158-Testing-Drive"


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="11158-Drive", group="Controlled")
public class MecanumDrive extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, outtake;

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");

        intake = hardwareMap.dcMotor.get("intake");
        outtake = hardwareMap.dcMotor.get("outtake");


        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        intake.setDirection(DcMotor.Direction.REVERSE);
        outtake.setDirection(DcMotor.Direction.FORWARD);

        // Set motor modes
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        aprilTagWebcam.init(hardwareMap, telemetry);

    }

    @Override
    public void loop() {
        // Gamepad inputs
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.right_stick_x;
        double rotate = gamepad1.right_trigger - gamepad1.left_trigger;

        // Calculate motor powers
        double frontLeftPower = drive + strafe + rotate;
        double frontRightPower = drive - strafe - rotate;
        double backLeftPower = drive - strafe + rotate;
        double backRightPower = drive + strafe - rotate;

        double speedReductionFactor = 0.6;
        double maxPower = Math.max(
                Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))
        );

        if (maxPower > 0) {
            frontLeftPower = (frontLeftPower / maxPower) * speedReductionFactor;
            frontRightPower = (frontRightPower / maxPower) * speedReductionFactor;
            backLeftPower = (backLeftPower / maxPower) * speedReductionFactor;
            backRightPower = (backRightPower / maxPower) * speedReductionFactor;
        }
        // Set Intake/Outtake controls
        if (gamepad2.xWasPressed()) {
            outtake.setPower(outtake.getPower() == 0 ? 1.0 : 0);
        }

        // Set motor power
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        intake.setPower(gamepad2.right_stick_y * -0.5);


        aprilTagWebcam.update();

        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecific(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);

        AprilTagDetection id24 = aprilTagWebcam.getTagBySpecific(24);
        aprilTagWebcam.displayDetectionTelemetry(id24);


        telemetry.addLine("We're running");
        telemetry.update();
    }
}