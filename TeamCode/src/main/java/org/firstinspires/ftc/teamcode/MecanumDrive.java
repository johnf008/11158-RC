
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="1158-Drive", group="Controlled")
public class MecanumDrive extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight, Lift1, Lift2, Intake1, Intake2;
    private Servo leftGrab, rightGrab, armTiltServo;

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("FrontLeftmotor");
        frontRight = hardwareMap.dcMotor.get("FrontRightmotor");
        backLeft = hardwareMap.dcMotor.get("BackLeftmotor");
        backRight = hardwareMap.dcMotor.get("BackRightmotor");
        Lift1 = hardwareMap.get(DcMotor.class, "Lift1");
        Lift2 = hardwareMap.get(DcMotor.class, "Lift2");
        Intake1 = hardwareMap.get(DcMotor.class, "Intake1");
        Intake2 = hardwareMap.get(DcMotor.class, "Intake2");

        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        Lift1.setDirection(DcMotorEx.Direction.FORWARD);
        Lift2.setDirection(DcMotorEx.Direction.FORWARD);
        Intake1.setDirection(DcMotor.Direction.FORWARD);
        Intake2.setDirection(DcMotor.Direction.FORWARD);

        Lift1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        Lift2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);


        // Initialize servos
        leftGrab = hardwareMap.get(Servo.class, "Leftgrab");
        rightGrab = hardwareMap.get(Servo.class, "Rightgrab");

        armTiltServo = hardwareMap.get(Servo.class, "Armtilt");


        // Set servo directions
        leftGrab.setDirection(Servo.Direction.FORWARD);
        rightGrab.setDirection(Servo.Direction.FORWARD);


        // Set motor modes
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Lift1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Lift2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Intake1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Intake2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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

        // Control Servos
        if (gamepad2.x) {
            leftGrab.setPosition(.5);
            rightGrab.setPosition(0.5);
        } else if (gamepad2.b) {

            leftGrab.setPosition(1);
            rightGrab.setPosition(0);
        }


        if (gamepad2.y) {
            armTiltServo.setPosition(0);
        } else if (gamepad2.a) {
            armTiltServo.setPosition(0.2);
        }


        // Set motor power
        frontLeft.setPower(-frontLeftPower);
        frontRight.setPower(-frontRightPower);
        backLeft.setPower(-backLeftPower);
        backRight.setPower(-backRightPower);

        if (gamepad2.right_stick_y >= -1 || gamepad2.right_stick_y >= -1) {
            Lift1.setPower(gamepad2.left_stick_y * -0.5);
            Lift2.setPower(gamepad2.left_stick_y * -0.5);
        }


        //Intake1.setPower(gamepad2.right_stick_y * 0.5);

        // double combinedMovement = gamepad2.right_trigger - gamepad2.left_trigger;
        //   Intake2.setPower(combinedMovement);

        //figure out if the servos are working
        telemetry.addData("Servo Position #1", leftGrab.getPosition());
        telemetry.addData("Servo Position #2", rightGrab.getPosition());


        telemetry.update();
    }
}