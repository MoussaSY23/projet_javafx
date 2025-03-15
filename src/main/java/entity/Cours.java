package entity;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "cours")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime heureDebut;

    @Column(nullable = false)
    private LocalDateTime heureFin;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private User professeur;

    public Cours(Long id, String nom, String description, LocalDateTime heure_debut, LocalDateTime heure_fin, Salle salle, User professeur) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.heureDebut = heure_debut;
        this.heureFin = heure_fin;
        this.salle = salle;
        this.professeur = professeur;
    }

    public Cours() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalDateTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalDateTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalDateTime heureFin) { this.heureFin = heureFin; }

    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }

    public User getProfesseur() {
        return professeur;
    }

    public void setProfesseur(User professeur) {
        this.professeur = professeur;
    }

    @Override
    public String toString() {

            return this.nom + " " + this.heureDebut + " - " + this.heureFin;


    }


    // JavaFX properties
    public SimpleStringProperty nomProperty() { return new SimpleStringProperty(nom); }
    public SimpleStringProperty descriptionProperty() { return new SimpleStringProperty(description); }
    public SimpleObjectProperty<LocalDateTime> heureDebutProperty() { return new SimpleObjectProperty<>(heureDebut); }
    public SimpleObjectProperty<LocalDateTime> heureFinProperty() { return new SimpleObjectProperty<>(heureFin); }
    public SimpleObjectProperty<Salle> SalleProperty() { return new SimpleObjectProperty<>(salle); }
}
