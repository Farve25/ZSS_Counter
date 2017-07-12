package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.PortFinder;
import model.StudentDB;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        PortFinder pf = new model.PortFinder("model.PortFinder");
        pf.start();
        model.StudentDB.setConnection();

        try {
            VBox page = (VBox) FXMLLoader.load(Main.class.getResource("/menu.fxml"));
            Scene scene = new Scene(page);



            primaryStage.setScene(scene);
            primaryStage.setTitle("hyi");
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("Закрытие программы");
                    PortFinder.setStop();
                    try {
                        StudentDB.closeDB();
                    } catch (Exception e){

                    }

                    //event.consume();
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

}