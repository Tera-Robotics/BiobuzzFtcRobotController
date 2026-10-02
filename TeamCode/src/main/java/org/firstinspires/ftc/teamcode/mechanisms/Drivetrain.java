package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;


public class Drivetrain implements Mechanism {
    public final NextMotor rightFront = new NextMotor("rightFront");
    public final NextMotor rightBack = new NextMotor("rightBack");
    public final NextMotor leftFront = new NextMotor("leftFront");
    public final NextMotor leftBack = new NextMotor("leftBack");


    public Drivetrain() {
        rightFront.setDirection(NextMotor.Direction.FORWARD);
        leftFront.setDirection(NextMotor.Direction.FORWARD);
        leftBack.setDirection(NextMotor.Direction.FORWARD);
        rightBack.setDirection(NextMotor.Direction.REVERSE);
    }

    public Command drive(Gamepad gamepad) {
        return DriveCommands.mecanumDrive(leftFront, rightFront, leftBack, rightBack, gamepad);
    }







}
