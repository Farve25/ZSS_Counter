package model;

import controller.MenuController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QueryProcessor {

    private static MenuController controller;
    public ArrayList<Person> result;

    public static void setController(MenuController controller1) {
        controller = controller1;
    }

    public void process(String cardId) {
        switch (controller.getMode()) {
            case Test://Проверка работоспособности
                //Вывести id карты на экран
                System.out.println(cardId);
                break;

            case Report://Создание отчета

                break;

            case Login: // Регистрация/Редактирование студента

                result = StudentDB.search(cardId);
                System.out.println(result);
                if (result.isEmpty()) {
                    System.out.println("хуй");
                } else {
                    System.out.println("хуй1");
                    if (result.get(0).name == null) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                controller.createLoginPane(result.get(0).cardId);
                            }
                        });
                        //controller.createLoginPane(result.get(0).cardId);
                    } else {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                controller.createLoginPane(result.get(0).cardId, result.get(0).name, result.get(0).group);
                            }
                        });
                        //controller.createLoginPane(result.get(0).cardId, result.get(0).name, result.get(0).group);
                    }
                }


                break;

            case Verify://Проверка регистрации студента
                ArrayList<Person> result1 = StudentDB.search(cardId);
                if (result1.isEmpty()) {
                    System.out.println("Студент не зарегистрирован");
                } else {
                    System.out.println("Имя: " + result1.get(0));
                    System.out.println("Группа: " + result1.get(1));
                }
                System.out.println("Редактировать? (Введите да если хотите редактировать)");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String answer = in.readLine();
                    if ("да".equals(answer.toLowerCase())) {
                        System.out.println("Введите ФИО:");
                        String name = in.readLine();
                        System.out.println("Введите группу:");
                        String group = in.readLine().toUpperCase();
                        StudentDB.update(cardId, name, group);
                    }
                } catch (IOException ie) {
                    System.out.println("Нельзя редактировать");
                }
                break;
            case Remove://Редактирование данных студента

                break;
            default:
                System.out.println("хуй");
                break;
        }
    }
}

