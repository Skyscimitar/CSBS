package com.csb.csb_test_webview;

/**
 * Created by Danny on 07/06/2017.
 */


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.csb.csb_test_webview.R;


public class ListActivity extends FragmentActivity implements comunicate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        setContentView(R.layout.list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItem();
            }
        });

        Button demandButton = (Button) findViewById(R.id.button6);
        demandButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = ListActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Demande demandList = new Demande();
                fragmentTransaction.replace(R.id.fragment_container, demandList);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button offerButton = (Button) findViewById(R.id.button);
        offerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = ListActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Offre offerList = new Offre();
                fragmentTransaction.replace(R.id.fragment_container, offerList);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        Offre offerList = new Offre();
        offerList.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, offerList);
        fragmentTransaction.commit();


    }
    @Override
    public void sendData(Article article){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ExtendedItem extendedItem = new ExtendedItem();
        FragmentTransaction fragmentTransaction  = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, extendedItem,"ExtendedItem");
        extendedItem.incrementData(article);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void createItem() {
        Intent intent = new Intent(this, AddItem.class);
        startActivity(intent);
    }


}
