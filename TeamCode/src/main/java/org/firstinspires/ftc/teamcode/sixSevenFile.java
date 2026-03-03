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

@TeleOp(name="SIXSEVENSIXSEVEN", group="Testing")
public class sixSevenFile extends OpMode {

    private DcMotorEx outtake;
    double P, F;
    double curTargetVelocity;
    double curVelocity;

    double error;


    @Override
    public void init() {

        outtake = hardwareMap.get(DcMotorEx.class, "motor1");
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);

        P=0;
        F = 0;

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        curTargetVelocity = 0;
        curVelocity = 0;
        error = 0;




    }

    @Override
    public void loop() {






        // Set Intake/Outtake controls

        //outtake.setPower( gamepad2.left_stick_y * -1);

        if (gamepad2.leftTriggerWasPressed()){
            curTargetVelocity = 1200;
            P = 130.0;
            F = 17.7;
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            outtake.setVelocity(1200);

        }
        if (gamepad2.leftBumperWasPressed()){
            curTargetVelocity = 1000;
            P = 250.0;
            F = 18.4;
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            outtake.setVelocity(1000);
        }

        if (gamepad2.rightTriggerWasPressed()){
            outtake.setVelocity(0);
        }


        telemetry.addData("Target Velocity", curTargetVelocity);
        telemetry.addData("Current Velocity", "%.2f", outtake.getVelocity());
        telemetry.addData("Error", "%.2f", curTargetVelocity - curVelocity);

        telemetry.update();









    }




}