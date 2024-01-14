package org.firstinspires.ftc.teamcode;

// Импортируем библиотеки для:

import com.qualcomm.robotcore.eventloop.opmode.OpMode; // Объявления управляемого периода
import com.qualcomm.robotcore.eventloop.opmode.TeleOp; // Объявления управляемого периода с доступом к телеметрии
import com.qualcomm.robotcore.util.Range; // Объявления функции области допустимых значений

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

import java.util.List;

@TeleOp(name="Pushbot: Teleop Tank3", group="Pushbot") // Создание TeleOp класса
public class PushbotTeleopTank_Iterative extends OpMode{

    /* Создание глобальных переменных */
    GT_Pushbot robot       = new GT_Pushbot(); // Подключение класса робота
    double          clawOffset  = 0.0 ; // Текущая позиция сервомотора
    final double    CLAW_SPEED  = 0.01 ; // Скорость вращения сервомотора

    double    servoOffset  = 0.0 ; // Текущая позиция Impl-сервомотора
    final double    SERVO_SPEED  = 0.0005 ; // Скорость вращения Impl-сервомотора

    /* Код инициализации управляемого периода */

    @Override
    public void init() {
        // Инициализируем связь
        robot.init(hardwareMap);

        // Отправляем сообщение об успешной инициализации
        telemetry.addData("Say", "Hello Driver");
    }

    /* Код выполнения управляемого периода */

    @Override
    public void init_loop() {
        /* Код единоразового выполнения управляемого периода с нажатия PLAY */
    }

    @Override
    public void loop() {
        // Объявление локальных переменных
        double left; // Скорость вращения левого мотора
        double right; // Скорость вращения правого мотора
        double arm_speed; // Скорость движения руки

        // Включаем независимое управление моторов
        left = -(gamepad1.left_stick_y - gamepad1.left_stick_x)*0.5; // Задаём скорость левого мотора
        right = -(gamepad1.left_stick_y + gamepad1.left_stick_x)*0.5; // Задаём скорость правого мотора
        arm_speed = gamepad2.left_stick_y; // Задаём скорость движения руки

        robot.leftDrive.setPower(left); // Включаем левый мотор
        robot.rightDrive.setPower(right); // Включаем правый мотор
        //robot.leftArm.setPower(arm_speed); // Включаем руку

         // Используем левый и правый бамперы геймпада, чтобы открывать и закрывать клешню
        if (gamepad2.right_bumper)
            clawOffset += CLAW_SPEED;
        else if (gamepad2.left_bumper)
            clawOffset -= CLAW_SPEED;

        // Меняем позиции сервомоторов.
        clawOffset = Range.clip(clawOffset, -0.5, 0.5);
        robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
        robot.rightClaw.setPosition(servoOffset);

        // Отправление телеметрии
        telemetry.addData("servo",  "Offset = %.2f", servoOffset);
        telemetry.addData("claw",  "Offset = %.2f", clawOffset);
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addData("arm", "%.2f", arm_speed);
    }

    /* Код единоразового выполнения управляемого периода после нажатия STOP */
    @Override
    public void stop() {

    }
}
