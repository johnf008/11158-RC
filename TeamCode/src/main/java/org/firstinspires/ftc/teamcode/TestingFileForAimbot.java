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
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.AprilTagWebcam;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.concurrent.atomic.AtomicReference;

import com.bylazar.camerastream.*;

@TeleOp(name="11158-Testing-Aimbot", group="Controlled")
public class TestingFileForAimbot extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, midtake;

    private DcMotorEx outtake;
    private CRServo servoLeft, servoRight;


    private ElapsedTime timer;

    private Double ticksPerRev; // ticks per revolution
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private Double rangeOfGoal;
    private Double powerAllocation;

    private Double gravity;
    private Double goalHeight;
    private Double robotHeight;
    private Double launchAngle;
    private Double linearVelocity;

    private Double angularVelocity;


    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");

        intake = hardwareMap.dcMotor.get("intake");
        outtake = hardwareMap.get(DcMotorEx.class, "outtake");
        midtake = hardwareMap.dcMotor.get("midtake");
        //test = hardwareMap.dcMotor.get("test");

        servoLeft = hardwareMap.crservo.get("leftServo");
        servoRight = hardwareMap.crservo.get("rightServo");

        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.FORWARD);
        outtake.setDirection(DcMotorEx.Direction.FORWARD);
        midtake.setDirection(DcMotorSimple.Direction.FORWARD);
        //test.setDirection(DcMotorSimple.Direction.FORWARD);

        servoLeft.setDirection(CRServo.Direction.FORWARD);
        servoRight.setDirection(CRServo.Direction.FORWARD);

        // Set motor modes
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //test.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        midtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //test.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ticksPerRev = intake.getMotorType().getTicksPerRev();

        aprilTagWebcam.init(hardwareMap, telemetry);

        // Iteration 1
        rangeOfGoal = 0.0;
        powerAllocation = 0.0;

        //Iteration 2
        gravity = 9.81;
        goalHeight = 0.9845; //meters
        robotHeight = 0.254; //meters
        launchAngle = 87.0; //degrees

        linearVelocity = 0.0;
        angularVelocity = 0.0;

        timer = new ElapsedTime();


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

        //April Tag Get the Horz, Range
        AprilTagDetection id21 = aprilTagWebcam.getTagBySpecific(21);
        aprilTagWebcam.displayDetectionTelemetry(id21);

        for (AprilTagDetection detection: aprilTagWebcam.getDetectedTags()){
            double detectedRange =detection.ftcPose.range;
            int aprilTag = detection.id;

            if (aprilTag == 20 || aprilTag == 24){
                rangeOfGoal = detectedRange;
            }
        }

        /*
        if (rangeOfGoal <= 25.0) {
            powerAllocation = 0.3;
        }
        else if (rangeOfGoal <= 30.0){
            powerAllocation = 0.4;
        }
        else if (rangeOfGoal <= 35.0){
            powerAllocation = 0.5;
        }
        else {
            powerAllocation = 0.6;
        }
        */

        // Set Intake/Outtake controls

        if (gamepad2.xWasPressed()) {

            outtake.setTargetPosition(outtake.getCurrentPosition() + 999999999);

            //Iteration #1
            //outtake.setPower(powerAllocation);

            //Iteration #2
            linearVelocity = Math.sqrt((Math.pow(gravity, 2) * rangeOfGoal) / (2 * (Math.pow(Math.cos(launchAngle) , 2)) * (rangeOfGoal * Math.tan(launchAngle)) + robotHeight - goalHeight));
            angularVelocity = linearVelocity / 0.0508; //radius
            outtake.setVelocity(angularVelocity, AngleUnit.RADIANS);
            outtake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if (gamepad2.yWasPressed()) {
            outtake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        if (gamepad2.aWasPressed()){
        }

        // Set motor power
        frontLeft.setPower(-frontLeftPower);
        frontRight.setPower(-frontRightPower);
        backLeft.setPower(-backLeftPower);
        backRight.setPower(-backRightPower);

        intake.setPower(gamepad2.left_stick_y * -1);
        //test.setPower(gamepad2.left_stick_y * -0.5);

        midtake.setPower((gamepad2.right_stick_y * .5));

        telemetry.addLine("We're running");
        telemetry.addData("Motor Revs", getMotorRevs());
        telemetry.addData("Timer", timer.milliseconds());
        telemetry.update();


    }

    public double getMotorRevs() {
        return intake.getCurrentPosition() / ticksPerRev; //ticks -> revolutions translate (need to check sku number to see if we have a gear ratio)
    }

}