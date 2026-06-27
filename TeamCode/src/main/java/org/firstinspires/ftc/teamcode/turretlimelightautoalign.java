package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "TurretAutoAlign", group = "TeleOp")
public class turretlimelightautoalign extends OpMode {

    // Limelight
    private Limelight3A limelight;

    // Turret
    private final limelightTURRETMECHANISM turret =
            new limelightTURRETMECHANISM();

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;

    // PID tuning
    private final double[] stepSizes = {
            0.1,
            0.01,
            0.001,
            0.0001,
            0.00001
    };

    private int stepIndex = 2;

    // Edge detection
    private boolean lastB = false;
    private boolean lastLeft = false;
    private boolean lastRight = false;
    private boolean lastUp = false;
    private boolean lastDown = false;

    @Override
    public void init() {

        // ---------------- LIMELIGHT ----------------

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);
        limelight.start();

        // ---------------- TURRET ----------------

        turret.init(hardwareMap);

        // ---------------- DRIVETRAIN ----------------

        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");

        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");

        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);

        backLeft.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void start() {

        turret.resetTimer();
    }

    @Override
    public void loop() {

        // ---------------- DRIVETRAIN ----------------

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(
                Math.abs(y) + Math.abs(x) + Math.abs(rx), 1
        );

        double fl = (y + x + rx) / denominator;
        double bl = (y - x + rx) / denominator;
        double fr = (y - x - rx) / denominator;
        double br = (y + x - rx) / denominator;

        frontLeft.setPower(fl);
        frontRight.setPower(fr);
        backLeft.setPower(bl);
        backRight.setPower(br);

        // ---------------- PID TUNING BUTTONS ----------------

        boolean bPressed     = gamepad1.b          && !lastB;
        boolean leftPressed  = gamepad1.dpad_left  && !lastLeft;
        boolean rightPressed = gamepad1.dpad_right && !lastRight;
        boolean upPressed    = gamepad1.dpad_up    && !lastUp;
        boolean downPressed  = gamepad1.dpad_down  && !lastDown;

        lastB     = gamepad1.b;
        lastLeft  = gamepad1.dpad_left;
        lastRight = gamepad1.dpad_right;
        lastUp    = gamepad1.dpad_up;
        lastDown  = gamepad1.dpad_down;

        if (bPressed) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (leftPressed) {
            turret.setkP(turret.getkP() - stepSizes[stepIndex]);
        }

        if (rightPressed) {
            turret.setkP(turret.getkP() + stepSizes[stepIndex]);
        }

        if (upPressed) {
            turret.setkD(turret.getkD() + stepSizes[stepIndex]);
        }

        if (downPressed) {
            turret.setkD(turret.getkD() - stepSizes[stepIndex]);
        }

        // ---------------- LIMELIGHT ----------------

        LLResult result = limelight.getLatestResult();

        boolean targetFound = false;
        double tx = 0;

        if (result != null && result.isValid()) {

            for (LLResultTypes.FiducialResult tag :
                    result.getFiducialResults()) {

                if (tag.getFiducialId() == 24) {

                    targetFound = true;
                    tx = result.getTx();
                    break;
                }
            }
        }

        // ---------------- TURRET UPDATE ----------------

        turret.update(targetFound, tx);

        // ---------------- TELEMETRY ----------------

        telemetry.addData("Target",  targetFound);
        telemetry.addData("TX",      "%.2f",  tx);
        telemetry.addData("kP",      "%.5f",  turret.getkP());
        telemetry.addData("kD",      "%.5f",  turret.getkD());
        telemetry.addData("Step",    "%.5f",  stepSizes[stepIndex]);
        telemetry.update();
    }

    @Override
    public void stop() {

        if (limelight != null) {
            limelight.stop();
        }
    }
}