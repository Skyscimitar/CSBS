package com.csb.csb_test_webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;

import com.csb.csb_test_webview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {
    String cityName;
    Uri filePath;
    int PICK_IMAGE_REQUEST = 111;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://csbandroid-2ef3f.appspot.com");

    //private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        final EditText description = (EditText) findViewById(R.id.item_description);
        final EditText price = (EditText) findViewById(R.id.item_price);
        final CheckBox offer = (CheckBox) findViewById(R.id.item_offer);

        Button chooseImg = (Button) findViewById(R.id.chooseImg);



        chooseImg.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_PICK);
               startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
           }
        });

        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(filePath != null) {
                    StorageReference childRef = storageRef.child("image.jpg");

                    UploadTask uploadTask = childRef.putFile(filePath);
                }
                else {
                    Toast.makeText(AddItem.this, "Sélectionnez une image", Toast.LENGTH_SHORT).show();
                }
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
