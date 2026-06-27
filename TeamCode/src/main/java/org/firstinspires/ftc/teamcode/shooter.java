package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name = "shooter test")
public class shooter extends OpMode {
    private DcMotor shooter1;
    private DcMotor shooter2;
    @Override
    public void init() {

        shooter1=hardwareMap.get(DcMotor.class,"shooter1");
        shooter2=hardwareMap.get(DcMotor.class,"shooter2");

    }

    @Override
    public void loop() {

        if (gamepad1.a){
            shooter2.setPower(1);
            shooter1.setPower(1);
        }
        if(gamepad1.b){
            shooter1.setPower(0);
            shooter2.setPower(0);
        }

    }
}
