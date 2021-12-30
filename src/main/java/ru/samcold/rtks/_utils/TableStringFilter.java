package ru.samcold.rtks._utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class TableStringFilter {

    public <T> void setFilter(TextField searchField, TableView<T> tableView) {

        ObservableList<T> list = tableView.getItems();

        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if (s != null && t1.length() < s.length()) {
                tableView.setItems(list);
            }

            String value = t1.toLowerCase();
            ObservableList<T> subentries = FXCollections.observableArrayList();

            long count = tableView.getColumns().size();
            for (int i = 0; i < tableView.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "";
                    entry += tableView.getColumns().get(j).getCellData(i);
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
