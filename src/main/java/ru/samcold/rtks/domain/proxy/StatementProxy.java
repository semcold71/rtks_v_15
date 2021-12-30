package ru.samcold.rtks.domain.proxy;

import javafx.beans.property.*;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.domain.Statement;

import java.time.LocalDate;

public class StatementProxy {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Crane> crane = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> statementDate = new SimpleObjectProperty<>();
    private final StringProperty statementNumber = new SimpleStringProperty();
    private final StringProperty cranePurpose = new SimpleStringProperty();
    private final StringProperty reportNumber = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> reportSignDate = new SimpleObjectProperty<>();
    private final StringProperty reportConclusion = new SimpleStringProperty();
    private final StringProperty opoName = new SimpleStringProperty();
    private final StringProperty opoAddress = new SimpleStringProperty();
    private final StringProperty opoDangerClass = new SimpleStringProperty();
    private final StringProperty opoRegNumber = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> nextDate = new SimpleObjectProperty<>();

    //region public properties
    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Crane> craneProperty() {
        return crane;
    }

    public ObjectProperty<LocalDate> statementDateProperty() {
        return statementDate;
    }

    public StringProperty statementNumberProperty() {
        return statementNumber;
    }

    public StringProperty cranePurposeProperty() {
        return cranePurpose;
    }

    public StringProperty reportNumberProperty() {
        return reportNumber;
    }

    public ObjectProperty<LocalDate> reportSignDateProperty() {
        return reportSignDate;
    }

    public StringProperty reportConclusionProperty() {
        return reportConclusion;
    }

    public StringProperty opoNameProperty() {
        return opoName;
    }

    public StringProperty opoAddressProperty() {
        return opoAddress;
    }

    public StringProperty opoDangerClassProperty() {
        return opoDangerClass;
    }

    public StringProperty opoRegNumberProperty() {
        return opoRegNumber;
    }

    public ObjectProperty<LocalDate> nextDateProperty() {
        return nextDate;
    }

    //endregion


    public StatementProxy(Statement stat) {
        idProperty().set(stat.getId());
        craneProperty().set(stat.getCrane());

        if (stat.getStatementDate() != null)
            statementDateProperty().set(stat.getStatementDate().toLocalDate());

        statementNumberProperty().set(stat.getStatementNumber());
        cranePurposeProperty().set(stat.getCranePurpose());
        reportNumberProperty().set(stat.getReportNumber());

        if (stat.getReportSignDate() != null)
            reportSignDateProperty().set(stat.getReportSignDate().toLocalDate());

        reportConclusionProperty().set(stat.getReportConclusion());
        opoNameProperty().set(stat.getOpoName());
        opoAddressProperty().set(stat.getOpoAddress());
        opoDangerClassProperty().set(stat.getOpoDangerClass());
        opoRegNumberProperty().set(stat.getOpoRegNumber());

        if (stat.getNextDate() != null)
            nextDateProperty().set(stat.getNextDate().toLocalDate());
    }
}
