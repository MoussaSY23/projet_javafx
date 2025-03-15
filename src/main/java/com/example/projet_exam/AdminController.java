package com.example.projet_exam;

import Util.NavigationUtil;
import entity.Emargement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import rapport.rapportImpl;  // Importer la classe rapportImpl
import service.EmargementImpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {


    @FXML
    private Button btnNotifications;

    @FXML
    private Button btnLogout;

    @FXML
    private AnchorPane Sidetwo;

    @FXML
    private VBox chartsContainer;  // Directement lié via FXML

    @FXML
    private Button btnUtilisateurs, btnCours, btnProfesseurs, btnPresences, btnRapports;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupButtonHoverEffect(btnUtilisateurs);
        setupButtonHoverEffect(btnCours);
        setupButtonHoverEffect(btnProfesseurs);
        setupButtonHoverEffect(btnPresences);
        setupButtonHoverEffect(btnRapports);
    }

    private void setupButtonHoverEffect(Button button) {
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 10px;");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 10px;");
        });
    }

    @FXML
    private void utilisateurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/gestionUser2.fxml"));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void professeurs() {}
    @FXML private void presences() {}
    @FXML private void rapports() {}

    public void onCours(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/Cours.fxml"));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSalle(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/Salle.fxml"));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onProf(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/gestionProf.fxml"));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPresence(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/presence.fxml"));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStatisticsGraphs() {
        // Création des services
        rapportImpl rapportService = new rapportImpl();
        EmargementImpl EI = new EmargementImpl();

        // Récupérer les émargements
        List<Emargement> emargements = EI.getAll();
        if (emargements.isEmpty()) {
            System.out.println("Aucun émargement trouvé.");
            return;  // Sortie si aucune donnée
        }

        // Génération des graphiques
        BarChart<String, Number> barChart = rapportService.createPresenceByProfessorChart(emargements);
        LineChart<Number, Number> lineChart = rapportService.createEmargementEvolutionChart(emargements);
        PieChart doughnutChart = rapportService.createPresenceRateByCourseChart(emargements);

        // Ajouter les graphiques au conteneur Sidetwo
        if (chartsContainer != null) {  // Utilise directement chartsContainer
            chartsContainer.getChildren().clear();  // Efface les anciens graphiques

            // Ajouter les nouveaux graphiques dans le conteneur
            chartsContainer.getChildren().add(barChart);
            chartsContainer.getChildren().add(lineChart);
            chartsContainer.getChildren().add(doughnutChart);

            // Forcer le redimensionnement
            barChart.setMaxWidth(Double.MAX_VALUE);
            lineChart.setMaxWidth(Double.MAX_VALUE);
            doughnutChart.setMaxWidth(Double.MAX_VALUE);

            // Rafraîchir le layout
            chartsContainer.requestLayout();
        } else {
            System.out.println("Le conteneur 'chartsContainer' est introuvable.");
        }
    }

    public void onRapport(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnRapports.getScene().getWindow();
            loadStatisticsGraphs();
        }catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void onLogout(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            NavigationUtil.changeScene(stage, "connection.fxml", "Formulaire de connexion", 820, 520);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNotification(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projet_exam/notification.fxml"));
            AnchorPane newView = loader.load();
            Sidetwo.getChildren().setAll(newView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
