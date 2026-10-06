package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Collector implements Mechanism {
    // Ajuste o nome do motor conforme configurado no seu Hub (ex: "intakeMotor" ou "collector")
    public final NextMotor intakeMotor = new NextMotor("intakeMotor");

    public Collector() {
        init();
    }

    public void init() {
        intakeMotor.setDirection(NextMotor.Direction.FORWARD);
    }

    /**
     * Liga o coletor na potência desejada e desliga ao soltar.
     */
    public Command collect(double power) {
        return infinite(() -> intakeMotor.setThrottle(power))
                .setEnd(endCondition -> intakeMotor.setThrottle(0.0));
    }

    /**
     * Reverte o coletor (útil para cuspir uma peça que entalou).
     */
    public Command reverse(double power) {
        return infinite(() -> intakeMotor.setThrottle(-power))
                .setEnd(endCondition -> intakeMotor.setThrottle(0.0));
    }

    /**
     * Comando padrão: mantém o coletor desligado quando inativo.
     */
    @Override
    public Command getDefaultCommand() {
        return infinite(() -> intakeMotor.setThrottle(0.0));
    }
}