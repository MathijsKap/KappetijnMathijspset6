package com.example.hellvox.kappetijnmathijspset6;

public class UserTop {

    // Initialize variables
    private String username;
    private Long karma;

    public UserTop(String username, Long karma) {
        this.username = username;
        this.karma = karma;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getKarma() {
        return karma;
    }

    public void setKarma(Long karma) {
        this.karma = karma;
    }

}
