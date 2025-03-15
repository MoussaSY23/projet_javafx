package entity;

import javafx.beans.property.SimpleStringProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String role;



    @OneToMany(mappedBy = "prof", fetch = FetchType.LAZY)
    private Set<Notification> notif;

    @OneToMany(mappedBy = "professeur", fetch = FetchType.EAGER)
    private Set<Cours> cours;


    // Constructeur sans arguments
    public User() {
    }

    // Constructeur avec arguments
    public User(String nom, String prenom, String email, String password, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public SimpleStringProperty nomProperty() {
        return new SimpleStringProperty(nom);
    }

    public SimpleStringProperty prenomProperty() {
        return new SimpleStringProperty(prenom);
    }

    public SimpleStringProperty emailProperty() {
        return new SimpleStringProperty(email);
    }

    public SimpleStringProperty passwordProperty() {
        return new SimpleStringProperty(password);
    }

    public SimpleStringProperty roleProperty() {
        return new SimpleStringProperty(role);
    }

    public Set<Cours> getCours() {
        return cours;
    }

    public void setCours(Set<Cours> cours) {
        this.cours = cours;
    }

    @Override
    public String toString() {
        return "User{" +
                 nom  +' '+
                 prenom  +
                '}';
    }
}

