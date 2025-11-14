package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutoBaseFile extends LinearOpMode {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;
    public DcMotor intake = null;
    public DcMotor outtake = null;


    public void runOpMode(){
        //drive initialize
        this.backLeft = (DcMotor) hardwareMap.get(DcMotor.class, "leftBack");
        this.backRight = (DcMotor) hardwareMap.get(DcMotor.class, "rightBack");
        this.frontLeft = (DcMotor) hardwareMap.get(DcMotor.class, "leftFront");
        this.frontRight = (DcMotor) hardwareMap.get(DcMotor.class, "rightFront");
        this.intake = (DcMotor) hardwareMap.get(DcMotor.class, "intake");
        this.outtake = (DcMotor) hardwareMap.get(DcMotor.class, "outtake");


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.FORWARD);
        outtake.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

    }

    public void forward(int time){
        backLeft.setPower(-0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(-0.5);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void backward(int time){
        backLeft.setPower(-0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(-0.5);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void strafeLeft(int time){
        backLeft.setPower(0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(0.5);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }
    public void strafeRight(int time){
        backLeft.setPower(-0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(-0.5);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void rotateLeft(){
        backLeft.setPower(-0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(0.5);

        sleep(1700);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void rotateRight(){
        backLeft.setPower(0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(-0.5);

        sleep(1700);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void toggleIntake(){

        intake.setPower(intake.getPower() == 0 ? .9 : 0);
    }
}
