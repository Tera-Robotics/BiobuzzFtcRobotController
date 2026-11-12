package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.robot.Mechanism;

public class LauncherServo implements Mechanism {

    public final NextCRServo shootServo = new NextCRServo("ShootServo");

    // Tempo de movimento em milissegundos (ajuste conforme a mecânica)
    private final long TEMPO_MOVIMENTO_MS = 350;

    public Command openShooter() {
        return sequential(
                instant(() -> shootServo.setPower(1.0)),
                waitMs(TEMPO_MOVIMENTO_MS),
                instant(() -> shootServo.setPower(0.0))
        );
    }

    public Command closeShooter() {
        return sequential(
                instant(() -> shootServo.setPower(-1.0)),
                waitMs(200),
                instant(() -> shootServo.setPower(0.0))
        );
    }

    public Command stopShooter() {
        return instant(() -> shootServo.setPower(0.0));
    }
}