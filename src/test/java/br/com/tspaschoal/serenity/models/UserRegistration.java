package br.com.tspaschoal.serenity.models;

public class UserRegistration {

    private String email;

    private String password;

    public UserRegistration(String email) {
        this.email = email;
    }

    public UserRegistration(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
