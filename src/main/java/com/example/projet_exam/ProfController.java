package com.example.projet_exam;

import Util.NavigationUtil;
import entity.Cours;
import entity.User;
import exception.AlertMessages;
import exception.InvalidInputException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import service.ConnexionImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfController implements Initializable {
    private ConnexionImpl Us;

    @FXML
    private Button Clear;

    @FXML
    private TableColumn<User, Integer> IdColumn;

    @FXML
    private TableView<User> UserTable;

    @FXML
    private Button bAdd;

    @FXML
    private Button bDelete;

    @FXML
    private Button bSearch;

    @FXML
    private Button bUpdate;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> nomColumn;

    @FXML
    private TableColumn<User, String> prenomColumn;

    @FXML
    private TableColumn<User, String> CoursColumn;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId2;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPrenom;

    @FXML
    private ComboBox<String> txtRole;

    @FXML
    private TextField txtSearch;



    @FXML
    void Load(MouseEvent event) {
        if (event.getClickCount() == 2) {
            User lv = (User) UserTable.getSelectionModel().getSelectedItem();
            txtNom.setText(lv.getNom());
            txtPrenom.setText(lv.getPrenom());
            txtEmail.setText(lv.getEmail());
            //txtAuteur.setValue(lv.getAuteur());
            txtId2.setText(lv.getId().toString());
            txtId2.setText(lv.getId().toString());
        }
    }

    @FXML
    void Search(ActionEvent event) {
        String prenom = txtSearch.getText().trim();

        if (prenom.isEmpty()) {
            printUsers(); // Si la recherche est vide, affiche tous les livres
            return;
        }

        User userTrouve = Us.getByPrenom(prenom);

        if (userTrouve != null) {
            ObservableList<User> list = FXCollections.observableArrayList();
            list.add(userTrouve);
            UserTable.setItems(list);
        } else {
            AlertMessages alert = new AlertMessages();
            alert.Alert("Aucun utilisateur trouvé pour ce prenom.", "erreur");
        }
    }

    Boolean isEmailValid(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    Boolean isPasswordValid(String password){
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }
    @FXML
    void onAdd(ActionEvent event) {
        String prenom = txtPrenom.getText().trim();
        String nom = txtNom.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        // Création de l'utilisateur
        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setRole("Professeur");
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
        AlertMessages alert = new AlertMessages();
        alert.Alert("professeur ajouter avec succes ", "succes");
        printUsers();

    }

    @FXML
    void onClear(ActionEvent event) {

        effacer();;
    }

    @FXML
    void onDelete(ActionEvent event) {

        User us = UserTable.getSelectionModel().getSelectedItem();
        if (us == null) {
            AlertMessages alert = new AlertMessages();
            alert.Alert("veuiller remplir tous les champs", "erreur");
            return;
        }

        String nom = us.getNom();
        int id = Integer.parseInt(txtId2.getText());
        User l = Us.getById(id);

        Us.delete(l);
        String message = "suppression de "+nom + " effectué avec succes";
        String issucces = "succes";
        AlertMessages alert = new AlertMessages();
        alert.Alert(message, issucces );
        printUsers();

        effacer();
    }

    @FXML
    void onUpdate(ActionEvent event) {
        User l1 = UserTable.getSelectionModel().getSelectedItem();
        if (l1 == null) {
            AlertMessages alert = new AlertMessages();
            alert.Alert("Veuillez sélectionner un professeur à modifier.", "erreur");
            return;
        }

        if (txtNom.getText().isEmpty() || txtNom.getText().isEmpty() || txtEmail.getText().isEmpty())
        {
            String message = "veuiller remplir tous les champs";
            String issucces = "error";
            AlertMessages alert = new AlertMessages();
            alert.Alert(message, issucces );
            return;
        }

        int id = Integer.parseInt(txtId2.getText());
        User l = Us.getById(id);
        String titre = l.getNom();
        l.setNom(txtNom.getText());
        l.setPrenom(txtPrenom.getText());
        l.setEmail(txtEmail.getText());
        Us.update(l);
        String message = "modification du professeur "+titre+" effectuer";
        String issucces = "succes";
        AlertMessages alert = new AlertMessages();
        alert.Alert(message, issucces );

        printUsers();
        effacer();
    }





    public void printUsers() {
        // Récupérer tous les utilisateurs et filtrer ceux ayant le rôle "Professeur"
        var allUsers = Us.getAll();
        ObservableList<User> professeurs = FXCollections.observableArrayList(
                allUsers.stream()
                        .filter(user -> "Professeur".equals(user.getRole()))
                        .toList()
        );

        // Définir les colonnes de la table
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        // Afficher les noms des cours associés au professeur
        CoursColumn.setCellValueFactory(cellData -> {
            var coursList = cellData.getValue().getCours(); // Assurez-vous d'avoir une méthode getCours() dans User
            String coursNoms = coursList.stream()
                    .map(Cours::getNom)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Aucun cours");
            return new SimpleStringProperty(coursNoms);
        });

        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(Math.toIntExact(cellData.getValue().getId())).asObject());

        // Afficher les professeurs dans la table
        UserTable.setItems(professeurs);
    }




    public void effacer(){
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtRole.getItems().clear();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Us = new ConnexionImpl();
        printUsers();
    }
}

