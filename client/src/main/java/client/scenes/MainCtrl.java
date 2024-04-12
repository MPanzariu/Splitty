package client.scenes;

import client.utils.AppStateManager;
import client.utils.ScreenInfo;
import client.utils.Translation;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MainCtrl {

    private Stage primaryStage;
    private StartupScreenCtrl startupScreenCtrl;
    private ExpenseScreenCtrl expenseScreenCtrl;
    private Scene startupScene;
    private Scene eventScene;
    private Scene participantScene;
    private ParticipantScreenCtrl participantScreenCtrl;
    private Scene managementOvervirewPasswordScene;
    private Scene managementOverviewScreenScene;
    private ManagementOverviewScreenCtrl managementOverviewScreenCtrl;
    private DeleteEventsScreenCtrl deleteEventsScreenCtrl;
    private Scene deleteEventsScene;
    private final Translation translation;
    private EventScreenCtrl eventScreenCtrl;
    private final HashMap<Class<?>, ScreenInfo> screenMap;
    private final String serverURL;
    private final String language;
    private final AppStateManager manager;

    /**
     * Constructor
     * @param translation the translation
     * @param manager the app state manager
     * @param serverURL the URl of the server
     * @param language the language tag used in the client
     */
    @Inject
    public MainCtrl(Translation translation, AppStateManager manager,
                    @Named("connection.URL") String serverURL,
                    @Named("client.language") String language) {
        this.translation = translation;
        this.manager = manager;
        this.serverURL = serverURL;
        this.language = language;
        this.screenMap = new HashMap<>();
    }

    /**
     * Initialize the main controller
     * @param primaryStage the primary stage
     * @param overview the startup screen
     * @param eventUI the event screen
     * @param expenseUI the expense screen
     * @param participantUI the participant screen
     * @param editTitlePair the edit title pair
     * @param managementOverviewPasswordUI the management overview password UI
     * @param managementOverviewScreenUI the management overview screen UI
     * @param settleDebtsUI the settle debts UI
     * @param deleteEventsScreenUI the delete events screen UI
     * @param participantListUI the participant list UI
     * @param transferMoneyUI the money transfer UI
     * @param addTagUI the add tag UI
     * @param emailInviteUI the email invite UI
     * @param statisticsScreenUI the statistics screen UI
     */
    public void initialize(Stage primaryStage, Pair<StartupScreenCtrl, Parent> overview,
                           Pair<EventScreenCtrl, Parent> eventUI,
                           Pair<ExpenseScreenCtrl, Parent> expenseUI,
                           Pair<ParticipantScreenCtrl, Parent> participantUI,
                           Pair<EditTitleCtrl, Parent> editTitlePair,
                           Pair<ManagementOverviewPasswordCtrl, Parent> managementOverviewPasswordUI,
                           Pair<ManagementOverviewScreenCtrl, Parent> managementOverviewScreenUI,
                           Pair<SettleDebtsScreenCtrl, Parent> settleDebtsUI,
                           Pair<DeleteEventsScreenCtrl, Parent> deleteEventsScreenUI,
                           Pair<ParticipantListScreenCtrl, Parent> participantListUI,
                           Pair<TransferMoneyCtrl, Parent> transferMoneyUI,
                           Pair<AddTagCtrl, Parent> addTagUI,
                           Pair<EmailInviteCtrl, Parent> emailInviteUI,
                           Pair<StatisticsScreenCtrl, Parent> statisticsScreenUI){
        this.primaryStage = primaryStage;
        this.startupScreenCtrl = overview.getKey();
        this.startupScene = new Scene(overview.getValue());
        this.eventScene = new Scene(eventUI.getValue());
        EventScreenCtrl eventScreenCtrl = eventUI.getKey();
        Scene expenseScene = new Scene(expenseUI.getValue());
        this.expenseScreenCtrl = expenseUI.getKey();
        Scene participantListScene = new Scene(participantListUI.getValue());
        ParticipantListScreenCtrl participantListScreenCtrl = participantListUI.getKey();
        this.participantScene = new Scene(participantUI.getValue());
        this.participantScreenCtrl = participantUI.getKey();
        this.eventScreenCtrl = eventUI.getKey();
        TransferMoneyCtrl transferMoneyCtrl = transferMoneyUI.getKey();
        Scene transferMoneyScene = new Scene(transferMoneyUI.getValue());
        EditTitleCtrl editTitleCtrl = editTitlePair.getKey();
        Scene editTitleScene = new Scene(editTitlePair.getValue());

        this.managementOvervirewPasswordScene = new Scene(managementOverviewPasswordUI.getValue());
        this.managementOverviewScreenScene = new Scene(managementOverviewScreenUI.getValue());
        this.managementOverviewScreenCtrl = managementOverviewScreenUI.getKey();

        SettleDebtsScreenCtrl settleDebtsScreenCtrl = settleDebtsUI.getKey();
        Scene settleDebtsScene = new Scene(settleDebtsUI.getValue());
        this.deleteEventsScene = new Scene(deleteEventsScreenUI.getValue());
        this.deleteEventsScreenCtrl = deleteEventsScreenUI.getKey();
        Scene addTagScene = new Scene(addTagUI.getValue());
        AddTagCtrl addTagCtrl = addTagUI.getKey();
        Scene statisticsScreenScene = new Scene(statisticsScreenUI.getValue());
        StatisticsScreenCtrl statisticsScreenCtrl = statisticsScreenUI.getKey();
        //initialize stylesheets
        this.startupScene.getStylesheets().add("stylesheets/main.css");
        this.managementOvervirewPasswordScene.getStylesheets().add("stylesheets/main.css");
        EmailInviteCtrl emailInviteCtrl = emailInviteUI.getKey();
        Scene emailInviteScene = new Scene(emailInviteUI.getValue());
        screenMap.put(EventScreenCtrl.class,
                new ScreenInfo(eventScreenCtrl, true, eventScene, "Event.Window.title"));
        screenMap.put(ExpenseScreenCtrl.class,
                new ScreenInfo(expenseScreenCtrl, false, expenseScene, "Expense.Window.title"));
        screenMap.put(EditTitleCtrl.class,
                new ScreenInfo(editTitleCtrl, false, editTitleScene, "editTitle.Window.title"));
        screenMap.put(ParticipantScreenCtrl.class,
                new ScreenInfo(participantScreenCtrl, false, participantScene, "Participants.Window.title"));
        screenMap.put(ParticipantListScreenCtrl.class,
                new ScreenInfo(participantListScreenCtrl, true, participantListScene, "ParticipantList.Window.title"));
        screenMap.put(SettleDebtsScreenCtrl.class,
                new ScreenInfo(settleDebtsScreenCtrl, true, settleDebtsScene, "SettleDebts.Window.title"));
        screenMap.put(AddTagCtrl.class,
                new ScreenInfo(addTagCtrl,true, addTagScene, "AddTag.Window.title"));
        screenMap.put(EmailInviteCtrl.class,
                new ScreenInfo(emailInviteCtrl, false, emailInviteScene, "Email.TitleLabel"));
        screenMap.put(TransferMoneyCtrl.class,
                new ScreenInfo(transferMoneyCtrl, true, transferMoneyScene, "TransferMoney.title"));
        screenMap.put(StatisticsScreenCtrl.class,
                new ScreenInfo(statisticsScreenCtrl, true, statisticsScreenScene, "Statistics.Screen.Window.Title"));
        manager.setScreenInfoMap(screenMap);
    }

    /**
     * Should be run upon starting the application, after initialize()
     */
    public void onStart(){
        translation.changeLanguage(Locale.forLanguageTag(language));
        Runnable connectionErrorCallback = (()-> Platform.runLater(this::onConnectionError));
        primaryStage.setOnCloseRequest(e -> manager.onStop());
        manager.setStartupScreen(startupScreenCtrl);
        manager.setOnCurrentEventDeletedCallback(this::showMainScreen);
        addMainScreenShortcuts();
        addEventScreenShortcuts();
        manager.subscribeToUpdates(connectionErrorCallback);
        showMainScreen();
        primaryStage.show();
    }

    /***
     * Executes screen switching to the target's instance
     * @param target the Class of the target screen
     */
    public void switchScreens(Class<?> target){
        ScreenInfo screenInfo = screenMap.get(target);
        manager.onSwitchScreens(target);
        ObservableValue<String> title = translation.getStringBinding(screenInfo.titleBinding());
        primaryStage.titleProperty().bind(title);
        primaryStage.setScene(screenInfo.scene());
    }

    /***
     * Switches back to the Startup screen
     */
    public void showMainScreen() {
        manager.closeOpenedEvent();
        startupScreenCtrl.refreshLanguageOnSwitchback();
        primaryStage.titleProperty().bind(translation.getStringBinding("Startup.Window.title"));
        primaryStage.setScene(startupScene);
    }

    /***
     * Executed when there is a connection error/interruption, generates a popup for the user
     */
    public void onConnectionError() {
        showMainScreen();
        Alert initialPopup = new Alert(Alert.AlertType.ERROR);
        String reconnectString = translation.getStringBinding("Main.ConnectionError.Reconnect").getValue();
        String exitString = translation.getStringBinding("Main.ConnectionError.Exit").getValue();
        ButtonType buttonTypeReconnect = new ButtonType(reconnectString, ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeExit = new ButtonType(exitString, ButtonBar.ButtonData.NO);
        var connectionErrorPopup = generateConnectionErrorPopup(initialPopup, serverURL, buttonTypeReconnect, buttonTypeExit);

        Optional<ButtonType> result = connectionErrorPopup.showAndWait();
        if(result.isPresent() && result.get().equals(buttonTypeReconnect)){
            manager.subscribeToUpdates(()-> Platform.runLater(this::onConnectionError));
        } else {
            Platform.exit();
        }
    }

    /***
     * Generates an alert popup on connection failure
     * @param initialPopup the already-initialized Alert popup
     * @param serverURL the URL of the server
     * @param buttonTypeReconnect the ButtonType of the Reconnect button
     * @param buttonTypeExit the ButtonType of the Exit button
     * @return an Alert popup telling the user to either reconnect or exit the app
     */
    public Alert generateConnectionErrorPopup(Alert initialPopup,String serverURL, ButtonType buttonTypeReconnect, ButtonType buttonTypeExit){
        Map<String, String> substitutionMap = new HashMap<>();
        substitutionMap.put("serverURL", serverURL);

        String errorTitle = translation.getStringBinding("Main.ConnectionError.Title").getValue();
        String errorHeader = translation.getStringSubstitutionBinding("Main.ConnectionError.Header", substitutionMap)
                .getValue();
        String errorContent = translation.getStringSubstitutionBinding("Main.ConnectionError.Content", substitutionMap)
                .getValue();

        initialPopup.setTitle(errorTitle);
        initialPopup.setHeaderText(errorHeader);
        initialPopup.setContentText(errorContent);

        initialPopup.getButtonTypes().setAll(buttonTypeExit, buttonTypeReconnect);

        return initialPopup;
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

    /**
     * Gets the ExpenseScreen
     * @return the Expense screen
     */
    public Scene getParticipantScene(){
        return participantScene;
    }

    /**
     * Switches to the edit expense screen
     * @param expenseId  the expense id of the expense to edit
     */
    public void switchToEditExpense(long expenseId) {
        switchScreens(ExpenseScreenCtrl.class);
        expenseScreenCtrl.setExpense(expenseId);
    }

    /**
     * Switches to the edit participant screen
     * @param participantId the participant id of the participant to edit
     */
    public void switchToEditParticipant(long participantId) {
        participantScreenCtrl.saveId(participantId);
        switchScreens(ParticipantScreenCtrl.class);
        participantScreenCtrl.setParticipant(participantId);
    }
    /**
     * switch to the login page for the management overview
     */
    public void switchToManagementOverviewPasswordScreen(){
        primaryStage.setScene(managementOvervirewPasswordScene);
        primaryStage.titleProperty().bind(translation.getStringBinding("MOPCtrl.Window.title"));
    }

    /**
     * go to the management overview screen
     */
    public void switchToManagementOverviewScreen(){
        primaryStage.setScene(managementOverviewScreenScene);
        primaryStage.titleProperty().bind(translation.getStringBinding("MOSCtrl.Window.title"));
        managementOverviewScreenCtrl.initializeAllEvents();
    }

    /**
     * Switches to the settle debts screen
     */
    public void switchToDeleteEventsScreen(){
        primaryStage.setScene(deleteEventsScene);
        primaryStage.titleProperty().bind(translation.getStringBinding("DES.Window.title"));
        deleteEventsScreenCtrl.initializeEventsCheckList();
    }

    /***
     * Replace the event being viewed
     * @param eventCode the event code to use
     */
    public void switchEvents(String eventCode) {
        manager.switchClientEvent(eventCode);
    }

    /**
     * Adds shortcuts to the scene
     * ctrl + a switches to the admin password screen
     * ctrl + e switches to the event screen with the most recently joined event
     */
    public void addMainScreenShortcuts() {
        EventHandler<KeyEvent> shortcutFilter = getEventHandlerForMainScreen();
        getMainMenuScene().addEventFilter(KeyEvent.KEY_PRESSED, shortcutFilter);
    }

    /**
     * Returns the event handler for main screen
     * @return the event handler
     */
    public EventHandler<KeyEvent> getEventHandlerForMainScreen() {
        return event -> {
            KeyCombination ctrlA = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
            //Switch to the event screen with the most recently joined event ctrl + e
            KeyCombination ctrlE = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
            if (ctrlE.match(event)) {
                startupScreenCtrl.joinMostRecentEvent();
            }else if (ctrlA.match(event)) {
                switchToManagementOverviewPasswordScreen();
            }
        };
    }

    /**
     * Adds shortcuts to the event scene
     * ctrl + a switches to the admin password screen
     * ctrl + t tests the email invite
     * ctrl + s switches to the statistics screen
     * ctrl + e edits the title of the event
     * ctrl + + adds a new expense
     * ctrl + p adds a new participant
     * ctrl + m transfers money
     */
    public void addEventScreenShortcuts(){
        EventHandler<KeyEvent> shortcutFilter = getEventHandlerForEventScreen();
        this.eventScene.addEventFilter(KeyEvent.KEY_PRESSED, shortcutFilter);
    }

    /**
     * Generates the event handler for the event screen
     * @return the event handler
     */
    public EventHandler<KeyEvent> getEventHandlerForEventScreen() {
        return event -> {
            //Switch to admin password screen ctrl + a
            KeyCombination ctrlA = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
            //Test email invite ctrl + t
            KeyCombination ctrlT = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
            //Switch to statistics screen ctrl + s
            KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            //Edit the title of the event ctrl + w
            KeyCombination ctrlW = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
            //Add a new expense ctrl + q
            KeyCombination ctrlQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
            //Add a new participant ctrl + p
            KeyCombination ctrlP = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
            //Invite by email ctrl + I
            KeyCombination ctrlI = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
            //Go to main screen ctrl + b
            KeyCombination ctrlB = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
            //Add tag ctrl + f
            KeyCombination ctrlF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
            //Transfer movie ctrl + d
            KeyCombination ctrlD = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
            //Settle debts ctrl + g
            KeyCombination ctrlG = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
            eventScene.setOnKeyPressed(e -> {
                if (ctrlA.match(e)) {
                    switchToManagementOverviewPasswordScreen();
                } else if (ctrlT.match(e)) {
                    eventScreenCtrl.sendTestEmail();
                } else if (ctrlS.match(e)) {
                    eventScreenCtrl.switchToStatistics();
                } else if (ctrlW.match(e)) {
                    switchScreens(EditTitleCtrl.class);
                } else if (ctrlQ.match(e)) {
                    eventScreenCtrl.addExpense();
                } else if (ctrlP.match(e)) {
                    eventScreenCtrl.addParticipants();
                } else if (ctrlI.match(e)) {
                    eventScreenCtrl.switchToInviteEmail();
                } else if (ctrlB.match(e)) {
                    eventScreenCtrl.switchToMainScreen();
                } else if (ctrlF.match(e)) {
                    eventScreenCtrl.switchToAddTag();
                } else if (ctrlD.match(e)) {
                    eventScreenCtrl.transferMoney();
                } else if (ctrlG.match(e)) {
                    eventScreenCtrl.settleDebts();
                }
            });
        };
    }

    /**
     * Shows an alert that tells the user if the email was sent successfully
     * @param wasSuccessful true if the email was sent successfully, false otherwise
     */
    public void showEmailPrompt(boolean wasSuccessful) {
        Alert a;
        if (wasSuccessful){
            System.out.println("Successfully sent email!");
            a = new Alert(Alert.AlertType.INFORMATION);
            a.contentTextProperty().bind(translation.getStringBinding("Event.Label.EmailFeedback.Success"));
            a.titleProperty().bind(translation.getStringBinding("Email.SuccessTitle"));
        }else{
            System.out.println("Error while sending email!");
            a = new Alert(Alert.AlertType.ERROR);
            a.contentTextProperty().bind(translation.getStringBinding("Event.Label.EmailFeedback.Fail"));
            a.titleProperty().bind(translation.getStringBinding("Email.ErrorTitle"));
        }
        a.headerTextProperty().bind(translation.getStringBinding("Email.EmailFeedback"));
        a.show();
    }
}

