package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "robotcentricmecanum")
public class Robotentriceccanum extends OpMode {

    private DcMotor frontleft, frontright, backright, backleft;

    private GoBildaPinpointDriver pinpoint;

    @Override
    public void init() {
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backright = hardwareMap.get(DcMotor.class, "backright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        telemetry.addLine("Pinpoint Initialized");
        telemetry.update();

        frontleft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontright.setDirection(DcMotorSimple.Direction.FORWARD);
        backleft.setDirection(DcMotorSimple.Direction.FORWARD);
        backright.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {

        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        frontleft.setPower(y + x + rx);
        frontright.setPower(y - x - rx);
        backleft.setPower(y - x + rx);
        backright.setPower(y + x - rx);

        telemetry.addData("FL", frontleft.getPower());
        telemetry.addData("FR", frontright.getPower());
        telemetry.addData("BL", backleft.getPower());
        telemetry.addData("BR", backright.getPower());
        telemetry.update();

        pinpoint.update();

        telemetry.addData("Heading", pinpoint.getHeading(AngleUnit.DEGREES));

        telemetry.update();
    }
}