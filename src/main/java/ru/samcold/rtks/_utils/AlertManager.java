package ru.samcold.rtks._utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.FormApplication;

@Component
public class AlertManager {

    private boolean dialogResult = false;

    public boolean showDeleteInfo(String text) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setContentText(text);
        alert.setHeaderText("");

        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("OK");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Отмена");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);

        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(FormApplication.class.getResource("/styles/style.css").toExternalForm());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dialogResult = true;
            }
        });

        return dialogResult;
    }
}
