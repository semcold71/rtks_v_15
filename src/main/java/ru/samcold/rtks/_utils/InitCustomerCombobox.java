package ru.samcold.rtks._utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.services.customer.CustomerService;

@Component
public class InitCustomerCombobox {

    private final CustomerService customerService;
    private final ComboAutoCompletter comboAutoCompletter;

    public InitCustomerCombobox(CustomerService customerService, ComboAutoCompletter comboAutoCompletter) {
        this.customerService = customerService;
        this.comboAutoCompletter = comboAutoCompletter;
    }

    public void init(ComboBox<Customer> comboBox) {

        ObservableList<Customer> customers = FXCollections.observableArrayList();
        customerService.findAll().forEach(customers::add);
        comboBox.setItems(customers);

        comboAutoCompletter.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toString()
                .toLowerCase().contains(typedText.toLowerCase()));

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Customer fromString(String string) {
                return comboBox.getItems().stream().filter(object ->
                        object.toString().equals(string)).findFirst().orElse(null);
            }
        });
    }
}
