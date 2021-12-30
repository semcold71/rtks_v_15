package ru.samcold.rtks._utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Component
public class TableColumnWithDate {

    public <T> void init(TableColumn<T, Date> column) {
        column.setCellFactory(col -> new TextFieldTableCell<>() {
            @Override
            public void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    this.setText(new SimpleDateFormat("dd.MM.yyyy").format(item));
                }
            }
        });
    }
}
