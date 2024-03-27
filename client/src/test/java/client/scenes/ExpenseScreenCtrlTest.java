package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import commons.Expense;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseScreenCtrlTest {

    private TestExpenseScreenCtrl sut;
    private TestServerUtils testServerUtils;
    private TestMainController testMainController;
    @BeforeEach
    public void setup() {
        this.testServerUtils = new TestServerUtils();
        this.testMainController = new TestMainController();
        this.sut = new TestExpenseScreenCtrl(this.testServerUtils, this.testMainController, null);
    }
    

    private Event createMockEvent() {
        return null;
    }

    private class TestExpenseScreenCtrl extends ExpenseScreenCtrl {
        public TextField expensePurpose = new TextField();
        public TextField sum = new TextField();
        public ComboBox<String> choosePayer = new ComboBox<>();
        public DatePicker datePicker = new DatePicker();

        public TestExpenseScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
            super(server, mainCtrl, translation);
        }

        @Override
        public String getTextFieldText(TextField textField) {
            if (textField == expensePurpose) return expensePurpose.getText();
            else if (textField == sum) return sum.getText();
            return null;
        }

        @Override
        public LocalDate getLocalDate(DatePicker datePicker) {
            return datePicker.getValue();
        }

        @Override
        public String getComboBox(ComboBox<String> comboBox) {
            return comboBox.getValue();
        }
    }

    private class TestServerUtils extends ServerUtils {
        public List<String> calls = new LinkedList<>();
        public String validEventId = "123456";
        public long validExpenseId = 123;
        public Expense validExpense = new Expense();
        @Override
        public Expense addExpense(String eventId, Expense expense) {
            calls.add(eventId);
            return expense;
        }

        @Override
        public Expense editExpense(String eventId, long expenseId, Expense expense) {
            if(eventId.equals(validEventId) && expenseId == validExpenseId) {
                return expense;
            }
            throw new jakarta.ws.rs.BadRequestException();
        }
    }

    private class TestMainController extends MainCtrl{
        public List<String> calls = new LinkedList<>();

        public TestMainController() {
            super(null, null);
        }
        /*Override
        public void switchEvents(String eventCode){
            calls.add("join");
        }*/
    }
}