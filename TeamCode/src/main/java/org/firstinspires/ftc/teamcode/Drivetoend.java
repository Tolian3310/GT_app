package org.firstinspires.ftc.teamcode;

// Импортируем библиотеки для:

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; // Объявления автономного периода с доступом к телеметрии
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; // Пользовательских линейных режимов работы
import com.qualcomm.robotcore.util.ElapsedTime; // Активации времени
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix; // Работы с матрицей преобразования для 3D однородные координаты
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable; // Списка с целевыми данными Vuforia
import java.util.List; // Объявления и создания списков


@Autonomous(name="Pushbot: Drivetoend ", group="Pushbot")
public class Drivetoend extends LinearOpMode {

    /*Объявление переменных*/

    GT_Pushbot robot = new GT_Pushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        /*Инициализация и подготовка к запуску*/

        robot.init(hardwareMap);
        telemetry.addData("Status", "Ready to Run");
        telemetry.update();

        waitForStart();

        robot.leftClaw.setPosition(robot.MID_SERVO);
        robot.Move_Forward(0.1);

    }
}
