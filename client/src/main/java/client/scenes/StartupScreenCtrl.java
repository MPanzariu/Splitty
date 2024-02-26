package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StartupScreenCtrl {
    private final ServerUtils server;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField inviteCode;

    @Inject
    public StartupScreenCtrl(ServerUtils server) {
        this.server = server;
    }

    public void joinEvent(){
        System.out.println(inviteCode.getText());
        System.out.println(server.getEvent(inviteCode.getText()));
    }

    public void createEvent(){
        System.out.println(eventTitle.getText());
        server.createEvent(eventTitle.getText());
    }
}
