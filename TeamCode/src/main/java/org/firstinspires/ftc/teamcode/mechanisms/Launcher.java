package org.firstinspires.ftc.teamcode.mechanisms;

// Importa os tipos de unidades angulares do NextFTC
import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.DegreesPerSecond;

import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.measuretypes.AngularVelocity;
import com.pedropathing.ivy.Command;

/**
 * Mecanismo de Lançador de Disco/Elemento (Flywheel Duplo 1:1 com Caixa de 90°).
 * Controlado inteiramente em RPM (Rotações Por Minuto).
 */
public class Launcher implements Mechanism {

    // =========================================================================
    // 1. CONSTANTES FÍSICAS E CONVERSÕES
    // =========================================================================

    // O motor REV HD Hex sem redução tem 28 ticks por volta completa (PPR)
    private static final double TICKS_PER_REV = 28.0;

    // Converte a leitura bruta do encoder para graus (360° / 28 ticks = ~12.857° por tick)
    private static final double TICKS_TO_DEGREES = 360.0 / TICKS_PER_REV;

    // Fator de conversão entre RPM e Graus por Segundo:
    // 1 volta = 360 graus | 1 minuto = 60 segundos -> 360 / 60 = 6.0
    // Portanto: GrausPorSegundo = RPM * 6.0
    private static final double RPM_TO_DEG_PER_SEC = 360.0 / 60.0; // Valor fixo: 6.0

    // =========================================================================
    // 2. CONFIGURAÇÕES DE VELOCIDADE (EM RPM)
    // =========================================================================

    // Rotação de espera (mantém o motor girando para acelerar mais rápido na hora do tiro)
    public static final double IDLE_RPM = 2000.0;

    // Preset de lançamento longo/alto (velocidade alta)
    public static final double HIGH_GOAL_RPM = 5200.0;

    // Preset de lançamento curto/baixo (velocidade média)
    public static final double LOW_GOAL_RPM = 4000.0;

    // Tolerância aceitável de erro para considerar o lançador "pronto" (ex: ±100 RPM)
    public static final double RPM_TOLERANCE = 100.0;

    // =========================================================================
    // 3. HARDWARE (MOTORES)
    // =========================================================================

    // Motor principal (Mestre) configurado para interpretar a escala do encoder em graus
    public final NextMotor rightLauncher = new NextMotor("rightLauncher", Degrees.of(TICKS_TO_DEGREES));

    // Motor secundário (Seguidor) que espelha os comandos do motor mestre
    public final NextMotor leftLauncher = new NextMotor("leftLauncher", Degrees.of(TICKS_TO_DEGREES));

    // =========================================================================
    // 4. MÁQUINA DE ESTADOS
    // =========================================================================

    public enum LauncherState {
        OFF,      // Motores desligados (0 RPM)
        IDLE,     // Girando em velocidade baixa de pré-aquecimento
        SPINUP,   // Acelerando em direção à velocidade alvo
        READY     // Estável na velocidade alvo (liberado para disparar!)
    }

    // Variáveis internas para monitorar o status atual
    private LauncherState launcherState = LauncherState.OFF;
    private AngularVelocity veloGoal = DegreesPerSecond.of(0); // Alvo no formato nativo do NextFTC
    private double targetRPM = 0.0;                           // Alvo armazenado em RPM
    private double currentRPM = 0.0;                          // Rotação real medida agora em RPM

    // =========================================================================
    // 5. CONSTRUTOR
    // =========================================================================

    public Launcher() {
        // Constantes do controlador de velocidade (Feedforward + PID)
        // kS: Força mínima inicial para vencer o atrito da caixa de 90° e começar a girar
        // kV: Tensão proporcional necessária para manter a alta rotação estável
        // kP: Correção ativa rápida se a roda perder velocidade ao tocar a peça
        rightLauncher.getVelocityConstants().setKS(0.0057);
        rightLauncher.getVelocityConstants().setKV(0.00045 / TICKS_TO_DEGREES);
        rightLauncher.getVelocityConstants().setKP(0.00036 / TICKS_TO_DEGREES);

        // Faz o motor esquerdo seguir o direito no sentido INVERSO para prensar e cuspir a peça
        // (Se as rodas girarem para fora em vez de para dentro, mude REVERSE para FORWARD)
        rightLauncher.setDirection(NextMotor.Direction.REVERSE);
        leftLauncher.follow(rightLauncher, NextMotor.Direction.FORWARD);

        // Inicia com os motores desligados
        stop();

    }

    // =========================================================================
    // 6. MÉTODOS PÚBLICOS DE COMANDO
    // =========================================================================

    /**
     * Define uma velocidade alvo customizada em RPM.
     * @param rpm Rotações por minuto desejadas (ex: 5000)
     */
    public void setGoalRPM(double rpm) {
        this.targetRPM = rpm;

        // Converte RPM para Graus por Segundo (unidade nativa do NextMotor)
        double degPerSec = rpm * RPM_TO_DEG_PER_SEC;
        this.veloGoal = DegreesPerSecond.of(degPerSec);

        // Envia o novo comando de velocidade para o motor mestre
        rightLauncher.setVelocitySetpoint(veloGoal);

        // Se o comando for zero ou negativo, desliga; caso contrário, entra em aceleração (SPINUP)
        this.launcherState = (rpm <= 0) ? LauncherState.OFF : LauncherState.SPINUP;
    }

    /**
     * Coloca o lançador em modo de espera (IDLE).
     * O motor fica girando a ~2000 RPM para não partir do zero no disparo.
     */
    public void setIdle() {
        setGoalRPM(IDLE_RPM);
        this.launcherState = LauncherState.IDLE;
    }

    /**
     * Desliga completamente os motores (0 RPM).
     */
    public void stop() {
        this.targetRPM = 0.0;
        this.veloGoal = DegreesPerSecond.of(0);

        // Corta a alimentação elétrica (modo Throttle 0.0) em vez de forçar PID a 0 RPM
        rightLauncher.setThrottle(0.0);

        this.launcherState = LauncherState.OFF;

    }

    /**
     * Verifica se o lançador atingiu a velocidade e está pronto para o disparo.
     * @return true se a velocidade real estiver dentro da tolerância do alvo.
     */
    public boolean isReady() {
        return launcherState == LauncherState.READY;
    }

    public LauncherState getState() {
        return launcherState;
    }

    public double getCurrentRPM() {
        return currentRPM;
    }

    public double getTargetRPM() {
        return targetRPM;
    }

    // =========================================================================
    // 7. LOOP PERIÓDICO (Chamado a cada ciclo do robô)
    // =========================================================================

    @Override
    public void periodic() {
        // 1. Lê a velocidade real atual em Graus/s e converte para RPM
        double currentDegPerSec = rightLauncher.getEncoderVelocity().getMagnitude();
        currentRPM = currentDegPerSec / RPM_TO_DEG_PER_SEC;

        // 2. Se estiver desligado ou em modo IDLE, não precisamos checar se está pronto para atirar
        if (launcherState == LauncherState.OFF || launcherState == LauncherState.IDLE) {
            return;
        }

        // 3. Calcula a diferença absoluta entre o alvo e a velocidade real medida
        double errorRPM = Math.abs(currentRPM - targetRPM);

        // 4. Transição automática de estados baseada na tolerância:
        if (errorRPM <= RPM_TOLERANCE) {
            // Se o erro for menor ou igual à tolerância (ex: erro <= 100 RPM), está PRONTO
            launcherState = LauncherState.READY;
        } else {
            // Se ainda estiver acelerando ou recuperando rotação após lançar uma peça
            launcherState = LauncherState.SPINUP;
        }
    }


    public Command spinUp(double rpm) {
        return infinite(() -> setGoalRPM(rpm))
                .setEnd(interrupted -> stop()); // Troque por setIdle() se preferir que fique em espera
    }

    /**
     * Comando instantâneo: define a velocidade e mantém mesmo após soltar o botão.
     */
    public Command setRpmCommand(double rpm) {
        return instant(() -> setGoalRPM(rpm));
    }

    /**
     * Comando instantâneo para desligar o lançador.
     */
    public Command stopCommand() {
        return instant(this::stop);
    }

    /**
     * Comando instantâneo para colocar em modo de espera (IDLE).
     */
    public Command idleCommand() {
        return instant(this::setIdle);
    }
}
