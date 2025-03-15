package com.example.projet_exam;

import Util.NavigationUtil;
import entity.User;
import exception.AlertMessages;
import exception.InvalidInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import service.ConnexionImpl;
import service.UserSession;  // Importation de la classe UserSession

import java.io.IOException;

public class ConnectionController {

    @FXML
    private TextField Emailtxt;
    @FXML
    private TextField Passwordtxt;
    @FXML
    private Button Connecterbtn;

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    Boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    Boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

    public void onConnecterbtn(ActionEvent actionEvent) throws IOException {
        String email = Emailtxt.getText();
        String password = Passwordtxt.getText();

        try {
            validateInput(email, password);
        } catch (InvalidInputException e) {
            AlertMessages alert = new AlertMessages();
            alert.Alert(e.getMessage(), "erreur");
            return;
        }

        ConnexionImpl conn = new ConnexionImpl();
        boolean isValidUser = conn.verifierCompte(email, password);

        if (!isValidUser) {
            return; // L'authentification échoue si l'utilisateur n'est pas valide
        }

        User user = conn.getByEmail(email);  // Récupérer l'utilisateur

        // Enregistrer l'utilisateur dans la session
        UserSession.setCurrentUser(user);

        // Redirection vers la scène appropriée en fonction du rôle
        if (user.getRole().equals("Admin")) {
            Stage stage = (Stage) Connecterbtn.getScene().getWindow();
            NavigationUtil.changeScene(stage, "Admin.fxml", "Page des admins", 950, 560);
        } else if (user.getRole().equals("Professeur")) {
            Stage stage = (Stage) Connecterbtn.getScene().getWindow();
            NavigationUtil.changeScene(stage, "professeur.fxml", "Page des professeurs", 950, 560);
        } else if (user.getRole().equals("Gestionnaire")){
            Stage stage = (Stage) Connecterbtn.getScene().getWindow();
            NavigationUtil.changeScene(stage, "Gestionnaire.fxml", "Page des gestionnaires", 785, 520);
        }
    }

    public void onInscriptionbtn(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) Emailtxt.getScene().getWindow();
            NavigationUtil.changeScene(stage, "Inscription.fxml", "Formulaire d inscription", 785, 520);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateInput(String email, String password) throws InvalidInputException {
        if (email.isEmpty() || password.isEmpty()) {
            throw new InvalidInputException("Veuillez remplir tous les champs !");
        }

        if (!isEmailValid(email)) {
            throw new InvalidInputException("L'email n'est pas valide !");
        }

        if (!isPasswordValid(password)) {
            throw new InvalidInputException("Le mot de passe doit comporter au moins 8 caractères, inclure une majuscule, une minuscule et un chiffre !");
        }
    }
}
