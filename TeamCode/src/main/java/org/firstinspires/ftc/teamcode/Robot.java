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

    public Command handleState(BooleanSupplier dpadDown) {

        EnumMap<RobotState, Command> cases =
                new EnumMap<>(RobotState.class);
        cases.put(RobotState.INITIAL, collectAntiGargalo(dpadDown));
        cases.put(RobotState.COLLECT, collectAntiGargalo(dpadDown));
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

    private double collectThrottle = 0.8;
    private long collectStartTime = 0;
    private boolean lastDpadDown = false;
    public Command collectAntiGargalo(BooleanSupplier dpadDown) {
        return Command.build()
                .setStart(() -> {
                    collectThrottle = 0.8;
                    collectStartTime = System.currentTimeMillis();
                    lastDpadDown = false;
                })
                .setExecute(() -> {
                    boolean currentDpad = dpadDown.getAsBoolean();

                    if (currentDpad && !lastDpadDown) {
                        collectThrottle = -collectThrottle;
                        collectStartTime = System.currentTimeMillis();
                    }
                    lastDpadDown = currentDpad;
                    collector.collectMotor1.setThrottle(collectThrottle);
                    collector.collectMotor2.setThrottle(collectThrottle);
                })
                .setDone(() -> System.currentTimeMillis() - collectStartTime >= 5000 && collectThrottle > 0)
                .setEnd(endCondition -> {
                    collector.collectMotor1.setThrottle(collectThrottle);
                    collector.collectMotor2.setThrottle(collectThrottle);
                });
    }
    // seleciona o comando dependendo do estado atual
    public Command handleState() {
        return match(
                () -> currentState,
                cases
        );
    }
}
