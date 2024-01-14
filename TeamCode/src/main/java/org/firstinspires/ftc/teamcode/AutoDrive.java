package org.firstinspires.ftc.teamcode;

// Импортируем библиотеки для:

import com.qualcomm.robotcore.eventloop.opmode.Autonomous; // Объявления автономного периода
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode; // Управления телеметрией
import com.qualcomm.robotcore.util.ElapsedTime; // Активации времени

@Autonomous(name="Pushbot: Auto Drive ", group="Pushbot")
public class AutoDrive extends LinearOpMode {

    /* Объявление глобальных переменных */
    GT_Pushbot robot   = new GT_Pushbot();   // Подключаем класс робота
    private ElapsedTime     runtime = new ElapsedTime(); // Привязываем к программе время


    static final double Move_SPEED = -0.6; // Назначаем скорость движения
    static final double Turn_SPEED = 0.5; // Назначаем скорость поворота
    static final int     Delay    = 500; // Назначаем общее время задержки

/* Функция для проезда вперёд */
    public void Move_Forward (double sec) {
        robot.leftDrive.setPower(Move_SPEED); // Запускаем левый мотор
        robot.rightDrive.setPower(Move_SPEED); // Запускаем правый мотор
        runtime.reset(); // Обнуляем время
        while (opModeIsActive() && (runtime.seconds() < sec)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update(); // Обновление телеметрии
        }
        robot.leftDrive.setPower(0); // Выключаем левый мотор
        robot.rightDrive.setPower(0); // Выключаем правый мотор
        sleep(Delay); // Задержка
    }
/* Функция для проезда назад */
    public void Move_Back (double sec) {
        robot.leftDrive.setPower(-Move_SPEED); // Запускаем левый мотор
        robot.rightDrive.setPower(-Move_SPEED); // Запускаем правый мотор
        runtime.reset(); // Обнуляем время
        while (opModeIsActive() && (runtime.seconds() < sec)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update(); // Обновление телеметрии
        }
        robot.leftDrive.setPower(0); // Выключаем левый мотор
        robot.rightDrive.setPower(0); // Выключаем правый мотор
        sleep(Delay); // Задержка
    }
/* Функция для поворота */
    public void Rotate (int direction, double sec) {
        if (direction == 1) {
            robot.leftDrive.setPower(Turn_SPEED); // Запускаем левый мотор
            robot.rightDrive.setPower(-Turn_SPEED); // Инверсированно запускаем правый мотор
        }
        else {
            robot.leftDrive.setPower(-Turn_SPEED); // Инверсированно запускаем левый мотор
            robot.rightDrive.setPower(Turn_SPEED); // Запускаем правый мотор
        }

        runtime.reset(); // Обнуляем время

        while (opModeIsActive() && (runtime.seconds() < sec)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update(); // Обновление телеметрии
        }

        robot.leftDrive.setPower(0); // Выключаем левый мотор
        robot.rightDrive.setPower(0); // Выключаем правый мотор
        sleep(Delay); // Задержка
    }

    @Override
    public void runOpMode() {

        /* Прогон автономного периода */
        robot.init(hardwareMap);

        // Отправляем сообщение об ожтдании старта
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // Ожидание старта
        waitForStart();

        Move_Forward(1);
        Rotate(1, 0.7);
        Move_Back(0.5);

        telemetry.addData("Path", "Complete");
        telemetry.update(); // Обновление телеметрии
        sleep(1000); // Задержка
    }
}
