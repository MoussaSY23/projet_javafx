package com.example.projet_exam;

import entity.Cours;
import entity.Emargement;
import entity.User;
import exception.AlertMessages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.ConnexionImpl;
import service.EmargementImpl;
import service.coursImpl;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class emargementController implements Initializable {

    @FXML
    private Button bAdd1;
    @FXML
    private VBox gestionpre;
    @FXML
    private Button Clear;

    @FXML
    private TableColumn<Emargement, LocalDateTime> DateColumn;

    @FXML
    private TableColumn<?, ?> IdColumn;

    @FXML
    private TableView<Emargement> PresenceTable;

    @FXML
    private Button bAdd;

    @FXML
    private Button bDelete;

    @FXML
    private Button bUpdate;

    @FXML
    private TableColumn<Emargement, Cours> coursColumn;

    @FXML
    private TableColumn<Emargement, User> professeurColumn;

    @FXML
    private TableColumn<Emargement, String> statutColumn;

    @FXML
    private ComboBox<Cours> txtCours;

    @FXML
    private TextField txtDate;

    @FXML
    private ComboBox<User> txtProfesseur;
    @FXML
    private Label lProf;

    @FXML
    private Label LDate;

    @FXML
    private Label LStatut;

    @FXML
    private ComboBox<String> txtstatut;

    private EmargementImpl EI;
    private coursImpl CI;
    private ConnexionImpl CoI;
    @FXML
    void Load(MouseEvent event) {

        txtCours.setItems(CI.getAll());

        txtProfesseur.setItems(CoI.getProfesseurs());
       txtstatut.setItems(FXCollections.observableArrayList("Présent", "Absent", "Retard"));

        PresenceTable.setItems(EI.getAll()); // Remplace emargementService par ton service d'émargements
    }


    @FXML
    void onAdd(ActionEvent event) {
        if (txtCours.getSelectionModel().getSelectedItem() != null ) {


            // Récupérer les autres valeurs
            Cours cours = txtCours.getSelectionModel().getSelectedItem();
            User professeur =  CoI.getCurrentUser();
            LocalDateTime debutCours = cours.getHeureDebut(); // Supposons que `Cours` a une méthode `getDateDebut()`
            LocalDateTime finCours = cours.getHeureFin();
             // Vérifier si l'heure actuelle est dans la fenêtre de 5 minutes avant et après le début du cours
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime windowStart = debutCours.minusMinutes(5);
            LocalDateTime windowEnd = debutCours.plusMinutes(5);

            LocalDateTime now2 = LocalDateTime.now();
            String statut;
            if (now.isBefore(windowStart) || now.isAfter(windowEnd)) {
                statut = "Absent"; // Le professeur sera marqué comme "Absent" si en dehors de la fenêtre
            }else if(now.isAfter(windowEnd) || now.isBefore(finCours)) {
                statut = "retard";
            }else{
                statut = "Present";
            }
            Emargement emargement = new Emargement(now2, statut, professeur, cours);
            EI.add(emargement);
            Load(null);
            AlertMessages alert = new AlertMessages();
            alert.Alert("Ajout réussi avec succès", "succes");
            print();
            Effacer();
            printcmb();
        } else {
            AlertMessages alert = new AlertMessages();
            alert.Alert("Veuillez remplir tous les champs", "erreur");
        }
    }





    @FXML
    void onClear(ActionEvent event) {
       Effacer();
    }

    @FXML
    void onDelete(ActionEvent event) {
        Emargement emargement = PresenceTable.getSelectionModel().getSelectedItem();
        if (emargement != null) {
            // Suppression via le service
            EI.delete(emargement);

            // Recharger la TableView
            Load(null);

            // Message de succès
            AlertMessages alert = new AlertMessages();
            alert.Alert("Suppression effectuée avec succès", "succes");
            print();
            Effacer();
            printcmb();
        } else {
            // Message d'erreur si aucun émargement n'est sélectionné
            AlertMessages alert = new AlertMessages();
            alert.Alert("Veuillez sélectionner un émargement à supprimer", "erreur");
        }
    }


    @FXML
    void onUpdate(ActionEvent event) {
        Emargement emargement = PresenceTable.getSelectionModel().getSelectedItem();
        if (emargement != null) {
            if (txtCours.getSelectionModel().getSelectedItem() != null &&
                    txtProfesseur.getSelectionModel().getSelectedItem() != null &&
                    txtstatut.getSelectionModel().getSelectedItem() != null &&
                    !txtDate.getText().isEmpty()) {
                String dateString = txtDate.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // format attendu avec heure
                LocalDateTime date;

                try {
                    date = LocalDateTime.parse(dateString, formatter); // Essayer de parser la date avec le format
                } catch (DateTimeParseException e) {
                    AlertMessages alert = new AlertMessages();
                    alert.Alert("Le format de la date est incorrect. Veuillez utiliser le format : yyyy-MM-dd HH:mm", "erreur");
                    return; // Sortir de la méthode si la date est invalide
                }
                Cours cours = txtCours.getSelectionModel().getSelectedItem();
                LocalDateTime debutCours = cours.getHeureDebut(); // Supposons que `Cours` a une méthode `getDateDebut()`

                // Vérifier si l'heure actuelle est dans la fenêtre de 5 minutes avant et après le début du cours
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime windowStart = debutCours.minusMinutes(5);
                LocalDateTime windowEnd = debutCours.plusMinutes(5);

                if (now.isBefore(windowStart) || now.isAfter(windowEnd)) {
                    // Si l'heure actuelle est en dehors de la fenêtre, mettre le statut à "Absent"
                    emargement.setStatut("Absent");
                } else {
                    // Sinon, mettre à jour le statut selon la sélection
                    emargement.setStatut(txtstatut.getSelectionModel().getSelectedItem());
                }

                // Mettre à jour les informations de l'émargement
                emargement.setCours(txtCours.getSelectionModel().getSelectedItem());
                emargement.setProfesseur(txtProfesseur.getSelectionModel().getSelectedItem());
                emargement.setDate(date);

                // Mettre à jour via le service
                EI.update(emargement);

                // Recharger la TableView
                Load(null);

                // Message de succès
                AlertMessages alert = new AlertMessages();
                alert.Alert("Mise à jour effectuée avec succès", "succes");
                print();
                Effacer();
                printcmb();
            } else {
                // Message d'erreur si des champs sont vides
                AlertMessages alert = new AlertMessages();
                alert.Alert("Veuillez remplir tous les champs", "erreur");
            }
        } else {
            // Message d'erreur si aucun émargement n'est sélectionné
            AlertMessages alert = new AlertMessages();
            alert.Alert("Veuillez sélectionner un émargement à modifier", "erreur");
        }
    }



    public void printcmb() {
        // Remplir le ComboBox des cours
        var coursList = CI.getAll();
        if (isProf()){
            User currentUser = CoI.getCurrentUser();
            var cours = CI.getCoursByProfesseur(currentUser);
        txtCours.setItems(cours);
      }else {
            txtCours.setItems(coursList);
        }

        var professeursList = CoI.getProfesseurs();
        txtProfesseur.setItems(professeursList);
        // Remplir le ComboBox des statuts
        txtstatut.setItems(FXCollections.observableArrayList("Présent", "Absent", "Retard"));
    }

    public void print() {
        // Récupérer la liste des émargements depuis le service
        ObservableList<Emargement>  emargements;
        if (isProf()) {
            User currentUser = CoI.getCurrentUser();
            emargements = EI.getEmargementByProfesseur(currentUser);
        } else {
            // Récupérer tous les cours pour les administrateurs ou autres rôles
            emargements = EI.getAll();
        }
        DateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        coursColumn.setCellValueFactory(cellData -> cellData.getValue().coursProperty());
        professeurColumn.setCellValueFactory(cellData -> cellData.getValue().professeurProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());

        // Ajouter les données dans la TableView
        PresenceTable.setItems(emargements);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EI = new EmargementImpl();
        CI = new coursImpl();
        CoI = new ConnexionImpl();
        if (!isProf()) {
            bAdd.setVisible(false);
            bDelete.setVisible(true);
            bUpdate.setVisible(true);
            gestionpre.setVisible(true);
            bAdd1.setVisible(true);
        } else {
            bAdd.setVisible(true);
            bAdd1.setVisible(false);
            bDelete.setVisible(true);
            bUpdate.setVisible(false);
            txtProfesseur.setVisible(false);
            txtstatut.setVisible(false);
            txtDate.setVisible(false);
            LDate.setVisible(false);
            LStatut.setVisible(false);
            lProf.setVisible(false);

        }

        print();
        printcmb();
    }

    private boolean isProf() {
        User currentUser = CoI.getCurrentUser();  // Obtenez l'utilisateur actuel
        return currentUser != null && currentUser.getRole().equals("Professeur");  // Vérifiez si le rôle est "Admin"
    }


    public void Effacer(){
        txtDate.clear();
        txtCours.getSelectionModel().clearSelection();
        txtProfesseur.getSelectionModel().clearSelection();
        txtstatut.getSelectionModel().clearSelection();
    }


    @FXML
    void onExport(ActionEvent event) {
        ObservableList<Emargement> emargements = PresenceTable.getItems();
        EI.exportToExcel(emargements);
        AlertMessages alert = new AlertMessages();
        alert.Alert("Les émargements ont été exportés avec succès!", "succes");
    }

    public void onExports(ActionEvent actionEvent) {
        // Récupérer la liste des émargements à exporter
        ObservableList<Emargement> emargements = PresenceTable.getItems();
        EI.exportToExcel(emargements);

        // Afficher un message de succès
        AlertMessages alert = new AlertMessages();
        alert.Alert("Les émargements ont été exportés avec succès!", "succes");
    }

    @FXML
    private void onPDF(ActionEvent actionEvent) {
        // Récupérer la liste des émargements à exporter
        EmargementImpl emargementImpl = new EmargementImpl();
        List<Emargement> emargements = emargementImpl.getAll();  // Tu peux ajuster cette ligne en fonction de ta logique pour récupérer les émargements

        // Exporter les émargements en PDF
        emargementImpl.exportToPdf(emargements); // Appel à la fonction exportToPdf avec la liste des émargements

        // Ouvrir une boîte de dialogue pour choisir l'emplacement du fichier si nécessaire (facultatif)
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            // Si un fichier est sélectionné, sauvegarder le fichier PDF
            emargementImpl.exportToPdf(emargements);  // Cette ligne exporte dans le répertoire sélectionné
        }
    }


    public void onAdd2(ActionEvent actionEvent) {

        if (txtCours.getSelectionModel().getSelectedItem() != null &&
                txtProfesseur.getSelectionModel().getSelectedItem() != null &&
                txtstatut.getSelectionModel().getSelectedItem() != null &&
                !txtDate.getText().isEmpty()) {

            String dateString = txtDate.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime date;

            try {
                date = LocalDateTime.parse(dateString, formatter); // Essayer de parser la date avec le format
            } catch (DateTimeParseException e) {
                AlertMessages alert = new AlertMessages();
                alert.Alert("Le format de la date est incorrect. Veuillez utiliser le format : yyyy-MM-dd HH:mm", "erreur");
                return; // Sortir de la méthode si la date est invalide
            }

            // Récupérer les autres valeurs
            Cours cours = txtCours.getSelectionModel().getSelectedItem();
            User professeur = txtProfesseur.getSelectionModel().getSelectedItem();
            String statut = txtstatut.getSelectionModel().getSelectedItem();

            // Récupérer l'heure de début du cours
            LocalDateTime debutCours = cours.getHeureDebut(); // Supposons que `Cours` a une méthode `getDateDebut()`

            // Vérifier si l'heure actuelle est dans la fenêtre de 5 minutes avant et après le début du cours
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime windowStart = debutCours.minusMinutes(5);
            LocalDateTime windowEnd = debutCours.plusMinutes(5);

            if (now.isBefore(windowStart) || now.isAfter(windowEnd)) {
                statut = "Absent"; // Le professeur sera marqué comme "Absent" si en dehors de la fenêtre
            }

            // Créer un nouvel émargement
            Emargement emargement = new Emargement(date, statut, professeur, cours);

            // Ajouter l'émargement via le service
            EI.add(emargement);

            // Recharger la TableView et afficher un message de succès
            Load(null);
            AlertMessages alert = new AlertMessages();
            alert.Alert("Ajout réussi avec succès", "succes");

            // Mettre à jour les autres éléments
            print();
            Effacer();
            printcmb();
        } else {
            AlertMessages alert = new AlertMessages();
            alert.Alert("Veuillez remplir tous les champs", "erreur");
        }
    }
}

