package ru.samcold.rtks.controllers.print;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.Worder;
import ru.samcold.rtks.domain.Contract;

import java.io.IOException;

@Component
@FxmlView(value = "/fxml/print/printAkt.fxml")
public class PrintAktController {

    @FXML
    private Button save_btn;

    private final Worder worder;

    public PrintAktController(Worder worder) {
        this.worder = worder;
    }

    @FXML
    void initialize(Contract contract) {
        save_btn.setOnAction(actionEvent -> {
            try {
                worder.printAkt(contract);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
