//This file shall hold the experimental stuff of the code our team will use in matches.
//This will be used to hold things we're testing with the robot that aren't final.
//Once the code is finalized in here, it shall be transferred to the MecanumDrive.Java file

package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.concurrent.atomic.AtomicReference;

import com.bylazar.camerastream.*;


@TeleOp(name="11158-Testing-Drive", group="Controlled")
public class TestingFile extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, outtake, test;
    private CRServo servoLeft, servoRight;
    private ElapsedTime timer;



    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");


        intake = hardwareMap.dcMotor.get("intake");
        outtake = hardwareMap.dcMotor.get("outtake");
        //test = hardwareMap.dcMotor.get("test");

        servoLeft = hardwareMap.crservo.get("leftServo");
        servoRight = hardwareMap.crservo.get("rightServo");




        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        intake.setDirection(DcMotor.Direction.FORWARD);
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        //test.setDirection(DcMotorSimple.Direction.FORWARD);

        servoLeft.setDirection(CRServo.Direction.FORWARD);
        servoRight.setDirection(CRServo.Direction.FORWARD);

        // Set motor modes
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //test.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);




        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //test.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        timer = new ElapsedTime();

        //For encoders
        final double TICKS_PER_REVOLUTION = frontLeft.getMotorType().getTicksPerRev();
        final double DRIVE_GEAR_REDUCTION = 1.0 ;     // Gear Ratio
        final double WHEEL_DIAMETER_INCHES = 5.51181102;

        final double TICKS_PER_INCH = (TICKS_PER_REVOLUTION * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
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
                Math.max( Math.abs(frontLeftPower), Math.abs(frontRightPower) ),
                Math.max( Math.abs(backLeftPower), Math.abs(backRightPower) )
        );

        if (maxPower > 0) {
            frontLeftPower = (frontLeftPower / maxPower) * speedReductionFactor;
            frontRightPower = (frontRightPower / maxPower) * speedReductionFactor;
            backLeftPower = (backLeftPower / maxPower) * speedReductionFactor;
            backRightPower = (backRightPower / maxPower) * speedReductionFactor;
        }

        // Set Intake/Outtake controls

        if (gamepad2.xWasPressed()) {
            outtake.setPower(outtake.getPower() == 0 ? 1 : 0);
        }
        if (gamepad2.yWasPressed()) {
            outtake.setPower(outtake.getPower() == 0 ? .9 : 0);
        }
        if (gamepad2.bWasPressed()) {
            outtake.setPower(outtake.getPower() == 0 ? .8 : 0);
        }
        if (gamepad2.aWasPressed()) {
            outtake.setPower(outtake.getPower() == 0 ? .4 : 0);
        }

        if (gamepad2.rightBumperWasPressed()) {
            timer.reset();
            servoRight.setPower(1);

        }

        if ((timer.milliseconds() >= 1500) && (servoRight.getPower() != 0)){
            servoRight.setPower(-1);
            timer.reset();
        }

        if ((timer.milliseconds() >= 700) && (servoRight.getPower() == -1)){
            servoRight.setPower(0);
            timer.reset();
        }




        // Set motor power
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        intake.setPower(gamepad2.right_stick_y * -0.5);
        servoRight.setPower(gamepad2.left_stick_y);
        //test.setPower(gamepad2.right_stick_y * -0.5);


        //

        telemetry.addLine("We're running");
        telemetry.addData("Motor Revs FL", frontLeft.getCurrentPosition());
        telemetry.addData("Motor Revs FR", frontRight.getCurrentPosition());
        telemetry.addData("Motor Revs BL", backLeft.getCurrentPosition());
        telemetry.addData("Motor Revs BR", backRight.getCurrentPosition());

        telemetry.addData("Timer", timer.milliseconds());
        telemetry.update();

    }



    @Override
    public void stop() {
        PanelsCameraStream.INSTANCE.stopStream();
    }
}