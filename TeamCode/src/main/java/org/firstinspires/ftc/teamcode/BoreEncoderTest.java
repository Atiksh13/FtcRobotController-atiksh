package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "Bore Encoder Test")
public class BoreEncoderTest extends OpMode {

    private DcMotorEx encoder;
    private int position = 0;

    @Override
    public void init() {

        encoder = hardwareMap.get(DcMotorEx.class, "encoder");

        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    @Override
    public void loop() {

        int currentPosition = encoder.getCurrentPosition();

        boolean isChanging = (currentPosition != position);

        position= currentPosition;

        if (gamepad1.a){
            encoder.setPower(1);
        }

        if (gamepad1.b){
            encoder.setPower(0);
        }


        telemetry.addData("Ticks", currentPosition);
        telemetry.addData("Encoder Status", isChanging ? "WORKING (Moving)" : "STUCK / Not Moving");


        sleep(100);


        telemetry.update();


    }
}
