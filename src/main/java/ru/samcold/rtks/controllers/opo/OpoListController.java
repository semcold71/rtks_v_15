package ru.samcold.rtks.controllers.opo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.textfield.CustomTextField;
import org.springframework.stereotype.Component;
import ru.samcold.rtks._utils.AlertManager;
import ru.samcold.rtks._utils.SetupClearButtonField;
import ru.samcold.rtks._utils.TableColumnAutoNumbering;
import ru.samcold.rtks._utils.TableStringFilter;
import ru.samcold.rtks.controllers.MainController;
import ru.samcold.rtks.controllers._utils.AnimationDirection;
import ru.samcold.rtks.controllers._utils.ListController;
import ru.samcold.rtks.controllers._utils.ShowView;
import ru.samcold.rtks.domain.*;
import ru.samcold.rtks.services.opo.OpoService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@FxmlView(value = "/fxml/opo/opoList.fxml")
public class OpoListController implements ListController<Opo> {

    //region FXML
    @FXML
    private CustomTextField searchTextField;

    @FXML
    private TableView<Opo> tableView;

    @FXML
    private TableColumn<Opo, Opo> numColumn;

    @FXML
    private TableColumn<Opo, String> nameColumn;

    @FXML
    private TableColumn<Opo, String> addressColumn;

    @FXML
    private TableColumn<Opo, String> dangerClassColumn;

    @FXML
    private TableColumn<Opo, String> regNumberColumn;

    @FXML
    private TableColumn<Opo, String> customerColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button policyButton;

    @FXML
    private Button cardButton;
    //endregion

    private final OpoService opoService;
    private final SetupClearButtonField setupClearButtonField;
    private final FxWeaver fxWeaver;
    private final TableColumnAutoNumbering<Opo> tableColumnAutoNumbering;
    private final TableStringFilter tableStringFilter;
    private final ShowView showView;
    private final AlertManager alertManager;


    public OpoListController(OpoService opoService,
                             SetupClearButtonField setupClearButtonField,
                             FxWeaver fxWeaver,
                             TableColumnAutoNumbering<Opo> tableColumnAutoNumbering,
                             TableStringFilter tableStringFilter,
                             ShowView showView,
                             AlertManager alertManager
    ) {
        this.opoService = opoService;
        this.setupClearButtonField = setupClearButtonField;
        this.fxWeaver = fxWeaver;
        this.tableColumnAutoNumbering = tableColumnAutoNumbering;
        this.tableStringFilter = tableStringFilter;
        this.showView = showView;
        this.alertManager = alertManager;
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
        dangerClassColumn.setCellValueFactory(new PropertyValueFactory<>("dangerClass"));
        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("regNumber"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        // fill data
        ObservableList<Opo> opos = FXCollections.observableArrayList();
        opoService.findAll().forEach(opos::add);
        tableView.setItems(opos);

        // select first row
        tableView.getSelectionModel().selectFirst();

        // set filter
        tableStringFilter.setFilter(searchTextField, tableView);
    }

    @Override
    public void initButtons() {
        addButton.setOnAction(actionEvent -> showDetail(new Opo()));

        editButton.setOnAction(actionEvent -> showDetail(tableView.getSelectionModel().getSelectedItem()));

        deleteButton.setOnAction(actionEvent -> {
            Opo opo = tableView.getSelectionModel().getSelectedItem();
            if (alertManager.showDeleteInfo("Удалить " + opo.getRegNumber() + "?")) {
                opoService.delete(opo);
                initTable();
                searchTextField.clear();
            }
        });

        cardButton.setOnAction(event -> getPdf(tableView.getSelectionModel().getSelectedItem().getOpoDoc()));

        policyButton.setOnAction(event -> getPdf(tableView.getSelectionModel().getSelectedItem().getPolicyDoc()));
    }

    @Override
    public void showDetail(Opo entity) {
        Parent root = fxWeaver.loadView(OpoDetailController.class);
        OpoDetailController controller = fxWeaver.getBean(OpoDetailController.class);
        controller.initialize(entity);

        showView.show(MainController.parentContainer, root, AnimationDirection.VERTICAL);
    }

    @Override
    public TableView<Opo> getTable() {
        return tableView;
    }

    private void getPdf(byte[] doc) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("выбор файла для загрузки");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Adobe acrobat (.pdf)", "*.pdf"));

        File file = chooser.showSaveDialog(new Stage());

        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(doc);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
