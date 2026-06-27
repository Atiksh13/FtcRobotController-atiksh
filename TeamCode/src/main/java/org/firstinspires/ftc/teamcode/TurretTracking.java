package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Turret Tuning")
public class TurretTracking extends OpMode {

    CRServo tServo1, tServo2;
    DcMotorEx turretEncoder;

    double kP = 0.01;
    double kPStep = 0.001;

    boolean lastDpadUp = false;
    boolean lastDpadDown = false;
    boolean lastDpadLeft = false;
    boolean lastDpadRight = false;

    @Override
    public void init() {
        tServo1 = hardwareMap.get(CRServo.class, "Tservo1");
        tServo2 = hardwareMap.get(CRServo.class, "Tservo2");
        turretEncoder = hardwareMap.get(DcMotorEx.class, "encoder");

        turretEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        // Change step size with dpad left/right
        if (gamepad1.dpad_right && !lastDpadRight) {
            kPStep *= 10;
            if (kPStep > 0.1) kPStep = 0.1;
        }
        if (gamepad1.dpad_left && !lastDpadLeft) {
            kPStep /= 10;
            if (kPStep < 0.0001) kPStep = 0.0001;
        }

        // Tune kP with dpad up/down
        if (gamepad1.dpad_up && !lastDpadUp) {
            kP += kPStep;
        }
        if (gamepad1.dpad_down && !lastDpadDown) {
            kP -= kPStep;
            if (kP < 0) kP = 0;
        }

        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;
        lastDpadLeft = gamepad1.dpad_left;
        lastDpadRight = gamepad1.dpad_right;

        // Turret control
        int ticks = turretEncoder.getCurrentPosition();
        double turretDegrees = (ticks / 8192.0) * 72.0;

        double output = kP * (0 - turretDegrees);
        output = Math.max(-1.0, Math.min(1.0, output));

        tServo1.setPower(output);
        tServo2.setPower(output);

        telemetry.addData("kP (dpad up/down)", kP);
        telemetry.addData("Step (dpad left/right)", kPStep);
        telemetry.addData("Turret Ticks", ticks);
        telemetry.addData("Turret Degrees", turretDegrees);
        telemetry.addData("Turret Output", output);
        telemetry.update();
    }
}