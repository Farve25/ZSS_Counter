package model;

import view.DialogManager;

import java.sql.*;

public class StudentDB {

    private static Connection connection = null;
    private static Statement stmt;
    private static ResultSet resSet = null;
    private static PreparedStatement pstAdd;
    private static PreparedStatement pstUpdate;
    private static PreparedStatement pstSearch;

    public static void add(String id, String name, String group) {
        try {
            pstAdd.setString(1, id);
            pstAdd.setString(2, name);
            pstAdd.setString(3, group);
            pstAdd.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не удалось добавить запись! " + e.getMessage());
        }
    }

    public static void update(String id, String name, String group) {
        try {
            pstUpdate.setString(1, name);
            pstUpdate.setString(2, group);
            pstUpdate.setString(3, id);
            pstUpdate.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не удалось обновить запись!");
        }

    }

    static Person search(String cardId) {
        Person student = null;
        try {
            pstSearch.setString(1, cardId);
            resSet = pstSearch.executeQuery();

            if (resSet.next()) {
                //System.out.println("запись");
                String name = resSet.getString("name");
                String group = resSet.getString("team");

                student = new Person(cardId, name, group);
            } else {
                student = new Person(cardId);
            }

        } catch (SQLException e) {
            DialogManager.showErrorDialog("Не удалось провести поиск!", "e.getMessage()");
        }
        try {
            resSet.close();
        } catch (Exception e) {

        }


        return student;
    }

    public static void setConnection(){
        try {
            connection = null;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Student.db");

            pstAdd = connection.prepareStatement(
                    "INSERT INTO Student (id, name, team) VALUES (?, ?, ?);");

            pstUpdate = connection.prepareStatement("Update Student set name = ?, team = ? where id = ?;");

            pstSearch = connection.prepareStatement("SELECT name, team FROM Student WHERE id = ?;");

            pstSearch = connection
                    .prepareStatement("Select name, team from Student where id = ?;");

            QueryProcessor.setDBConnection();
        } catch (Exception e) {
            DialogManager.showErrorDialog("Не удалось подключится к БД", e.getMessage());
        }
    }

    static void fullDB() throws ClassNotFoundException, SQLException {
        stmt = connection.createStatement();
        resSet = stmt.executeQuery("SELECT * FROM Student");

        while (resSet.next()) {
            String id = resSet.getString("id");
            String name = resSet.getString("name");
            String group = resSet.getString("team");
            System.out.println("ID = " + id);
            System.out.println("name = " + name);
            System.out.println("group = " + group);
            System.out.println();
        }

        System.out.println("Таблица выведена");
    }

    public static void closeDB() throws ClassNotFoundException, SQLException {
        if (connection != null) {
            pstAdd.close();
            pstUpdate.close();
            pstSearch.close();
            connection.close();
            if (resSet != null)
                resSet.close();
        }

        System.out.println("Соединения закрыты");
    }
}
