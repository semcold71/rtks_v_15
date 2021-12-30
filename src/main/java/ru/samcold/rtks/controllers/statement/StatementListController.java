package ru.samcold.rtks.controllers.statement;

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
import ru.samcold.rtks._utils.*;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.ListController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.domain.Statement;
import ru.samcold.rtks.services.statement.StatementService;

import java.sql.Date;

@Component
@FxmlView("/fxml/statement/statementList.fxml")
public class StatementListController implements ListController<Statement> {

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
    private Button wordButton;

    @FXML
    private TableView<Statement> tableView;

    @FXML
    private TableColumn<Statement, Statement> numColumn;

    @FXML
    private TableColumn<Statement, Date> dateColumn;

    @FXML
    private TableColumn<Statement, String> customerColumn;

    @FXML
    private TableColumn<Statement, String> craneColumn;

    @FXML
    private TableColumn<Statement, String> reportNumberColumn;

    @FXML
    private TableColumn<Statement, Date> nextDateColumn;

    @FXML
    private CustomTextField searchTextField;
    //endregion

    private final StatementService statementService;
    private final SetupClearButtonField setupClearButtonField;
    private final TableColumnAutoNumbering<Statement> tableColumnAutoNumbering;
    private final TableColumnWithDate tableColumnWithDate;
    private final WrapTableCell<Statement, String> wrapTableCell;
    private final TableStringFilter tableStringFilter;
    private final AlertManager alertManager;
    private final FxWeaver fxWeaver;
    private final ShowView showView;

    public StatementListController(
            StatementService statementService,
            SetupClearButtonField setupClearButtonField,
            TableColumnAutoNumbering<Statement> tableColumnAutoNumbering,
            TableColumnWithDate tableColumnWithDate,
            WrapTableCell<Statement, String> wrapTableCell,
            TableStringFilter tableStringFilter,
            AlertManager alertManager,
            FxWeaver fxWeaver,
            ShowView showView
    ) {
        this.statementService = statementService;
        this.setupClearButtonField = setupClearButtonField;
        this.tableColumnAutoNumbering = tableColumnAutoNumbering;
        this.tableColumnWithDate = tableColumnWithDate;
        this.wrapTableCell = wrapTableCell;
        this.tableStringFilter = tableStringFilter;
        this.alertManager = alertManager;
        this.fxWeaver = fxWeaver;
        this.showView = showView;
    }

    @Override
    public void initialize() {
        ListController.super.initialize();
        setupClearButtonField.setup(new CustomTextField[]{searchTextField});
    }

    @Override
    public void initTable() {
        // columns
        tableColumnAutoNumbering.create(numColumn);

        tableColumnWithDate.init(dateColumn);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("statementDate"));

        wrapTableCell.wrapCell(customerColumn);
        customerColumn.setCellValueFactory(param -> param.getValue().customerNameProperty());

        wrapTableCell.wrapCell(craneColumn);
        craneColumn.setCellValueFactory(param -> param.getValue().craneNameProperty());

        reportNumberColumn.setCellValueFactory(new PropertyValueFactory<>("reportNumber"));

        tableColumnWithDate.init(nextDateColumn);
        nextDateColumn.setCellValueFactory(new PropertyValueFactory<>("nextDate"));

        // table
        tableView.setPlaceholder(new Label("Нет данных для показа"));

        ObservableList<Statement> statements = FXCollections.observableArrayList();
        statementService.findAll().forEach(statements::add);
        tableView.setItems(statements);

        tableView.getSelectionModel().selectFirst();

        tableStringFilter.setFilter(searchTextField, tableView);
    }

    @Override
    public void initButtons() {

        addButton.setOnAction(actionEvent -> showDetail(new Statement()));

        editButton.setOnAction(actionEvent -> showDetail(tableView.getSelectionModel().getSelectedItem()));

        deleteButton.setOnAction(actionEvent -> {
            Statement statement = tableView.getSelectionModel().getSelectedItem();
            if (alertManager.showDeleteInfo("Удалить " + statement.getReportNumber() + "?")) {
                statementService.delete(statement);
                initTable();
                searchTextField.clear();
            }
        });
    }

    @Override
    public void showDetail(Statement entity) {
        Parent root = fxWeaver.loadView(StatementDetailController.class);
        StatementDetailController controller = fxWeaver.getBean(StatementDetailController.class);
        controller.initialize(entity);

        // AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent();
        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    @Override
    public TableView<Statement> getTable() {
        return tableView;
    }
}
