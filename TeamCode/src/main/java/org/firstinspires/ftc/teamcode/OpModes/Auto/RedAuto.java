
package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.api.Paths;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class RedAuto extends OpMode {
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(56, 8
            , 90);
    private final Pose flower = p.of(20.095289079229122, 48.90792291220555, 180);

    private final Pose point2Control1 = p.of(54.39293361884366, 94.24197002141327, 0);
    private final Pose point2Control2 = p.of(1.3715203426124227, 105.68950749464668, 0);

    private final Pose secondTip = p.of(40.33511777301926, 118.035, -65);

    private final Pose redPark = p.of(9.884368308351185, 106.93468950749465, 3);

    private Path park() {
        return line(startPose, flower).linear(startPose, flower);
    }

    public Path path2() {
        return line(flower,secondTip).linear(flower,secondTip);
    }

    public Path pathFinal() {
        return line (secondTip, redPark).linear(secondTip, redPark);
    }

@Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
    }



    @Override
    public void start() {
        schedule(
                sequential(
                        follow(follower, park()),
                        waitMs(3000), // ⏱️ Espera 3000ms
                        follow(follower, path2()),
                        waitMs(3000),
                        follow(follower, pathFinal())

                )
        );
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}

