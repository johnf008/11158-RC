package org.firstinspires.ftc.teamcode;

import com.bylazar.panels.Panels;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.bylazar.graph.*;
import com.bylazar.fullpanels.*;
import com.bylazar.telemetry.PanelsTelemetry;

@TeleOp
public class FlywheelTuning extends OpMode {
    public DcMotorEx flywheelMotor;
    public DcMotor intake, midtake, midtake_two;


    public double highVelocity = 1200;
    public double lowVelocity = 1000;
    double curTargetVelocity = highVelocity;
    boolean isCurTargetVelocityHigh = true;

    private double F = 0;
    private double P = 0;

    double[] stepSizes = {100.0, 10.0, 1.0, 0.1, 0.0001};

    int stepIndex = 1;


    @Override
    public void init() {
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "outtake");
        flywheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        intake = hardwareMap.dcMotor.get("intake");
        midtake = hardwareMap.dcMotor.get("midtake");

        intake.setDirection(DcMotor.Direction.REVERSE);
        midtake.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        midtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init complete");

        midtake_two = hardwareMap.dcMotor.get("secretMotor");
        midtake_two.setDirection(DcMotorSimple.Direction.FORWARD);


    }

    @Override
    public void loop() {
        //get gamepad commands
        //set target velocity
        //update telemetry

        if (gamepad1.yWasPressed()) {
            if (isCurTargetVelocityHigh) {
                curTargetVelocity = lowVelocity;
                isCurTargetVelocityHigh = false;
            } else {
                curTargetVelocity = highVelocity;
                isCurTargetVelocityHigh = true;
            }
        }

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }
        if (gamepad1.aWasPressed()) {
            curTargetVelocity = 0;
            isCurTargetVelocityHigh = false;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }

        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }
        if (gamepad1.leftBumperWasPressed()) {
            if (isCurTargetVelocityHigh) {
                highVelocity -= stepSizes[stepIndex];
            } else {
                lowVelocity -= stepSizes[stepIndex];
            }
        }
        if (gamepad1.rightBumperWasPressed()) {
            if (isCurTargetVelocityHigh) {
                highVelocity += stepSizes[stepIndex];
            } else {
                lowVelocity += stepSizes[stepIndex];
            }
        }

            intake.setPower(gamepad1.left_stick_y * 1);
            midtake.setPower(gamepad1.left_stick_y);
            midtake_two.setPower(gamepad1.right_stick_y);


            //set new PIDF coefficients
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            flywheelMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

            flywheelMotor.setVelocity(curTargetVelocity);

            double curVelocity = flywheelMotor.getVelocity();
            double error = curTargetVelocity - curVelocity;

            telemetry.addData("Target Velocity", curTargetVelocity);
            telemetry.addData("Current Velocity", "%.2f", curVelocity);
            telemetry.addData("Error", "%.2f", error);
            telemetry.addLine("X to change from high velocity to low velocity & vice versa, A to turn off");
            telemetry.addLine("-----------------");

            telemetry.addData("HighVelocity", highVelocity);
            telemetry.addData("LowVelocity", lowVelocity);
            telemetry.addLine("Use Bumpers to decrease/increase high and low velocities");
            telemetry.addLine("-----------------");

            telemetry.addData("Tuning P", "%.4f (D-Pad U/D)", P);
            telemetry.addData("Tuning F", "%.4f (D-Pad L/R", F);
            telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIndex]);

            PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Velocity", curTargetVelocity);
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Current Velocity", curVelocity);

            telemetry.update();
            PanelsTelemetry.INSTANCE.getTelemetry().update(telemetry);

    }
}
