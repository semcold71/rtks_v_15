package ru.samcold.rtks.controllers.print;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.domain.Contract;

@Component
@FxmlView(value = "/fxml/print/print.fxml")
public class PrintController {

    //region FXML
    @FXML
    private RadioButton contract_rb;

    @FXML
    private RadioButton order_rb;

    @FXML
    private RadioButton act_rb;

    @FXML
    private AnchorPane container_pane;

    @FXML
    private Button cancel_btn;
    //endregion

    private Contract contract;
    private Stage thisControllerStage;
    private Stage mainControllerStage;

    private final FxWeaver fxWeaver;

    public PrintController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @FXML
    public void initialize(Contract contract) {

        this.contract = contract;

        Platform.runLater(() -> thisControllerStage = (Stage) cancel_btn.getScene().getWindow());

        //Platform.runLater(() -> mainControllerStage = (Stage) fxWeaver.loadView(MainController.class).getScene().getWindow());

        cancel_btn.setOnAction(event -> thisControllerStage.close());

        initToggle();
    }

    private void initButtons() {

    }

    private void initToggle() {

        ToggleGroup toggleGroup = new ToggleGroup();

        contract_rb.setToggleGroup(toggleGroup);

        order_rb.setToggleGroup(toggleGroup);

        act_rb.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            Object bean = null;

            if (t1.equals(contract_rb)) {
                bean = fxWeaver.getBean(PrintContractController.class);

            } else if (t1.equals(order_rb)) {
                bean = fxWeaver.getBean(PrintOrderController.class);

            } else if (t1.equals(act_rb)) {
                bean = fxWeaver.getBean(PrintAktController.class);
            }

            showPane(contract, bean);
        });

        contract_rb.setSelected(true);
    }

    private void showPane(Contract contract, Object bean) {

        Parent parent = fxWeaver.loadView(bean.getClass());

        if (bean.getClass() == PrintContractController.class) {
            ((PrintContractController) bean).initialize(contract);

        } else if (bean.getClass() == PrintOrderController.class) {
            ((PrintOrderController) bean).initialize(contract);

        } else if (bean.getClass() == PrintAktController.class) {
            ((PrintAktController) bean).initialize(contract);
        }

        container_pane.getChildren().clear();
        container_pane.getChildren().add(parent);
    }
}
