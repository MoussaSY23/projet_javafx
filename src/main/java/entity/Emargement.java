package entity;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emargements")
public class Emargement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String statut; // Présent, Absent, Retard, etc.

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private User professeur;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    public Emargement() {}

    public Emargement(LocalDateTime date, String statut, User professeur, Cours cours) {
        this.date = date;
        this.statut = statut;
        this.professeur = professeur;
        this.cours = cours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public User getProfesseur() {
        return professeur;
    }

    public void setProfesseur(User professeur) {
        this.professeur = professeur;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    // JavaFX properties for binding
    public SimpleObjectProperty<LocalDateTime> dateProperty() {
        return new SimpleObjectProperty<>(date);
    }

    public SimpleStringProperty statutProperty() {
        return new SimpleStringProperty(statut);
    }

    public SimpleObjectProperty<User> professeurProperty() {
        return new SimpleObjectProperty<>(professeur);
    }

    public SimpleObjectProperty<Cours> coursProperty() {
        return new SimpleObjectProperty<>(cours);
    }
}
