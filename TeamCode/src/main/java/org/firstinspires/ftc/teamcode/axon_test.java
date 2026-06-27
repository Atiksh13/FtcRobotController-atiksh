package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "Axon Test")
public class axon_test extends OpMode {

    private CRServo axon1;
    private CRServo axon2;


    @Override
    public void init() {
        axon1 = hardwareMap.get(CRServo.class, "axon1");
        axon2 = hardwareMap.get(CRServo.class, "axon2");

    }

    @Override
    public void loop() {

        if(gamepad1.a){
            axon1.setPower(0.5);
            axon2.setPower(0.5);
        }


        if (gamepad1.b){
            axon1.setPower(-0.5);
            axon2.setPower(-0.5);
        }
    }
}
