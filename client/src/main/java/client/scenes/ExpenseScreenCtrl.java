package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class ExpenseScreenCtrl {
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
    private Checkbox splitBetweenAll;
    @FXML
    private Checkbox splitBetweenCustom;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;

}
