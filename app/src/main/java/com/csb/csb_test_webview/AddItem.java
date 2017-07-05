package com.csb.csb_test_webview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;



import com.csb.csb_test_webview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {
    String cityName;
    //private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        final EditText description = (EditText) findViewById(R.id.item_description);
        final EditText price = (EditText) findViewById(R.id.item_price);
        final CheckBox offer = (CheckBox) findViewById(R.id.item_offer);
        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(offer.isChecked()){
                    writeArticle(description.getText().toString(), price.getText().toString(), true);
                }else{
                    writeArticle(description.getText().toString(), price.getText().toString(), false);
                }

        }
    });
    }

    protected void writeArticle(String description, String price, Boolean offer ) {
        Article article = new Article(description, price, "000", user.getDisplayName(),user.getDisplayName());
        if(offer){
            mDatabase.child("offer").child(description).setValue(article);
            Log.i("INFO","bite");
        }else{
            mDatabase.child("demand").child(description).setValue(article);
        }
    }
}
