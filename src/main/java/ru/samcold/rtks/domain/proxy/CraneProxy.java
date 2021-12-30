package ru.samcold.rtks.domain.proxy;

import javafx.beans.property.*;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.domain.Customer;

public class CraneProxy {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty zav = new SimpleStringProperty();
    private final StringProperty reg = new SimpleStringProperty();

    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty zavProperty() {
        return zav;
    }

    public StringProperty regProperty() {
        return reg;
    }

    public CraneProxy(Crane crane) {
        idProperty().set(crane.getId());
        customerProperty().set(crane.getCustomer());
        nameProperty().set(crane.getName());
        zavProperty().set(crane.getZav());
        regProperty().set(crane.getReg());
    }
}
