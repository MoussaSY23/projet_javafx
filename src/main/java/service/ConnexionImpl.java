package service;

import entity.JPAUtil;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class ConnexionImpl implements IRepository<User>{
    public ConnexionImpl() {
    }
    public EntityManager getEntityManager() {
        return JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public void add(User obj) {
        var em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de l'utilisateur !");
        } finally {
            em.close();
        }
    }


    @Override
    public void update(User obj) {
        var em = getEntityManager();
        em.getTransaction().begin();
        em.merge(obj);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(User obj) {
        var em = getEntityManager();
        User userToDelete = em.find(User.class, obj.getId());

        if (userToDelete != null) {
            em.getTransaction().begin();
            em.remove(userToDelete);
            em.getTransaction().commit();
        }
        em.close();
    }

    @Override
    public ObservableList<User> getAll() {
        ObservableList<User> list = FXCollections.observableArrayList();
        var em = getEntityManager();
        em.getTransaction().begin();
        List<User> resultList = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        list.addAll(resultList);
        em.getTransaction().commit();
        em.close();
        return list;

    }

    @Override
    public User getById(long id) {
        var em = getEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        em.getTransaction().commit();
        em.close();
        return user;
    }


    public User getByEmail(String email) {
        EntityManager entityManager = getEntityManager();
        User userFound = null;

        try {
            entityManager.getTransaction().begin();
            userFound = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        } finally {
            entityManager.close();
        }

        return userFound;
    }

    public User getByPrenom(String prenom) {
        EntityManager entityManager = getEntityManager();
        User userFound = null;

        try {
            entityManager.getTransaction().begin();
            userFound = entityManager.createQuery("SELECT u FROM User u WHERE u.prenom = :prenom", User.class)
                    .setParameter("prenom", prenom)
                    .getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        } finally {
            entityManager.close();
        }

        return userFound;
    }



    public boolean verifierCompte(String email, String password) {
        EntityManager entityManager = getEntityManager();
        boolean exists = false;

        try {
            entityManager.getTransaction().begin();

            // Vérifier si l'email existe d'abord
            User user = null;
            try {
                user = entityManager.createQuery(
                                "SELECT u FROM User u WHERE u.email = :email", User.class)
                        .setParameter("email", email)
                        .getSingleResult();
            } catch (NoResultException e) {
                System.out.println("Email non trouvé en base.");
                return false;
            }

            // Vérifier le mot de passe
            if (user != null) {
                System.out.println("Utilisateur trouvé : " + user.getEmail());
                if (BCrypt.checkpw(password, user.getPassword())) {
                    exists = true;
                } else {
                    System.out.println("Mot de passe incorrect !");
                }
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();  // Voir l'erreur complète
        } finally {
            entityManager.close();
        }

        return exists;
    }


    public ObservableList<User> getProfesseurs() {
        ObservableList<User> professeursList = FXCollections.observableArrayList();
        var em = getEntityManager();
        try {
            em.getTransaction().begin();
            // Récupérer les utilisateurs dont le rôle est "Professeur"
            List<User> professeurs = em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                    .setParameter("role", "Professeur")
                    .getResultList();
            professeursList.addAll(professeurs);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        return professeursList;
    }

    public User verifyUser(String email, String password) {
        EntityManager entityManager = getEntityManager();

            entityManager.getTransaction().begin();

            // Vérifier si l'email existe d'abord
            User user = null;

                user = entityManager.createQuery(
                                "SELECT u FROM User u WHERE u.email = :email", User.class)
                        .setParameter("email", email)
                        .getSingleResult();

            entityManager.getTransaction().commit();


        return user;
    }

    public boolean login(String email, String password) {
        // Code pour vérifier les informations de connexion (email et mot de passe)
        User user = verifyUser(email, password);  // Méthode fictive qui vérifie les données d'utilisateur

        if (user != null) {
            // Si l'utilisateur est trouvé et que le mot de passe est correct
            UserSession.setCurrentUser(user);  // Stocke l'utilisateur dans la session
            return true;
        }
        return false;  // Si l'authentification échoue
    }

    public User getCurrentUser() {
        // Retourne l'utilisateur connecté depuis la session
        return UserSession.getCurrentUser();
    }



}
