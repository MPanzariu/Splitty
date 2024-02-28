package client.utils;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import java.util.Map;

// Adapted from https://stackoverflow.com/questions/32464974/javafx-change-application-language-on-the-run
public class ObservableResourceFactory {

    private final SimpleMapProperty<String, String> resources =
            new SimpleMapProperty<>(FXCollections.observableHashMap());

    /***
     * Returns the Observable SMP, used for binding
     * @return the SMP holding all resources
     */
    public final SimpleMapProperty<String, String> resourcesProperty() {
        return resources;
    }

    /***
     * Live-replaces the language resources used with the provided Map
     * @param map - the Map containing key-value pairs of localized strings
     */
    public final void setResources(Map<String, String> map) {
        resourcesProperty().clear();
        resourcesProperty().putAll(map);
    }

    /***
     * Provides a binding for the translated value corresponding to a text key
     * @param key - the text key (e.g. "startup.label.create")
     * @return - a live-updating binding for the translated value
     */
    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            { bind(resourcesProperty()); }
            @Override
            public String computeValue() {
                return resourcesProperty().get(key);
            }
        };
    }
}
