package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name = "AprilTagDetector", group = "Teste")
public class LimeLightAprilTag extends LinearOpMode {

    private Limelight3A limelight;

    // h1: Altura da lente da câmera até o chão (em cm)
    private static final double CAMERA_HEIGHT_METERS = 0.322;
    // h2: Altura do centro da AprilTag até o chão (em cm)
    private static final double TARGET_HEIGHT_METERS = 1.268;

    // a1: Ângulo de inclinação da câmera para cima (em graus).
    // Se estiver 100% reta na horizontal, coloque 0.0.
    private static final double CAMERA_ANGLE_DEGREES = 35.22;

    @Override
    public void runOpMode() {
        // PEGA A LIMELIGHT DIRETO PELO TIPO (SEM PRECISAR DE NOME)
        List<Limelight3A> lista = hardwareMap.getAll(Limelight3A.class);

        if (lista.isEmpty()) {
            telemetry.addLine("ERRO: Nenhuma Limelight encontrada na lista de dispositivos!");
            telemetry.update();
            waitForStart();
            return;
        }


        // Pega a primeira Limelight conectada
        limelight = lista.get(0);

        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.addLine("Limelight inicializada com sucesso!");
        telemetry.addLine("Aperte START no Driver Hub.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double ty = result.getTy();
                double distance = calculateDistance(ty);
                telemetry.addLine("=== ALVO DETECTADO ===");
                telemetry.addData("Distância Calculada", "%.2f metros", distance);
                telemetry.addData("TY (Vertical)", "%.2f°", ty);
                telemetry.addData("TX (Horizontal)", "%.2f°", result.getTx());
                telemetry.addData("TA (Área)", "%.2f%%", result.getTa());
            } else {
                telemetry.addLine("Procurando AprilTag...");
            }
            telemetry.update();
        }
        limelight.stop();
    }
    public double calculateDistance(double tyDegrees) {
        // Ângulo total (a1 + a2)
        double totalAngleDegrees = CAMERA_ANGLE_DEGREES + tyDegrees;
        // O Math.tan do Java exige o ângulo em radianos!
        double totalAngleRadians = Math.toRadians(totalAngleDegrees);
        double heightDifference = TARGET_HEIGHT_METERS - CAMERA_HEIGHT_METERS;
        // Evita divisão por zero ou tangentes inválidas
        double tangent = Math.tan(totalAngleRadians);
        return heightDifference / tangent;
    }
}
