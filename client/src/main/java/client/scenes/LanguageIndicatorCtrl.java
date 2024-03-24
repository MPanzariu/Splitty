package client.scenes;

import client.utils.Translation;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.Locale;

public class LanguageIndicatorCtrl {
    ObservableList<Locale> languages = FXCollections.observableArrayList();
    private Translation translation;

    @Inject
    public LanguageIndicatorCtrl(Translation translation) {
        this.translation = translation;
    }

    /**
     * Initialize language indicator with a list of languages.
     */
    public void initializeLanguageIndicator(ComboBox<Locale> languageIndicator) {
        languageIndicator.setItems(languages);
        Callback<ListView<Locale>, ListCell<Locale>> cellFactory = listView -> new ListCell<>() {
            @Override
            public void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item.getLanguage());
                }
            }
        };
        languageIndicator.setButtonCell(cellFactory.call(null));
        languageIndicator.setCellFactory(cellFactory);
    }

    /**
     * Refresh the given language indicator.
     * @param languageIndicator Language Indicator combo box
     */
    public void refresh(ComboBox<Locale> languageIndicator) {
        languages.setAll(translation.getLocale());
        languageIndicator.getSelectionModel().select(translation.getLocale());
    }
}
