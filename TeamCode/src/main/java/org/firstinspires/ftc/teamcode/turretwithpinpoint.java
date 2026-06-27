package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Turret Field Lock", group = "TeleOp")
public class turretwithpinpoint extends OpMode {

    private DcMotor turret;
    private GoBildaPinpointDriver pinpoint;

    private static final double TICKS_PER_DEGREE = 5.3416666667;

    @Override
    public void init() {
        turret = hardwareMap.get(DcMotor.class, "turret");
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        turret.setDirection(DcMotor.Direction.REVERSE);

        // Reset turret encoder
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set up RUN_TO_POSITION
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.8);

        // Reset Pinpoint heading
        pinpoint.resetPosAndIMU();
    }

    @Override
    public void loop() {
        // Update Pinpoint
        pinpoint.update();

        // Get heading in degrees
        double headingDegrees = pinpoint.getHeading(AngleUnit.DEGREES);

        // Convert to ticks (negative to counter-rotate)
        int targetTicks = (int) (-headingDegrees * TICKS_PER_DEGREE);

        // Set turret target
        turret.setTargetPosition(targetTicks);

        // Telemetry
        telemetry.addData("Heading (deg)", headingDegrees);
        telemetry.addData("Target Ticks", targetTicks);
        telemetry.addData("Current Ticks", turret.getCurrentPosition());
        telemetry.update();
    }
}