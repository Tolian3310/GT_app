package org.firstinspires.ftc.teamcode;

/* Импортируем библиотеки для: */
import com.qualcomm.robotcore.hardware.HardwareMap; // Связи между устройствами
import com.qualcomm.robotcore.util.RobotLog; // Обеспечения согласованного ведения журнала во всех пакетах RobotCore
import org.firstinspires.ftc.robotcore.external.ClassFactory; // Подключение объекта ClassFactory
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix; // Работы с матрицей преобразования 3D координат
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit; // Преобразования углов между единицами измерения
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder; // Указания хронологического порядка осей
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference; // Взаимодействия осей с вращающимися объектами
import org.firstinspires.ftc.robotcore.external.navigation.Orientation; // Добавления экземпляров ориентации
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer; // Установки собственного положения и ориентации робота
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable; // Обращения к отдельному целевому объекту
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener; // Приёма данных с VuforiaTrackable
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables; // Создания списка с целевыми объектами Vuforia
import java.util.ArrayList; // Реализации всех необязательных операций со списками
import java.util.List; // Объявления и создания списков

public class GT_Vuforia
{
    HardwareMap hwMap           =  null; // Объявляем возможность передачи телеметрии
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    static final String TAG = "Vuforia Navigation Sample";

    public List<VuforiaTrackable> init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        /* Конструктор базовых настроек камеры */

        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ae23VAT/////AAABmfRz/QK5AE2kiUdGMOsyXWgK3nhqZnA7AbFTuDIUa8ztAZA/" +
                "MrzxC3d95giXOrnuY+6JzfKSRW22eBqNYEeb/B/y/VcYhaY+H9/JG7+3Npah8f1" +
                "KMOdZ9slh7ksCdRk4cyesduxA0DJl8z4doRlpaeJO73rOoeFGnM3V5ALmeohdJ47" +
                "hxppcLJT6vdzAn6zPsWVUEB45Av+1jYwDEgB08COovaSgjA18UYGwkuiaWb3qMvxk" +
                "7J01UXS+T/nvex7o6nq6fKyPNFRJW6QsEVuZRGR5AuhdkTtLhc7rwnjNg27ToRV" +
                "eFd18P9E7zPog0EMDn+57Iwqw+MDC31lt8LW5o126UOnR622zlpRdTAO6UDfO";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables GTTeam_QR = this.vuforia.loadTrackablesFromAsset("GTTeam_QR");
        VuforiaTrackable redTarget = GTTeam_QR.get(0);
        redTarget.setName("RedTarget");  // Создание целевого объекта

        /* Объединяем все отслеживаемые объекты в одну коллекцию */

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(GTTeam_QR);

        /* Добавление габаритов робота */

        float mmPerInch = 25.4f;
        float mmBotWidth = 18 * mmPerInch;
        float mmFTCFieldWidth = (3 * 3 - 2) * mmPerInch;

        OpenGLMatrix redTargetLocationOnField = OpenGLMatrix
                /* Переводим цель на красную стену. Перевод здесь является отрицательным переводом в Х */
                .translation(-mmFTCFieldWidth / 2, 0, 0)
                .multiplied(Orientation.getRotationMatrix(
                        /* Сначала в полевой системе координат поворачиваем на 90 градусов в Х, затем на 90 градусов в Z */
                        AxesReference.EXTRINSIC, AxesOrder.XZX,
                        AngleUnit.DEGREES, 90, 90, 0));
        redTarget.setLocation(redTargetLocationOnField);
        RobotLog.ii(TAG, "Red Target=%s", format(redTargetLocationOnField));

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(mmBotWidth / 2, 0, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.YZY,
                        AngleUnit.DEGREES, -90, 0, 0));
        RobotLog.ii(TAG, "phone=%s", format(phoneLocationOnRobot));

        ((VuforiaTrackableDefaultListener) redTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        GTTeam_QR.activate(); // Отслеживание цели относительно лоокации камеры
        return allTrackables;
    }
    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
    /* Определение локации QR-кода */
    public OpenGLMatrix VFscan(List<VuforiaTrackable> allTrackables) {
        for (VuforiaTrackable trackable : allTrackables) {
            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
            if (robotLocationTransform != null) {
                lastLocation = robotLocationTransform;
            }
        }
        return lastLocation;
    }
}
