package ru.samcold.rtks._utils;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Component
public class TableDateFilter {

    public <T> void setFilter(TextField searchField, TableView<T> tableView) {

        ObservableList<T> items = tableView.getItems();

        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                tableView.setItems(items);
            }

            String value = newValue.toLowerCase();
            ObservableList<T> subentries = FXCollections.observableArrayList();

            int count = tableView.getColumns().size();
            for (int i = 0; i < tableView.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "";
                    Object o = tableView.getColumns().get(j).getCellData(i);
                    if (o.getClass() == Date.class) {
                        entry += new SimpleDateFormat("dd.MM.yyyy").format(o);
                    } else {
                        entry += o.toString();
                    }

                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(tableView.getItems().get(i));
                        break;
                    }
                }
            }

            tableView.setItems(subentries);
        });
    }
}
