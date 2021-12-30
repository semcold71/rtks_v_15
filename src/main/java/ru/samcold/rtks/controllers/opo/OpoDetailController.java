package ru.samcold.rtks.controllers.opo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.InitCustomerCombobox;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.DetailController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.Opo;
import ru.samcold.rtks.domain.proxy.OpoProxy;
import ru.samcold.rtks.services.opo.OpoService;

import java.io.*;
import java.util.Objects;

@Component
@FxmlView(value = "/fxml/opo/opoDetail.fxml")
public class OpoDetailController implements DetailController<Opo> {

    //region FXML
    @FXML
    private ComboBox<Customer> customerCombo;

    @FXML
    private AnchorPane anchorRoot;

    @FXML
    private TextField name_tf;

    @FXML
    private TextField address_tf;

    @FXML
    private TextField dangerClass_tf;

    @FXML
    private TextField regNumber_tf;

    @FXML
    private TextArea note_ta;

    @FXML
    private Button save_btn;

    @FXML
    private Button cancel_btn;

    @FXML
    private Label opoDocLabel;

    @FXML
    private Button opoDocButton;

    @FXML
    private Label policyDocLabel;

    @FXML
    private Button policyDocButton;
    //endregion

    private OpoProxy proxy;
    private int saveMode;
    private final OpoService opoService;
    private final FxWeaver fxWeaver;
    private final ShowView showView;
    private final InitCustomerCombobox initCustomerCombobox;

    public OpoDetailController(
            OpoService opoService,
            FxWeaver fxWeaver,
            ShowView showView,
            InitCustomerCombobox initCustomerCombobox
    ) {
        this.opoService = opoService;
        this.fxWeaver = fxWeaver;
        this.showView = showView;
        this.initCustomerCombobox = initCustomerCombobox;
    }

    private final BooleanProperty isLoadedOpo = new SimpleBooleanProperty();
    private final BooleanProperty isLoadedPolicy = new SimpleBooleanProperty();

    @Override
    public void initialize(Opo entity) {

        isLoaded(isLoadedOpo, opoDocButton, opoDocLabel);
        isLoadedOpo.set(entity.getOpoDoc() != null);

        isLoaded(isLoadedPolicy, policyDocButton, policyDocLabel);
        isLoadedPolicy.set(entity.getPolicyDoc() != null);

        proxy = new OpoProxy(entity);
        proxy.opoDocProperty().addListener((observableValue, bytes, t1) -> isLoadedOpo.set(t1 != null));
        proxy.policyDocProperty().addListener((observableValue, bytes, t1) -> isLoadedPolicy.set(t1 != null));

        saveMode = entity.getId();
        initCustomerCombobox.init(customerCombo);
        DetailController.super.initialize(entity);

        if (saveMode > 0)
            customerCombo.setDisable(true);
    }

    @Override
    public void initFields() {
        customerCombo.valueProperty().bindBidirectional(proxy.customerProperty());
        name_tf.textProperty().bindBidirectional(proxy.nameProperty());
        address_tf.textProperty().bindBidirectional(proxy.addressProperty());
        dangerClass_tf.textProperty().bindBidirectional(proxy.dangerClassProperty());
        regNumber_tf.textProperty().bindBidirectional(proxy.regNumberProperty());
        note_ta.textProperty().bindBidirectional(proxy.noteProperty());
    }

    @Override
    public void initButtons() {

        initGeneralButtons(save_btn, cancel_btn);

        opoDocButton.setOnAction(event -> {
            if (opoDocButton.getText().equals("Загрузить")) {
                loadPdf("opo");
            } else {
                proxy.opoDocProperty().set(null);
            }
        });

        policyDocButton.setOnAction(event -> {
            if (policyDocButton.getText().equals("Загрузить")) {
                loadPdf("policy");
            } else {
                proxy.policyDocProperty().set(null);
            }
        });
    }

    @Override
    public void save() {
        cancel_btn.setText("<< Назад");

        Opo savedOpo = opoService.save(getEntity());

        if (saveMode == 0) {
            exit(savedOpo);
        } else {
            initialize(savedOpo);
            cancel_btn.setOnAction(actionEvent -> exit(savedOpo));
        }
    }

    @Override
    public void exit(Opo entity) {
        Parent root = fxWeaver.loadView(OpoListController.class);
        OpoListController controller = fxWeaver.getBean(OpoListController.class);

        if (entity != null) {
            for (Opo opo : controller.getTable().getItems()) {
                if (Objects.equals(opo.getId(), entity.getId())) {
                    controller.getTable().getSelectionModel().select(opo);
                }
            }
            controller.getTable().scrollTo(controller.getTable().getSelectionModel().getSelectedItem());
        }

        AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(parentContainer, root, AnimationDirection.HORIZONTAL);
    }

    @Override
    public Opo getEntity() {
        return new Opo(proxy);
    }

    private void loadPdf(String str) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("выбор файла для загрузки");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Adobe acrobat (.pdf)", "*.pdf"));

        File file = chooser.showOpenDialog(new Stage());

        if (file != null) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                if (Objects.equals(str, "opo")) {
                    proxy.opoDocProperty().set(IOUtils.toByteArray(inputStream));
                } else if (Objects.equals(str, "policy")) {
                    proxy.policyDocProperty().set(IOUtils.toByteArray(inputStream));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void isLoaded(BooleanProperty booleanProperty, Button button, Label label) {
        booleanProperty.addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                button.setText("Удалить");
                label.setText("(загружено)");
                label.setStyle("-fx-text-fill: #5CBB57;");
            } else {
                button.setText("Загрузить");
                label.setText("(не загружено)");
                label.setStyle("-fx-text-fill: yellow;");
            }
        });
    }
}
