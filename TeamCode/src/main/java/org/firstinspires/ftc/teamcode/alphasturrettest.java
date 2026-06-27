package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Alphas Turret Test")
public class alphasturrettest extends OpMode {
    private DcMotor turret;
    int fullrotation=1923;
    @Override
    public void init() {


        turret = hardwareMap.get(DcMotor.class, "turret");

        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Target Position", turret.getTargetPosition());
        telemetry.addData("Current Position", turret.getCurrentPosition());
        telemetry.update();

        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {

        turret.setTargetPosition(fullrotation);

        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        turret.setPower(0.5);

        telemetry.addData("Target Position", turret.getTargetPosition());
        telemetry.addData("Current Position", turret.getCurrentPosition());
        telemetry.update();
    }
}
