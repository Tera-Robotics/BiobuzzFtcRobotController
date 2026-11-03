package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.match;
import static com.pedropathing.ivy.groups.Groups.sequential;

import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;

import org.firstinspires.ftc.teamcode.OpModes.teleop.RobotState;
import org.firstinspires.ftc.teamcode.mechanisms.Collector;
import org.firstinspires.ftc.teamcode.mechanisms.ContinuousServo;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherServo;

import java.util.EnumMap;
import java.util.Set;
import java.util.function.BooleanSupplier;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;

public class Robot implements NextRobot {

    public final Drivetrain drivetrain = new Drivetrain();

    public final Collector collector = new Collector();
    public final LauncherServo launcherServo = new LauncherServo();
    public final Launcher launcher = new Launcher();
    public final ContinuousServo continuousServo = new ContinuousServo();

    public RobotState currentState = RobotState.COLLECT;

    private final EnumMap<RobotState, Command> cases =
            new EnumMap<>(RobotState.class);

    public Command handleState(BooleanSupplier dpadDown) {

        EnumMap<RobotState, Command> cases =
                new EnumMap<>(RobotState.class);

        cases.put(RobotState.INITIAL, collectState());
        cases.put(RobotState.COLLECT, collectState());
        cases.put(RobotState.PREPARE, prepareState());
        cases.put(RobotState.SHOOT, shootState(dpadDown));

        return match(() -> currentState, cases);
    }
    public RobotState getCurrentState() {
        return currentState;
    }
    public Command shoot() {
        return sequential(
                        launcherServo.openShooter(),
                        collector.collect(0.8),
                        launcher.setRpmCommand(3000)

                        );
    }

    public Command shootState(BooleanSupplier DpadDown) {
        return sequential(
                shoot(),
                race(
                waitMs(5000),
                        waitUntil(DpadDown)
        ),
        instant(() -> currentState = RobotState.COLLECT)
        );
    }

    public Command prepareState() {
        return sequential(
                collector.stopCollector(),
                launcher.setRpmCommand(3000),
                waitMs(5000),
                instant(() -> currentState = RobotState.SHOOT)
        );
    }

    public Command collectState() {
        return sequential(
                launcherServo.closeShooter(),
                launcher.idleCommand(),
                collector.collect(0.6),
                instant(() -> currentState = RobotState.PREPARE)
        );
    }

    // seleciona o comando dependendo do estado atual
    public Command handleState() {
        return match(
                () -> currentState,
                cases
        );
    }

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(
                drivetrain,
                collector,
                launcherServo,
                launcher,
                continuousServo
        );
    }

    @Override
    public void periodic() {
        // Executado a cada ciclo do robô
    }
}