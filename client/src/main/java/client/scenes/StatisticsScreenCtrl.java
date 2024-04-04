package client.scenes;

import client.utils.ImageUtils;
import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import commons.Expense;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.*;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsScreenCtrl implements Initializable, SimpleRefreshable {
    @FXML
    private Label statisticsLabel;
    @FXML
    private Label totalCostLabel;
    @FXML
    private Label expenseSumLabel;
    @FXML
    private PieChart tagPieChart;
    @FXML
    private Button goBackButton;
    private Event event;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private final LanguageIndicatorCtrl languageCtrl;
    private final ImageUtils imageUtils;

    /**
     *
     * @param server the server to which the client is connected
     * @param mainCtrl the main controller
     * @param translation the class that manages translations
     * @param languageCtrl the LanguageIndicator to use
     */
    @Inject
    public StatisticsScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
                                LanguageIndicatorCtrl languageCtrl, ImageUtils imageUtils){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.languageCtrl = languageCtrl;
        this.imageUtils = imageUtils;
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
        addGeneratedImages();
    }

    /**
     * sets the new total sum of expenses
     * sets the pie chart of the tags
     * @param event the new Event data to process
     */
    @Override
    public void refresh(Event event) {
        this.event = event;
        setTotalSumOfExpenses();
        setPieChart();
    }

    /**
     * it sets the label holding the cost of the entire event with the sum of expenses inside the event
     */
    public void setTotalSumOfExpenses(){
        float sum = 0;
        for(Expense expense : event.getExpenses()){
            sum+=expense.getPriceInCents();
        }
        sum/=100;
        String euro = "\u20ac";
        expenseSumLabel.setText(String.valueOf(sum) + euro);
    }

    /**
     * set up the pie chart with the colors of the tags, their contribution to the pie Chart and
     * also has a way of showing the name of each tag in the piechart by using a line
     */
    private void setPieChart(){
        Map<String, Integer> tagExpenseMap = new HashMap<>();
        Map<String, String> tagColorMap = new HashMap<>();
        Set<Expense> expenses = event.getExpenses();
        for (Expense expense : expenses) {
            String tagName = expense.getExpenseTag().getTagName();
            int expensePrice = expense.getPriceInCents();
            String colorCode = expense.getExpenseTag().getColorCode();
            tagExpenseMap.merge(tagName, expensePrice, Integer::sum);
            tagColorMap.putIfAbsent(tagName, colorCode);
        }
        tagPieChart.getData().clear();
        tagPieChart.layout();
        for (Map.Entry<String, Integer> entry : tagExpenseMap.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            tagPieChart.getData().add(data);
        }
        tagPieChart.getData().forEach(data -> {
            String color = tagColorMap.get(data.getName());
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + color + ";");
            } else {
                data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        newValue.setStyle("-fx-pie-color: " + color + ";");
                    }
                });
            }
        });
        tagPieChart.setLegendVisible(false);
    }

    /**
     * add the image to the go back button
     */
    private void addGeneratedImages() {
        ImageView goBackImage = imageUtils.generateImageView("goBack.png", 15);
        goBackButton.setGraphic(goBackImage);
    }

    /**
     * goes back to the event screen when pressing the go back button
     */
    public void switchToEventScreen() {
        mainCtrl.switchScreens(EventScreenCtrl.class);
    }
}
