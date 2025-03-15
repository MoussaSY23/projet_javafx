package com.example.projet_exam;

import Util.NavigationUtil;
import entity.User;
import exception.AlertMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.procedure.internal.Util;
import org.mindrot.jbcrypt.BCrypt;
import service.ConnexionImpl;
import exception.InvalidInputException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InscriptionController implements Initializable {

    @FXML
    private TextField Prenomtxt;
    @FXML
    private TextField Nomtxt;
    @FXML
    private TextField Emailtxt;
    @FXML
    private TextField Passwordtxt;

    @FXML
    private ComboBox<String> txtRole;




    // Méthode de validation des champs
    private void validateInput(String prenom, String nom, String email, String password) throws InvalidInputException {
        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new InvalidInputException("Veuillez remplir tous les champs !");
        }

        if (!isEmailValid(email)) {
            throw new InvalidInputException("L'email n'est pas valide !");
        }

        if (!isPasswordValid(password)) {
            throw new InvalidInputException("Le mot de passe doit comporter au moins 8 caractères, inclure une majuscule, une minuscule et un chiffre !");
        }
    }

    // Vérification de l'email
    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    // Vérification du mot de passe
    private boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

    @FXML
    private void onAnnulerbtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) Emailtxt.getScene().getWindow();
        NavigationUtil.changeScene(stage, "connection.fxml", "Formulaire de connexion", 740, 477);
    }

    public void onTerminerbtnClick(ActionEvent actionEvent) {
        String prenom = Prenomtxt.getText().trim();
        String nom = Nomtxt.getText().trim();
        String email = Emailtxt.getText().trim();
        String password = Passwordtxt.getText();
        String role = txtRole.getValue().trim();
        // Validation des données
        try {
            validateInput(prenom, nom, email, password);
        } catch (InvalidInputException e) {
            AlertMessages alert = new AlertMessages();
            alert.Alert(e.getMessage(), "erreur");
            return;
        }

        // Création de l'utilisateur
        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt())); // Hash du mot de passe
        //user.setRole("utilisateur"); // Assigner un rôle par défaut

        // Enregistrement dans la base de données
        ConnexionImpl inscription = new ConnexionImpl();
        try {
            inscription.add(user);
            System.out.println("Utilisateur ajouté dans la base.");
        } catch (Exception e) {
            e.printStackTrace();
            AlertMessages alert = new AlertMessages();
            alert.Alert("Erreur lors de l'inscription", "erreur");
            return;
        }

        // Message de succès
        AlertMessages alert = new AlertMessages();
        alert.Alert("Inscription réussie !", "succes");

        // Redirection vers la page de connexion
        try {
            Stage stage = (Stage) Emailtxt.getScene().getWindow();
            NavigationUtil.changeScene(stage, "connection.fxml", "Formulaire de connexion", 820, 520);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Subscribe(ActionEvent actionEvent) {



    }

    public void printRoles(){
        txtRole.getItems().addAll("Professeur", "Admin", "Gestionnaire"); //
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        printRoles();
    }
}
