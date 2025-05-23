package aracyonetim.util;

import aracyonetim.model.Kullanici;

/**
 * Singleton class to manage user sessions
 */
public class SessionManager {
    private static SessionManager instance;
    private Kullanici currentUser;

    private SessionManager() {
        // Private constructor to enforce singleton pattern
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Kullanici getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Kullanici currentUser) {
        this.currentUser = currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(String role) {
        return isLoggedIn() && currentUser.getRol().equals(role);
    }
}