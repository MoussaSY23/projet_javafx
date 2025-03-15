package entity;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private User prof;

    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi;



    public Notification(Long id, String message, User prof, LocalDateTime dateEnvoi) {
        this.id = id;
        this.message = message;
        this.prof = prof;
        this.dateEnvoi = dateEnvoi;
    }

    public Notification() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getProfId() { return prof; }
    public void setDestinataireId(User prof) { this.prof = prof; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", destinataireId=" + prof +
                ", dateEnvoi=" + dateEnvoi +
                '}';
    }

    // JavaFX properties
    public SimpleLongProperty idProperty() { return new SimpleLongProperty(id); }
    public SimpleStringProperty messageProperty() { return new SimpleStringProperty(message); }
    public SimpleObjectProperty<User> ProfProperty() { return new SimpleObjectProperty<>(prof); }
    public SimpleObjectProperty<LocalDateTime> dateEnvoiProperty() { return new SimpleObjectProperty<>(dateEnvoi); }
}