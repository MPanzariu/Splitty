package client.scenes;

import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.inject.Named;
import java.util.Locale;

public class MainCtrl {

    private Stage primaryStage;
    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private StartupScreenCtrl startupScreenCtrl;
    private Scene startupScene;
    private Scene add;
    private AddQuoteCtrl addCtrl;

    @Inject
    Translation translation;
    @Inject
    @Named("client.language")
    String language;



    public void initialize(Stage primaryStage, Pair<StartupScreenCtrl, Parent> overview) {
        translation.changeLanguage(Locale.forLanguageTag(language));
        this.primaryStage = primaryStage;
        this.startupScreenCtrl = overview.getKey();
        this.startupScene = new Scene(overview.getValue());
        this.startupScreenCtrl.bindFields();
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
     * @param event the event to join
     */
    public void joinEvent(Event event){
        //TODO implement
        System.out.println("Joining event!" + event);
    }

    /**
     * Gets startup screen
     * @return the startup screen
     */
    public Scene getMainMenuScene(){
        return startupScene;
    }
}
