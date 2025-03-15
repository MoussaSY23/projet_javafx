package com.example.projet_exam;

import entity.Cours;
import entity.Salle;
import exception.AlertMessages;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import service.salleImpl;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SalleController implements Initializable {

    @FXML
    private TableColumn<Salle, Long> IdColumn;

    @FXML
    private TableView<Salle> SalleTable;

    @FXML
    private Button bClear;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Salle, List<Cours>> coursColumn;

    @FXML
    private TableColumn<Salle, String> libelleColumn;

    @FXML
    private TextField txtlibelle;

    @FXML
    void onClear(ActionEvent event) {
        Effacer();
    }


    @FXML
    void onClicked(MouseEvent event) {
        Salle salle = SalleTable.getSelectionModel().getSelectedItem();
        if (salle != null) {
            txtlibelle.setText(salle.getLibelle());
        }
    }


    @FXML
    void onAdd(ActionEvent event) {

        // Vérification que le champ libelle n'est pas vide
        if (txtlibelle.getText().isEmpty()) {
            new AlertMessages().Alert("Veuillez remplir tous les champs", "erreur");
            return;
        }

        // Création d'une nouvelle salle
        Salle salle = new Salle();
        salle.setLibelle(txtlibelle.getText());

        // Appel de la méthode pour ajouter la salle
        salleImpl SI = new salleImpl();
        SI.add(salle);

        // Affichage du message de succès
        new AlertMessages().Alert("Salle ajoutée avec succès", "succes");

        // Mise à jour de la table des salles
        printSalle();

        // Effacement des champs
        Effacer();
    }

    @FXML
    void onDelete(ActionEvent event) {
        Salle salle = SalleTable.getSelectionModel().getSelectedItem();
        if (salle == null) {
            new AlertMessages().Alert("Veuillez sélectionner une salle à supprimer", "erreur");
            return;
        }

        // Appel de la méthode pour supprimer la salle
        salleImpl SI = new salleImpl();
        SI.delete(salle);

        // Affichage du message de succès
        new AlertMessages().Alert("Salle supprimée avec succès", "succes");

        // Mise à jour de la table des salles
        printSalle();
    }
    @FXML
    void onUpdate(ActionEvent event) {
        Salle salle = SalleTable.getSelectionModel().getSelectedItem();
        if (salle == null) {
            new AlertMessages().Alert("Veuillez sélectionner une salle à modifier", "erreur");
            return;
        }

        // Vérification que le champ libelle n'est pas vide
        if (txtlibelle.getText().isEmpty()) {
            new AlertMessages().Alert("Veuillez remplir tous les champs", "erreur");
            return;
        }

        // Mise à jour de la salle
        salle.setLibelle(txtlibelle.getText());

        // Appel de la méthode pour mettre à jour la salle
        salleImpl SI = new salleImpl();
        SI.update(salle);

        // Affichage du message de succès
        new AlertMessages().Alert("Salle mise à jour avec succès", "succes");

        // Mise à jour de la table des salles
        printSalle();

        // Effacement des champs
        Effacer();
    }

    public void printSalle() {
        salleImpl SI = new salleImpl();
        ObservableList<Salle> sallesList = SI.getAll();

        // Lier les colonnes de la table avec les propriétés de la classe Salle
        IdColumn.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
        libelleColumn.setCellValueFactory(cellData -> cellData.getValue().libelleProperty());
        coursColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCours())); // Si vous souhaitez afficher les cours dans la table, sinon vous pouvez l'ignorer.

        // Ajouter les salles à la table
        SalleTable.setItems(sallesList);
    }

    public void Effacer() {
        txtlibelle.clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        printSalle();
    }
}
