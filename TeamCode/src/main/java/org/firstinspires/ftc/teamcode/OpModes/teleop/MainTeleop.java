package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.Robot;

import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@NextTeleop(name = "TeleOp Principal", group = "TeleOp")
public class MainTeleop extends NextOpMode {

    private final Robot robot;
    private final CommandGamepad driver;

    // O NextFTC injeta a instância do Robot criada no passo 2 aqui automaticamente
    public MainTeleop(Robot robot) {
        // Passamos BulkReadHook.INSTANCE para otimizar as leituras do Hub via barramento
        super(robot, BulkReadHook.INSTANCE);
        this.robot = robot;
        this.driver = new CommandGamepad(gamepad1);
    }

    @Override
    public void start() {
        // 1. Inicia o comando de pilotagem do Drivetrain
        robot.drivetrain.drive(gamepad1).schedule();

        // 2. BÔNUS: Modo Lento (Slow Mode) ao segurar o Left Bumper
        // Quando aperta o Bumper, reduz a velocidade para 40%; ao soltar, volta para 100%
        driver.leftBumper().whileTrue(
                Commands.infinite(() -> DriveCommands.setScalar(0.4))
                        .setEnd(interrupted -> DriveCommands.setScalar(1.0))
        );
    }

    @Override
    public void periodic() {
        // Roda a cada ciclo enquanto estiver em PLAY.
        // O NextFTC já dá flush automático na telemetria, não precisa chamar telemetry.update()!
        telemetry.addData("Status", "NextFTC Ativo!");
        telemetry.addData("Velocidade Atual", DriveCommands.getScalar());
    }
}