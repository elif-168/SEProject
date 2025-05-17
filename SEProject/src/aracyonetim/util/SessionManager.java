package aracyonetim.util;

import aracyonetim.model.Kullanici;

/**
 * SessionManager is a singleton class that manages the current user session
 * It allows controllers to store and retrieve information about the currently logged-in user
 */
public class SessionManager {
    private static SessionManager instance;
    private Kullanici currentUser;

    // Private constructor for singleton pattern
    private SessionManager() {
    }

    // Get the single instance of SessionManager
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Set the current logged-in user
    public void setCurrentUser(Kullanici user) {
        this.currentUser = user;
    }

    // Get the current logged-in user
    public Kullanici getCurrentUser() {
        return currentUser;
    }

    // Check if a user is logged in
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    // Clear the session (logout)
    public void clearSession() {
        currentUser = null;
    }
}