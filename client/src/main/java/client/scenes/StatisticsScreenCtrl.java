package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import commons.Expense;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsScreenCtrl implements Initializable, SimpleRefreshable {
    @FXML
    private Label statisticsLabel;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Label expenseSumLabel;
    private Event event;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private final LanguageIndicatorCtrl languageCtrl;

    /**
     *
     * @param server the server to which the client is connected
     * @param mainCtrl the main controller
     * @param translation the class that manages translations
     * @param languageCtrl the LanguageIndicator to use
     */
    @Inject
    public StatisticsScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
                      LanguageIndicatorCtrl languageCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.languageCtrl = languageCtrl;
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statisticsLabel.textProperty()
                .bind(translation.getStringBinding("SS.Label.Statistics"));
        totalCostLabel.textProperty()
                .bind(translation.getStringBinding("SS.Label.TotalCost"));
    }

    @Override
    public void refresh(Event event) {
        this.event = event;
        setTotalSumOfExpenses();
    }

    public void setTotalSumOfExpenses(){
        float sum = 0;
        for(Expense expense : event.getExpenses()){
            sum+=expense.getPriceInCents();
        }
        sum/=100;
        String euro = "\u20ac";
        expenseSumLabel.setText(String.valueOf(sum) + euro);
    }

    public void switchToEventScreen() {
        mainCtrl.switchScreens(EventScreenCtrl.class);
    }
}
