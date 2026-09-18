package org.firstinspires.ftc.teamcode.autonomous;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Autonomous(name="Auto_Program_Blue", group = "Auto")
public class ForwardAutoPowerBased extends  AutoBaseFile {
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    public enum LAUNCH_POSITION {

        //If we are adding aimbot, cords can go in here too.

        BLUE_MID(10,18.4, 1000, 8),
        BLUE_PILLAR_FAR(10, 17.7, 1200, 6),
        BLUE_WALL_FAR(10, 15, 1500, 5), //untested values

        RED_MID(10, 14.3, 1000, 8),
        RED_PILLAR_FAR(11, 14.3, 1200, 6),
        RED_WALL_FAR(10, 15, 1500, 5); // untested values

        final double P, F, velocityNeeded, durationSeconds;

        LAUNCH_POSITION(double P, double F, double velocityNeeded, double durationSeconds) {
            this.P = P;
            this.F = F;
            this.velocityNeeded = velocityNeeded;
            this.durationSeconds = durationSeconds;
        }
    }
    public void runOpMode(){
        this.backLeft = (DcMotor) hardwareMap.get(DcMotor.class, "BackLeftmotor");
        this.backRight = (DcMotor) hardwareMap.get(DcMotor.class, "BackRightmotor");
        this.frontLeft = (DcMotor) hardwareMap.get(DcMotor.class, "FrontLeftmotor");
        this.frontRight = (DcMotor) hardwareMap.get(DcMotor.class, "FrontRightmotor");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();


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

    public void launch(AutoBaseFile.LAUNCH_POSITION position){


        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(position.P, 0, 0, position.F);
        outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        outtake.setVelocity(position.velocityNeeded);

        sleep(5000);
        toggleIntake();
        toggleMidtake();
        toggleMidtakeTwo();
        sleep(4000);
        toggleIntake();
        toggleMidtake();
        toggleMidtakeTwo();
        outtake.setVelocity(0);




        runTime.reset();

        while (runTime.seconds() <= position.durationSeconds){
            telemetry.addData("Target Velocity", 1500);
            telemetry.addData("Current Velocity", outtake.getVelocity());
            telemetry.addData("Error", 1500 - outtake.getVelocity());
            telemetry.addLine();

            telemetry.addData("Timer", runTime.milliseconds());
            telemetry.addData("Timeout", position.durationSeconds);


            PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Velocity", 1500);
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Current Velocity", outtake.getVelocity());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Error", 1500 - outtake.getVelocity());

            telemetry.update();
            PanelsTelemetry.INSTANCE.getTelemetry().update(telemetry);


        }


        toggleIntake();
        toggleMidtake();





        outtake.setVelocity(0);

    }

}