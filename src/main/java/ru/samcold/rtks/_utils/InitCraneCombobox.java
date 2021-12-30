package ru.samcold.rtks._utils;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.domain.Crane;

@Component
public class InitCraneCombobox {

    public final ComboAutoCompletter comboAutoCompletter;

    public InitCraneCombobox(ComboAutoCompletter comboAutoCompletter) {
        this.comboAutoCompletter = comboAutoCompletter;
    }

    public void init(ComboBox<Crane> comboBox, ObservableList<Crane> craneList) {

        comboBox.setItems(craneList);

        comboAutoCompletter.autoCompleteComboBoxPlus(comboBox, (typedText, itemToCompare) -> itemToCompare.toString()
                .toLowerCase().contains(typedText.toLowerCase()));

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Crane object) {
                return object != null ? object.toString() : "";
            }

            @Override
            public Crane fromString(String string) {
                return comboBox.getItems().stream().filter(object ->
                        object.toString().equals(string)).findFirst().orElse(null);
            }
        });

    }
}
