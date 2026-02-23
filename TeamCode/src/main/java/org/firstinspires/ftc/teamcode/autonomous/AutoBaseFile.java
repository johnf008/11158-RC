package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutoBaseFile extends LinearOpMode {

    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;

    public DcMotor intake = null;
    public DcMotor midtake = null;
    public DcMotorEx outtake = null;

    public ElapsedTime runTime = new ElapsedTime();
    public final double distance_For_360_Turn = 240;
    public double distanceTurnNeeded;


    // Calculate TICKS_PER_INCH for encoders ....................................................................................
    static final double CM_REDUCTION_MULTIPLIER = 0.1724137931; // Test how accurate the encoders are with real world CM

    static final double TICKS_PER_REVOLUTION = 2_786.2;
    static final double DRIVE_GEAR_RATIO = 1.0 ;
    static final double WHEEL_DIAMETER_CM = 9.6;

    static final double TICKS_PER_MM = (TICKS_PER_REVOLUTION * DRIVE_GEAR_RATIO) /
            (WHEEL_DIAMETER_CM * 3.1415);

    // ..........................................................................................................................

    public void runOpMode(){
        //drive initialize
        this.backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        this.backRight = hardwareMap.get(DcMotor.class, "rightBack");
        this.frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        this.frontRight = hardwareMap.get(DcMotor.class, "rightFront");

        this.intake = hardwareMap.get(DcMotor.class, "intake");
        this.midtake = hardwareMap.get(DcMotor.class, "midtake");
        this.outtake = hardwareMap.get(DcMotorEx.class, "outtake");




        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set wheel motor settings..................................................................
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //set other motor settings..................................................................
        intake.setDirection(DcMotor.Direction.FORWARD);
        outtake.setDirection(DcMotorSimple.Direction.FORWARD);
        midtake.setDirection(DcMotorSimple.Direction.FORWARD);

        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        midtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        midtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outtake.setTargetPosition(0);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(250, 0, 0, 18.4);
        outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);



    }
    //set a function to use encoders in the auto functions.............................................
    public void encoderDrive( double speed,
                              double frontLeftInches, double frontRightInches,
                              double backLeftInches, double backRightInches,
                              double timeOutSeconds ) {

        resetWheelMotorsEncoders();

        frontLeft.setTargetPosition (  -(frontLeft.getCurrentPosition() + (int) ( frontLeftInches * TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)  ) );
        frontRight.setTargetPosition( -(frontRight.getCurrentPosition() + (int) ( frontRightInches * TICKS_PER_MM * CM_REDUCTION_MULTIPLIER) ) );
        backLeft.setTargetPosition  ( (backLeft.getCurrentPosition()   + (int) ( backLeftInches *  TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)  ) );
        backRight.setTargetPosition ( (backRight.getCurrentPosition()  + (int) ( backRightInches * TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)  ) );



        frontRight.setMode( DcMotor.RunMode.RUN_TO_POSITION );
        frontLeft.setMode ( DcMotor.RunMode.RUN_TO_POSITION );
        backLeft.setMode  ( DcMotor.RunMode.RUN_TO_POSITION );
        backRight.setMode ( DcMotor.RunMode.RUN_TO_POSITION );

        runTime.reset();
        setWheelMotorsPower(1);

        while ( runTime.seconds() <= timeOutSeconds &&
                ( frontLeft.isBusy() || frontRight.isBusy() ||
                backLeft.isBusy()  || backRight.isBusy()  ) )
        {

            telemetry.addData("FL Busy; ticks; target ticks", frontLeft.isBusy() + " " + frontLeft.getCurrentPosition() + " " + frontLeft.getTargetPosition()   );
            telemetry.addData("FR Busy; ticks; target ticks", frontRight.isBusy() + " " + frontRight.getCurrentPosition() + " " + frontRight.getTargetPosition());
            telemetry.addData("BL Busy; ticks; target ticks", backLeft.isBusy() + " " + backLeft.getCurrentPosition() + " " + backLeft.getTargetPosition()      );
            telemetry.addData("BR Busy; ticks; target ticks", backRight.isBusy() + " " + backRight.getCurrentPosition() + " " + backRight.getTargetPosition()   );

            telemetry.addLine();
            telemetry.addData("Intake Busy", intake.isBusy());
            telemetry.addData("Midtake Busy", midtake.isBusy());
            telemetry.addData("Outtake Busy", outtake.isBusy());



            telemetry.addLine();
            telemetry.addData("Timer", runTime);
            telemetry.addData("Timeout", timeOutSeconds);
            telemetry.update();

        }

        setWheelMotorsPower(0);
        resetWheelMotorsEncoders();
    }

    //movement methods..............................................................................
    public void setWheelMotorsPower(double power){
        backLeft.setPower(power);
        backRight.setPower(power);
        frontLeft.setPower(power);
        frontRight.setPower(power);
    }
    public void resetWheelMotorsEncoders(){
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void forward(double speed , double distanceCM, double timeOutSeconds){
        encoderDrive(speed,
                distanceCM, distanceCM,
                distanceCM, distanceCM,
                timeOutSeconds );
    }
    public void backward(double speed, double distanceInches, double timeOutSeconds){
        encoderDrive(speed,
                -distanceInches, -distanceInches,
                -distanceInches, -distanceInches,
                timeOutSeconds );
    }

    public void move_left(double speed, double distanceInches, double timeOutSeconds){
        encoderDrive(speed,
                -distanceInches, distanceInches,
                distanceInches, -distanceInches,
                timeOutSeconds );
    }

    public void move_right(double speed, double distanceInches, double timeOutSeconds){
        encoderDrive(speed,
                distanceInches, -distanceInches,
                -distanceInches, distanceInches,
                timeOutSeconds );
    }
    public void strafeLeft(double speed, long durationSeconds){
        frontLeft.setPower(-speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(-speed);

        sleep(durationSeconds*1000);

        setWheelMotorsPower(0);
    }
    public void strafeRight(double speed, long durationSeconds){

        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        backLeft.setPower(-speed);
        backRight.setPower(speed);

        sleep(durationSeconds*1000);

        setWheelMotorsPower(0);
    }
    public void rotateLeft(double speed, double turnDegree, double timeOutSeconds){
        turnDegree = turnDegree/360;
        distanceTurnNeeded = distance_For_360_Turn * turnDegree;
        encoderDrive(speed,
                -distanceTurnNeeded, distanceTurnNeeded,
                -distanceTurnNeeded, distanceTurnNeeded,
                timeOutSeconds );
    }
    public void rotateRight(double speed, double turnDegree, double timeOutSeconds){
        turnDegree = turnDegree/360;
        distanceTurnNeeded = distance_For_360_Turn * turnDegree;
        encoderDrive(speed,
                distanceTurnNeeded, -distanceTurnNeeded,
                distanceTurnNeeded, -distanceTurnNeeded,
                timeOutSeconds );
    }
    //other methods

    public void toggleIntake(){

        intake.setPower(intake.getPower() == 0 ? .9 : 0);
    }

    public void launch(){
        //start the velocity for the flywheel
        outtake.setVelocity(1000);
        //wait 5 seconds
        sleep(5000);
        //power intake and midtake
        outtake.setVelocity(1200);
        intake.setPower(1);
        midtake.setPower(-1);
        sleep(4000);


    }
}