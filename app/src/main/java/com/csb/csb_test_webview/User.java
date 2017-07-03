package com.csb.csb_test_webview;

/**
 * Created by theOne on 03/07/2017.
 */

public class User {

    public String prenom;
    public String nom;
    public String tel;
    public String email;
    public String id;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        this.email = email;
        this.prenom = "";
        this.nom = "";
        this.tel = "";
    }
    public User(String email, String prenom, String nom, String tel) {
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
        this.tel = tel;
    }

}