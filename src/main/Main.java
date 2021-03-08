package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/telma.fxml"));
        primaryStage.setTitle("TELMA");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            launch(args);
        }
        catch (Exception e) {
            try {
                PrintWriter pw = new PrintWriter(new File("error.txt"));
                e.printStackTrace(pw);
                pw.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
