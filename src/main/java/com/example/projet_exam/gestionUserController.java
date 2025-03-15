package com.example.projet_exam;

import entity.User;
import exception.AlertMessages;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.ConnexionImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class gestionUserController implements Initializable {

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
    private TableColumn<User, String> roleColumn;

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

    @FXML
    void onAdd(ActionEvent event) {
        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() ) {
            AlertMessages alert = new AlertMessages();
            alert.Alert("veuiller remplir tous les champs", "erreur");
            return;
        }

        User l = new User();
        l.setNom(txtNom.getText());
        l.setPrenom(txtPrenom.getText());
        l.setEmail(txtEmail.getText());
        l.setRole(txtRole.getValue());

        Us.add(l);
        String message = "Ajout reussit avec succes";
        String issucces = "succes";
        AlertMessages alert = new AlertMessages();
        alert.Alert(message, issucces);
        effacer();
        printUsers();
        printRoles();
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
        printRoles();
        effacer();
    }

    @FXML
    void onUpdate(ActionEvent event) {
        User l1 = UserTable.getSelectionModel().getSelectedItem();
        if (l1 == null) {
            AlertMessages alert = new AlertMessages();
            alert.Alert("Veuillez sélectionner un livre à modifier.", "erreur");
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
        l.setRole(txtRole.getValue());
        Us.update(l);
        String message = "modification du livre "+titre+" effectuer";
        String issucces = "succes";
        AlertMessages alert = new AlertMessages();
        alert.Alert(message, issucces );

        printUsers();
        effacer();
        printRoles();
    }





    public void printUsers(){
        var Users = Us.getAll();

        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        IdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(Math.toIntExact(cellData.getValue().getId())).asObject());


        UserTable.setItems(Users);
    }



    public void effacer(){
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtRole.getItems().clear();

    }

    public void printRoles(){
        txtRole.getItems().addAll("Professeur", "Admin", "Gestionnaire"); //
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Us = new ConnexionImpl();
        printUsers();
        printRoles();
    }
}
