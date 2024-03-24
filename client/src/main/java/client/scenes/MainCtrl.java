package client.scenes;

import client.utils.AppStateManager;
import client.utils.Translation;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Locale;

public class MainCtrl {

    private Stage primaryStage;
    private Scene overview;
    private StartupScreenCtrl startupScreenCtrl;
    private EventScreenCtrl eventScreenCtrl;
    private ExpenseScreenCtrl expenseScreenCtrl;
    private EditTitleCtrl editTitleCtrl;
    private Scene startupScene;
    private Scene add;
    private Scene eventScene;
    private Scene expenseScene;

    private Scene participantScene;
    private ParticipantScreenCtrl participantScreenCtrl;
    private Scene participantListScene;
    private Scene editTitleScene;
    private Scene managementOvervirewPasswordScene;
    private ParticipantListScreenCtrl participantListScreenCtrl;
    private ManagementOverviewPasswordCtrl managementOverviewPasswordCtrl;
    private Scene managementOverviewScreenScene;
    private ManagementOverviewScreenCtrl managementOverviewScreenCtrl;
    private SettleDebtsScreenCtrl settleDebtsScreenCtrl;
    private Scene settleDebtsScene;
    private DeleteEventsScreenCtrl deleteEventsScreenCtrl;
    private Scene deleteEventsScene;
    private final Translation translation;
    @Inject
    @Named("client.language")
    private String language;
    private final AppStateManager manager;
    private String eventCode;

    @Inject
    public MainCtrl(Translation translation, AppStateManager manager) {
        this.translation = translation;
        this.manager = manager;
        this.eventCode = null;
    }

    public void initialize(Stage primaryStage, Pair<StartupScreenCtrl, Parent> overview,
                           Pair<EventScreenCtrl, Parent> eventUI,
                           Pair<ExpenseScreenCtrl, Parent> expenseUI,
                           Pair<ParticipantScreenCtrl, Parent> participantUI,
                           Pair<EditTitleCtrl, Parent> editTitlePair,
                           Pair<ManagementOverviewPasswordCtrl, Parent> managementOverviewPasswordUI,
                           Pair<ManagementOverviewScreenCtrl, Parent> managementOverviewScreenUI,
                           Pair<SettleDebtsScreenCtrl, Parent> settleDebtsUI,
                           Pair<DeleteEventsScreenCtrl, Parent> deleteEventsScreenUI,
                           Pair<ParticipantListScreenCtrl, Parent> participantListUI){


        translation.changeLanguage(Locale.forLanguageTag(language));
        this.primaryStage = primaryStage;
        this.startupScreenCtrl = overview.getKey();
        this.startupScene = new Scene(overview.getValue());
        this.eventScene = new Scene(eventUI.getValue());
        this.eventScreenCtrl = eventUI.getKey();
        this.expenseScene = new Scene(expenseUI.getValue());
        this.expenseScreenCtrl = expenseUI.getKey();
        this.participantListScene = new Scene(participantListUI.getValue());
        this.participantListScreenCtrl = participantListUI.getKey();
        this.participantScene = new Scene(participantUI.getValue());
        this.participantScreenCtrl = participantUI.getKey();

        this.editTitleCtrl = editTitlePair.getKey();
        this.editTitleScene = new Scene(editTitlePair.getValue());
        showMainScreen();
        this.managementOvervirewPasswordScene = new Scene(managementOverviewPasswordUI.getValue());
        this.managementOverviewScreenScene = new Scene(managementOverviewScreenUI.getValue());
        this.managementOverviewScreenCtrl = managementOverviewScreenUI.getKey();

        this.settleDebtsScreenCtrl = settleDebtsUI.getKey();
        this.settleDebtsScene = new Scene(settleDebtsUI.getValue());
        this.deleteEventsScene = new Scene(deleteEventsScreenUI.getValue());
        this.deleteEventsScreenCtrl = deleteEventsScreenUI.getKey();
        //initialize stylesheets
        this.startupScene.getStylesheets().add("stylesheets/main.css");
        this.managementOvervirewPasswordScene.getStylesheets().add("stylesheets/main.css");

        HashMap<Class<?>, SimpleRefreshable> controllerMap = new HashMap<>();
        controllerMap.put(EventScreenCtrl.class, eventScreenCtrl);
        controllerMap.put(ExpenseScreenCtrl.class, expenseScreenCtrl);
        controllerMap.put(EditTitleCtrl.class, editTitleCtrl);
        controllerMap.put(ParticipantScreenCtrl.class, participantScreenCtrl);
        controllerMap.put(SettleDebtsScreenCtrl.class, settleDebtsScreenCtrl);
        manager.setControllerMap(controllerMap);

        primaryStage.show();
    }

    public void switchScreens(Class<?> target){
        manager.onSwitchScreens(target);
    }

    public void showMainScreen() {
        startupScreenCtrl.refreshEvents();
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(startupScene);
    }

    /**
     * When called the view changes to the event specified as input.
     * join an event (either used when creating or joining one) and updating the fields in the event screen
     */
    public void switchToEventScreen() {
        switchScreens(EventScreenCtrl.class);
        primaryStage.setScene(eventScene);
        primaryStage.setTitle("Event Screen");
    }

    /**
     * switch the primary screen to the main screen
     */
    public void switchBackToMainScreen(){
        startupScreenCtrl.refreshEvents();
        primaryStage.setScene(startupScene);
        primaryStage.setTitle("Main Menu");
    }

    /**
     * Gets startup screen
     * @return the startup screen
     */
    public Scene getMainMenuScene(){
        return startupScene;
    }

    /**
     * Gets the EventScreen
     * @return the Event screen
     */
    public Scene getEventScene() {
        return eventScene;
    }

    public Scene getParticipantScene(){
        return participantScene;
    }

    public void switchToAddExpense() {
        switchScreens(ExpenseScreenCtrl.class);
        expenseScreenCtrl.resetAll();
        primaryStage.setScene(expenseScene);
    }
    public void switchToEditExpense(long expenseId) {
        switchScreens(ExpenseScreenCtrl.class);
        expenseScreenCtrl.setExpense(expenseId);
        primaryStage.setScene(expenseScene);
    }

    public void openEditTitle() {
        switchScreens(EditTitleCtrl.class);
        primaryStage.setScene(editTitleScene);
    }

    public void switchToAddParticipant() {
        switchScreens(ParticipantScreenCtrl.class);
        primaryStage.setScene(participantScene);
    }

    public void switchToParticipantListScreen() {
        Event event = server.getEvent(eventCode);
        participantListScreenCtrl.refresh(event);
        primaryStage.setScene(participantListScene);
    }

    public void switchToEditParticipant(long participantId) {
        Event event = server.getEvent(eventCode);
        participantScreenCtrl.refresh(event);
        participantScreenCtrl.setParticipant(participantId);
        primaryStage.setScene(participantScene);
    }


    /**
     * switch to the log in page for the management overview
     */
    public void switchToMnagamentOverviewPasswordScreen(){
        primaryStage.setScene(managementOvervirewPasswordScene);
        primaryStage.setTitle("Log in");
    }

    /**
     * go to the management overview screen
     */
    public void switchToManagementOverviewScreen(){
        primaryStage.setScene(managementOverviewScreenScene);
        primaryStage.setTitle("Management Overview");
        managementOverviewScreenCtrl.initializeAllEvents();
    }

    /***
     * Switch to the Debt Settle Screen
     */
    public void switchToSettleScreen() {
        switchScreens(SettleDebtsScreenCtrl.class);
        primaryStage.setScene(settleDebtsScene);
        primaryStage.setTitle("Settle Debts");
    }

    public void switchToDeleteEventsScreen(){
        primaryStage.setScene(deleteEventsScene);
        primaryStage.setTitle("Events Removal");
        deleteEventsScreenCtrl.initializeEventsCheckList();
    }

    /***
     * Replace the event being viewed
     * @param eventCode the event code to use
     */
    public void switchEvents(String eventCode) {
        this.eventCode = eventCode;
        manager.switchClientEvent(eventCode);
    }
}
