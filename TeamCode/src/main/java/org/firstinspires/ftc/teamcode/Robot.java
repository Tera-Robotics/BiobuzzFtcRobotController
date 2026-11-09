package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.match;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.OpModes.teleop.RobotState;
import org.firstinspires.ftc.teamcode.mechanisms.Collector;
import org.firstinspires.ftc.teamcode.mechanisms.ContinuousServo;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherServo;

import java.util.EnumMap;
import java.util.Set;
import java.util.function.BooleanSupplier;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class Robot implements NextRobot {

    public final Drivetrain drivetrain = new Drivetrain();

    public final Collector collector = new Collector();
    public final LauncherServo launcherServo = new LauncherServo();
    public final Launcher launcher = new Launcher();
    public final ContinuousServo continuousServo = new ContinuousServo();

    public RobotState currentState = RobotState.COLLECT;

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(launcherServo, launcher, collector,continuousServo, drivetrain);
    }

    private final EnumMap<RobotState, Command> cases =
            new EnumMap<>(RobotState.class);


    public Command handleState(BooleanSupplier buttonX, BooleanSupplier dpadDown) {

        cases.put(RobotState.COLLECT, collectState(buttonX));
        cases.put(RobotState.PREPARE, prepareState(buttonX));
        cases.put(RobotState.SHOOT, shootState(dpadDown));

        return match(() -> currentState, cases);
    }

    public RobotState getCurrentState() {
        return currentState;
    }

    // 1. ESTADO DE COLETA:
    public Command collectState(BooleanSupplier buttonX) {
        return sequential(
                launcherServo.closeShooter(),               // Fecha o servo
                launcher.idleCommand(),                     // Deixa o lançador em espera/desligado
                collector.smartCollect(0.8, 0.6).until(buttonX),
                instant(() -> currentState = RobotState.PREPARE)
        );
    }

    // 2. ESTADO DE PREPARO:
    public Command prepareState(BooleanSupplier buttonX) {
        return sequential(
                launcher.setRpmCommand(2200),// Sobe o giro do lançador
                waitUntil(() -> !buttonX.getAsBoolean()),
                collector.holdStopped().until(buttonX),

                instant(() -> currentState = RobotState.SHOOT)
        );
    }

    // 3. ESTADO DE DISPARO:
    public Command shootState(BooleanSupplier cancelTrigger) {
        return sequential(
                launcherServo.openShooter(),                // Abre o servo
                race(
                        waitMs(3000),                       // Atira por 3 segundos
                        waitUntil(cancelTrigger),
                        collector.feedShooter()
                ),
                launcherServo.closeShooter(),               // Fecha o servo
                collector.stopCollector(),                    // Desliga o coletor
                instant(() -> currentState = RobotState.COLLECT) // Volta pro início
        );
    }


    }




