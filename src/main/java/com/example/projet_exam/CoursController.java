package com.example.projet_exam;

import entity.Cours;
import entity.Notification;
import entity.Salle;
import entity.User;
import exception.AlertMessages;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import service.*;
import javafx.util.StringConverter;


import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class CoursController implements Initializable {

    @FXML
    private Button bUpdate;
    @FXML
    private Button Clear;

    @FXML
    private Button bAdd;

    @FXML
    private Button bDelete;
    @FXML
    private ComboBox<Salle> comboSalle;

    @FXML
    private ComboBox<User> comboProfesseur; // ComboBox pour les professeurs

    @FXML
    private TableView<Cours> coursTable;

    @FXML
    private TableColumn<Cours, String> descriptionColumn;

    @FXML
    private TableColumn<Cours, LocalDateTime> heureDebutColumn;

    @FXML
    private TableColumn<Cours, LocalDateTime> heureFinColumn;

    @FXML
    private TableColumn<Cours, Integer> idColumn;

    @FXML
    private TableColumn<Cours, String> nomColumn;

    @FXML
    private TableColumn<Cours, Salle> salleColumn;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtHeureDebut;

    @FXML
    private TextField txtHeureFin;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNom;

    @FXML
    private VBox txtform;
    @FXML
    private TextField txtSearch;

    private coursImpl CI;
    private salleImpl SI;
    private ConnexionImpl UI; // Service pour les utilisateurs
    private ConnexionImpl CoI;
    private NotificationImpl NOTI;

    @FXML
    void onAdd(ActionEvent event) {
        if (txtNom.getText().isEmpty() || txtDescription.getText().isEmpty() || txtHeureDebut.getText().isEmpty() || txtHeureFin.getText().isEmpty()) {
            new AlertMessages().Alert("Veuillez remplir tous les champs", "erreur");
            return;
        }

        Cours cours = new Cours();
        cours.setNom(txtNom.getText());
        cours.setDescription(txtDescription.getText());

        try {
            LocalDateTime heureDebut = LocalDateTime.parse(txtHeureDebut.getText());
            LocalDateTime heureFin = LocalDateTime.parse(txtHeureFin.getText());

            // Vérification si l'heure de début est dans le futur
            if (heureDebut.isBefore(LocalDateTime.now())) {
                new AlertMessages().Alert("L'heure de début doit être dans le futur", "erreur");
                return;
            }

            // Vérification si l'heure de fin est après l'heure de début
            if (heureFin.isBefore(heureDebut)) {
                new AlertMessages().Alert("L'heure de fin doit être après l'heure de début", "erreur");
                return;
            }

            cours.setHeureDebut(heureDebut);
            cours.setHeureFin(heureFin);
        } catch (Exception e) {
            new AlertMessages().Alert("Format d'heure invalide", "erreur");
            return;
        }

        Salle salle = comboSalle.getValue();
        User professeur = comboProfesseur.getValue();
        if (salle == null) {
            new AlertMessages().Alert("Veuillez sélectionner une salle", "erreur");
            return;
        }
        if (professeur == null) {
            new AlertMessages().Alert("Veuillez sélectionner un professeur", "erreur");
            return;
        }
        cours.setSalle(salle);
        cours.setProfesseur(professeur);

        // Ajout du cours
        CI.add(cours);

        // Envoi de l'e-mail au professeur
        String emailProfesseur = professeur.getEmail();
        String subject = "Nouveau Cours Assigné";
        String body = "Bonjour " + professeur.getNom() + ",\n\n" +
                "Vous avez été assigné à un nouveau cours :\n" +
                "- **Nom du Cours** : " + cours.getNom() + "\n" +
                "- **Description** : " + cours.getDescription() + "\n" +
                "- **Date de Début** : " + cours.getHeureDebut() + "\n" +
                "- **Date de Fin** : " + cours.getHeureFin() + "\n" +
                "- **Salle** : " + salle.getLibelle() + "\n\n" +
                "Merci de prendre note de cette information.";

        try {
            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail(emailProfesseur, subject, body);
            System.out.println("E-mail envoyé à " + emailProfesseur);
        } catch (Exception e) {
            new AlertMessages().Alert("Erreur lors de l'envoi de l'e-mail : " + e.getMessage(), "erreur");
        }
        LocalDateTime l = LocalDateTime.now();

        Notification noti = new Notification();
        noti.setMessage(body);
        noti.setDateEnvoi(l);
        noti.setDestinataireId(professeur);
        NOTI.add(noti);

        new AlertMessages().Alert("Cours ajouté avec succès et e-mail envoyé", "succes");
        Effacer();
        printCours();


    }


    @FXML
    void onClear(ActionEvent event) {
        Effacer();
    }

    @FXML
    void onDelete(ActionEvent event) {
        Cours cours = coursTable.getSelectionModel().getSelectedItem();
        if (cours == null) {
            new AlertMessages().Alert("Veuillez sélectionner un cours à supprimer", "erreur");
            return;
        }

        CI.delete(cours);
        new AlertMessages().Alert("Cours supprimé avec succès", "succes");
        printCours();
        Effacer();
    }

    @FXML
    void onUpdate(ActionEvent event) {
        Cours cours = coursTable.getSelectionModel().getSelectedItem();
        if (cours == null) {
            new AlertMessages().Alert("Veuillez sélectionner un cours à modifier", "erreur");
            return;
        }

        if (txtNom.getText().isEmpty() || txtDescription.getText().isEmpty() || txtHeureDebut.getText().isEmpty() || txtHeureFin.getText().isEmpty()) {
            new AlertMessages().Alert("Veuillez remplir tous les champs", "erreur");
            return;
        }

        cours.setNom(txtNom.getText());
        cours.setDescription(txtDescription.getText());

        try {
            LocalDateTime heureDebut = LocalDateTime.parse(txtHeureDebut.getText());
            LocalDateTime heureFin = LocalDateTime.parse(txtHeureFin.getText());

            // Vérification si l'heure de début est dans le futur
            if (heureDebut.isBefore(LocalDateTime.now())) {
                new AlertMessages().Alert("L'heure de début doit être dans le futur", "erreur");
                return;
            }

            // Vérification si l'heure de fin est dans le futur et après l'heure de début
            if (heureFin.isBefore(heureDebut)) {
                new AlertMessages().Alert("L'heure de fin doit être après l'heure de début", "erreur");
                return;
            }

            cours.setHeureDebut(heureDebut);
            cours.setHeureFin(heureFin);
        } catch (Exception e) {
            new AlertMessages().Alert("Format d'heure invalide", "erreur");
            return;
        }

        Salle salle = comboSalle.getValue();
        User professeur = comboProfesseur.getValue();
        if (salle == null) {
            new AlertMessages().Alert("Veuillez sélectionner une salle", "erreur");
            return;
        }
        if (professeur == null) {
            new AlertMessages().Alert("Veuillez sélectionner un professeur", "erreur");
            return;
        }
        cours.setSalle(salle);
        cours.setProfesseur(professeur);

        CI.update(cours);
        new AlertMessages().Alert("Cours modifié avec succès", "succes");
        printCours();
        Effacer();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CI = new coursImpl();
        SI = new salleImpl();
        CoI = new ConnexionImpl();
        UI = new ConnexionImpl(); // Initialiser le service pour les utilisateurs
        NOTI = new NotificationImpl();
        printcmbSalle();
        printcmbProfesseur(); // Charger les professeurs
        printCours();
        if (!isProf()) {
            bAdd.setVisible(true);
           bDelete.setVisible(true);
          bUpdate.setVisible(true);
            txtform.setVisible(true);
        } else {
           bAdd.setVisible(false);
            bDelete.setVisible(false);
            bUpdate.setVisible(true);
            txtform.setVisible(false);
        }
    }

    private boolean isProf() {
        User currentUser = CoI.getCurrentUser();
        return currentUser != null && currentUser.getRole().equals("Professeur");
    }

    public void printCours() {
        ObservableList<Cours> coursList;

        if (isProf()) {
            User currentUser = CoI.getCurrentUser();
            coursList = CI.getCoursByProfesseur(currentUser);
        } else {
            // Récupérer tous les cours pour les administrateurs ou autres rôles
            coursList = CI.getAll();
        }
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        heureDebutColumn.setCellValueFactory(cellData -> cellData.getValue().heureDebutProperty());
        heureFinColumn.setCellValueFactory(cellData -> cellData.getValue().heureFinProperty());
        salleColumn.setCellValueFactory(cellData -> cellData.getValue().SalleProperty());
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(Math.toIntExact(cellData.getValue().getId())).asObject());

        coursTable.setItems(coursList);
    }


    public void printcmbSalle() {
        ObservableList<Salle> salles = SI.getAll(); // Récupération des salles
        comboSalle.setItems(salles); // Remplir le ComboBox avec la liste des salles
    }




    public void printcmbProfesseur() {
        ObservableList<User> professeurs = UI.getProfesseurs(); // Récupérer les professeurs
        comboProfesseur.setItems(professeurs);

        // Créer un StringConverter pour afficher correctement les noms
        comboProfesseur.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) {
                    return null;
                }
                return user.getPrenom() + " " + user.getNom(); // Afficher le prénom et le nom
            }

            @Override
            public User fromString(String string) {
                // Implémenter cette méthode si nécessaire, généralement inutile dans ce cas
                return null;
            }
        });
    }


    public void Effacer() {
        txtNom.clear();
        txtDescription.clear();
        txtHeureDebut.clear();
        txtHeureFin.clear();
        comboSalle.getSelectionModel().clearSelection();
        comboProfesseur.getSelectionModel().clearSelection(); // Effacer la sélection du professeur
    }

    @FXML
    void onSearch(ActionEvent event){
        String prenom = txtSearch.getText().trim();

        if (prenom.isEmpty()) {
            printCours(); // Si la recherche est vide, affiche tous les livres
            return;
        }

        Cours coursTrouve = CI.getByNom(prenom);

        if (coursTrouve != null) {
            ObservableList<Cours> list = FXCollections.observableArrayList();
            list.add(coursTrouve);
            coursTable.setItems(list);
        } else {
            AlertMessages alert = new AlertMessages();
            alert.Alert("Aucun cours trouvé pour ce nom.", "erreur");
        }
    }

    @FXML
    void Load(ActionEvent event){

    }
}
