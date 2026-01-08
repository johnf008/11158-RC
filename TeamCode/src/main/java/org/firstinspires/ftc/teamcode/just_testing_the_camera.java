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
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.concurrent.atomic.AtomicReference;

import com.bylazar.camerastream.*;


@TeleOp(name="Just-Testing-Camera", group="Controlled")
public class just_testing_the_camera extends OpMode {

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private Double rangeOfGoal;
    private Double telemetry_test_var;

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);

        rangeOfGoal = 0.0;
        telemetry_test_var = 0.0;
    }

    @Override
    public void loop() {

        AprilTagDetection id21 = aprilTagWebcam.getTagBySpecific(21);
        aprilTagWebcam.displayDetectionTelemetry(id21);

        //assign the range for the distance for each goal that will be used
        //this is able to be applied for both the red and blue goals
        for (AprilTagDetection detection: aprilTagWebcam.getDetectedTags()){
            double detected_range = detection.ftcPose.range;
            int aprilTag = detection.id;

            if (aprilTag == 20 || aprilTag == 24){
                rangeOfGoal = detected_range;
            }

        }

        //Based on the blue goal at HCH
        //at 25 in: 0.50 power 80% success rate
        //at 30 in: 0.50 power 90% success rate
        //at 35 in: 0.50 power 70% success rate

        if (rangeOfGoal >= 35.0){
            telemetry_test_var = 0.5;
        }
        else if (rangeOfGoal >= 30) {
            telemetry_test_var = 0.4;
        } else if (telemetry_test_var >= 25.0) {
            telemetry_test_var = 0.3;
        }
        else {
            telemetry_test_var = 0.2;
        }
        telemetry.addData("Range of the goal: ", rangeOfGoal);
        telemetry.addData("We're giving the flywheel this much power: ", telemetry_test_var);

        /*
        if (gamepad2.dpadDownWasPressed()){
            outtake.setPower(0.75);
        }

        if (gamepad2.dpadLeftWasPressed()){
            outtake.setPower(0.5);
        }

        if (gamepad2.dpadRightWasPressed()){
            outtake.setPower(0.25);
        }

        if (gamepad2.dpadUpWasPressed()){
            outtake.setPower(0);
        }
*/



        telemetry.update();
        aprilTagWebcam.update();

    }





}
