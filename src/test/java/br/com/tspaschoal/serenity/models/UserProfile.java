package br.com.tspaschoal.serenity.models;

public class UserProfile {

    private String name;

    private String job;

    public UserProfile(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }
}
