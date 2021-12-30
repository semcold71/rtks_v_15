package ru.samcold.rtks.controllers.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.textfield.CustomTextField;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.controllers._utils.ListController;
import ru.samcold.rtks.controllers.contract.ContractDetailController;
import ru.samcold.rtks.controllers.crane.CraneDetailController;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.services.customer.CustomerService;
import ru.samcold.rtks._utils.*;

@Component
@FxmlView("/fxml/customer/customerList.fxml")
public class CustomerListController implements ListController<Customer> {

    //region FXML
//    @FXML
//    private AnchorPane anchorRoot;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addCraneButton;

    @FXML
    private Button addContractButton;

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, Customer> numColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private CustomTextField searchTextField;
    //endregion

    private final FxWeaver fxWeaver;
    private final CustomerService customerService;
    private final AlertManager alertManager;
    private final SetupClearButtonField setupClearButtonField;
    private final TableColumnAutoNumbering<Customer> tableColumnAutoNumbering;
    private final TableStringFilter tableStringFilter;
    private final ShowView showView;

    public CustomerListController(
            FxWeaver fxWeaver,
            CustomerService customerService,
            AlertManager alertManager,
            SetupClearButtonField setupClearButtonField,
            TableColumnAutoNumbering<Customer> tableColumnAutoNumbering,
            TableStringFilter tableStringFilter,
            ShowView showView
    ) {
        this.fxWeaver = fxWeaver;
        this.customerService = customerService;
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
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // fill data
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        customerService.findAll().forEach(customers::add);
        tableView.setItems(customers);

        // select first row
        tableView.getSelectionModel().selectFirst();

        // set filter
        tableStringFilter.setFilter(searchTextField, tableView);
    }

    @Override
    public void initButtons() {
        addButton.setOnAction(actionEvent -> showDetail(new Customer()));
        editButton.setOnAction(actionEvent -> showDetail(tableView.getSelectionModel().getSelectedItem()));
        deleteButton.setOnAction(actionEvent -> {
            Customer customer = tableView.getSelectionModel().getSelectedItem();
            if (alertManager.showDeleteInfo("Удалить " + customer.getName() + "?")) {
                customerService.delete(customer);
                initTable();
                searchTextField.clear();
            }
        });

        addCraneButton.setOnAction(event -> showCraneDetail());
        addContractButton.setOnAction(event -> showContractDetail());
    }

    @Override
    public void showDetail(Customer entity) {
        Parent root = fxWeaver.loadView(CustomerDetailController.class);
        CustomerDetailController controller = fxWeaver.getBean(CustomerDetailController.class);
        controller.initialize(entity);

        // получает родительский контейнер, если это не главный контейнер приложения
        // anchorRoot - главный контейнер данного view
        // -- AnchorPane parentContainer = (AnchorPane) anchorRoot.getParent(); --

        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    private void showCraneDetail() {
        Parent root = fxWeaver.loadView(CraneDetailController.class);
        CraneDetailController controller = fxWeaver.getBean(CraneDetailController.class);
        Crane crane = Crane.builder().customer(tableView.getSelectionModel().getSelectedItem()).build();
        controller.initialize(crane);
        controller.isCustomerCalls = true;
        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    private void showContractDetail() {
        Parent root = fxWeaver.loadView(ContractDetailController.class);
        ContractDetailController controller = fxWeaver.getBean(ContractDetailController.class);
        Contract contract = Contract.builder().customer(tableView.getSelectionModel().getSelectedItem()).build();
        controller.initialize(contract);
        controller.isCustomerCalls = true;
        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    @Override
    public TableView<Customer> getTable() {
        return tableView;
    }
}
