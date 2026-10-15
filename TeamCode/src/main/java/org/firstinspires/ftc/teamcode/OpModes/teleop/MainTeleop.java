package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@NextTeleop(name = "TeleOp Principal", group = "TeleOp")
public class MainTeleop extends NextOpMode {

    private final Robot robot;
    private final CommandGamepad driver;

    public MainTeleop() {
        this(new Robot());
    }
    private MainTeleop(Robot robot) {

        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;
        this.driver = new CommandGamepad(gamepad1);
    }

    @Override
    public void start() {

        robot.drivetrain.drive(gamepad1).schedule();

        driver.leftBumper().whileTrue(
                Commands.infinite(() -> DriveCommands.setScalar(0.4))
                        .setEnd(interrupted -> DriveCommands.setScalar(1.0))
        );

        driver.y().onTrue(robot.launcher.setRpmCommand(2200));
        // Aperta X -> Desliga o lançador
        driver.x().onTrue(robot.launcher.stopCommand());
        // (Opcional) D-pad UP -> Coloca em modo de espera (IDLE ~2000 RPM)
        driver.a().onTrue(robot.launcher.idleCommand());
    }


    @Override
    public void periodic() {

        telemetry.addData("Status", "NextFTC Ativo!");
        telemetry.addData("Velocidade Drivetrain", DriveCommands.getScalar());
        // Informações em tempo real do Lançador
        telemetry.addData("Lançador Estado", robot.launcher.getState());
        telemetry.addData("Lançador RPM Alvo", "%.0f RPM", robot.launcher.getTargetRPM());
        telemetry.addData("Lançador RPM Real", "%.0f RPM", robot.launcher.getCurrentRPM());
        telemetry.addData("Lançador Pronto?", robot.launcher.isReady() ? "SIM (LIVRE)" : "AGUARDE");


        telemetry.addData("Status", "NextFTC Ativo!");
        telemetry.addData("Velocidade Atual", DriveCommands.getScalar());
    }

    @Override
    public void end() {
        NextMotor.Companion.getMotorEventLoop().clear();
    }
}