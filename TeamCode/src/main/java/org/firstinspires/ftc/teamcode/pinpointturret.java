package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "Turret Heading Lock")
public class pinpointturret extends OpMode {

    private GoBildaPinpointDriver pinpoint;
    private DcMotor turretMotor;
    private ElapsedTime timer = new ElapsedTime();

    // --- PID Coefficients (tune these) ---
    private static final double Kp = 0.02;
    private static final double Ki = 0.0;
    private static final double Kd = 0.002;

    // --- Turret target field heading (degrees) ---
    private static final double TARGET_HEADING = 0.0;

    // --- PID state ---
    private double lastError = 0.0;
    private double integrator = 0.0;
    private double lastTime = 0.0;

    // --- Motor output clamp ---
    private static final double MAX_POWER = 0.6;

    // --- Deadband (degrees) ---
    private static final double DEADBAND = 1.5;

    @Override
    public void init() {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        turretMotor = hardwareMap.get(DcMotor.class, "turret");

        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pinpoint.resetPosAndIMU();

        timer.reset();
        lastTime = timer.seconds();

        telemetry.addLine("Turret Heading Lock Ready");
        telemetry.update();
    }

    @Override
    public void loop() {
        pinpoint.update();

        double currentHeading = pinpoint.getHeading(AngleUnit.DEGREES);
        double currentTime = timer.seconds();
        double dt = currentTime - lastTime;
        lastTime = currentTime;

        // Wrap error to [-180, 180]
        double error = normalizeAngle(TARGET_HEADING - currentHeading);

        if (Math.abs(error) < DEADBAND) {
            // Inside deadband — stop motor, reset integrator
            turretMotor.setPower(0);
            integrator = 0.0;
            lastError = 0.0;
        } else {
            // PID
            double derivative = (dt > 0) ? (error - lastError) / dt : 0.0;
            integrator += error * dt;

            // Anti-windup clamp on integrator
            integrator = Math.max(-20.0, Math.min(20.0, integrator));

            double output = (Kp * error) + (Ki * integrator) + (Kd * derivative);

            // Clamp output
            output = Math.max(-MAX_POWER, Math.min(MAX_POWER, output));

            turretMotor.setPower(output);
            lastError = error;
        }

        telemetry.addData("Target Heading", TARGET_HEADING);
        telemetry.addData("Current Heading", "%.2f", currentHeading);
        telemetry.addData("Error", "%.2f", normalizeAngle(TARGET_HEADING - currentHeading));
        telemetry.addData("Motor Power", turretMotor.getPower());
        telemetry.addData("Pinpoint Status", pinpoint.getDeviceStatus());
        telemetry.update();
    }

    /**
     * Wraps an angle to the range [-180, 180] degrees.
     * Ensures PID always takes the shortest path.
     */
    private double normalizeAngle(double angle) {
        while (angle > 180.0)  angle -= 360.0;
        while (angle < -180.0) angle += 360.0;
        return angle;
    }
}