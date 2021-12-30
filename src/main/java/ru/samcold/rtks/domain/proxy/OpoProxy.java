package ru.samcold.rtks.domain.proxy;

import javafx.beans.property.*;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.Opo;

public class OpoProxy {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty dangerClass = new SimpleStringProperty();
    private final StringProperty regNumber = new SimpleStringProperty();
    private final StringProperty note = new SimpleStringProperty();

    private final ObjectProperty<byte[]> opoDoc = new SimpleObjectProperty<>();
    private final ObjectProperty<byte[]> policyDoc = new SimpleObjectProperty<>();

    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty dangerClassProperty() {
        return dangerClass;
    }

    public StringProperty regNumberProperty() {
        return regNumber;
    }

    public StringProperty noteProperty() {
        return note;
    }




    public ObjectProperty<byte[]> opoDocProperty() {
        return opoDoc;
    }

    public ObjectProperty<byte[]> policyDocProperty() {
        return policyDoc;
    }

    public OpoProxy(Opo opo) {
        idProperty().set(opo.getId());
        customerProperty().set(opo.getCustomer());
        nameProperty().set(opo.getName());
        addressProperty().set(opo.getAddress());
        dangerClassProperty().set(opo.getDangerClass());
        regNumberProperty().set(opo.getRegNumber());
        noteProperty().set(opo.getNote());

        opoDocProperty().set(opo.getOpoDoc());
        policyDocProperty().set(opo.getPolicyDoc());
    }
}
