package org.firstinspires.ftc.teamcode;

/* Импортируем библиотеки для: */
import com.qualcomm.robotcore.hardware.DcMotor; // Управления моторами
import com.qualcomm.robotcore.hardware.HardwareMap; // Связи между устройствами
import com.qualcomm.robotcore.hardware.Servo; // Управления сервомоторами
import com.qualcomm.robotcore.util.ElapsedTime; // Активации времени

/* Назначаем класс: */
public class GT_Pushbot
{
    /* Создание моторов и сервомоторов: */
    public DcMotor  leftDrive   = null; // Создаём мотор leftDrive
    public DcMotor  rightDrive  = null; // Создаём мотор rightDrive
    //public DcMotor  leftArm     = null; // Создаём мотор leftArm
    public Servo leftClaw    = null; // Создаём сервомотор leftClaw
    public Servo rightClaw   = null; // Создаём сервомотор rightClaw

    /* Создание глобальных переменных */
    public static final double MID_SERVO       =  0.48 ; // Создаём константу изначального положения сервомотора
    private static final double ARM_UP_POWER    =  0.45 ; // Создаём константу силы вращения сервомотора (по часовой стрелке)
    private static final double ARM_DOWN_POWER  = -0.45 ; // Создаём константу силы вращения сервомотора (против часовой стрелки)

    /* Создание локальных переменных */
    HardwareMap hwMap           =  null; // Объявляем возможность передачи телеметрии
    private ElapsedTime bot_time  = new ElapsedTime(); // Привязываем к программе время движения
    private ElapsedTime period  = new ElapsedTime(); // Привязываем к программе время периода

    private double MoveSpeed = 1.0; // Назначаем скорость движения
    private double TurnSpeed = 1.0; // Назначаем скорость поворота
    private double TurnSpeedKoef = 0.25; // Назначаем дополнительный коэффициент скорости поворота
    private double MoveSpeedKoef = 0.25; // Назначаем дополнительный коэффициент скорости движения
    private final int     Auto_Period    = 30; // Назначаем общее время задержки

    /* Генерация: */
    public GT_Pushbot(){

    }

    /* Инициализация основ: */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap; // Перезаписываем связь в локальную переменную
        period.reset(); // Задаём время начала периода

        /* Объявляем и инициализируем моторы: */
        leftDrive  = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");
        //leftArm    = hwMap.get(DcMotor.class, "left_arm");
        leftDrive.setDirection(DcMotor.Direction.REVERSE); // Объявление директории вращения на leftDrive
        rightDrive.setDirection(DcMotor.Direction.FORWARD); // Объявление директории вращения на rightDrive
        //leftArm.setDirection(DcMotor.Direction.FORWARD); // Объявление директории вращения на leftArm

        /* Выключаем все моторы: */
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        //leftArm.setPower(0);

        /* Включаем все моторы без использования энкодера: */
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //leftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Также можно использовать RUN_USING_ENCODERS, если энкодеры установлены.

        /* Объявляем и инициализируем сервомоторы: */
        leftClaw  = hwMap.get(Servo.class, "left_hand");
        rightClaw = hwMap.get(Servo.class, "right_hand");
        leftClaw.setPosition(MID_SERVO);
        rightClaw.setPosition(MID_SERVO);
    }

    /* Функция для остановки моторов */
    private void stop(){
        leftDrive.setPower(0); // Выключаем левый мотор
        rightDrive.setPower(0); // Выключаем правый мотор
        while ((leftDrive.getPower() != 0) || (rightDrive.getPower() != 0)) {
            leftDrive.setPower(0); // Выключаем левый мотор
            rightDrive.setPower(0); // Выключаем правый мотор
        }
    }

    /* Функция для запуска моторов с разной скоростью */
    private void start(double speed1, double speed2){
        leftDrive.setPower(speed1); // Запускаем левый мотор
        rightDrive.setPower(speed2); // Запускаем правый мотор
    }

    /* Функция для запуска моторов с одинаковой скоростью */
    private void start(double speed){
        leftDrive.setPower(speed); // Запускаем левый мотор
        rightDrive.setPower(speed); // Запускаем правый мотор
    }

    /* Функция для проезда вперёд */
    public void Move_Forward (double sec) {
        bot_time.reset(); // Обнуляем время
        while ((bot_time.seconds() < sec) && (period.seconds() < Auto_Period)) {
            start(MoveSpeed*(bot_time.seconds()/sec + MoveSpeedKoef)); // Запускаем моторы вперёд
        }
        stop(); // Останавливаемся
    }

    /* Функция для проезда назад */
    public void Move_Back (double sec) {
        bot_time.reset(); // Обнуляем время
        while ((bot_time.seconds() < sec) && (period.seconds() < Auto_Period)) {
            start(-MoveSpeed*(bot_time.seconds()/sec + MoveSpeedKoef)); // Запускаем моторы назад
        }
        stop(); // Останавливаемся
    }

    /* Функция для поворота */
        public void Rotate (int direction, double sec) {
        bot_time.reset(); // Обнуляем время
        while ((bot_time.seconds() < sec) && (period.seconds() < Auto_Period)) {
            if (direction == 1) {
                start(TurnSpeed*(bot_time.seconds()/sec + TurnSpeedKoef),
                        -TurnSpeed*(bot_time.seconds()/sec + TurnSpeedKoef)); // Запускаем моторы по часовой стрелке
            }
            else {
                start(-TurnSpeed*(bot_time.seconds()/sec + TurnSpeedKoef),
                        TurnSpeed*(bot_time.seconds()/sec + TurnSpeedKoef)); // Запускаем моторы поротив часовой стрелкми
            }
        }
        stop(); // Останавливаемся
    }

    /* Функция для изменения скорости движения */
    public void setMoveSpeed(double newSpeed){
        MoveSpeed = newSpeed;
    }

    /* Функция для изменения скорости поворота */
    public void setTurnSpeed(double newSpeed){
        MoveSpeed = newSpeed;
    }

    /* Функция для изменения дополнительного коэффициента скорости движения */
    public void setMoveSpeedKoef(double newSpeedKoef){
        MoveSpeedKoef = newSpeedKoef;
    }

    /* Функция для изменения дополнительного коэффициента скорости поворота */
    public void setTurnSpeedKoef(double newSpeedKoef){
        TurnSpeedKoef = newSpeedKoef;
    }
 }

