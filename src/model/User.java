package model;

public class User {
    private String username;
    private String password; 

    public User(String username, String password) {
        this.username = username;
        this.password = password; 
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean verifyPassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    @Override
    public String toString() {
        return username + "," + password;
    }
}
