package org.firstinspires.ftc.teamcode.mechanisms;

import androidx.annotation.NonNull;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Collector implements Mechanism {
    public final NextMotor rightMotor = new NextMotor("rightIntakeMotor");
    public final NextMotor leftMotor = new NextMotor("leftIntakeMotor");

public Collector() {
    leftMotor.follow(rightMotor, NextMotor.Direction.REVERSE);
}
public Command collect(double Throttle){
    return instant(() -> rightMotor.setThrottle(Throttle));
}

    public Command stopCollector() {
        return instant(() -> rightMotor.setThrottle(0.0));
    }
}