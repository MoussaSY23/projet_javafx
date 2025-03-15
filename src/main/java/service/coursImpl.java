package service;

import entity.Cours;
import entity.JPAUtil;
import entity.Salle;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class coursImpl implements IRepository<Cours> {

    public EntityManager getEntityManager() {
        return JPAUtil.getEntityManagerFactory().createEntityManager();
    }
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public coursImpl() {
        this.emf = Persistence.createEntityManagerFactory("PERSISTENCE");
        this.em = emf.createEntityManager();
    }

    @Override
    public void add(Cours cours) {
        try {
            em.getTransaction().begin();

            // Vérification de conflit d'horaire
            if (!isHoraireDisponible(cours.getSalle(), cours.getHeureDebut(), cours.getHeureFin())) {
                throw new IllegalArgumentException("Conflit d'horaire détecté pour cette salle !");
            }
            if (cours.getHeureDebut() == null || cours.getHeureFin() == null) {
                throw new IllegalArgumentException("Les horaires doivent être spécifiés.");
            }

            em.persist(cours);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cours cours) {
        try {
            em.getTransaction().begin();

            // Vérification de conflit d'horaire lors de la mise à jour
            if (!isHoraireDisponible(cours.getSalle(), cours.getHeureDebut(), cours.getHeureFin())) {
                throw new IllegalArgumentException("Conflit d'horaire détecté pour cette salle !");
            }

            em.merge(cours);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Cours cours) {
        try {
            em.getTransaction().begin();
            Cours toDelete = em.find(Cours.class, cours.getId());
            if (toDelete != null) {
                em.remove(toDelete);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Cours> getAll() {
        try {
            TypedQuery<Cours> query = em.createQuery("SELECT c FROM Cours c", Cours.class);
            List<Cours> coursList = query.getResultList();
            return FXCollections.observableArrayList(coursList);
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    @Override
    public Cours getById(long id) {
        try {
            return em.find(Cours.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Vérifie la disponibilité horaire dans une salle.
     *
     * @param salle      Salle à vérifier
     * @param heureDebut Heure de début souhaitée
     * @param heureFin   Heure de fin souhaitée
     * @return true si l'horaire est disponible, false sinon
     */
    public boolean isHoraireDisponible(Salle salle, LocalDateTime heureDebut, LocalDateTime heureFin) {
        try {
            TypedQuery<Cours> query = em.createQuery(
                    "SELECT c FROM Cours c WHERE c.salle = :salle AND " +
                            "(:heureDebut < c.heureFin AND :heureFin > c.heureDebut)", Cours.class);
            query.setParameter("salle", salle);
            query.setParameter("heureDebut", heureDebut);
            query.setParameter("heureFin", heureFin);

            return query.getResultList().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        em.close();
        emf.close();
    }

    public ObservableList<Cours> getCoursByProfesseur(User professeur) {

        try {
            List<Cours> coursList = em.createQuery(
                            "SELECT c FROM Cours c WHERE c.professeur = :professeur", Cours.class)
                    .setParameter("professeur", professeur)
                    .getResultList();
            return FXCollections.observableArrayList(coursList);
        } finally {
            em.close();
        }
    }

    public Cours getByNom(String nom) {
        EntityManager entityManager = getEntityManager();
        Cours cFound = null;

        try {
            entityManager.getTransaction().begin();
            cFound = entityManager.createQuery("SELECT u FROM Cours u WHERE u.nom = :nom", Cours.class)
                    .setParameter("nom", nom)
                    .getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Erreur lors de la récupération du cours : " + e.getMessage());
        } finally {
            entityManager.close();
        }

        return cFound;
    }

}
