package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import java.util.List;

@TeleOp(name = "Limelight AprilTag Distancia", group = "Sensor")

public class TesteLimeLightAprilTag extends LinearOpMode {
    private Limelight3A limelight;

    @Override
    public void runOpMode() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        telemetry.addData("Status", "LimeLight inicializada. Aguardando Start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                List<LLResultTypes.FiducialResult> Fiducials = result.getFiducialResults();
                if (!Fiducials.isEmpty()) {

                    LLResultTypes.FiducialResult tag = Fiducials.get(0);

                    int tagId = tag.getFiducialId();

                    Pose3D targetPose = tag.getTargetPoseCameraSpace();
                    Position pos = targetPose.getPosition();

                    double x = pos.x;
                    double y = pos.y;
                    double z = pos.z;

                    double distancia3D = Math.sqrt(x * x + y * y + z * z);

                    double distanciaHorizontal = Math.hypot(x, z);

                    telemetry.addData("Tag ID", tagId);
                    telemetry.addData("X (Lateral)", "%.2f m", x);
                    telemetry.addData("Z (Profundidade)", "%.2f m", z);
                    telemetry.addData("Distância Direta (3D)", "%.2f m", distancia3D);
                    telemetry.addData("Distância Horizontal", "%.2f m", distanciaHorizontal);
                } else {
                    telemetry.addLine("Nenhuma Apriltag detectada no momento.");
                }
            } else {
                telemetry.addLine("Aguardando alvos válidos da Limelight...");
            }

            telemetry.update();
        }
        limelight.stop();
    }
}
