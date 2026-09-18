package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
public class defaultSettings extends LinearOpMode {

    public void setWheelSettingsTeleOp(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight){


        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void setInOuttakesSettings(DcMotor intake, DcMotor midtake, DcMotor midtake_two, DcMotor outtake){


        intake.setDirection(DcMotor.Direction.REVERSE);
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        midtake.setDirection(DcMotorSimple.Direction.REVERSE);
        midtake_two.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        midtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        midtake_two.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        midtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        midtake_two.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setTargetPosition(0);

    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
}
