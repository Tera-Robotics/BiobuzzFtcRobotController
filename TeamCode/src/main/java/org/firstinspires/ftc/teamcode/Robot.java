package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.branch;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import dev.nextftc.hardware.actuators.NextFeedbackServo;
import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;

import org.firstinspires.ftc.teamcode.mechanisms.Collector;
import org.firstinspires.ftc.teamcode.mechanisms.ContinuousServo;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherServo;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class Robot implements NextRobot {
    public final Drivetrain drivetrain = new Drivetrain();

    public final Collector collector = new Collector();
    public final LauncherServo launcherServo = new LauncherServo();
    public final Launcher launcher = new Launcher();
    public final ContinuousServo continuousServo = new ContinuousServo();

    boolean isShooting = false;
    public Command shoot() {
        return sequential(
                launcherServo.openShooter(),
                collector.stopCollector(),
                launcher.setRpmCommand(2650)
                );
    }
    public Command collect() {
        return sequential(
                launcherServo.closeShooter(),
                launcher.idleCommand(),
                collector.collect(0.6)
        );}

    public Command shootOrCollect() {
        LinkedHashMap<BooleanSupplier, Command> shootOrNotShoot = new LinkedHashMap<>();
        shootOrNotShoot.put(() -> isShooting, collect());
        shootOrNotShoot.put(() -> isShooting, shoot());
        return branch(shootOrNotShoot);
    }

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(launcher, collector);
    }

    @Override
    public void periodic() {
        // Opcional: roda a cada ciclo antes de atualizar os mecanismos
    }
}
