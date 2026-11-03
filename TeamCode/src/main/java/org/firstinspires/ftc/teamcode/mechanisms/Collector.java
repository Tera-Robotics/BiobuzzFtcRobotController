package org.firstinspires.ftc.teamcode.mechanisms;

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

    public Command collect(double Throttle) {
        return infinite(() -> {
            collectMotor1.setThrottle(Throttle);
            collectMotor2.setThrottle(Throttle);
        });
    }

    public Command stopCollector() {
        return infinite(() -> {
            collectMotor1.setThrottle(0.0);
            collectMotor2.setThrottle(0.0);
        });
    }
}

