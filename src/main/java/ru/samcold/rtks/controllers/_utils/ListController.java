package ru.samcold.rtks.controllers._utils;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public interface ListController<T> {

    @FXML
    default void initialize() {
        initTable();
        initButtons();
    }

    void initTable();

    void initButtons();

    void showDetail(T entity);

    TableView<T> getTable();
}

