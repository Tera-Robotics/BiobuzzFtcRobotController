package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name = "Detectar e Contar Pollen", group = "Visao")
public class LimeLightDetectorDePollen extends LinearOpMode {

    private Limelight3A limelight;
    private static final int PIPELINE_POLLEN = 1;

    @Override
    public void runOpMode() {
        List<Limelight3A> lista = hardwareMap.getAll(Limelight3A.class);

        if (lista.isEmpty()) {
            telemetry.addLine("ERRO: Nenhuma Limelight encontrada!");
            telemetry.update();
            waitForStart();
            return;
        }

        limelight = lista.get(0);

        // muda para o pipeline do Pollen e inicia
        limelight.pipelineSwitch(PIPELINE_POLLEN);
        limelight.start();

        telemetry.addLine("Limelight conectada com sucesso!");
        telemetry.addLine("Aguardando START no Driver Hub...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                // Pega alvos por detecção de cor
                List<LLResultTypes.ColorResult> pollensCores = result.getColorResults();

                // Define a quantidade total de pollens encontrados
                int qtdPollens = 0;
                if (!pollensCores.isEmpty()) {
                    qtdPollens = pollensCores.size();
                } else {
                    // Fallback: se isValid() é true mas a lista veio vazia, há pelo menos 1 alvo principal
                    qtdPollens = 1;
                }

                telemetry.addLine("=== POLLEN DETECTADO ===");
                telemetry.addData("Quantidade de Pollens", qtdPollens);

                // Dados do alvo mais central/principal
                telemetry.addData("TX (Horizontal)", "%.2f°", result.getTx());
                telemetry.addData("TY (Vertical)", "%.2f°", result.getTy());
                telemetry.addData("TA (Área)", "%.2f%%", result.getTa());

                // Detalhes de cada pollen individual encontrado (por cor)
                if (!pollensCores.isEmpty()) {
                    for (int i = 0; i < pollensCores.size(); i++) {
                        LLResultTypes.ColorResult p = pollensCores.get(i);
                        telemetry.addData("Pollen #" + (i + 1), "X: %.1f°, Y: %.1f°",
                                p.getTargetXDegrees(), p.getTargetYDegrees());
                    }
                }

            } else {
                telemetry.addLine("=== PROCURANDO POLLEN ===");
                telemetry.addData("Quantidade de Pollens", 0);
            }

            telemetry.update();
        }

        limelight.stop();
    }
}