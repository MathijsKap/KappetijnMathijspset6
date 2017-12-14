package com.example.hellvox.kappetijnmathijspset6;

public class UserTop {

    // Initialize variables
    private String mUsername;
    private Long mKarma;

    public UserTop(String username, Long karma) {
        this.mUsername = username;
        this.mKarma = karma;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public Long getKarma() {
        return mKarma;
    }

    public void setKarma(Long karma) {
        this.mKarma = karma;
    }

}
