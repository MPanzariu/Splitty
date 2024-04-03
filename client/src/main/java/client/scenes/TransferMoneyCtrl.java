package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TransferMoneyCtrl implements Initializable, SimpleRefreshable {
    @FXML
    private Button transferButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label header;
    @FXML
    private Label toLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private ChoiceBox<String> currency = new ChoiceBox<>();
    private final ServerUtils utils;
    private final Translation translation;
    private final MainCtrl ctrl;

    @Inject
    public TransferMoneyCtrl(Translation translation, ServerUtils utils, MainCtrl ctrl) {
        this.translation = translation;
        this.utils = utils;
        this.ctrl = ctrl;
    }

    /**
     * Initialize the FXML fields.
     * @param url URL of something
     * @param resources Resources used
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        toLabel.textProperty().bind(translation.getStringBinding("TransferMoney.Label.To"));
        fromLabel.textProperty().bind(translation.getStringBinding("TransferMoney.Label.From"));
        header.textProperty().bind(translation.getStringBinding("TransferMoney.title"));
        cancelButton.textProperty().bind(translation.getStringBinding("TransferMoney.Button.Cancel"));
        transferButton.textProperty().bind(translation.getStringBinding("TransferMoney.Button.Confirm"));
        amountLabel.textProperty().bind(translation.getStringBinding("TransferMoney.Label.Amount"));
        currency.setItems(FXCollections.observableArrayList("EUR"));
        currency.getSelectionModel().select(0);
    }

    /**
     * Switch screens when clicking cancel button
     */
    public void cancel() {
        ctrl.switchScreens(EventScreenCtrl.class);
    }

    /**
     * Switch screens when clicking confirm button.
     */
    public void confirm() {
        ctrl.switchScreens(EventScreenCtrl.class);
    }

    /**
     * Update names of changed participants
     * @param event the new Event data to process
     */
    @Override
    public void refresh(Event event) {
        // TODO
    }
}
