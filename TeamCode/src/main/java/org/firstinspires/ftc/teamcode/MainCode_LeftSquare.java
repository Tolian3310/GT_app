package org.firstinspires.ftc.teamcode;

// Импортируем библиотеки для:

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; // Объявления автономного периода с доступом к телеметрии
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; // Пользовательских линейных режимов работы
import com.qualcomm.robotcore.util.ElapsedTime; // Активации времени
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix; // Работы с матрицей преобразования для 3D однородные координаты
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable; // Списка с целевыми данными Vuforia
import java.util.List; // Объявления и создания списков


@Autonomous(name="Pushbot: MainCode_LeftSquare ", group="Pushbot")
public class MainCode_LeftSquare extends LinearOpMode {

    /*Объявление переменных*/

    GT_Pushbot robot = new GT_Pushbot();   // Use a Pushbot's hardware
    GT_Vuforia Vuforia = new GT_Vuforia();
    private ElapsedTime runtime = new ElapsedTime();
    private OpenGLMatrix lastLocation = null;
    private List<VuforiaTrackable> allTrackables = null;

    @Override
    public void runOpMode() {

        /*Инициализация и подготовка к запуску*/

        robot.init(hardwareMap);
        allTrackables = Vuforia.init(hardwareMap);

        byte position = 0;

        robot.leftClaw.setPosition(0.0);
        telemetry.addData("Status", "Ready to Run");
        telemetry.update();

        waitForStart();

        /*Обнаружение и отображение целевой зоны*/

        lastLocation = Vuforia.VFscan(allTrackables);
        sleep(100);
        lastLocation = Vuforia.VFscan(allTrackables);
        if(lastLocation != null) {
            position = 3;
        } else {
            robot.leftClaw.setPosition(robot.MID_SERVO);
            robot.Move_Forward(0.1);
            sleep(500);
            lastLocation = Vuforia.VFscan(allTrackables);
            sleep(250);
            lastLocation = Vuforia.VFscan(allTrackables);
            if(lastLocation != null) {
                position = 2;
            } else {
                position = 1;
            }
        }

        telemetry.addData("pos", position);
        telemetry.update();

        // point 1:
        if (position == 1) {
            robot.Move_Forward(2.0);
            robot.Rotate(1, 0.9);
            robot.Move_Forward(2.5);
        }

        // point 2:
        if (position == 2) {
            robot.Rotate(0,0.5);
            robot.Move_Forward(0.3);
            robot.Rotate(1,0.45);
            robot.Move_Forward(1.3);

        }

        // point 3:
        if (position == 3) {
            robot.Move_Forward(0.5);
            robot.Rotate(1,0.9);
            robot.Move_Forward(1.5);
        }


    }
}
