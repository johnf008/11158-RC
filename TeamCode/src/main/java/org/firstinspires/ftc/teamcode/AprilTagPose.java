
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


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

            if (aprilTag == 20 || aprilTag == 24){
                rangeOfGoal = detected_range;
            }

        }


        telemetry.addData("Ts (this) should always be the range: ", rangeOfGoal);

        AprilTagDetection id24 = aprilTagWebcam.getTagBySpecific(24);
        aprilTagWebcam.displayDetectionTelemetry(id24);

        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecific(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);


        aprilTagWebcam.update();

    }

}