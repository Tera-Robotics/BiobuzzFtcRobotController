
package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.function.Supplier;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.sensors.NextIMU;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;


public class Drivetrain implements Mechanism {

    public final NextMotor rightFront = new NextMotor("rightFront");
    public final NextMotor rightBack = new NextMotor("rightBack");
    public final NextMotor leftFront = new NextMotor("leftFront");
    public final NextMotor leftBack = new NextMotor("leftBack");

    public final NextIMU imu = new NextIMU("imu");


    public Drivetrain() {
        rightFront.setDirection(NextMotor.Direction.FORWARD);
        leftFront.setDirection(NextMotor.Direction.FORWARD);
        leftBack.setDirection(NextMotor.Direction.FORWARD);
        rightBack.setDirection(NextMotor.Direction.REVERSE);
    }

    // 1. Modo SEM orientação à arena (Robot-Centric)
    public Command driveRobotCentric(Gamepad gamepad) {
        return DriveCommands.mecanumDrive(
                leftFront,
                rightFront,
                leftBack,
                rightBack,
                gamepad
        );
    }
    // 2. Modo COM orientação à arena (Field-Centric)
    public Command driveFieldCentric(Gamepad gamepad, Supplier<Double> headingSupplier) {
        return DriveCommands.mecanumDriveFieldCentric(
                leftFront,
                rightFront,
                leftBack,
                rightBack,
                gamepad,
                headingSupplier
        );
    }
}








