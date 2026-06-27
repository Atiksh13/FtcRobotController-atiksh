package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "2 Motor Intake")
public class TwoMotor_intake extends OpMode {

    private DcMotor intake1;
    private Servo Rservo1;

    private Servo Rservo2;
    private Servo servo1;
    private Servo servo2;
    private DcMotor shooter1;
    private DcMotor shooter2;

    @Override
    public void init() {

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        servo1.setDirection(Servo.Direction.FORWARD);
        servo2.setDirection(Servo.Direction.REVERSE);

        intake1 = hardwareMap.get(DcMotor.class, "intake1");

        shooter1 = hardwareMap.get(DcMotor.class, "shooter1");
        shooter2 = hardwareMap.get(DcMotor.class, "shooter2");

        Rservo1 = hardwareMap.get(Servo.class,"Rservo1");
        Rservo2 = hardwareMap.get(Servo.class, "Rservo2");

        intake1.setDirection(DcMotorSimple.Direction.FORWARD);

        Rservo1.setDirection(Servo.Direction.REVERSE);
        Rservo2.setDirection(Servo.Direction.FORWARD);

        Rservo1.setPosition(0);
        Rservo2.setPosition(0);

        servo1.setPosition(0);
        servo2.setPosition(0);



    }

    @Override
    public void loop() {


        if(gamepad1.b){
            Rservo1.setPosition(40);
            Rservo2.setPosition(40);
        }

        if(gamepad1.x){
            Rservo1.setPosition(20);
            Rservo2.setPosition(20);
        }

        intake1.setPower(-gamepad1.left_trigger + gamepad1.right_trigger);

        if(gamepad1.yWasPressed()){
            shooter1.setPower(0.75);
            shooter2.setPower(0.75);
        }

        if(gamepad1.yWasReleased()){
            shooter1.setPower(0);
            shooter2.setPower(0);
        }



        telemetry.addData("intake1 Speed", intake1.getPower());

        telemetry.addData("Servo position", Rservo1.getPosition());

        if(gamepad1.aWasReleased()){
            servo1.setPosition(0);
            servo2.setPosition(0);
        }

        if(gamepad1.aWasPressed()){
            servo1.setPosition(0.5);
            servo2.setPosition(0.5);

        }

        telemetry.addData("intake1 Speed", intake1.getPower());
        telemetry.addData("RServo1 position", Rservo1.getPosition());
        telemetry.addData("Rservo2 position", Rservo2.getPosition());
        telemetry.addData("Servo1", servo1.getPosition());
        telemetry.addData("Servo2", servo2.getPosition());
        telemetry.addData("Shooter 1", shooter1.getPower());
        telemetry.addData("Shooter 2", shooter2.getPower());
        telemetry.update();
    }
}