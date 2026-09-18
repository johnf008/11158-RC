package org.firstinspires.ftc.teamcode.autonomous;

import com.bylazar.telemetry.PanelsTelemetry;
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
    public DcMotor midtake, midtake_two = null;
    public DcMotorEx outtake = null;

    public ElapsedTime runTime = new ElapsedTime();
    public final double distance_For_360_Turn = 240; //
    public double distanceTurnNeeded;


    // Calculate TICKS_PER_INCH for encoders ....................................................................................
    static final double CM_REDUCTION_MULTIPLIER = 0.1724137931; // Test how accurate the encoders are with real world CM

    static final double TICKS_PER_REVOLUTION = 2_786.2;
    static final double DRIVE_GEAR_RATIO = 1.0 ;
    static final double WHEEL_DIAMETER_CM = 9.6;

    static final double TICKS_PER_MM = (TICKS_PER_REVOLUTION * DRIVE_GEAR_RATIO) /
            (WHEEL_DIAMETER_CM * 3.1415); //Math.PI?


    static final double TICK_TOLERANCE = 15; // The error the motors are allowed

    // ..........................................................................................................................

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
        //drive initialize
        this.backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        this.backRight = hardwareMap.get(DcMotor.class, "rightBack");
        this.frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        this.frontRight = hardwareMap.get(DcMotor.class, "rightFront");

        this.intake = hardwareMap.get(DcMotor.class, "intake");
        this.midtake = hardwareMap.get(DcMotor.class, "midtake");

        this.midtake_two = hardwareMap.get(DcMotor.class, "secretMotor");

        this.outtake = hardwareMap.get(DcMotorEx.class, "outtake");


        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //set wheel motor settings..................................................................
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
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

        midtake_two.setDirection(DcMotor.Direction.REVERSE);
        midtake_two.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outtake.setTargetPosition(0);
        resetWheelMotorsEncoders();


    }
    //set a function to use encoders in the auto functions.............................................
    public void encoderDrive( double speed,
                              double frontLeftCM, double frontRightCM,
                              double backLeftCM, double backRightCM,
                              double timeOutSeconds ) {

        resetWheelMotorsEncoders();

        //
        frontLeft.setTargetPosition (  (int) (-frontLeft.getCurrentPosition() +  ( frontLeftCM * TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)   ) );
        frontRight.setTargetPosition( -(int) (frontRight.getCurrentPosition() +  ( frontRightCM * TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)  ) );
        backLeft.setTargetPosition  (  (int) (backLeft.getCurrentPosition()   +  ( backLeftCM *  TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)   ) );
        backRight.setTargetPosition (  (int) (backRight.getCurrentPosition()  +  ( backRightCM * TICKS_PER_MM * CM_REDUCTION_MULTIPLIER)   ) );



        frontRight.setMode( DcMotor.RunMode.RUN_TO_POSITION );
        frontLeft.setMode ( DcMotor.RunMode.RUN_TO_POSITION );
        backLeft.setMode  ( DcMotor.RunMode.RUN_TO_POSITION );
        backRight.setMode ( DcMotor.RunMode.RUN_TO_POSITION );

        runTime.reset();
        setWheelMotorsPower(speed);

        // WAIT FOR ALL MOTORS TO FINISH THEIR MOVEMENT
        telemetry.setCaptionValueSeparator(": ");
        while ( runTime.seconds() <= timeOutSeconds &&
                ( frontLeft.isBusy() || frontRight.isBusy() ||
                  backLeft.isBusy()  || backRight.isBusy()  ) )
        {

            //Show motor data from the robot
            telemetry.addData("FL Busy", frontLeft.isBusy() +
                    "\n Ticks" +frontLeft.getCurrentPosition()+
                    " Target Ticks" +frontLeft.getTargetPosition() );

            telemetry.addData("FR Busy", frontRight.isBusy() +
                    "\n Ticks" +frontRight.getCurrentPosition()+
                    " Target Ticks" +frontRight.getTargetPosition() );

            telemetry.addData("BL Busy", backLeft.isBusy() +
                    "\n Ticks" +backLeft.getCurrentPosition()+
                    " Target Ticks" +backLeft.getTargetPosition() );

            telemetry.addData("BR Busy", backRight.isBusy() +
                    "\n Ticks" +backRight.getCurrentPosition()+
                    " Target Ticks" +backRight.getTargetPosition() );

            telemetry.addLine();
            telemetry.addData("Intake Busy", intake.isBusy());
            telemetry.addData("Midtake Busy", midtake.isBusy());
            telemetry.addData("Outtake Busy", outtake.isBusy());

            telemetry.addLine();
            telemetry.addData("Timer", runTime);
            telemetry.addData("Timeout", timeOutSeconds);
            telemetry.update();

            if (runTime.seconds() < 0.2) {
                continue;  //Gives time to the motors to set target position
            }

            if (Math.abs(frontLeft.getTargetPosition() - frontLeft.getCurrentPosition()) < TICK_TOLERANCE) {
                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (Math.abs(frontRight.getTargetPosition() - frontRight.getCurrentPosition()) < TICK_TOLERANCE) {
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (Math.abs(backLeft.getTargetPosition() - backLeft.getCurrentPosition()) < TICK_TOLERANCE) {
                backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (Math.abs(backRight.getTargetPosition() - backRight.getCurrentPosition()) < TICK_TOLERANCE) {
                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
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
    public void backward(double speed, double distanceCM, double timeOutSeconds){
        encoderDrive(speed,
                -distanceCM, -distanceCM,
                -distanceCM, -distanceCM,
                timeOutSeconds );
    }

    public void move_left(double speed, double distanceCM, double timeOutSeconds){
        encoderDrive(speed,
                -distanceCM, distanceCM,
                distanceCM, -distanceCM,
                timeOutSeconds );
    }

    public void move_right(double speed, double distanceCM, double timeOutSeconds){
        encoderDrive(speed,
                distanceCM, -distanceCM,
                -distanceCM, distanceCM,
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

    public void rotateRightQuarter(double speed, double turnDegree, double timeOutSeconds){
        turnDegree = turnDegree/360;
        distanceTurnNeeded = (170 * turnDegree);
        encoderDrive(speed,
                -distanceTurnNeeded, distanceTurnNeeded,
                -distanceTurnNeeded, distanceTurnNeeded,
                timeOutSeconds );
    }

    //Other methods....
    public void toggleIntake(){
        intake.setPower(intake.getPower() == 0 ? 1 : 0);
    }
    public void toggleMidtake(){
        midtake.setPower(midtake.getPower() == 0 ? 1 : 0);
    }

    public void toggleMidtakeTwo() {
        midtake_two.setPower(midtake_two.getPower() == 0 ? 1 : 0);
    }

    public void launch(LAUNCH_POSITION position){


        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(position.P, 0, 0, position.F);
        outtake.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        outtake.setVelocity(position.velocityNeeded);

        //sleep(5000);
        toggleIntake();
        toggleMidtake();
        toggleMidtakeTwo();
        runTime.reset();

        while (runTime.seconds() <= position.durationSeconds){

            telemetry.setCaptionValueSeparator(": ");
            telemetry.addData("Target Velocity", position.velocityNeeded );
            telemetry.addData("Current Velocity", outtake.getVelocity());
            telemetry.addData("Error", position.velocityNeeded - outtake.getVelocity());
            telemetry.addLine();

            telemetry.addData("Timer", runTime.milliseconds());
            telemetry.addData("Timeout", position.durationSeconds);


            PanelsTelemetry.INSTANCE.getTelemetry().addData("Target Velocity", position.velocityNeeded);
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Current Velocity", outtake.getVelocity());
            PanelsTelemetry.INSTANCE.getTelemetry().addData("Error", position.velocityNeeded - outtake.getVelocity());

            telemetry.update();
            PanelsTelemetry.INSTANCE.getTelemetry().update(telemetry);
        }

        toggleIntake();
        toggleMidtake();
        toggleMidtakeTwo();
        outtake.setVelocity(0);

    }
}