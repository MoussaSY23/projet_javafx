package com.example.projet_exam;



import entity.Cours;
import entity.Notification;
import entity.User;
import exception.AlertMessages;
import exception.InvalidInputException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.xmlbeans.impl.config.UserTypeImpl;
import service.ConnexionImpl;
import service.EmailSender;
import service.NotificationImpl;
import service.coursImpl;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    @FXML
    private Button btnEnvoyer;

    @FXML
    private ComboBox<User> cmbDestinataire;

    @FXML
    private TableColumn<Notification, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<Notification, User> destinataireColumn;

    @FXML
    private TableColumn<Notification, String> messageColumn;

    @FXML
    private TableView<Notification> notiTable;


    @FXML
    private TextField txtTitre;
    @FXML
    private TextArea txtMessage;

    private NotificationImpl notifI;
    private ConnexionImpl CoI;
    private UserTypeImpl UI;

    @FXML
    void onEnvoyer(ActionEvent event) {
       String message = txtMessage.getText();
       var destinataire = cmbDestinataire.getValue();
        AlertMessages a = new AlertMessages();
       if (destinataire == null || message.isEmpty()) {
           a.Alert("veuiller remplir tous les champs","error");
           return;
       }
       LocalDateTime date = LocalDateTime.now();
       Notification n = new Notification();
       n.setMessage(message);
       n.setDestinataireId(destinataire);
       n.setDateEnvoi(date);
       notifI.add(n);

       String  email = destinataire.getEmail();
        String subject = txtTitre.getText();
        String body = message;
        try {
            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail(email, subject, body);
            System.out.println("E-mail envoyé à " + email);
        } catch (Exception e) {
            new AlertMessages().Alert("Erreur lors de l'envoi de l'e-mail : " + e.getMessage(), "erreur");
        }
       a.Alert("message envoyer avec succes!", "success");
       print();
       printcmb();
       Effacer();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        notifI = new NotificationImpl();
        CoI = new ConnexionImpl();
       printcmb();
        print();
    }

    public void print(){
        ObservableList<Notification> list;

        if (isProf()) {
            User currentUser = CoI.getCurrentUser();
            list = notifI.getNotificationByProfesseur(currentUser);
        } else {
            // Récupérer tous les cours pour les administrateurs ou autres rôles
            list = notifI.getAll();
        }
        messageColumn.setCellValueFactory(cellData -> cellData.getValue().messageProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateEnvoiProperty());
        destinataireColumn.setCellValueFactory(cellData -> cellData.getValue().ProfProperty());

        notiTable.setItems(list);
    }

    private boolean isProf() {
        User currentUser = CoI.getCurrentUser();
        return currentUser != null && currentUser.getRole().equals("Professeur");
    }

    public void printcmb(){
        ObservableList<User> professeurs = CoI.getProfesseurs(); // Récupérer les professeurs
        cmbDestinataire.setItems(professeurs);

        // Créer un StringConverter pour afficher correctement les noms
        cmbDestinataire.setConverter(new StringConverter<User>() {
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

    public void Effacer(){
        cmbDestinataire.getSelectionModel().clearSelection();
        txtMessage.clear();
        txtTitre.clear();
    }
}

