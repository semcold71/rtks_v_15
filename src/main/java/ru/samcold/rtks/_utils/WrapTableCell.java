package ru.samcold.rtks._utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

@Component
public class WrapTableCell<T, S> {

    public void wrapCell(TableColumn<T, S> column) {
        column.setCellFactory(column1 -> {
            TableCell<T, S> cell = new TableCell<>();
            Text text = new Text();
            text.setStyle("-fx-fill: #BBBBBB;");
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);

            DoubleProperty prop = new SimpleDoubleProperty();
            prop.set(column1.widthProperty().get() - 4);

            //text.wrappingWidthProperty().bind(column1.widthProperty());
            text.wrappingWidthProperty().bind(prop);

            text.textProperty().bind((ObservableValue<? extends String>) cell.itemProperty());
            return cell;
        });
    }
}


