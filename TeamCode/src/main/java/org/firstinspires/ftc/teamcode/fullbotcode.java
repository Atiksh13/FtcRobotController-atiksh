package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "FULLBOTCODE")
public class fullbotcode extends OpMode {
    DcMotor frontleft, frontright, backleft, backright;
    IMU imu;
    DcMotor intake1,intake2,shooter1,shooter2;
    Servo Rservo1;
    Servo Rservo2;

    @Override
    public void init() {
        Rservo1 = hardwareMap.get(Servo.class, "Rservo1");
        Rservo2 = hardwareMap.get(Servo.class, "Rservo2");
        intake1  = hardwareMap.get(DcMotor.class,"intake1");
        intake2  = hardwareMap.get(DcMotor.class,"intake2");
        shooter1=hardwareMap.get(DcMotor.class,"shooter1");
        shooter2=hardwareMap.get(DcMotor.class,"shooter2");
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        backright = hardwareMap.get(DcMotor.class, "backright");

        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);

        Rservo1.setDirection(Servo.Direction.REVERSE);
        Rservo2.setDirection(Servo.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        Rservo1.setPosition(0.22);
        Rservo2.setPosition(0.22);

        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);
    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            Rservo1.setPosition(0.33);
            Rservo2.setPosition(0.22);
        }
        else if (gamepad1.b) {
            Rservo1.setPosition(0.5);
            Rservo2.setPosition(0.5);
        }

        if (gamepad1.x){
            shooter1.setPower(1);
            shooter2.setPower(1);
        }
        intake1.setPower(-gamepad1.left_trigger + gamepad1.right_trigger);
        intake2.setPower(-gamepad1.left_trigger + gamepad1.right_trigger);


        double y = gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double  rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        rotX = rotX * 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

        double frontleftPower = (rotY + rotX + rx) / denominator;
        double backleftPower = (rotY - rotX + rx) / denominator;
        double frontrightPower = (rotY - rotX - rx) / denominator;
        double backrightPower = (rotY + rotX - rx) / denominator;

        frontleft.setPower(frontleftPower);
        backleft.setPower(backleftPower);
        frontright.setPower(frontrightPower);
        backright.setPower(backrightPower);




        telemetry.addData("Rservo1 Position", Rservo1.getPosition());
        telemetry.addData("Rservo2 Position", Rservo2.getPosition());
        telemetry.update();
    }
}