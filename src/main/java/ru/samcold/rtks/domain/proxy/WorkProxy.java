package ru.samcold.rtks.domain.proxy;

import javafx.beans.property.*;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Work;

public class WorkProxy {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<Contract> contract = new SimpleObjectProperty<>();
    private final IntegerProperty num = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final IntegerProperty count = new SimpleIntegerProperty();
    private final StringProperty unit = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final DoubleProperty total = new SimpleDoubleProperty();

    public WorkProxy() {
        this.countProperty().set(1);
    }

    public WorkProxy(Contract contract) {
        this.contractProperty().set(contract);
        this.countProperty().set(1);
    }

    public WorkProxy(Work work) {
        this.idProperty().set(work.getId());
        this.contractProperty().set(work.getContract());
        this.numProperty().set(work.getNum());
        this.descriptionProperty().set(work.getDescription());
        this.countProperty().set(work.getCount());
        this.unitProperty().set(work.getUnit());
        this.priceProperty().set(work.getPrice());
        this.totalProperty().set(work.getTotal());
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<Contract> contractProperty() {
        return contract;
    }

    public IntegerProperty numProperty() {
        return num;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
