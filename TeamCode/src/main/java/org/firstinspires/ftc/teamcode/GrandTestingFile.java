//This file shall hold the experimental stuff of the code our team will use in matches.
//This will be used to hold things we're testing with the robot that aren't final.
//Once the code is finalized in here, it shall be transferred to the MecanumDrive.Java file

package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import com.bylazar.camerastream.*;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="11158-Final-Drive-Red", group="Controlled")
public class GrandTestingFile extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, midtake;
    private DcMotorEx outtake;

    //:3
    // Adjust these numbers to suit your robot.
    final double DESIRED_DISTANCE = 66.2; //  this is how close the camera should get to the target (inches)
    final double DESIRED_HEADING = -0.8;
    final double DESIRED_YAW = -8.7;

    final double DESIRED_DISTANCE_CLOSE = 42.6; //  this is how close the camera should get to the target (inches)
    final double DESIRED_HEADING_CLOSE = -8.8;
    final double DESIRED_YAW_CLOSE = 4.2;

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the strafing speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)
    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static final int DESIRED_TAG_ID = 24;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
    private boolean targetFound;




    private ElapsedTime timer;

    private Double ticksPerRev; // ticks per revolution
    double outtakePower = 0.7;
    double P = 600;
    double F = 18.6004;


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


        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        intake.setDirection(DcMotor.Direction.FORWARD);
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        midtake.setDirection(DcMotorSimple.Direction.FORWARD);
        //test.setDirection(DcMotorSimple.Direction.FORWARD);


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
        outtake.setTargetPosition(0);


        midtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //test.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ticksPerRev = intake.getMotorType().getTicksPerRev();

        //set new PIDF coefficients
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        timer = new ElapsedTime();

        targetFound = false;    // Set to true when an AprilTag target is detected
        initAprilTag();

        if (USE_WEBCAM)
            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

    }

    @Override
    public void loop() {

        // Gamepad inputs
        double drive =  -gamepad1.left_stick_y;
        double strafe = -gamepad1.right_stick_x;
        double rotate = -(gamepad1.right_trigger - gamepad1.left_trigger);



        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            // Look to see if we have size info on this tag.
            if (detection.metadata != null) {
                //  Check to see if we want to track towards this tag.
                if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                    // Yes, we want to use this tag.
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                } else {
                    // This tag is in the library, but we do not want to track it right now.
                    telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                    targetFound = false;
                }
            } else {
                // This tag is NOT in the library, so we don't have enough information to track to it.
                telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
                targetFound = false;
            }
        }

        // Tell the driver what we see, and what to do.
        if (targetFound) {
            telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
            telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
            telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
            telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
        } else {
            telemetry.addData("\n>","Drive using joysticks to find valid target\n");
        }

        // If Left Bumper is being pressed, AND we have found the desired target, Drive to target Automatically .
        if (gamepad1.left_bumper && targetFound) {

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double  rangeError      = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
            double  headingError    = (desiredTag.ftcPose.bearing - DESIRED_HEADING);
            double  yawError        = (desiredTag.ftcPose.yaw - DESIRED_YAW);

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
            rotate   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
            strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

            telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
        } else {

            telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
        }

        if (gamepad1.right_bumper && targetFound) {

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double  rangeError      = (desiredTag.ftcPose.range - DESIRED_DISTANCE_CLOSE);
            double  headingError    = (desiredTag.ftcPose.bearing - DESIRED_HEADING_CLOSE);
            double  yawError        = (desiredTag.ftcPose.yaw - DESIRED_YAW_CLOSE);

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
            rotate   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
            strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

            telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
        } else {

            telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, rotate);
        }
        telemetry.update();

        // Apply desired axes motions to the drivetrain.



        // Calculate motor powers
        // Calculate wheel powers.
        double frontLeftPower    =  drive - strafe - rotate;
        double frontRightPower   =  drive + strafe + rotate;
        double backLeftPower     =  drive + strafe - rotate;
        double backRightPower    =  drive - strafe + rotate;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        // Set Intake/Outtake controls

        intake.setPower( gamepad2.left_stick_y * -1);
        midtake.setPower( gamepad2.right_stick_y);

        /*

        if ( gamepad2.dpadUpWasPressed())
            outtakePower += .1;
        if ( gamepad2.dpadRightWasPressed())
            outtakePower += .05;
        if ( gamepad2.dpadDownWasPressed())
            outtakePower -= .1;
        if ( gamepad2.dpadLeftWasPressed())
            outtakePower -= .05;

        if ( outtakePower <= 0)
            outtakePower = .05;
        else if ( outtakePower >= 1.05)
            outtakePower = 1;

        */




        if ( gamepad2.yWasPressed() ) { //Far-Field
            outtake.setPower(outtake.getPower() == 0 ? 1 : 0);
        }
        if ( gamepad2.aWasPressed() ) { //MidField
            outtake.setPower(outtake.getPower() == 0 ? .85 : 0);
        }
        if ( gamepad2.xWasPressed())
            outtake.setVelocity(outtake.getVelocity() == 0 ? -1100 : 0 );


        //SETTING VELOCITY BASED ON THE PIDF COEFFICIENTS DECLARED IN INIT
        if (gamepad2.rightBumperWasPressed()){
            outtake.setVelocity(0);
        }

        if (gamepad2.leftTriggerWasPressed()){
            P = 130;
            F = 17.7;
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            outtake.setVelocity(1200);
        }
        if (gamepad2.leftBumperWasPressed()){
            P = 250;
            F = 18.4;
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            outtake.setVelocity(1000);

        }




        // Set motor power
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        // Display
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish

        telemetry.addLine();
        telemetry.addData("Motor Revs FL", frontLeft.getCurrentPosition());
        telemetry.addData("Motor Revs FR", frontRight.getCurrentPosition());
        telemetry.addData("Motor Revs BL", backLeft.getCurrentPosition());
        telemetry.addData("Motor Revs BR", backRight.getCurrentPosition());

        telemetry.addLine();
        telemetry.addData("Motor Revs Intake", intake.getCurrentPosition());
        telemetry.addData("Motor Revs Midtake", midtake.getCurrentPosition());
        telemetry.addData("Motor Revs Outtake", outtake.getCurrentPosition());

        //stuff
        telemetry.addLine();
        telemetry.addData("Outtake power: ", outtake.getPower());
        telemetry.addData("Current outtake velocity: ", outtake.getVelocity());


        telemetry.addLine();
        telemetry.addData("Timer", timer.milliseconds());
        telemetry.addLine();

        //pidf for john
        telemetry.addData("Target Velocity", 1500);
        telemetry.addData("Current Velocity", outtake.getVelocity());
        telemetry.addData("Error", 1500 - outtake.getVelocity());
        telemetry.addLine();

        telemetry.addData("P: ", P);
        telemetry.addData("F: ", F);
        telemetry.addLine();

        telemetry.addData("Frontleft port", frontLeft.getPortNumber());
        telemetry.addData("Frontright port", frontRight.getPortNumber());
        telemetry.addData("BackLeft port", backLeft.getPortNumber());
        telemetry.addData("Backright port", backRight.getPortNumber());
        telemetry.addLine();

        PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Velocity", 1500);
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Current Velocity", outtake.getVelocity());
        PanelsTelemetry.INSTANCE.getTelemetry().addData("Error", 1500 - outtake.getVelocity());



        telemetry.update();
        PanelsTelemetry.INSTANCE.getTelemetry().update(telemetry);


        telemetry.update();

    }

    public double getMotorRevs() {
        return intake.getCurrentPosition() / ticksPerRev; //ticks -> revolutions translate (need to check sku number to see if we have a gear ratio)
    }

    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // e.g. Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    private void    setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while ((visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        // Set camera controls unless we are stopping.
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
    }


}