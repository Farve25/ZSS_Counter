package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.*;

import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

public class MenuController {

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Button checkButton;

    @FXML
    private Button reportButton;

    @FXML
    private Button okData;

    @FXML
    private Button cancelData;

    @FXML
    private Button addPrepodReportButton;

    @FXML
    private Button addSubjectButton;

    @FXML
    private Button cancelStart;

    @FXML
    private Button login;

    @FXML
    private Button endReport;

    @FXML
    private Button cancelReport;

    @FXML
    private Button startReportButton;

    @FXML
    private Button cancelRegistrationButton;

    @FXML
    private Label label;

    @FXML
    private Label prepod;

    @FXML
    private Label subject;

    @FXML
    private Label type;

    @FXML
    private Label dataLabel;

    @FXML
    private Label dataError;

    @FXML
    private Label warningLabel;

    @FXML
    private Label studId;

    @FXML
    private Label errorMessage;

    @FXML
    private TextField dataField;

    @FXML
    private TextField studName;

    @FXML
    private TextField studGroup;

    @FXML
    private TabPane studentTabPane;

    @FXML
    private AnchorPane createReportPane;

    @FXML
    private AnchorPane reportPane;

    @FXML
    private AnchorPane warningPane;

    @FXML
    private AnchorPane registrationPane;

    @FXML
    private AnchorPane dataPane;

    @FXML
    private ComboBox<String> prepodBox;

    @FXML
    private ComboBox<String> subjectBox;

    @FXML
    private ComboBox<String> typeBox;


    public WorkingMode mode;

    @FXML
    public void initialize() {
        model.QueryProcessor.setController(this);
        System.out.println("Запуск программы");
        typeBox.getItems().addAll("Лекция", "Практическое занятие", "Лабораторное занятие");
    }

    @FXML
    public void checkPress(ActionEvent event){


    }


    @FXML
    public void reportPress (ActionEvent event){
        String list[] = new File("C:\\Users\\Farvest\\workspace1\\ZSS_Counter\\Отчеты").list();
        prepodBox.getItems().setAll(list);
        createReportPane.setVisible(true);
    }

    @FXML
    public void choosePrepod (ActionEvent event){
        System.out.println(prepodBox.getValue());

        String list[] = new File("C:\\Users\\Farvest\\workspace1\\ZSS_Counter\\Отчеты\\" + prepodBox.getValue()).list();
        subjectBox.getItems().clear();
        subjectBox.getItems().addAll(list);
        subjectBox.setDisable(false);
        addSubjectButton.setDisable(false);
    }

    @FXML
    public void chooseSubject (ActionEvent event){
        typeBox.setDisable(false);
        System.out.println("1");
    }

    @FXML
    public void chooseType (ActionEvent event){
        startReportButton.setDisable(false);
    }


    @FXML
    public void addPrepod (ActionEvent event){
        dataLabel.setText("Введите ФИО преподователя");
        dataField.setPromptText("Пример: Красов Андрей Владимирович");
        dataField.setText("");
        dataPane.setVisible(true);
    }

    @FXML
    public void addSubject (ActionEvent event){
        dataLabel.setText("Введите название предмета");
        dataField.setPromptText("Пример: Компьютерные вирусы");
        dataField.setText("");
        dataPane.setVisible(true);
    }

    @FXML
    public void startReport (ActionEvent event){
        if ("Выберите предмет".equals(subjectBox.getValue())) {

        } else {
            prepod.setText(prepodBox.getValue());
            subject.setText(subjectBox.getValue());
            type.setText(typeBox.getValue());
            createReportPane.setVisible(false);
            prepodBox.getValue();
            reportPane.setVisible(true);
            mode = WorkingMode.Report;
        }

    }

    @FXML
    public void createReport (ActionEvent event){
        prepod.getText();
        subject.getText();
        type.getText();

    }

    public void addStudent(String name, String group){
        //studentTabPane.getTabs().

    }

    @FXML
    public void cancelDataPress (ActionEvent event){
        dataPane.setVisible(false);
    }

    @FXML
    public void okDataPress (ActionEvent event){
        System.out.println(dataField.getText());
        if (dataField.getText().equals("")) {
            dataPane.setVisible(false);
        } else {
            dataError.setText("Поле пустое");
        }
    }

    public void createLoginPane(String cardId, String name, String group){
        studId.setText("id: " + cardId);
        studName.setText(name);
        studGroup.setText(group);
        login.setText("Редактировать");
        registrationPane.setVisible(true);
    }

    public void createLoginPane(String cardId){
        studId.setText(cardId);
        studName.clear();
        studGroup.clear();
        login.setText("Зарегистрировать");
        registrationPane.setVisible(true);
    }



    @FXML
    public void processLogin (ActionEvent event){

        if ((studName.getText().equals("")) || (studGroup.getText().equals(""))){
            errorMessage.setText("Заполните все поля!");
        }else {
            if (login.getText().equals("Редактировать")) {
                model.StudentDB.update(studId.getText(), studName.getText(), studGroup.getText());
            } else {
                model.StudentDB.add(studId.getText(), studName.getText(), studGroup.getText());
            }
        }
        registrationPane.setVisible(false);
    }

    @FXML
    public void menuRegistrationPress (){
        mode = WorkingMode.Login;
        try {
            readCard();
        } catch (Exception e) {

        }
        
        registrationPane.setVisible(true);
    }

    @FXML
    public void cancelRegistration (){
        registrationPane.setVisible(false);
    }

    @FXML
    public void pressCancelStart(){
        createReportPane.setVisible(false);
    }

    private void hidePane(){
        if (reportPane.isVisible()){
            warningLabel.setText("При переходе");
            warningPane.setVisible(true);
            registrationPane.setVisible(false);
        }


        dataPane.setVisible(false);
        reportPane.setVisible(false);
        createReportPane.setVisible(false);
    }



    private void readCard() throws Exception {
        if (model.PortFinder.getStatus()) {
            System.out.println("Приподнесите карту к считывателю.");
            model.HardReader.startReading();
        } else {
            System.out.println("Считыватель не найден!!! Проверьте подключение.");
        }
    }



    public WorkingMode getMode(){
        return mode;
    }

    public MenuController getController(){
        return this;
    }

    public enum WorkingMode {Login, Test, Verify, Report, Remove}
}
