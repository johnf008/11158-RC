//This file shall hold the experimental stuff of the code our team will use in matches.
//This will be used to hold things we're testing with the robot that aren't final.
//Once the code is finalized in here, it shall be transferred to the MecanumDrive.Java file

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="11158-Testing-Drive", group="Controlled")
public class TestingFile extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor intake, outtake, test;
    private Servo intake_one, intake_two;
    private Double ticksPerRev; // ticks per revolution

    public double intake_num = 0.0;

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");

        intake = hardwareMap.dcMotor.get("intake");
        //outtake = hardwareMap.dcMotor.get("outtake");
        test = hardwareMap.dcMotor.get("test");


        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.FORWARD);
        test.setDirection(DcMotorSimple.Direction.FORWARD);
        //outtake.setDirection(DcMotorSimple.Direction.FORWARD);



        // Initialize servos
        intake_one = hardwareMap.get(Servo.class, "IntakeOne");
        intake_two = hardwareMap.get(Servo.class, "IntakeTwo");

        // Set servo directions
        intake_one.setDirection(Servo.Direction.FORWARD);
        intake_two.setDirection(Servo.Direction.REVERSE);


        // Set motor modes
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        test.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        test.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ticksPerRev = intake.getMotorType().getTicksPerRev();

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
     /*   if (gamepad2.x) {
            if ((intake_one.getPosition()) == 0.0) {
                intake_one.setPosition(1);
                intake_two.setPosition(1);

            } else if ((intake_one.getPosition()) == 1.0) {
                intake_one.setPosition(0);
                intake_two.setPosition(0);
            }
        }*/


       /* if (gamepad2.y){
            if ((intake_two.getPosition()) == 0.0)
            {
                intake_two.setPosition(1);
            }
            else if ((intake_two.getPosition()) == 1.0)
            {
                intake_two.setPosition(0);
            }
        }*/

        if (gamepad2.aWasPressed()) { // Hold for 2 seconds to turn off
            intake.setPower(intake.getPower() == 0 ? .9 : 0);
        }

        if (gamepad2.xWasPressed()) { // Hold for 2 seconds to turn off
            outtake.setPower(outtake.getPower() == 0 ? 1 : 0);
        }


        // Set motor power
        frontLeft.setPower(-frontLeftPower);
        frontRight.setPower(-frontRightPower);
        backLeft.setPower(-backLeftPower);
        backRight.setPower(-backRightPower);

        intake.setPower(gamepad2.right_stick_y * -0.5);
        test.setPower(gamepad2.right_stick_y * -0.5);

        telemetry.addLine("We're running");
        telemetry.addData("Motor Revs", getMotorRevs());
        telemetry.update();
    }

    public double getMotorRevs() {
        return intake.getCurrentPosition() / ticksPerRev; //ticks -> revolutions translate (need to check sku number to see if we have a gear ratio)
    }
}