package service;

import entity.Cours;
import entity.Salle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class salleImpl implements IRepository<Salle> {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public salleImpl() {
        this.emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        this.em = emf.createEntityManager();
    }

    @Override
    public void add(Salle salle) {
        try {
            em.getTransaction().begin();
            em.persist(salle); // Ajoute la salle
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Salle salle) {
        try {
            em.getTransaction().begin();
            em.merge(salle); // Met à jour la salle
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Salle salle) {
        try {
            em.getTransaction().begin();
            Salle salleToDelete = em.find(Salle.class, salle.getId());
            if (salleToDelete != null) {
                em.remove(salleToDelete); // Supprime la salle
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Salle> getAll() {
        try {
            TypedQuery<Salle> query = em.createQuery("SELECT s FROM Salle s", Salle.class);
            List<Salle> sallesList = query.getResultList();
            return FXCollections.observableArrayList(sallesList); // Retourne toutes les salles
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    @Override
    public Salle getById(long id) {
        try {
            return em.find(Salle.class, id); // Récupère une salle par son ID
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère une salle par son libellé.
     *
     * @param libelle Libellé de la salle
     * @return La salle correspondante
     */
    public Salle getByLibelle(String libelle) {
        try {
            TypedQuery<Salle> query = em.createQuery("SELECT s FROM Salle s WHERE s.libelle = :libelle", Salle.class);
            query.setParameter("libelle", libelle);
            return query.getSingleResult(); // Retourne la salle correspondante au libellé
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ajoute un cours à une salle.
     *
     * @param salle Salle à laquelle ajouter le cours
     * @param cours Cours à ajouter
     */
    public void addCoursToSalle(Salle salle, Cours cours) {
        try {
            em.getTransaction().begin();
            salle.getCours().add(cours); // Ajoute le cours à la liste des cours de la salle
            cours.setSalle(salle); // Associe la salle au cours
            em.merge(salle); // Met à jour la salle dans la base
            em.merge(cours); // Met à jour le cours dans la base
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    /**
     * Supprime un cours d'une salle.
     *
     * @param salle Salle dont on veut supprimer le cours
     * @param cours Cours à supprimer
     */
    public void removeCoursFromSalle(Salle salle, Cours cours) {
        try {
            em.getTransaction().begin();
            salle.getCours().remove(cours); // Retire le cours de la salle
            cours.setSalle(null); // Déassocie la salle du cours
            em.merge(salle); // Met à jour la salle dans la base
            em.merge(cours); // Met à jour le cours dans la base
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
}
