package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutoBaseFile extends LinearOpMode {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;
    public DcMotor intake = null;


    public void runOpMode(){
        //drive initialize
        this.backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        this.backRight = hardwareMap.get(DcMotor.class, "rightBack");
        this.frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        this.frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        this.intake = hardwareMap.get(DcMotor.class, "intake");


        //set motor directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    //movement methods
    public void setWheelMotorsPower(double power){
        backLeft.setPower(power);
        backRight.setPower(power);
        frontLeft.setPower(power);
        frontRight.setPower(power);
    }

    public void forward(int durationMilliseconds){
        setWheelMotorsPower(0.5);

        sleep(durationMilliseconds);

        setWheelMotorsPower(0);
    }

    public void backward(int durationMilliseconds){
        setWheelMotorsPower(-0.5);

        sleep(durationMilliseconds);

        setWheelMotorsPower(0);
    }

    public void strafeLeft(int durationMilliseconds){
        backLeft.setPower(0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(0.5);

        sleep(durationMilliseconds);

        setWheelMotorsPower(0);
    }
    public void strafeRight(int durationMilliseconds){
        backLeft.setPower(-0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(-0.5);

        sleep(durationMilliseconds);

        setWheelMotorsPower(0);
    }

    public void rotateLeft(){
        backLeft.setPower(-0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(0.5);

        sleep(1700);

        setWheelMotorsPower(0);
    }

    public void rotateRight(){
        backLeft.setPower(0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(-0.5);

        sleep(1700);

        setWheelMotorsPower(0);
    }
    //other methods

    public void toggleIntake(){

        intake.setPower(intake.getPower() == 0 ? .9 : 0);
    }
}
