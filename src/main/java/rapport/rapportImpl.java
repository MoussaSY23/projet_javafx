package rapport;

import entity.Emargement;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rapportImpl {

    public BarChart<String, Number> createPresenceByProfessorChart(List<Emargement> emargements) {
        // Créer un axe des catégories pour les noms des professeurs
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Professeurs");

        // Créer un axe des nombres pour le nombre d'émargements
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'émargements");

        // Créer le graphique en barres
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Nombre d'émargements par professeur");

        // Créer une série de données
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Présences");

        // Calculer le nombre d'émargements par professeur
        Map<String, Integer> professorPresenceCount = new HashMap<>();
        for (Emargement emargement : emargements) {
            String professorName = emargement.getProfesseur().getNom();
            professorPresenceCount.put(professorName, professorPresenceCount.getOrDefault(professorName, 0) + 1);
        }

        // Ajouter les données au graphique
        for (Map.Entry<String, Integer> entry : professorPresenceCount.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Ajouter la série au graphique
        barChart.getData().add(series);

        return barChart;
    }

    public LineChart<Number, Number> createEmargementEvolutionChart(List<Emargement> emargements) {
        // Créer un axe des nombres pour la date
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Date");

        // Créer un axe des nombres pour le nombre d'émargements
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'émargements");

        // Créer le graphique en ligne
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Évolution des émargements");

        // Créer une série de données
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Présences");

        // Regrouper les données par date
        Map<LocalDate, Integer> dateEmargementCount = new HashMap<>();
        for (Emargement emargement : emargements) {
            LocalDate date = emargement.getDate().toLocalDate();
            dateEmargementCount.put(date, dateEmargementCount.getOrDefault(date, 0) + 1);
        }

        // Ajouter les données au graphique
        for (Map.Entry<LocalDate, Integer> entry : dateEmargementCount.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toEpochDay(), entry.getValue()));
        }

        // Ajouter la série au graphique
        lineChart.getData().add(series);

        return lineChart;
    }

    public PieChart createPresenceRateByCourseChart(List<Emargement> emargements) {
        // Créer un graphique en camembert
        PieChart pieChart = new PieChart();

        // Calculer le nombre d'émargements par cours
        Map<String, Integer> coursePresenceCount = new HashMap<>();
        int totalEmargements = emargements.size();
        for (Emargement emargement : emargements) {
            String courseName = emargement.getCours().getNom();
            coursePresenceCount.put(courseName, coursePresenceCount.getOrDefault(courseName, 0) + 1);
        }

        // Ajouter les données au graphique
        for (Map.Entry<String, Integer> entry : coursePresenceCount.entrySet()) {
            double percentage = (entry.getValue() / (double) totalEmargements) * 100;
            PieChart.Data slice = new PieChart.Data(entry.getKey() + " - " + (int) percentage + "%", percentage);

            // Ajouter la tranche au graphique
            pieChart.getData().add(slice);

            // Appliquer une couleur à chaque tranche via CSS
            int index = pieChart.getData().indexOf(slice);
            slice.getNode().setStyle("-fx-pie-color: " + Color.hsb(Math.random() * 360, 0.7, 0.7).toString() + ";");
        }

        pieChart.setTitle("Taux de présence par cours");

        return pieChart;
    }




}
