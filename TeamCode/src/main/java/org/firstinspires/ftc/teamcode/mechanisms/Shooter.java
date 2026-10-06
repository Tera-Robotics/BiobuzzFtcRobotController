package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Shooter implements Mechanism {
    // Certifique-se de que os nomes entre aspas correspondam à sua configuração no Robot Controller
    public final NextMotor shooterLeftMotor = new NextMotor("shooterLeft");
    public final NextMotor shooterRightMotor = new NextMotor("shooterRight");

    public Shooter() {
        init();
    }

    public void init() {
        // Geralmente as duas rodas do shooter giram em sentidos opostos para impulsionar a peça
        shooterLeftMotor.setDirection(NextMotor.Direction.FORWARD);
        shooterRightMotor.setDirection(NextMotor.Direction.REVERSE);
    }

    /**
     * Comando que liga o shooter na potência indicada enquanto estiver ativo,
     * e desliga os motores quando o comando for finalizado/interrompido.
     */
    public Command shoot(double power) {
        return infinite(() -> {
            shooterLeftMotor.setThrottle(power);
            shooterRightMotor.setThrottle(power);
        }).setEnd(endCondition -> {
            shooterLeftMotor.setThrottle(0.0);
            shooterRightMotor.setThrottle(0.0);
        });
    }

    /**
     * Comando padrão: quando nenhum botão estiver acionando o shooter, ele permanece desligado.
     */
    @Override
    public Command getDefaultCommand() {
        return infinite(() -> {
            shooterLeftMotor.setThrottle(0.0);
            shooterRightMotor.setThrottle(0.0);
        });
    }
}