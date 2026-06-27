package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name = "TurretAutoAlign", group = "TeleOp")
public class TurretAutoAlign extends OpMode {

    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private LOGITURRETMECHANISM turret = new LOGITURRETMECHANISM();

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001};
    int stepIndex = 2;

    // Manual edge detection booleans
    boolean lastB = false;
    boolean lastDpadLeft = false;
    boolean lastDpadRight = false;
    boolean lastDpadUp = false;
    boolean lastDpadDown = false;

    @Override
    public void init() {
        aprilTagProcessor = new AprilTagProcessor.Builder().build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTagProcessor)
                .build();

        turret.init(hardwareMap);
        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        turret.resetTimer();
    }

    @Override
    public void loop() {

        // --- Edge detection ---
        boolean bPressed = gamepad1.b && !lastB;
        boolean dpadLeft = gamepad1.dpad_left && !lastDpadLeft;
        boolean dpadRight = gamepad1.dpad_right && !lastDpadRight;
        boolean dpadUp = gamepad1.dpad_up && !lastDpadUp;
        boolean dpadDown = gamepad1.dpad_down && !lastDpadDown;

        lastB = gamepad1.b;
        lastDpadLeft = gamepad1.dpad_left;
        lastDpadRight = gamepad1.dpad_right;
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;

        // --- Step index cycling ---
        if (bPressed) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        // --- PD tuning via D-pad ---
        if (dpadLeft) turret.setkP(turret.getkP() - stepSizes[stepIndex]);
        if (dpadRight) turret.setkP(turret.getkP() + stepSizes[stepIndex]);
        if (dpadUp) turret.setkD(turret.getkD() + stepSizes[stepIndex]);
        if (dpadDown) turret.setkD(turret.getkD() - stepSizes[stepIndex]);

        // --- Vision: find tag ID 20 ---
        AprilTagDetection id20 = null;
        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
        for (AprilTagDetection det : detections) {
            if (det.id == 20) {
                id20 = det;
                break;
            }
        }

        // --- Turret update ---
        turret.update(id20);

        // --- Telemetry ---
        if (id20 != null) {
            telemetry.addData("Tag ID", id20.id);
            telemetry.addData("Tag X", "%.2f", id20.ftcPose.x);
            telemetry.addData("Tag Y", "%.2f", id20.ftcPose.y);
            telemetry.addData("Tag Z", "%.2f", id20.ftcPose.z);
            telemetry.addData("Yaw", "%.2f", id20.ftcPose.yaw);
            telemetry.addData("Bearing","%.2f", id20.ftcPose.bearing);
        } else {
            telemetry.addLine("No tag detected — turret stopped");
        }

        telemetry.addLine("-----------------------");
        telemetry.addData("Tuning P", "%.5f (D-pad L/R)", turret.getkP());
        telemetry.addData("Tuning D", "%.5f (D-pad U/D)", turret.getkD());
        telemetry.addData("Step Size", "%.5f (B button)", stepSizes[stepIndex]);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}