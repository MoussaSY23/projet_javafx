package Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigationUtil {

    public static void changeScene(Stage currentStage, String fxmlFile, String title, int width, int height) {
        try {
            // Charger le fichier FXML avec un chemin absolu correct
            URL fxmlLocation = NavigationUtil.class.getResource("/com/example/projet_exam/" + fxmlFile);

            if (fxmlLocation == null) {
                throw new IOException("Fichier FXML non trouvé : " + fxmlFile);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, width, height);
            currentStage.setTitle(title);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de la page : " + fxmlFile, e);
        }
    }
}
