package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Collector implements Mechanism {
    public final NextMotor collectMotor1 = new NextMotor("collectMotor1");
    public final NextMotor collectMotor2 = new NextMotor("collectMotor2");

public Collector() {
    collectMotor1.follow(collectMotor2, NextMotor.Direction.FORWARD);
}
public Command collect(double Throttle){
    return instant(() -> collectMotor2.setThrottle(Throttle));
}

    public Command stopCollector() {
        return instant(() -> collectMotor2.setThrottle(0.0));
    }
}