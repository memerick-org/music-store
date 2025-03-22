package model;

import java.io.*;
import java.util.HashMap;

public class UserManager {
    private HashMap<String, String> userCredentials; 
    private static final String USER_DB_FILE = "users.txt";

    public UserManager() {
        userCredentials = new HashMap<>();
        loadUserData();
    }

    public boolean createUser(String username, String password) {
        if (userCredentials.containsKey(username)) {
            return false; 
        }

        userCredentials.put(username, password);
        saveUserData();
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        if (!userCredentials.containsKey(username)) {
            return false;
        }

        String storedPassword = userCredentials.get(username);
        return storedPassword.equals(password);
    }

    public void printUsers() {
        for (String username : userCredentials.keySet()) {
            System.out.println("Username: " + username + ", Password: " + userCredentials.get(username));
        }
    }

    private void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DB_FILE))) {
            for (String username : userCredentials.keySet()) {
                writer.write(username + "," + userCredentials.get(username));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    private void loadUserData() {
        File file = new File(USER_DB_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }
    public User getUser(String username) {
        String password = userCredentials.get(username);
        if (password != null) {
            return new User(username, password);
        }
        return null;
    }
    
    
}
