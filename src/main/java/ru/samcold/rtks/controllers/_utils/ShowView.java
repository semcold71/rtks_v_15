package ru.samcold.rtks.controllers._utils;

import javafx.animation.*;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import ru.samcold.rtks.controllers.contract.ContractDetailController;
import ru.samcold.rtks.controllers.customer.CustomerDetailController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class ShowView {

    public void show(AnchorPane parentContainer, Parent root, AnimationDirection direction) {

        KeyValue kv;

        parentContainer.getChildren().add(root);

        if (direction == AnimationDirection.HORIZONTAL) {
            root.translateXProperty().set(parentContainer.getWidth());
            kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        } else {
            root.translateYProperty().set(parentContainer.getHeight());
            kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        }

        KeyFrame kf = new KeyFrame(Duration.millis(750), kv);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(actionEvent -> parentContainer.getChildren().remove(0));
        timeline.play();
    }
}
