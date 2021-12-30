package ru.samcold.rtks.controllers.crane;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.DetailController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.controllers.customer.CustomerListController;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.proxy.CraneProxy;
import ru.samcold.rtks.services.crane.CraneService;
import ru.samcold.rtks._utils.InitCustomerCombobox;

import java.util.Objects;

@Component
@FxmlView("/fxml/crane/craneDetail.fxml")
public class CraneDetailController implements DetailController<Crane> {

    //region FXML
    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private ComboBox<Customer> customer_cbo;

    @FXML
    private TextField name_tf;

    @FXML
    private TextField zav_tf;

    @FXML
    private TextField reg_tf;

    @FXML
    private Button save_btn;

    @FXML
    private Button cancel_btn;
    //endregion

    private final CraneService craneService;
    private final FxWeaver fxWeaver;
    private final InitCustomerCombobox initCustomerCombobox;
    private final ShowView showView;
    private CraneProxy proxy;
    private int saveMode;
    public boolean isCustomerCalls = false; // вызов из Customer List

    public CraneDetailController(
            CraneService craneService,
            FxWeaver fxWeaver,
            InitCustomerCombobox initCustomerCombobox,
            ShowView showView
    ) {
        this.craneService = craneService;
        this.fxWeaver = fxWeaver;
        this.initCustomerCombobox = initCustomerCombobox;
        this.showView = showView;
    }

    @Override
    public void initialize(Crane entity) {
        proxy = new CraneProxy(entity);
        initCustomerCombobox.init(customer_cbo);
        saveMode = entity.getId();

        DetailController.super.initialize(entity);

        if (entity.getId() != 0) {
            customer_cbo.setDisable(true);
        }
    }

    @Override
    public void initFields() {
        customer_cbo.valueProperty().bindBidirectional(proxy.customerProperty());
        name_tf.textProperty().bindBidirectional(proxy.nameProperty());
        zav_tf.textProperty().bindBidirectional(proxy.zavProperty());
        reg_tf.textProperty().bindBidirectional(proxy.regProperty());

        ValidationSupport vs = new ValidationSupport();
        vs.registerValidator(customer_cbo, Validator.createEmptyValidator("Это поле должно быть заполнено.", Severity.ERROR));
        vs.registerValidator(name_tf, Validator.createEmptyValidator("Это поле должно быть заполнено.", Severity.ERROR));

        save_btn.disableProperty().bind(vs.invalidProperty());
    }

    @Override
    public void initButtons() {
        initGeneralButtons(save_btn, cancel_btn);
    }

    @Override
    public void save() {
        cancel_btn.setText("<< Назад");

        Crane changedCrane = craneService.save(getEntity());

        if (saveMode == 0) {
            exit(changedCrane);
        } else {
            initialize(changedCrane);
            cancel_btn.setOnAction(actionEvent -> exit(changedCrane));
        }
    }

    @Override
    public void exit(Crane entity) {

        Parent root;

        if (isCustomerCalls) {
            root = fxWeaver.loadView(CustomerListController.class);

        } else {
            root = fxWeaver.loadView(CustomerListController.class);
            CraneListController controller = fxWeaver.getBean(CraneListController.class);
            if (entity != null) {
                for (Crane crane : controller.getTable().getItems()) {
                    if (Objects.equals(crane.getId(), entity.getId())) {
                        controller.getTable().getSelectionModel().select(crane);
                    }
                }
                controller.getTable().scrollTo(controller.getTable().getSelectionModel().getSelectedItem());
            }
        }

        AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(parentContainer, root, AnimationDirection.HORIZONTAL);
    }

    @Override
    public Crane getEntity() {
        return new Crane(proxy);
    }
}
