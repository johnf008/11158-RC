
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


@TeleOp(name="April-Tag-Pose", group="Controlled")
public class AprilTagPose extends OpMode {

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private Double rangeOfGoal;

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
        rangeOfGoal = 0.0;
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

            if (aprilTag == 20 || aprilTag == 24 || aprilTag == 21){
                rangeOfGoal = detected_range;
            }

        }


        telemetry.addData("Ts (this) should always be the range: ", rangeOfGoal);

        AprilTagDetection id24 = aprilTagWebcam.getTagBySpecific(24);
        aprilTagWebcam.displayDetectionTelemetry(id24);








        aprilTagWebcam.update();

    }

}