package pl.polsl.quizwyzwanie.views.domain.model;

import java.io.Serializable;

public class AppUser implements Serializable{

    private String id;
    private String email;
    private String displayName;
    private Long wins;

    public AppUser() {
    }

    public AppUser(String id, String email, String displayName, Long wins) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.wins = wins;
    }

    public AppUser(String username, String email) {
        this.wins = Long.valueOf(0);
        this.email = email;
        this.displayName = username;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Long getWins() {
        return wins;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setWins(Long wins) {
        this.wins = wins;
    }
}
