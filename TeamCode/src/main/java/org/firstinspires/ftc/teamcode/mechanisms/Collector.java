package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.pedropathing.ivy.Command;

import java.util.function.BooleanSupplier;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Collector implements Mechanism {
    public final NextMotor collectMotor1 = new NextMotor("collectMotor1");
    public final NextMotor collectMotor2 = new NextMotor("collectMotor2");

    public Collector() {
        collectMotor1.setDirection(NextMotor.Direction.REVERSE);
        collectMotor2.setDirection(NextMotor.Direction.REVERSE);
    }

    public boolean isMotor1Stalled() {
        return collectMotor1.getCurrent().getMagnitude() > 4.2;
    }

    public boolean isMotor2Stalled() {
        return collectMotor2.getCurrent().getMagnitude() > 4.0; // M2 normal é 1A; acima de 4A tem bolinha!
    }

    public boolean isFull() {
        return isMotor1Stalled() && isMotor2Stalled();
    }

    @NonNull
    @Override
    public Command getDefaultCommand(){
        return infinite(() -> {
            collectMotor1.setThrottle(0.8);
            collectMotor2.setThrottle(0.6);
        });
    }

    public Command smartCollect(double t1, double t2) {
        // Ciclo do Motor 1 (Estágio Interno / 1ª Bolinha):
        Command cicloMotor1 = sequential(
                race(infinite(() -> collectMotor1.setThrottle(t1)), waitMs(1000)),
                infinite(() -> collectMotor1.setThrottle(t1)).until(this::isMotor1Stalled),
                infinite(() -> collectMotor1.setThrottle(0.0)) // Desliga M1 e aguarda
        );
        // Ciclo do Motor 2 (Estágio Externo / 2ª Bolinha):
        Command cicloMotor2 = sequential(
                race(infinite(() -> collectMotor2.setThrottle(t2)), waitMs(1000)),
                infinite(() -> collectMotor2.setThrottle(t2)).until(this::isMotor2Stalled),
                infinite(() -> collectMotor2.setThrottle(0.0)) // Desliga M2 quando encher!
        );
        // Executa os dois ciclos em paralelo de forma totalmente independente:
        return parallel(cicloMotor1, cicloMotor2);
    }

    public Command expellCollect(double Throttle, double Throttle2) {
        return infinite(() -> {
            collectMotor1.setThrottle(Throttle);
            collectMotor2.setThrottle(Throttle2);
        });
    }

    public Command feedShooter() {
        return expellCollect(0.8, 0.6);
    }

    public Command stopCollector() {
        return instant(() -> {
            collectMotor1.setThrottle(0.0);
            collectMotor2.setThrottle(0.0);
        });

    }

    public Command holdStopped() {
        return infinite(() -> {
            collectMotor1.setThrottle(0.0);
            collectMotor2.setThrottle(0.0);
        });
    }


    }


