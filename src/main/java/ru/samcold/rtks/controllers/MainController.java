package ru.samcold.rtks.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.controllers.contract.ContractListController;
import ru.samcold.rtks.controllers.crane.CraneListController;
import ru.samcold.rtks.controllers.customer.CustomerListController;
import ru.samcold.rtks.controllers.opo.OpoListController;
import ru.samcold.rtks.controllers.statement.StatementListController;

@Component
@FxmlView("/fxml/main.fxml")
public class MainController {

    //region FXML
    @FXML
    private Button customersButton;

    @FXML
    private Button cranesButton;

    @FXML
    private Button contractsButton;

    @FXML
    private Button statementButton;

    @FXML
    private Button opoButton;

    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane containerPane;
    //endregion

    private final FxWeaver fxWeaver;

    private final ShowView showView;

    public static AnchorPane parentContainer;

    public MainController(FxWeaver fxWeaver, ShowView showView) {
        this.fxWeaver = fxWeaver;
        this.showView = showView;
    }

    @FXML
    public void initialize() {
        initButtons();
        titleLabel.setText("");
        parentContainer = containerPane;
    }

    private void initButtons() {
        customersButton.setOnAction(actionEvent -> {
            titleLabel.setText("Заказчики");
            showView.show(containerPane, fxWeaver.loadView(CustomerListController.class), AnimationDirection.HORIZONTAL);
        });

        cranesButton.setOnAction(actionEvent -> {
            titleLabel.setText("Подъемные сооружения");
            showView.show(containerPane, fxWeaver.loadView(CraneListController.class), AnimationDirection.HORIZONTAL);
        });

        contractsButton.setOnAction(actionEvent -> {
            titleLabel.setText("Договора");
            showView.show(containerPane, fxWeaver.loadView(ContractListController.class), AnimationDirection.HORIZONTAL);
        });

        statementButton.setOnAction(actionEvent -> {
            titleLabel.setText("Заявления");
            showView.show(containerPane, fxWeaver.loadView(StatementListController.class), AnimationDirection.HORIZONTAL);
        });

        opoButton.setOnAction(actionEvent -> {
            titleLabel.setText("ОПО и отчеты о ПК");
            showView.show(containerPane, fxWeaver.loadView(OpoListController.class), AnimationDirection.HORIZONTAL);
        });
    }
}

