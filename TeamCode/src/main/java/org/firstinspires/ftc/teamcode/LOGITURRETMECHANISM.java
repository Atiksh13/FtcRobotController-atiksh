package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class LOGITURRETMECHANISM {

    private DcMotorEx turret;

    // FIX 1: kP and kD are now on a similar tuning scale because the
    // D-term no longer divides by deltaTime (which caused wild spikes).
    // Start tuning from these defaults and adjust with the gamepad.
    private double kP = 0.02370;
    private double kD = 0.00008;

    private double goalX = 0.0; // target bearing (0 = centred)
    private double lastError = 0.0;
    private double angleTolerance = 0.2; // degrees — inside this, turret stops
    private final double MAX_POWER = 0.6;

    private final ElapsedTime timer = new ElapsedTime();

    // -------------------------------------------------------------------------
    public void init(HardwareMap hwmap) {
        turret = hwmap.get(DcMotorEx.class, "turret");
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // FIX 2: If the turret still runs the wrong way after the sign fix,
        // uncomment the line below instead of flipping signs again.
        // turret.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    // --- Getters / Setters ---------------------------------------------------
    public void setkP(double v) { kP = v; }
    public double getkP() { return kP; }
    public void setkD(double v) { kD = v; }
    public double getkD() { return kD; }
    public void resetTimer() { timer.reset(); }

    // -------------------------------------------------------------------------
    public void update(AprilTagDetection curID) {

        // Guard: stop if no tag or pose is missing
        if (curID == null || curID.ftcPose == null) {
            turret.setPower(0);
            lastError = 0;
            timer.reset();
            return;
        }

        // FIX 3: Error sign was backwards.
        // ftcPose.bearing is positive to the LEFT in FTC SDK convention.
        // error = bearing - goalX → positive bearing means turret turns left
        // to re-centre, which is the correct direction.
        double error = curID.ftcPose.bearing - goalX;

        // FIX 4: D-term no longer divided by deltaTime.
        // Dividing by a tiny deltaTime (fast loop) produced enormous spikes.
        // Raw delta-error * kD is stable and easy to tune on the same scale as kP.
        double pTerm = error * kP;
        double dTerm = (error - lastError) * kD;

        double power;
        if (Math.abs(error) < angleTolerance) {
            power = 0;
        } else {
            power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
        }

        turret.setPower(power);
        lastError = error;
    }
}