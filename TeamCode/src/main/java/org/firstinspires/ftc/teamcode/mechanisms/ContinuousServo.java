package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import java.util.LinkedHashMap;
import java.util.function.BooleanSupplier;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.robot.Mechanism;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.branch;

public class ContinuousServo implements Mechanism {
    /*public final NextCRServo limelightServo = new NextCRServo(RobotController.controlHub(), 0);
    protected boolean lIsRaised = false;

    public boolean isLimelightRaised(){
        return lIsRaised;
    }

    public Command raiseArm() {
            return sequential(
                    instant(() -> limelightServo.setPower(0.1)),
                    waitMs(3000),
                    instant(() -> limelightServo.setPower(0.0)),
                    instant(() -> lIsRaised = true)
            );}
        public Command lowerArm(){
        lIsRaised = false;
                  return sequential(
                    instant(() -> limelightServo.setPower(-0.1)),
                    waitMs(3000),
                    instant(() -> limelightServo.setPower(0.0)),
                    instant(() -> lIsRaised = false)

            );}
    public Command toggleLime() {
        LinkedHashMap<BooleanSupplier, Command> lPosition = new LinkedHashMap<>();
        lPosition.put(() -> lIsRaised, raiseArm());
        lPosition.put(() -> true, lowerArm());

        return branch(lPosition);
    }*/
    }

