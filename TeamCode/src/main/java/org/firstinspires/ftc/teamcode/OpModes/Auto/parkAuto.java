
package org.firstinspires.ftc.teamcode.OpModes.Auto;

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
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class parkAuto extends OpMode {
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(9.641327623126342, 84.05246252676659
            , 0);
    private final Pose park = p.of(35.69914346895076, 110.62037156169873, 0);

    private final Pose controlPose = p.of(36, 60, 45);

    private Path park() {
        return line(startPose, park).linear(startPose, park);
    }
    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
    }



    @Override
    public void start() {
        schedule(follow(follower,park()));
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }
}

