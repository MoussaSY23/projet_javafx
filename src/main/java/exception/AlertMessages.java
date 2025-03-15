package exception;

import javafx.scene.control.Alert;

public class AlertMessages {



    public void Alert(String message, String type) {
        Alert.AlertType alertType = type.equals("succes") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        Alert alert = new Alert(alertType);
        alert.setTitle(type.equals("succes") ? "Succès" : "Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
