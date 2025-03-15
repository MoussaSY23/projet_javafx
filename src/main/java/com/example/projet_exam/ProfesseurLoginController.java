package com.example.projet_exam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfesseurLoginController implements Initializable {

    @FXML
    private AnchorPane ContentPane;

    @FXML
    private AnchorPane GestionProfesseur;

    @FXML
    private Button btnEmargement;

    @FXML
    private Button btnMesCours;

    @FXML
    private Button btnNotifications;

    @FXML
    private Button btnRapports;

    @FXML
    void onEmargement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/presence.fxml"));
            AnchorPane newView = loader.load();
            ContentPane.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onMesCours(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/Cours.fxml"));
            AnchorPane newView = loader.load();
            ContentPane.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onNotifications(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/notification.fxml"));
            AnchorPane newView = loader.load();
            ContentPane.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRapports(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
