package ru.samcold.rtks.controllers.print;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.Worder;
import ru.samcold.rtks.domain.Contract;

import java.io.IOException;

@Component
@FxmlView(value = "/fxml/print/printContract.fxml")
public class PrintContractController {

    private final Worder worder;

    @FXML
    private Button save_btn;

    public PrintContractController(Worder worder) {
        this.worder = worder;
    }

    @FXML
    public void initialize(Contract contract) {
        save_btn.setOnAction(actionEvent -> {
            try {
                worder.printContract(contract);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
