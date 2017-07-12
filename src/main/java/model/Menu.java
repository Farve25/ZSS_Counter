package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class Menu extends Thread{
    static WorkingMode mode;

    static void menu() throws Exception {
        System.out.println("Меню:");
        System.out.println("1. Проверка работы");
        System.out.println("2. Создание отчета");
        System.out.println("3. Регистрация студента");
        System.out.println("4. Проверка регистрации/редактирование");
        System.out.println("0. Выход");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        switch (Integer.parseInt(in.readLine())) {
            case 1:
                System.out.println("Проверка работоспособности");
                mode = WorkingMode.Test;
                readCard();
                break;

            case 2:
                System.out.println("Имя преподователя:");
                String nameOfPrepod = in.readLine();
                System.out.println("Пердмет:");
                String subject = in.readLine();
                System.out.println("Форма занятия:");
                String form = in.readLine();
                mode = WorkingMode.Report;
                ReportProcess rp = new ReportProcess(nameOfPrepod, subject, form);
                HardReader.startReporting();
                sleep(10000);
                ReportProcess.close();
                HardReader.endReporting();
                menu();
                break;

            case 3:
                System.out.println("Регистрация студента:");
                mode = WorkingMode.Login;
                readCard();
                break;

            case 4:
                System.out.println("Поверки регистрации");
                mode = WorkingMode.Verify;
                HardReader.startReading();
                break;

            case 0://Выход
                System.out.println("Завершение работы");
                PortFinder.setStop();
                StudentDB.closeDB();
                break;

            default:
                break;
        }
    }

    static void readCard() throws Exception {
        if (PortFinder.getStatus()) {
            System.out.println("Приподнесите карту к считывателю.");
            HardReader.startReading();
        } else {
            System.out.println("Считыватель не найден!!! Проверьте подключение.");
            menu();
        }
    }

    enum WorkingMode {Login, Test, Verify, Report, Remove}
}
