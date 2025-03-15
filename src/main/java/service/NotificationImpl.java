package service;

import entity.Cours;
import entity.Notification;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class NotificationImpl implements IRepository<Notification> {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public NotificationImpl() {
        this.emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        this.em = emf.createEntityManager();
    }
    @Override
    public void add(Notification obj) {
        try {
            em.getTransaction().begin();

            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Notification obj) {

    }

    @Override
    public void delete(Notification obj) {

    }

    @Override
    public ObservableList<Notification> getAll() {
        try {
            TypedQuery<Notification> query = em.createQuery("SELECT n FROM Notification n", Notification.class);
            List<Notification> notifList = query.getResultList();
            return FXCollections.observableArrayList(notifList);
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    @Override
    public Notification getById(long id) {
        return null;
    }

    public ObservableList<Notification>  getNotificationByProfesseur(User professeur) {
        try {
            List<Notification> List = em.createQuery(
                            "SELECT c FROM Notification c WHERE c.prof = :professeur", Notification.class)
                    .setParameter("professeur", professeur)
                    .getResultList();
            return FXCollections.observableArrayList(List);
        } finally {
            em.close();
        }
    }
}
