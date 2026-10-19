package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name = "Detectar Pollen Limelight", group = "Visao")
public class LimeLightDetectorDePollen extends LinearOpMode {
    private Limelight3A limelight;
    // Configuração do Pipeline (coloque o número do pipeline que você calibrou para o pollen)
    private static final int PIPELINE_POLLEN = 0;
    @Override
    public void runOpMode() {
        // Busca a Limelight pelo tipo, sem depender de nome específico no Control Hub
        List<Limelight3A> lista = hardwareMap.getAll(Limelight3A.class);
        if (lista.isEmpty()) {
            telemetry.addLine("ERRO: Nenhuma Limelight encontrada conectada!");
            telemetry.update();
            waitForStart();
            return;
        }
        // Pega a primeira Limelight disponível
        limelight = lista.get(0);
        // Troca para o pipeline de detecção do Pollen
        limelight.pipelineSwitch(PIPELINE_POLLEN);
        limelight.start();
        telemetry.addLine("Limelight conectada com sucesso!");
        telemetry.addLine("Aguardando START...");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            // result.isValid() é o equivalente ao tv == 1 (está vendo o pollen)
            if (result != null && result.isValid()) {
                double tx = result.getTx(); // Ângulo horizontal para o pollen
                double ty = result.getTy(); // Ângulo vertical
                double ta = result.getTa(); // Área (% da tela)
                int quantidade = result.getColorResults().size();
                telemetry.addLine("=== POLLEN DETECTADO ===");
                telemetry.addData("Vê Pollen?", "SIM");
                telemetry.addData("TX (Girar)", "%.2f graus", tx);
                telemetry.addData("TY", "%.2f graus", ty);
                telemetry.addData("TA (Área)", "%.2f %%", ta);
                telemetry.addData("Quantidade de pollen:", "%d", quantidade);
                // Dica: se tx < 0 o pollen está à esquerda; se tx > 0 está à direita

            } else {
                telemetry.addLine("=== PROCURANDO POLLEN ===");
                telemetry.addData("Vê Pollen?", "NÃO");
            }
            telemetry.update();
        }
        limelight.stop();
    }
}