package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReportProcess {
    static String subject;
    static String nameOfPrepod;
    static String form;
    static String file;
    private static Map<String, ReportWriter> reports;

    ReportProcess(String nameOfPrepod, String subject, String type) {
        this.subject = subject;
        this.nameOfPrepod = nameOfPrepod;
        this.form = type;
        reports = new HashMap<String, ReportWriter>();

        file = "Отчеты/" + nameOfPrepod + "/" + subject + "/" + type;

        File folder = new File(file);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        reports = new HashMap<String, ReportWriter>();
    }


    static void writeRecord(String cardId) {
        ArrayList<Person> result = StudentDB.search(cardId);
        if (result.isEmpty()) {
            System.out.println("Студент не зарегистрирован");
        } else {
            String name = result.get(0).name;
            String group = result.get(0).group;

            ReportWriter rw = getReport(group);
            if (rw == null) {

                rw = new ReportWriter();
                addReport(group, rw);
                try {
                    rw.openFile(group);
                    rw.addDate();
                } catch (IOException ie) {
                    System.out.println("Что то пошло не так");
                }
            }
            rw.updateStudent(name);
        }
    }

    private static void addReport(String group, ReportWriter process) {

        reports.put(group, process);
    }

    private static ReportWriter getReport(String group) {

        return reports.get(group);
    }

    public static void close() {
        ReportWriter rw;
        for (Map.Entry<String, ReportWriter> e : reports.entrySet()) {

            rw = e.getValue();
            rw.close();
        }
    }

    public static void activate() {

    }
}
