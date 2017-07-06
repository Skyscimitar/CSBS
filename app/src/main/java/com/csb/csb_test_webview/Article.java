package com.csb.csb_test_webview;

import com.google.firebase.storage.StorageReference;

/**
 * Created by Danny on 06/06/2017.
 */

public class Article {
    private String nom;
    private String prix;
    private String telephone;
    private String sellerName;
    private String sellerSurname;
    private StorageReference storageReference;

    public Article(String nom, String prix, String telephone, String sellerName, String sellerSurname, StorageReference storageReference){
        this.nom = nom;
        this.prix = prix;
        this.telephone = telephone;
        this.sellerName = sellerName;
        this.sellerSurname = sellerSurname;
        this.storageReference = storageReference;
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

    public StorageReference getStorageReference() { return storageReference; }

    @Override
    public String toString(){
        return String.format(nom + " Prix:" + prix + "€");
    }
}
