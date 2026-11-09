package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@NextTeleop(name = "TeleOp Principal", group = "TeleOp")
public class MainTeleop extends NextOpMode {

    private final Robot robot;
    private final CommandGamepad driver;
    private Follower follower;
    private RobotState lastState = null;

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    public MainTeleop(Robot robot) {
        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;
        this.driver = new CommandGamepad(gamepad1);
    }



    @Override
    public void start() {
        follower = Constants.create(hardwareMap);

        // 1. Tração Mecanum
        DriveCommands.mecanumDriveFieldCentric(
                robot.drivetrain.leftFront,
                robot.drivetrain.rightFront,
                robot.drivetrain.leftBack,
                robot.drivetrain.rightBack,
                gamepad1,
                () -> follower.pose().heading()
        ).schedule();

        // 2. Slow mode no Left Bumper
        driver.leftBumper().whileTrue(
                Commands.infinite(() -> DriveCommands.setScalar(0.4))
                        .setEnd(interrupted -> DriveCommands.setScalar(1.0))
        );

        // 3. Anti-Gargalo: Segurou DPAD DOWN, cospe a peça!
        driver.dpadDown().whileTrue(
                robot.collector.expellCollect(-0.8, -0.6)
        );

        // 4. Inicia a Máquina de Estados no início do PLAY
        agendarEstadoAtual();
    }

    private void agendarEstadoAtual() {
        robot.handleState(
                () -> gamepad1.x, // R2 avança de estado
                () -> gamepad1.dpad_down            // Dpad Down cancela/encerra o tiro
        ).schedule();
        lastState = robot.currentState;
    }

    @Override
    public void periodic() {
        // Atualiza o Pedro Pathing em
        follower.update();

        // Se o estado mudou (ex: de COLLECT para PREPARE ou SHOOT), agenda o novo estado:
        if (robot.currentState != lastState) {
            agendarEstadoAtual();
        }

        // Telemetrias atualizando 100% em tempo real:
        telemetry.addData("Estado do Robô", robot.getCurrentState());
        telemetry.addData("Status", "NextFTC Ativo!");
        telemetry.addData("Lançador Estado", robot.launcher.getState());
        telemetry.addData("Lançador RPM Alvo", "%.0f RPM", robot.launcher.getTargetRPM());
        telemetry.addData("Lançador RPM Real", "%.0f RPM", robot.launcher.getCurrentRPM());
        telemetry.addData("Lançador Pronto?", robot.launcher.isReady() ? "SIM (LIVRE)" : "AGUARDE");

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("M1 Corrente (A)", robot.collector.collectMotor1.getCurrent().getMagnitude());
        packet.put("M2 Corrente (A)", robot.collector.collectMotor2.getCurrent().getMagnitude());
        packet.put("Bolinha 1 Presa", robot.collector.isMotor1Stalled() ? 3.0 : 0.0);
        packet.put("Robo Cheio (2 Bolinhas)", robot.collector.isMotor2Stalled() ? 5.0 : 0.0);
        packet.put("Bateria (V)", hardwareMap.voltageSensor.iterator().next().getVoltage());
        //packet.put("Corrente M1 (A)", robot.collector.collectMotor1.getCurrent().getMagnitude());
        //packet.put("Corrente M2 (A)", robot.collector.collectMotor2.getCurrent().getMagnitude());
        //packet.put("Limite Corte (A)", 4.2);
        //packet.put("Bateria (V)", hardwareMap.voltageSensor.iterator().next().getVoltage());
        //packet.put("Stall Ativo", robot.collector.isMotor1Stalled() ? 5.0 : 0.0);
       // dashboard.sendTelemetryPacket(packet);
    }

    @Override
    public void end() {
    }
}