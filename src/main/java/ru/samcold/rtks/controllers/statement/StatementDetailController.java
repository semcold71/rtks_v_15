package ru.samcold.rtks.controllers.statement;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.InitCraneCombobox;
import ru.samcold.rtks._utils.InitCustomerCombobox;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.DetailController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.Statement;
import ru.samcold.rtks.domain.proxy.StatementProxy;
import ru.samcold.rtks.services.statement.StatementService;

import java.util.Objects;

@Component
@FxmlView(value = "/fxml/statement/statementDetail.fxml")
public class StatementDetailController implements DetailController<Statement> {

    //region FXML
    @FXML
    private Button cancel_btn;

    @FXML
    private TextArea cranePurpose_ta;

    @FXML
    private ComboBox<Crane> crane_cbo;

    @FXML
    private ComboBox<Customer> customer_cbo;

    @FXML
    private DatePicker nextDate_dp;

    @FXML
    private TextArea opoAddress_ta;

    @FXML
    private TextField opoDangerClass_tf;

    @FXML
    private TextArea opoName_ta;

    @FXML
    private TextField opoRegNumber_tf;

    @FXML
    private TextArea reportConclusion_ta;

    @FXML
    private TextField reportNumber_tf;

    @FXML
    private DatePicker reportSignDate_dp;

    @FXML
    private Button save_btn;

    @FXML
    private DatePicker statementDate_dp;

    @FXML
    private TextField statementNumber_tf;
    //endregion

    private int saveMode;
    private StatementProxy proxy;
    private final FxWeaver fxWeaver;
    private final StatementService statementService;
    private final InitCustomerCombobox initCustomerCombobox;
    private final InitCraneCombobox initCraneCombobox;
    private final ShowView showView;

    public StatementDetailController(FxWeaver fxWeaver,
                                     StatementService statementService,
                                     InitCustomerCombobox initCustomerCombobox,
                                     InitCraneCombobox initCraneCombobox,
                                     ShowView showView
    ) {
        this.fxWeaver = fxWeaver;
        this.statementService = statementService;
        this.initCustomerCombobox = initCustomerCombobox;
        this.initCraneCombobox = initCraneCombobox;
        this.showView = showView;
    }

    @Override
    public void initialize(Statement entity) {

        saveMode = entity.getId();

        proxy = new StatementProxy(entity);

        initCustomerCombobox.init(customer_cbo);

        // first case, init with empty collection
        initCraneCombobox.init(crane_cbo, FXCollections.observableArrayList());

        customer_cbo.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, t1) -> {
            crane_cbo.getItems().clear();
            crane_cbo.setItems(FXCollections.observableArrayList(t1.getCraneList()));
        });

        // for update or new statement for current crane
        if (entity.getCrane() != null) {
            customer_cbo.getSelectionModel().select(entity.getCrane().getCustomer());
            crane_cbo.getSelectionModel().select(entity.getCrane());
            customer_cbo.setDisable(true);
            crane_cbo.setDisable(true);
        }

        DetailController.super.initialize(entity);
    }

    @Override
    public void initFields() {
        statementDate_dp.valueProperty().bindBidirectional(proxy.statementDateProperty());
        statementNumber_tf.textProperty().bindBidirectional(proxy.statementNumberProperty());
        crane_cbo.valueProperty().bindBidirectional(proxy.craneProperty());
        cranePurpose_ta.textProperty().bindBidirectional(proxy.cranePurposeProperty());
        reportNumber_tf.textProperty().bindBidirectional(proxy.reportNumberProperty());
        reportSignDate_dp.valueProperty().bindBidirectional(proxy.reportSignDateProperty());
        nextDate_dp.valueProperty().bindBidirectional(proxy.nextDateProperty());
        reportConclusion_ta.textProperty().bindBidirectional(proxy.reportConclusionProperty());
        opoName_ta.textProperty().bindBidirectional(proxy.opoNameProperty());
        opoAddress_ta.textProperty().bindBidirectional(proxy.opoAddressProperty());
        opoDangerClass_tf.textProperty().bindBidirectional(proxy.opoDangerClassProperty());
        opoRegNumber_tf.textProperty().bindBidirectional(proxy.opoRegNumberProperty());

        // validator
        ValidationSupport vs = new ValidationSupport();
        setEmptyValidator(vs,statementDate_dp);
        setEmptyValidator(vs,statementNumber_tf);
        setEmptyValidator(vs,customer_cbo);
        setEmptyValidator(vs,crane_cbo);
        setEmptyValidator(vs,cranePurpose_ta);
        setEmptyValidator(vs,reportNumber_tf);
        setEmptyValidator(vs,reportSignDate_dp);
        setEmptyValidator(vs,nextDate_dp);
        setEmptyValidator(vs,reportConclusion_ta);
        setEmptyValidator(vs,opoName_ta);
        setEmptyValidator(vs,opoAddress_ta);
        setEmptyValidator(vs,opoDangerClass_tf);
        setEmptyValidator(vs,opoRegNumber_tf);

        save_btn.disableProperty().bind(vs.invalidProperty());
    }

    @Override
    public void initButtons() {
        initGeneralButtons(save_btn, cancel_btn);
    }


    @Override
    public void save() {
        cancel_btn.setText("<< Назад");

        Statement changedStatement = statementService.save(getEntity());

        if (saveMode == 0) {
            exit(changedStatement);
        } else {
            initialize(changedStatement);
            cancel_btn.setOnAction(actionEvent -> exit(changedStatement));
        }
    }

    @Override
    public void exit(Statement entity) {
        // init vars
        Parent root = fxWeaver.loadView(StatementListController.class);
        StatementListController controller = fxWeaver.getBean(StatementListController.class);

        // select and scroll to changed/added customer row in table
        if (entity != null) {
            for (Statement statement : controller.getTable().getItems()) {
                if (Objects.equals(statement.getId(), entity.getId())) {
                    controller.getTable().getSelectionModel().select(statement);
                }
            }
            controller.getTable().scrollTo(controller.getTable().getSelectionModel().getSelectedItem());
        }

        // set list controller
        showView.show(MainController.parentContainer, root, AnimationDirection.HORIZONTAL);
    }

    @Override
    public Statement getEntity() {
        return new Statement(proxy);
    }

    private void setEmptyValidator(ValidationSupport vs, Control control) {
        vs.registerValidator(control, Validator.createEmptyValidator("Это поле должно быть заполнено.", Severity.ERROR));
    }
}
