package service;

import entity.User;

public class UserSession {
    private static User currentUser;

    // Méthode pour obtenir l'utilisateur actuel
    public static User getCurrentUser() {
        return currentUser;
    }

    // Méthode pour définir l'utilisateur actuel (par exemple lors de la connexion)
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Méthode pour réinitialiser la session (par exemple lors de la déconnexion)
    public static void clearSession() {
        currentUser = null;
    }
}
