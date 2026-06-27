package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "2 motor intake")
public class
TwoMotor_intake1 extends OpMode {

    private DcMotor intake1;
    private DcMotor intake2;
    private Servo servo;
    private Servo servo1;
    private Servo servo2;
    private DcMotor shooter1;
    private DcMotor shooter2;
    private IMU imu;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void init() {

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        servo1.setDirection(Servo.Direction.REVERSE);
        servo2.setDirection(Servo.Direction.FORWARD);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        intake1 = hardwareMap.get(DcMotor.class, "intake1");
        intake2 = hardwareMap.get(DcMotor.class, "intake2");

        shooter1 = hardwareMap.get(DcMotor.class, "shooter1");
        shooter2 = hardwareMap.get(DcMotor.class, "shooter2");

        servo = hardwareMap.get(Servo.class,"servo");


        intake1.setDirection(DcMotorSimple.Direction.FORWARD);
        intake2.setDirection(DcMotorSimple.Direction.REVERSE);

        servo.setDirection(Servo.Direction.REVERSE);

        servo.setPosition(0);

        servo1.setPosition(0);
        servo2.setPosition(0);

        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );

        imu.initialize(parameters);

        telemetry.addLine("IMU Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {

        if(gamepad1.b){
            servo.setPosition(40);
        }

        if(gamepad1.y){
            servo.setPosition(0);
        }

        intake1.setPower(-gamepad1.left_trigger + gamepad1.right_trigger);
        intake2.setPower(-gamepad1.left_trigger + gamepad1.right_trigger);


        shooter1.setPower(gamepad1.left_stick_y);
        shooter2.setPower(gamepad1.left_stick_y);

        telemetry.addData("intake1 Speed", intake1.getPower());
        telemetry.addData("intake2 Speed", intake2.getPower());

        telemetry.addData("Servo position", servo.getPosition());

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if (gamepad1.dpad_down) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles()
                .getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;


        double denominator = Math.max(
                Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx),
                1
        );

        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;


        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);


        if(gamepad1.a){
            servo1.setPosition(0);
            servo2.setPosition(0);
        }

        if(gamepad1.x){
            servo1.setPosition(70);
            servo2.setPosition(70);
        }

        telemetry.addData("Heading (deg)", Math.toDegrees(botHeading));
        telemetry.addData("intake1 Speed", intake1.getPower());
        telemetry.addData("intake2 Speed", intake2.getPower());
        telemetry.addData("Servo position", servo.getPosition());
        telemetry.addData("Servo1", servo1.getPosition());
        telemetry.addData("Servo2", servo2.getPosition());
        telemetry.update();

    }
}