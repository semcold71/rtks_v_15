package ru.samcold.rtks._utils;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SetupClearButtonField {

    public void setup(CustomTextField[] fields) {

        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            for (CustomTextField field : fields) {
                m.invoke(null, field, field.rightProperty());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
