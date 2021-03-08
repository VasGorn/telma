package controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class MessageBox {
    static public void showError(String content){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                //alert.setHeaderText(header);
                alert.setContentText(content);

                alert.showAndWait();
            }
        });

    }
}
