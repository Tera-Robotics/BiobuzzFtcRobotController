package org.firstinspires.ftc.teamcode;

import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Mechanism;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;

import java.util.Set;

public class Robot implements NextRobot {
    public final Drivetrain drivetrain = new Drivetrain();

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain);
    }

    @Override
    public void periodic() {
        // Opcional: roda a cada ciclo antes de atualizar os mecanismos
    }
}