package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Pinpoint Test")
public class pinpointtest extends OpMode {

    private GoBildaPinpointDriver pinpoint;

    @Override
    public void init() {

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        telemetry.addLine("Pinpoint Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        pinpoint.update();

        telemetry.addData("Heading", pinpoint.getHeading(AngleUnit.DEGREES));

        telemetry.update();
    }
}
