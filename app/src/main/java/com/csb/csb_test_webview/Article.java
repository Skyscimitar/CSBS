package com.csb.csb_test_webview;

/**
 * Created by Danny on 06/06/2017.
 */

public class Article {
    private String nom;
    private String prix;
    private String telephone;
    private String sellerName;
    private String sellerSurname;

    public Article(String nom, String prix, String telephone, String sellerName, String sellerSurname){
        this.nom = nom;
        this.prix = prix;
        this.telephone = telephone;
        this.sellerName = sellerName;
        this.sellerSurname = sellerSurname;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPrix() {
        return prix;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerSurname() {
        return sellerSurname;
    }

    @Override
    public String toString(){
        return String.format(nom + " Prix:" + prix + "€");
    }
}
