package ru.samcold.rtks.controllers.contract;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.InitCustomerCombobox;
import ru.samcold.rtks._utils.NumberValidator;
import ru.samcold.rtks._utils.TableColumnFormatter;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.DetailController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.controllers.customer.CustomerListController;
import ru.samcold.rtks.controllers.work.WorkController;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.Work;
import ru.samcold.rtks.domain.proxy.ContractProxy;
import ru.samcold.rtks.services.contract.ContractService;
import ru.samcold.rtks.services.work.WorkService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@Component
@FxmlView("/fxml/contract/contractDetail.fxml")
public class ContractDetailController implements DetailController<Contract> {

    //region FXML
    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private Label title_lbl;

    @FXML
    private ComboBox<Customer> customer_cbo;

    @FXML
    private TextField number_tf;

    @FXML
    private DatePicker date_picker;

    @FXML
    private TextField start_tf;

    @FXML
    private TextField finish_tf;

    @FXML
    private TextField prepay_tf;

    @FXML
    private TableView<Work> work_table;

    @FXML
    private TableColumn<Work, Integer> num_col;

    @FXML
    private TableColumn<Work, String> description_col;

    @FXML
    private TableColumn<Work, String> unit_col;

    @FXML
    private TableColumn<Work, Integer> count_col;

    @FXML
    private TableColumn<Work, Double> price_col;

    @FXML
    private TableColumn<Work, Double> total_work_col;

    @FXML
    private Button addWork_btn;

    @FXML
    private Button delWork_btn;

    @FXML
    private TextField total_tf;

    @FXML
    private TextArea note_tf;

    @FXML
    private Button save_btn;

    @FXML
    private Button cancel_btn;
    //endregion

    public boolean isCustomerCalls = false;

    private int saveMode;
    private ContractProxy proxy;
    private final ContractService contractService;
    private final WorkService workService;
    private final InitCustomerCombobox initCustomerCombobox;
    private final NumberValidator numberValidator;
    private final FxWeaver fxWeaver;
    private final ShowView showView;

    public ContractDetailController(
            ContractService contractService,
            WorkService workService,
            InitCustomerCombobox initCustomerCombobox,
            NumberValidator numberValidator,
            FxWeaver fxWeaver,
            ShowView showView
    ) {
        this.contractService = contractService;
        this.workService = workService;
        this.initCustomerCombobox = initCustomerCombobox;
        this.numberValidator = numberValidator;
        this.fxWeaver = fxWeaver;
        this.showView = showView;
    }

    @Override
    public void initialize(Contract entity) {
        saveMode = entity.getId();
        proxy = new ContractProxy(entity);
        initCustomerCombobox.init(customer_cbo);

        DetailController.super.initialize(entity);

        if (entity.getId() != 0) {
            customer_cbo.setDisable(true);
        }

        initWorksTable(entity.getWorkList());

        if (saveMode == 0) {
            title_lbl.setText("Договор (новый)");
        } else {
            title_lbl.setText("Договор № "
                            + entity.getNumber()
                            + " от " + new SimpleDateFormat("dd.MM.yyyy").format(entity.getDate())
                            + " (редактирование)");
        }
    }

    @Override
    public void initFields() {
        number_tf.textProperty().bindBidirectional(proxy.numberProperty());
        date_picker.valueProperty().bindBidirectional(proxy.dateProperty());
        customer_cbo.valueProperty().bindBidirectional(proxy.customerProperty());
        start_tf.textProperty().bindBidirectional(proxy.startProperty());
        finish_tf.textProperty().bindBidirectional(proxy.finishProperty());
        note_tf.textProperty().bindBidirectional(proxy.noteProperty());

        prepay_tf.textProperty().bindBidirectional(proxy.prepayProperty(), new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number);
            }

            @Override
            public Number fromString(String s) {
                return Integer.parseInt(s);
            }
        });

        total_tf.textProperty().bindBidirectional(proxy.priceProperty(), new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.format("%,.2f", number.doubleValue());
            }

            @Override
            public Number fromString(String s) {
                return Double.parseDouble(s.replace(',', '.'));
            }
        });

        // validator
        ValidationSupport vs = new ValidationSupport();

        vs.registerValidator(number_tf, Validator.createEmptyValidator("Это поле должно быть заполнено", Severity.ERROR));
        vs.registerValidator(date_picker, Validator.createEmptyValidator("Это поле должно быть заполнено", Severity.ERROR));
        vs.registerValidator(customer_cbo, Validator.createEmptyValidator("Это поле должно быть заполнено", Severity.ERROR));
        vs.registerValidator(start_tf, Validator.createEmptyValidator("Это поле должно быть заполнено", Severity.ERROR));
        vs.registerValidator(finish_tf, Validator.createEmptyValidator("Это поле должно быть заполнено", Severity.ERROR));

        vs.registerValidator(prepay_tf, Validator.createEmptyValidator("Это поле должно быть заполнено", Severity.ERROR));
        numberValidator.IntegerValidator(prepay_tf, 1, 100);

        Validator<String> validator = (control, value) -> {
            boolean condition = value == null || value.equals("0,00");
            return ValidationResult.fromMessageIf(control, "This field don't equals 0", Severity.ERROR, condition);
        };
        vs.registerValidator(total_tf, true, validator);

        save_btn.disableProperty().bind(vs.invalidProperty());
    }

    @Override
    public void initButtons() {

        initGeneralButtons(save_btn, cancel_btn);

        addWork_btn.setOnAction(actionEvent -> {
            if (proxy.customerProperty().get() != null)
                showWorkWindow();
        });

        delWork_btn.setOnAction(actionEvent -> {
            if (work_table.getItems().size() > 0)
                deleteWork();
        });
    }

    @Override
    public void save() {
        cancel_btn.setText("<< Назад");

        Contract changedContract = contractService.save(getEntity());

        for (Work work : getWorkList()) {
            work.setContract(changedContract);
            workService.save(work);
        }

        if (saveMode == 0) {
            exit(changedContract);
        } else {
            initialize(changedContract);
            cancel_btn.setOnAction(actionEvent -> exit(changedContract));
        }
    }

    @Override
    public void exit(Contract entity) {

        Parent root;

        if (isCustomerCalls) {
            root = fxWeaver.loadView(CustomerListController.class);

        } else {
            root = fxWeaver.loadView(ContractListController.class);
            ContractListController controller = fxWeaver.getBean(ContractListController.class);

            if (entity != null) {
                for (Contract contract : controller.getTable().getItems()) {
                    if (Objects.equals(contract.getId(), entity.getId())) {
                        controller.getTable().getSelectionModel().select(contract);
                    }
                }
                controller.getTable().scrollTo(controller.getTable().getSelectionModel().getSelectedItem());
            }
        }

        AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(parentContainer, root, AnimationDirection.HORIZONTAL);
    }

    @Override
    public Contract getEntity() {
        return new Contract(proxy);
    }

    private void initWorksTable(List<Work> workList) {

        // placeholder
        work_table.setPlaceholder(new Label("Нет данных для показа"));

        // init columns
        num_col.setCellValueFactory(new PropertyValueFactory<>("num"));
        unit_col.setCellValueFactory(new PropertyValueFactory<>("unit"));
        count_col.setCellValueFactory(new PropertyValueFactory<>("count"));

        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
        price_col.setCellFactory(new TableColumnFormatter<>(new DecimalFormat("#,#00.00")));

        total_work_col.setCellValueFactory(new PropertyValueFactory<>("total"));
        total_work_col.setCellFactory(new TableColumnFormatter<>(new DecimalFormat("#,#00.00")));

        // multiline cell text
        description_col.setCellFactory(tc -> {
            TableCell<Work, String> cell = new TableCell<>();
            Text text = new Text();
            text.setStyle("-fx-fill: #BBBBBB;");
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(description_col.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));

        // fill data
        if (workList != null) {
            work_table.setItems(FXCollections.observableArrayList(workList));
            work_table.getSelectionModel().selectFirst();
        }
    }

    private void showWorkWindow() {
        Stage stage = new Stage();

        Parent parent = fxWeaver.loadView(WorkController.class);
        WorkController controller = fxWeaver.getBean(WorkController.class);

        controller.getSave_btn().setOnAction(actionEvent -> {
            Work newWork = controller.getWork();
            newWork.setContract(getEntity());
            newWork.setNum(work_table.getItems().size() + 1);
            work_table.getItems().add(newWork);

            calcTotal();

            controller.initialize(new Work(getEntity()));
            controller.clearLostFields();
        });

        controller.getCancel_btn().setText("Закрыть");
        controller.getCancel_btn().setOnAction(event -> stage.close());

        controller.initialize(new Work(getEntity()));

        stage.setScene(new Scene(parent));
        stage.setTitle("Работы по договору (добавить/изменить)");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void calcTotal() {
        double total = 0;

        for (Work w : work_table.getItems()) {
            total += w.getTotal();
        }

        proxy.priceProperty().set(total);
    }

    private void deleteWork() {

        Work w = work_table.getSelectionModel().getSelectedItem();
        work_table.getItems().removeAll(w);

        if (w.getId() != 0) {
            workService.delete(w);
        }

        int i = 0;
        for (Work work : work_table.getItems()) {
            work.setNum(++i);
        }

        calcTotal();
    }

    public List<Work> getWorkList() {
        return work_table.getItems();
    }
}
