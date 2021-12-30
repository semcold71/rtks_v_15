package ru.samcold.rtks.controllers._utils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public interface DetailController<T> {

    @FXML
    default void initialize(T entity) {
        initButtons();
        initFields();
    }

    void initFields();

    void initButtons();

    void save();

    void exit(T entity);

    T getEntity();

    default void initGeneralButtons(Button saveBtn, Button cancelBtn) {
        saveBtn.setOnAction(actionEvent -> save());
        cancelBtn.setOnAction(actionEvent -> exit(null));
    }
}
