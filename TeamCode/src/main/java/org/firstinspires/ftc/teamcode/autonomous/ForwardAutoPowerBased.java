package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="Auto_Program_Blue", group = "Auto")
public class ForwardAutoPowerBased extends LinearOpMode {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    public void runOpMode(){
        this.backLeft = (DcMotor) hardwareMap.get(DcMotor.class, "BackLeftmotor");
        this.backRight = (DcMotor) hardwareMap.get(DcMotor.class, "BackRightmotor");
        this.frontLeft = (DcMotor) hardwareMap.get(DcMotor.class, "FrontLeftmotor");
        this.frontRight = (DcMotor) hardwareMap.get(DcMotor.class, "FrontRightmotor");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        forward(1450);
        strafeLeft(3700);
        rotateRight(0);
        strafeLeft(700);
        //grab

        forward(3500);
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

        sleep(1700);

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

        sleep(1700);

        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }

    public void openClaw(){
        /*
        leftGrab.setPosition(0.5);
        rightGrab.setPosition(0.5);
         */
    }

    public void closeClaw(){
        /*
        leftGrab.setPosition(0);
        rightGrab.setPosition(0.2);
         */
    }
    public void forwardForDistance (double inches){

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int wheelDiameter = 4; //inches
        double wheelCircumference = wheelDiameter * Math.PI;
        double rotation = inches/wheelCircumference; //number of wheel rotations you need to make
        double ticksPerRevolution = 560; //ticks per revolution on the motor your using
        int ticks = (int) (rotation * ticksPerRevolution);  //total number of motor ticks

        frontLeft.setPower(1);
        frontRight.setPower(1);
        backLeft.setPower(1);
        backRight.setPower(1);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy()){
            //empty while to stop motors from setting power = 0 below when motor is running
        }

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);


    }

}