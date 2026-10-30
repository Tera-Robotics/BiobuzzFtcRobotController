package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import java.util.List;

@TeleOp(name = "Exemplo Limelight 3A")
public class LimeLightDetectorDePollen extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() {
        // 1. Inicializa a Limelight configurada no Robot Controller
        List<Limelight3A> lista = hardwareMap.getAll(Limelight3A.class);
        limelight = lista.get(0);

        // 2. Seleciona o pipeline da rede neural (número do pipeline que você criou)
        limelight.pipelineSwitch(2);

        // 3. Liga o processamento de visão da Limelight
        limelight.start();

        telemetry.addData("Status", "Limelight pronta!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // 4. Pega o resultado mais recente da câmera
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                // Pega a lista de todas as bolinhas que ela detectou
                List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();

                for (LLResultTypes.DetectorResult dr : detectorResults) {
                    String nome = dr.getClassName();        // Nome da classe
                    double tx = dr.getTargetXDegrees();     // Ângulo horizontal
                    double ty = dr.getTargetYDegrees();     // Ângulo vertical
                    double confianca = dr.getConfidence();  // Certeza da IA (0 a 1)

                    telemetry.addData("Objeto", nome);
                    telemetry.addData("TX (Horizontal)", tx);
                    telemetry.addData("TY (Vertical)", ty);
                    telemetry.addData("Confiança", confianca * 100 + "%");

                    // 5. Calcula a distância aproximada até a bolinha (em cm)
                    double distancia = calcularDistancia(ty);
                    telemetry.addData("Distância (cm)", distancia);
                }
            } else {
                telemetry.addData("Status", "Procurando bolinha...");
            }

            telemetry.update();
        }

        // Desliga a câmera ao encerrar
        limelight.stop();
    }

    // Método simples para calcular distância por trigonometria
    public double calcularDistancia(double ty) {
        double alturaCamera = 29; // Altura da lente em cm do chão (meça com fita métrica)
        double alturaAlvo = 3.665;    // Altura do centro da bolinha no chão (cm)
        double anguloCamera = 19.1; // Ângulo de inclinação da câmera para baixo/cima (graus)

        // Fórmula trigonométrica oficial da Limelight
        double anguloTotalRad = Math.toRadians(anguloCamera - ty);
        return (alturaAlvo - alturaCamera) / Math.tan(anguloTotalRad);
    }
}