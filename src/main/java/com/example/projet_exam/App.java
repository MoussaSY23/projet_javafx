package com.example.projet_exam;

import entity.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("connection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 520);
        stage.setTitle("Gestion groupe ISI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        EntityManager entityManager = JPAUtil
                .getEntityManagerFactory()
                .createEntityManager();

        launch();
    }
}

