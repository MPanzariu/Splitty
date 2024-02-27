package client.utils;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

import java.util.Map;

// Adapted from https://stackoverflow.com/questions/32464974/javafx-change-application-language-on-the-run
public class ObservableResourceFactory {

    private final SimpleMapProperty<String, String> resources =
            new SimpleMapProperty<>(FXCollections.observableHashMap());
    public final SimpleMapProperty<String, String> resourcesProperty() {
        return resources;
    }
    public final void setResources(Map<String, String> map) {
        resourcesProperty().clear();
        resourcesProperty().putAll(map);
    }

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
