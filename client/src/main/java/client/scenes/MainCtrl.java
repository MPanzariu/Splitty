package client.scenes;

import client.utils.Translation;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.Locale;

public class MainCtrl {

    private Stage primaryStage;
    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private StartupScreenCtrl startupScreenCtrl;
    private EventScreenCtrl eventScreenCtrl;
    private Scene startupScene;
    private Scene add;
    private AddQuoteCtrl addCtrl;
    private Scene eventScene;

    @Inject
    Translation translation;
    @Inject
    @Named("client.language")
    String language;

    public void initialize(Stage primaryStage, Pair<StartupScreenCtrl, Parent> overview, Pair<EventScreenCtrl, Parent> eventUI) {
        translation.changeLanguage(Locale.forLanguageTag(language));
        this.primaryStage = primaryStage;
        this.startupScreenCtrl = overview.getKey();
        this.startupScene = new Scene(overview.getValue());
        this.eventScene = new Scene(eventUI.getValue());
        this.eventScreenCtrl = eventUI.getKey();
        showOverview();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(startupScene);
    }

    public void showAdd() {
        //primaryStage.setTitle("Quotes: Adding Quote");
        //primaryStage.setScene(add);
        //add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    /**
     * When called the view changes to the event specified as input.
     * join an event (either used when creating or joining one) and updating the fields in the event screen
     * @param event the event to join
     */
    public void joinEvent(Event event){
        //TODO implement
        primaryStage.setScene(eventScene);
        eventScreenCtrl.setEvent(event);
        eventScreenCtrl.setParticipants(event);
        eventScreenCtrl.setParticipantsForExpenses(event);
    }

    /**
     * switch the primary screen to the main screen
     */
    public void switchBackToMainScreen(){
        primaryStage.setScene(startupScene);
    }

    /**
     * Gets startup screen
     * @return the startup screen
     */
    public Scene getMainMenuScene(){
        return startupScene;
    }
}
