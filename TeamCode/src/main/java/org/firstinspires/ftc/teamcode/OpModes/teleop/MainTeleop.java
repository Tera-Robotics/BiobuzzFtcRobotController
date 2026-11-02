package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@NextTeleop(name = "TeleOp Principal", group = "TeleOp")
public class MainTeleop extends NextOpMode {

    private final Robot robot;
    private final CommandGamepad driver;

   private Follower follower;

    public MainTeleop(Robot robot) {

        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;
        this.driver = new CommandGamepad(gamepad1);
    }

    @Override
    public void start() {

        follower = Constants.create(hardwareMap);

        DriveCommands.mecanumDriveFieldCentric(
                robot.drivetrain.leftFront,
                robot.drivetrain.rightFront,
                robot.drivetrain.leftBack,
                robot.drivetrain.rightBack,
                gamepad1,
                () -> follower.pose().heading()
        ).schedule();


        driver.leftBumper().whileTrue(
                Commands.infinite(() -> DriveCommands.setScalar(0.4))
                        .setEnd(interrupted -> DriveCommands.setScalar(1.0))
        );




        driver.y().onTrue(robot.launcher.setRpmCommand(2300));
        driver.x().onTrue(robot.launcher.stopCommand());
        driver.a().onTrue(robot.launcher.idleCommand());
        driver.b().onTrue(robot.collector.collect(-0.6));


    }


    @Override
    public void periodic() {

        //follower.update();

        telemetry.addData("Status", "NextFTC Ativo!");
        //telemetry.addData("Velocidade Drivetrain", DriveCommands.getScalar());
        // Informações em tempo real do Lançador
        telemetry.addData("Lançador Estado", robot.launcher.getState());
        telemetry.addData("Lançador RPM Alvo", "%.0f RPM", robot.launcher.getTargetRPM());
        telemetry.addData("Lançador RPM Real", "%.0f RPM", robot.launcher.getCurrentRPM());
        telemetry.addData("Lançador Pronto?", robot.launcher.isReady() ? "SIM (LIVRE)" : "AGUARDE");
        telemetry.addData("Status", "NextFTC Ativo!");
        //telemetry.addData("Velocidade Atual", DriveCommands.getScalar());
    }

    @Override
    public void end() {
    }
}
