package ru.samcold.rtks.controllers.contract;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.textfield.CustomTextField;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.controllers._utils.ListController;
import ru.samcold.rtks.controllers.print.PrintController;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.services.contract.ContractService;
import ru.samcold.rtks._utils.*;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

@Component
@FxmlView("/fxml/contract/contractList.fxml")
public class ContractListController implements ListController<Contract> {

    private final ContractService contractService;
    private final SetupClearButtonField setupClearButtonField;
    private final TableColumnAutoNumbering<Contract> tableColumnAutoNumbering;
    private final TableColumnWithDate tableColumnWithDate;
    private final TableDateFilter tableDateFilter;
    private final FxWeaver fxWeaver;
    private final AlertManager alertManager;
    private final ShowView showView;

    public ContractListController(
            ContractService contractService,
            SetupClearButtonField setupClearButtonField,
            TableColumnAutoNumbering<Contract> tableColumnAutoNumbering,
            TableColumnWithDate tableColumnWithDate,
            TableDateFilter tableDateFilter,
            FxWeaver fxWeaver,
            AlertManager alertManager,
            ShowView showView
    ) {
        this.contractService = contractService;
        this.setupClearButtonField = setupClearButtonField;
        this.tableColumnAutoNumbering = tableColumnAutoNumbering;
        this.tableColumnWithDate = tableColumnWithDate;
        this.tableDateFilter = tableDateFilter;
        this.fxWeaver = fxWeaver;
        this.alertManager = alertManager;
        this.showView = showView;
    }

    //region FXML
    @FXML
    AnchorPane anchorRoot;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button wordButton;

    @FXML
    private TableView<Contract> tableView;

    @FXML
    private TableColumn<Contract, Contract> numColumn;

    @FXML
    private TableColumn<Contract, String> numberColumn;

    @FXML
    private TableColumn<Contract, Date> dateColumn;

    @FXML
    private TableColumn<Contract, String> customerColumn;

    @FXML
    private TableColumn<Contract, String> priceColumn;

    @FXML
    private CustomTextField searchTextField;
    //endregion

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

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        tableColumnWithDate.init(dateColumn);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(new TableColumnFormatter<>(new DecimalFormat("#,#00.00")));

        // fill data
        ObservableList<Contract> contracts = FXCollections.observableArrayList();
        contractService.findAll().forEach(contracts::add);
        contracts.sort(Comparator.comparing(Contract::getDate).reversed());
        tableView.setItems(contracts);

        // select first row
        tableView.getSelectionModel().selectFirst();

        // set filter
        tableDateFilter.setFilter(searchTextField, tableView);
    }

    @Override
    public void initButtons() {

        addButton.setOnAction(actionEvent -> showDetail(new Contract()));

        editButton.setOnAction(actionEvent -> showDetail(tableView.getSelectionModel().getSelectedItem()));

        deleteButton.setOnAction(actionEvent -> {
            Contract contract = tableView.getSelectionModel().getSelectedItem();

            if (alertManager.showDeleteInfo(
                    "Удалить договор № "
                            + contract.getNumber() + " от "
                            + new SimpleDateFormat("dd.MM.yyyy").format(contract.getDate())
                            + "?")) {
                contractService.delete(contract);

                initTable();
                searchTextField.clear();
            }
        });

        wordButton.setOnAction(actionEvent -> showPrintWindow());
    }

    @Override
    public TableView<Contract> getTable() {
        return tableView;
    }

    public void showDetail(Contract entity) {
        Parent root = fxWeaver.loadView(ContractDetailController.class);
        ContractDetailController controller = fxWeaver.getBean(ContractDetailController.class);
        controller.initialize(entity);
        controller.isCustomerCalls = false;

        // AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    private void showPrintWindow() {
        Stage stage = new Stage();
        Parent parent = fxWeaver.loadView(PrintController.class);
        PrintController controller = fxWeaver.getBean(PrintController.class);
        controller.initialize(tableView.getSelectionModel().getSelectedItem());

        stage.setScene(new Scene(parent));
        stage.setTitle("Сохранение в Word");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        stage.showAndWait();
    }
}
