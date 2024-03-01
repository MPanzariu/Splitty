package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.util.concurrent.Callable;

public class ExpenseScreenCtrl {
    private final ServerUtils server;
    @FXML
    private Label addEditExpense;
    @FXML
    private Label paidBy;
    @FXML
    private ComboBox<String> choosePayer;
    @FXML
    private Label purpose;
    @FXML
    private TextField expensePurpose;
    @FXML
    private Label amount;
    @FXML
    private TextField sum;
    @FXML
    private ComboBox<String> currency;
    @FXML
    private Label date;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label splitMethod;
    @FXML
    private Label expenseType;
    @FXML
    private Checkbox splitBetweenAllCheckBox;
    @FXML
    private Label splitBetweenAllLabel;
    @FXML
    private Checkbox splitBetweenCustomCheckBox;
    @FXML
    private Label getSplitBetweenCustomLabel;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    @Inject
    public ExpenseScreenCtrl (ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.server = server;
    }


    public void initialize(URL location, ResourceBundle resources) {
        addEditExpense.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.Add"));
        paidBy.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.paid"));
        purpose.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.purpose"));
        amount.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.amount"));
        date.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.date"));
        splitMethod.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.split"));
        splitBetweenAllLabel.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.splitAll"));
        getSplitBetweenCustomLabel.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.splitCustom"));
    }
    public void switchToEventScreen(ActionEvent actionEvent) {
        mainCtrl.switchBackToEventScreen();
    }
}
