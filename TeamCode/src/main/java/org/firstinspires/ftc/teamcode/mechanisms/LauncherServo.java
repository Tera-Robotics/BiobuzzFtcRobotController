package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextFeedbackServo;
import dev.nextftc.hardware.sensors.NextAnalogInput;
import dev.nextftc.robot.Mechanism;

public class LauncherServo implements Mechanism {
    NextFeedbackServo shootServo = new NextFeedbackServo("ShooterServo", new NextAnalogInput("ServoEncoder"));
    public Command openShooter() {
        return instant(() -> shootServo.setPosition(0.0));
    }
    public Command closeShooter() {
        return instant(() -> shootServo.setPosition(0.25));
    }
}
