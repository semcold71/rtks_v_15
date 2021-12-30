package ru.samcold.rtks.controllers.print;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.Worder;
import ru.samcold.rtks.domain.Contract;

import java.io.IOException;

@Component
@FxmlView(value = "/fxml/print/printOrder.fxml")
public class PrintOrderController {

    //region FXML
    @FXML
    private Button save_btn;

    @FXML
    private RadioButton prepay_rb;

    @FXML
    private RadioButton endpay_rb;

    @FXML
    private Label total_lbl;
    //endregion

    private final Worder worder;

    private Contract contract;
    private ToggleGroup toggleGroup;
    private int prepayKey;

    public PrintOrderController(Worder worder) {
        this.worder = worder;
    }

    @FXML
    void initialize(Contract contract) {
        this.contract = contract;
        initToggle(contract);
        save_btn.setOnAction(actionEvent -> {
            try {
                worder.printOrder(contract, prepayKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initToggle(Contract contract) {
        if (contract.getPrepay() < 100) {
            toggleGroup = new ToggleGroup();
            prepay_rb.setToggleGroup(toggleGroup);
            endpay_rb.setToggleGroup(toggleGroup);

            toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
                prepayKey = t1 == prepay_rb ? 1 : 2;
                calc(t1);
            });
            prepay_rb.setSelected(true);
            prepay_rb.setText(prepay_rb.getText() + " (" + contract.getPrepay() + "%)");

        } else {
            prepayKey = 0;
            prepay_rb.setDisable(true);
            endpay_rb.setDisable(true);
            total_lbl.setText(String.format("%,.2f", contract.getPrice()));
        }
    }

    private void calc(Toggle toggle) {
        double sum = contract.getPrice();
        int prepay = contract.getPrepay();
        double pre = sum * prepay / 100.0;
        double end = sum - pre;

        if (toggle.equals(prepay_rb)) {
            total_lbl.setText(String.format("%,.2f", pre));

        } else if (toggle.equals(endpay_rb)) {
            total_lbl.setText(String.format("%,.2f", end));
        }
    }

}
