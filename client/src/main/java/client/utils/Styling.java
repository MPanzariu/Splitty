package client.utils;

import javafx.scene.control.Label;

public class Styling {
    /**
     * Adds the success styling to a label
     */
    public static void addErrorStyling(Label label) {
        if (label != null) {
            label.getStyleClass().remove("successText");
            if (!label.getStyleClass().contains("errorText")) {
                label.getStyleClass().add("errorText");
            }
        }
    }
    /**
     * Adds the error styling to a label
     */
    public static void addSuccessStyling(Label label) {
        if (label != null) {
            label.getStyleClass().remove("errorText");
            if (!label.getStyleClass().contains("successText")) {
                label.getStyleClass().add("successText");
            }
        }
    }
}
