package ru.samcold.rtks.controllers.crane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.textfield.CustomTextField;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.controllers._utils.ListController;
import ru.samcold.rtks.controllers.customer.CustomerDetailController;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.services.crane.CraneService;
import ru.samcold.rtks._utils.AlertManager;
import ru.samcold.rtks._utils.SetupClearButtonField;
import ru.samcold.rtks._utils.TableColumnAutoNumbering;
import ru.samcold.rtks._utils.TableStringFilter;

@Component
@FxmlView("/fxml/crane/craneList.fxml")
public class CraneListController implements ListController<Crane> {

    //region FXML
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button statement_btn;

    @FXML
    private TableView<Crane> tableView;

    @FXML
    private TableColumn<Crane, Crane> numColumn;

    @FXML
    private TableColumn<Crane, String> nameColumn;

    @FXML
    private TableColumn<Crane, String> zavColumn;

    @FXML
    private TableColumn<Crane, String> regColumn;

    @FXML
    private TableColumn<Crane, String> customerColumn;

    @FXML
    private CustomTextField searchTextField;
    //endregion

    private final FxWeaver fxWeaver;
    private final CraneService craneService;
    private final AlertManager alertManager;
    private final SetupClearButtonField setupClearButtonField;
    private final TableColumnAutoNumbering<Crane> tableColumnAutoNumbering;
    private final TableStringFilter tableStringFilter;
    private final ShowView showView;

    public CraneListController(
            FxWeaver fxWeaver,
            CraneService craneService,
            AlertManager alertManager,
            SetupClearButtonField setupClearButtonField,
            TableColumnAutoNumbering<Crane> tableColumnAutoNumbering,
            TableStringFilter tableStringFilter,
            ShowView showView
    ) {
        this.fxWeaver = fxWeaver;
        this.craneService = craneService;
        this.alertManager = alertManager;
        this.setupClearButtonField = setupClearButtonField;
        this.tableColumnAutoNumbering = tableColumnAutoNumbering;
        this.tableStringFilter = tableStringFilter;
        this.showView = showView;
    }

    @Override
    public void initialize() {
        ListController.super.initialize();
        setupClearButtonField.setup(new CustomTextField[]{searchTextField});
    }

    @Override
    public void initTable() {
        tableView.setPlaceholder(new Label("Нет данных для показа"));

        //init columns
        tableColumnAutoNumbering.create(numColumn);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        zavColumn.setCellValueFactory(new PropertyValueFactory<>("zav"));
        regColumn.setCellValueFactory(new PropertyValueFactory<>("reg"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        // fill data
        ObservableList<Crane> cranes = FXCollections.observableArrayList();
        craneService.findAll().forEach(cranes::add);
        tableView.setItems(cranes);

        // select first row
        tableView.getSelectionModel().selectFirst();

        // set filter
        tableStringFilter.setFilter(searchTextField, tableView);
    }

    @Override
    public void initButtons() {
        addButton.setOnAction(actionEvent -> showDetail(new Crane()));
        editButton.setOnAction(actionEvent -> showDetail(tableView.getSelectionModel().getSelectedItem()));
        deleteButton.setOnAction(actionEvent -> {
            Crane crane = tableView.getSelectionModel().getSelectedItem();
            if (alertManager.showDeleteInfo("Удалить " + crane.getName() + "?")) {
                craneService.delete(crane);
                initTable();
                searchTextField.clear();
            }
        });
    }

    @Override
    public void showDetail(Crane entity) {
        Parent root = fxWeaver.loadView(CraneDetailController.class);
        CraneDetailController controller = fxWeaver.getBean(CraneDetailController.class);
        controller.initialize(entity);
        controller.isCustomerCalls = false;

        // AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    @Override
    public TableView<Crane> getTable() {
        return tableView;
    }
}
