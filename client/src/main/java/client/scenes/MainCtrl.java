package client.scenes;

import client.utils.AppStateManager;
import client.utils.ScreenInfo;
import client.utils.Translation;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Locale;

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
    private HashMap<Class<?>, ScreenInfo> screenMap;
    @Inject
    @Named("client.language")
    private String language;
    private final AppStateManager manager;

    @Inject
    public MainCtrl(Translation translation, AppStateManager manager) {
        this.translation = translation;
        this.manager = manager;
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
                           Pair<ParticipantListScreenCtrl, Parent> participantListUI,
                           Pair<AddTagCtrl, Parent> addTagUI,
                           Pair<EmailInviteCtrl, Parent> emailInviteUI,
                           Pair<StatisticsScreenCtrl, Parent> statisticsScreenUI){


        translation.changeLanguage(Locale.forLanguageTag(language));
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

        EditTitleCtrl editTitleCtrl = editTitlePair.getKey();
        Scene editTitleScene = new Scene(editTitlePair.getValue());
        showMainScreen();
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
        this.screenMap = new HashMap<>();
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
                new ScreenInfo(addTagCtrl,true, addTagScene, "AddTag.WIndow.title"));
        screenMap.put(EmailInviteCtrl.class,
                new ScreenInfo(emailInviteCtrl, false, emailInviteScene, "Email.TitleLabel"));
        screenMap.put(StatisticsScreenCtrl.class,
                new ScreenInfo(statisticsScreenCtrl, true, statisticsScreenScene, "Statistics.Screen.Window.Title"));
        manager.setScreenInfoMap(screenMap);

        manager.setStartupScreen(startupScreenCtrl);
        manager.subscribeToUpdates();
        //This can also show a pop-up in the future, but right now it doesn't
        manager.setOnCurrentEventDeletedCallback(this::showMainScreen);

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

    public void switchToEditExpense(long expenseId) {
        switchScreens(ExpenseScreenCtrl.class);
        expenseScreenCtrl.setExpense(expenseId);
    }

    public void switchToEditParticipant(long participantId) {
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
}
