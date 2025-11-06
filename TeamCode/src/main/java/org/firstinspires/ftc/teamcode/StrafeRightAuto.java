package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Strafe Right Auto", group = "Auto")
public class StrafeRightAuto extends LinearOpMode {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    public void runOpMode(){
        //drive intitialize
        this.backLeft = (DcMotor) hardwareMap.get(DcMotor.class, "leftBack");
        this.backRight = (DcMotor) hardwareMap.get(DcMotor.class, "rightBack");
        this.frontLeft = (DcMotor) hardwareMap.get(DcMotor.class, "leftFront");
        this.frontRight = (DcMotor) hardwareMap.get(DcMotor.class, "rightFront");


        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        sleep(5000);

        strafeRight(2200);


    }

    public void forward(int time){
        backLeft.setPower(0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(0.5);

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

    public void rotateLeft(int time){
        backLeft.setPower(-0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(-0.5);
        frontRight.setPower(0.5);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void rotateRight(int time){
        backLeft.setPower(0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(0.5);
        frontRight.setPower(-0.5);

        sleep(time);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }



}