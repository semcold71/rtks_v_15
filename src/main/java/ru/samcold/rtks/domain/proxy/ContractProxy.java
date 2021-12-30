package ru.samcold.rtks.domain.proxy;

import javafx.beans.property.*;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Customer;

import java.time.LocalDate;

public class ContractProxy {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private final StringProperty number = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty prepay = new SimpleIntegerProperty();
    private final StringProperty start = new SimpleStringProperty();
    private final StringProperty finish = new SimpleStringProperty();
    private final StringProperty note = new SimpleStringProperty();

    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Customer> customerProperty() {
        return customer;
    }

    public StringProperty numberProperty() {
        return number;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty prepayProperty() {
        return prepay;
    }

    public StringProperty startProperty() {
        return start;
    }

    public StringProperty finishProperty() {
        return finish;
    }

    public StringProperty noteProperty() {
        return note;
    }

    public ContractProxy(Contract contract) {
        this.idProperty().set(contract.getId());
        this.customerProperty().set(contract.getCustomer());
        this.numberProperty().set(contract.getNumber());

        if (contract.getDate() == null){
            this.dateProperty().set(LocalDate.now());
        } else {
            this.dateProperty().set(contract.getDate().toLocalDate());
        }

        this.priceProperty().set(contract.getPrice());
        this.prepayProperty().set(contract.getPrepay());
        this.startProperty().set(contract.getStart());
        this.finishProperty().set(contract.getFinish());
        this.noteProperty().set(contract.getNote());
    }
}
