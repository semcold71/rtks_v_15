package ru.samcold.rtks.controllers.work;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.domain.Work;
import ru.samcold.rtks.domain.proxy.WorkProxy;
import ru.samcold.rtks._utils.ComboAutoCompletter;
import ru.samcold.rtks._utils.DescriptionBuilder;
import ru.samcold.rtks._utils.InitCraneCombobox;
import ru.samcold.rtks._utils.NumberValidator;

@Component
@FxmlView("/fxml/work/work.fxml")
public class WorkController {

    private Contract contract;
    private WorkProxy workProxy;
    private static final ObservableList<String> UNITS = FXCollections.observableArrayList("шт.", "пог.м.");

    private final NumberValidator numberValidator;
    private final InitCraneCombobox initCraneCombobox;
    private final ComboAutoCompletter comboAutoCompletter;

    public WorkController(
            NumberValidator numberValidator,
            InitCraneCombobox initCraneCombobox,
            ComboAutoCompletter comboAutoCompletter
    ) {
        this.numberValidator = numberValidator;
        this.initCraneCombobox = initCraneCombobox;
        this.comboAutoCompletter = comboAutoCompletter;
    }

    //region FXML
    @FXML
    private ComboBox<Crane> crane_cbo;

    @FXML
    private RadioButton epb_rb;

    @FXML
    private RadioButton td_rb;

    @FXML
    private RadioButton pto_rb;

    @FXML
    private RadioButton kokp_rb;

    @FXML
    private RadioButton ps_rb;

    @FXML
    private RadioButton pskp_rb;

    @FXML
    private TextArea description_ta;

    @FXML
    private TextField price_tf;

    @FXML
    private TextField total_tf;

    @FXML
    private TextField count_tf;

    @FXML
    private ComboBox<String> unit_cbo;

    @FXML
    private Button save_btn;

    @FXML
    private Button cancel_btn;

    //endregion

    @FXML
    public void initialize(Work work) {
        contract = work.getContract();
        workProxy = new WorkProxy(work.getContract());
        unit_cbo.setItems(UNITS);

        DescriptionBuilder builder = new DescriptionBuilder("", "");
        initFields();
        initToggleGroup(builder);
        initCraneCombo(builder);

        Platform.runLater(() -> crane_cbo.requestFocus());
    }

    private void initFields() {
        // work.description
        description_ta.textProperty().bindBidirectional(workProxy.descriptionProperty());

        // work.count
        numberValidator.IntegerValidator(count_tf, 1, 10000);
        count_tf.textProperty().bindBidirectional(workProxy.countProperty(), new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number);
            }

            @Override
            public Number fromString(String s) {
                return Integer.parseInt(s);
            }
        });

        count_tf.textProperty().addListener((observableValue, s, t1) ->
                workProxy.totalProperty().set(workProxy.countProperty().get() * workProxy.priceProperty().get()));

        // work.unit
        unit_cbo.valueProperty().bindBidirectional(workProxy.unitProperty());

        // work.price
        numberValidator.DoubleValidator(price_tf);
        price_tf.textProperty().bindBidirectional(workProxy.priceProperty(), new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number);
            }

            @Override
            public Number fromString(String s) {
                return Double.parseDouble(s);
            }
        });
        price_tf.textProperty().addListener((observableValue, s, t1) ->
                workProxy.totalProperty().set(workProxy.countProperty().get() * workProxy.priceProperty().get()));

        // work.total
        total_tf.textProperty().bindBidirectional(workProxy.totalProperty(), new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number);
            }

            @Override
            public Number fromString(String s) {
                return Double.parseDouble(s);
            }
        });

        // validator
        ValidationSupport vs = new ValidationSupport();

        vs.registerValidator(description_ta, Validator.createEmptyValidator("Not is empty", Severity.ERROR));
        vs.registerValidator(unit_cbo, Validator.createEmptyValidator("Not is empty", Severity.ERROR));
        Validator<String> moneyValidator = (control, value) -> {
            boolean condition = value == null || value.equals("0.0");
            return ValidationResult.fromMessageIf(control, "not a 0", Severity.ERROR, condition);
        };
        vs.registerValidator(price_tf, true, moneyValidator);
        vs.registerValidator(total_tf, true, moneyValidator);

        save_btn.disableProperty().bind(vs.invalidProperty());
    }

    private void initToggleGroup(DescriptionBuilder builder) {

        ToggleGroup toggleGroup = new ToggleGroup();

        epb_rb.setToggleGroup(toggleGroup);
        td_rb.setToggleGroup(toggleGroup);
        pto_rb.setToggleGroup(toggleGroup);
        kokp_rb.setToggleGroup(toggleGroup);
        ps_rb.setToggleGroup(toggleGroup);
        pskp_rb.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            if (t1 == null)
                return;

            if (t1.equals(epb_rb))
                builder.setWork("Экспертиза промышленной безопасности");

            if (t1.equals(td_rb))
                builder.setWork("Техническое диагностирование");

            if (t1.equals(pto_rb))
                builder.setWork("Полное техническое освидетельствование");

            if (t1.equals(kokp_rb))
                builder.setWork("Комплексное обследование кранового пути");

            if (t1.equals(ps_rb))
                builder.setWork("Разработка дубликата паспорта");

            if (t1.equals(ps_rb))
                builder.setWork("Разработка паспорта кранового пути");

            workProxy.descriptionProperty().set(builder.getDescription());
        });
    }

    private void initCraneCombo(DescriptionBuilder builder) {

        ObservableList<Crane> craneObservableList = FXCollections.observableArrayList(contract.getCustomer().getCraneList());

        initCraneCombobox.init(crane_cbo, craneObservableList);

        crane_cbo.getSelectionModel().selectedItemProperty().addListener((observableValue, crane, t1) -> {
            if (crane_cbo.getValue() != null) {
                builder.setCrane(String.valueOf(comboAutoCompletter.getComboBoxValue(crane_cbo)));

                if (!builder.getWork().equals("")) {
                    workProxy.descriptionProperty().set(builder.getDescription());
                }
            }
        });
    }

    // костыль
    public void clearLostFields() {
        crane_cbo.getEditor().clear();
        description_ta.setText("");
    }


    // getters
    public Button getSave_btn() {
        return save_btn;
    }

    public Button getCancel_btn() {
        return cancel_btn;
    }

    public Work getWork() {
        return new Work(workProxy);
    }

}
