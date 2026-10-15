package org.firstinspires.ftc.teamcode;

import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;

import org.firstinspires.ftc.teamcode.mechanisms.Collector;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

import java.util.Set;

public class Robot implements NextRobot {
    public final Drivetrain drivetrain = new Drivetrain();

    //public final Collector collector = new Collector();

    public final Launcher launcher = new Launcher();

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain,launcher);
    }

    @Override
    public void periodic() {
        // Opcional: roda a cada ciclo antes de atualizar os mecanismos
    }
}