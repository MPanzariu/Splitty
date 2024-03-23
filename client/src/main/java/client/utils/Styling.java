package client.utils;

import javafx.scene.Node;

public class Styling {
    /**
     * Applies a styling to a node
     * @param node the node to apply the styling to
     * @param style the style to apply
     */
    public static void applyStyling(Node node, String style) {
        if (node != null) {
            if (!node.getStyleClass().contains(style)) {
                node.getStyleClass().add(style);
            }
        }
    }

    /**
     * Changes the styling of a node
     * @param node the node to change the styling of
     * @param oldStyle the old style to remove
     * @param newStyle the new style to add
     */
    public static void changeStyling(Node node, String oldStyle, String newStyle) {
        if (node != null) {
            node.getStyleClass().remove(oldStyle);
            applyStyling(node, newStyle);
        }
    }

}
