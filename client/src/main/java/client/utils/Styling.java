package client.utils;

import javafx.scene.Node;

public class Styling {
    /**
     * Adds the success styling to a node
     * @param node the node to add the styling to
     */
    public static void addErrorStyling(Node node) {
        if (node != null) {
            node.getStyleClass().remove("successText");
            if (!node.getStyleClass().contains("errorText")) {
                node.getStyleClass().add("errorText");
            }
        }
    }
    /**
     * Adds the error styling to a node
     * @param node the node to add the styling to
     */
    public static void addSuccessStyling(Node node) {
        if (node != null) {
            node.getStyleClass().remove("errorText");
            if (!node.getStyleClass().contains("successText")) {
                node.getStyleClass().add("successText");
            }
        }
    }

    /**
     * Applies positive button styling to a node
     */
    public static void applyPositiveButtonStyling(Node node) {
        if (node != null) {
            if (!node.getStyleClass().contains("positiveButton")) {
                node.getStyleClass().add("positiveButton");
            }
        }
    }
}
