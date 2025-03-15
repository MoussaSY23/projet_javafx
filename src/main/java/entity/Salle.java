package entity;

import javafx.beans.property.SimpleStringProperty;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salle")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cours> cours = new ArrayList<>();

    public Salle(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Salle() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public List<Cours> getCours() { return cours; }
    public void setCours(List<Cours> cours) { this.cours = cours; }

    public SimpleStringProperty libelleProperty() {
        return new SimpleStringProperty(libelle);


    }

    @Override
    public String toString() {
        return this.libelle; // Renvoie le nom de la salle
    }

    public SimpleStringProperty nomProperty() {
        return new SimpleStringProperty(libelle);
    }
}
