package pl.polsl.quizwyzwanie.domain.model;

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

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    @SuppressWarnings("unused")
    public Long getWins() {
        return wins;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    @SuppressWarnings("unused")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @SuppressWarnings("unused")
    public void setWins(Long wins) {
        this.wins = wins;
    }
}
