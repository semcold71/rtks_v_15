package ru.samcold.rtks._utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

@Component
public class TableColumnAutoNumbering<T> {

    public void create(TableColumn<T, T> column) {

        column.setCellValueFactory(craneCraneCellDataFeatures ->
                new ReadOnlyObjectWrapper<>(craneCraneCellDataFeatures.getValue()));

        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<T, T> call(TableColumn<T, T> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(String.valueOf(this.getTableRow().getIndex() + 1));
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        column.setSortable(false);
    }
}
