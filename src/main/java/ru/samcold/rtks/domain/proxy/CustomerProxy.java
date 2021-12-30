package ru.samcold.rtks.domain.proxy;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.domain.Customer;

public class CustomerProxy {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty name2 = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty address2 = new SimpleStringProperty();
    private final StringProperty inn = new SimpleStringProperty();
    private final StringProperty kpp = new SimpleStringProperty();
    private final StringProperty ogrn = new SimpleStringProperty();
    private final StringProperty rs = new SimpleStringProperty();
    private final StringProperty bank = new SimpleStringProperty();
    private final StringProperty bik = new SimpleStringProperty();
    private final StringProperty ks = new SimpleStringProperty();
    private final StringProperty boss = new SimpleStringProperty();
    private final StringProperty post = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty fax = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty web = new SimpleStringProperty();
    private final StringProperty note = new SimpleStringProperty();

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty name2Property() {
        return name2;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty address2Property() {
        return address2;
    }

    public StringProperty innProperty() {
        return inn;
    }

    public StringProperty kppProperty() {
        return kpp;
    }

    public StringProperty ogrnProperty() {
        return ogrn;
    }

    public StringProperty rsProperty() {
        return rs;
    }

    public StringProperty bankProperty() {
        return bank;
    }

    public StringProperty bikProperty() {
        return bik;
    }

    public StringProperty ksProperty() {
        return ks;
    }

    public StringProperty bossProperty() {
        return boss;
    }

    public StringProperty postProperty() {
        return post;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty faxProperty() {
        return fax;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty webProperty() {
        return web;
    }

    public StringProperty noteProperty() {
        return note;
    }

    public CustomerProxy(Customer customer) {
        idProperty().set(customer.getId());
        nameProperty().set(customer.getName());
        name2Property().set(customer.getName2());
        addressProperty().set(customer.getAddress());
        address2Property().set(customer.getAddress2());
        innProperty().set(customer.getInn());
        kppProperty().set(customer.getKpp());
        ogrnProperty().set(customer.getOgrn());
        rsProperty().set(customer.getRs());
        bankProperty().set(customer.getBank());
        bikProperty().set(customer.getBik());
        ksProperty().set(customer.getKs());
        bossProperty().set(customer.getBoss());
        postProperty().set(customer.getPost());
        phoneProperty().set(customer.getPhone());
        faxProperty().set(customer.getFax());
        emailProperty().set(customer.getEmail());
        webProperty().set(customer.getWeb());
        noteProperty().set(customer.getNote());
    }
}
