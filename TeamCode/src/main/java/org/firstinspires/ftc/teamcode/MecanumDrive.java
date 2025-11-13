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

    public double intake_num = 0.0;

    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");



        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);



        /*
        // Initialize servos
        intake_one = hardwareMap.get(Servo.class, "IntakeOne");
        intake_two = hardwareMap.get(Servo.class, "IntakeTwo");

        // Set servo directions
        intake_one.setDirection(Servo.Direction.FORWARD);
        intake_two.setDirection(Servo.Direction.REVERSE);
        */



        // Set motor modes
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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


        /*
        // Control Servos
        if (gamepad2.x) {
            if ((intake_one.getPosition()) == 0.0) {
                intake_one.setPosition(1);
                intake_two.setPosition(1);

            } else if ((intake_one.getPosition()) == 1.0) {
                intake_one.setPosition(0);
                intake_two.setPosition(0);
            }
        }


        if (gamepad2.y){
            if ((intake_two.getPosition()) == 0.0)
            {
                intake_two.setPosition(1);
            }
            else if ((intake_two.getPosition()) == 1.0)
            {
                intake_two.setPosition(0);
            }
        }

         */

        // Set motor power
        frontLeft.setPower(-frontLeftPower);
        frontRight.setPower(-frontRightPower);
        backLeft.setPower(-backLeftPower);
        backRight.setPower(-backRightPower);

        aprilTagWebcam.update();

        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecific(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);




        telemetry.addLine("We're running");
        telemetry.update();
    }
}