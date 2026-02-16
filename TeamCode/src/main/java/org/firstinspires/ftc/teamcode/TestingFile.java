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
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
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
    private DcMotor intake, midtake;
    private DcMotorEx outtake;


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
        backRight.setDirection(DcMotor.Direction.REVERSE);

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

    }

    @Override
    public void loop() {
        // Gamepad inputs
        double drive =  -gamepad1.left_stick_y;
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

        intake.setPower( gamepad2.left_stick_y * -1);
        midtake.setPower( gamepad2.right_stick_y);

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


        if ( gamepad2.xWasPressed())
            outtake.setPower(outtake.getPower() == 0 ? -0.85 : 0 );

        if ( gamepad2.aWasPressed() ) {
            outtake.setPower(outtake.getPower() == 0 ? -outtakePower : 0);
        }
        if ( gamepad2.yWasPressed() ) {
            outtake.setPower(outtake.getPower() == 0 ? outtakePower : 0);
        }


        //SETTING VELOCITY BASED ON THE PIDF COEFFICIENTS DECLARED IN INIT
        /*
        if (gamepad2.rightTriggerWasPressed()){
            outtake.setVelocity(1500);
        }
        if (gamepad2.rightBumperWasPressed()){
            outtake.setVelocity(0);
        }
        */



        // Set motor power
        frontLeft.setPower(-frontLeftPower);
        frontRight.setPower(-frontRightPower);
        backLeft.setPower(-backLeftPower);
        backRight.setPower(-backRightPower);

        // Display
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addData("Outtake Power", outtakePower);

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

        //pidf for john
        telemetry.addData("Target Velocity", 1500);
        telemetry.addData("Current Velocity", outtake.getVelocity());
        telemetry.addData("Error", 1500 - outtake.getVelocity());


        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish
        telemetry.addLine("\uD80C\uDD9D \uD80C\uDD9F \uD80C\uDD9E \uD80C\uDD9D \uD80C\uDD9F"); //fish

        telemetry.update();

    }

    public double getMotorRevs() {
        return intake.getCurrentPosition() / ticksPerRev; //ticks -> revolutions translate (need to check sku number to see if we have a gear ratio)
    }

}