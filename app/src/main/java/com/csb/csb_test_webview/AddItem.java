package com.csb.csb_test_webview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;

import com.csb.csb_test_webview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.csb.csb_test_webview.R.styleable.View;

public class AddItem extends AppCompatActivity {
    String token;
    double longitude;
    double latitude;
    String urlPict = "";
    Uri filePath;
    int PICK_IMAGE_REQUEST = 111;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://csbandroid-2ef3f.appspot.com");
    LocationManager locationManager;
    private StorageReference finalRef;


    //private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private User userInfos;
    Geoloc geoloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        setContentView(R.layout.activity_add_item);
        finalRef = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("csb");
        geoloc = Geoloc.getInstance(this);
        longitude = 0;
        latitude = 0;
        urlPict = "";

        Log.e("user",user.getUid());
        mDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userInfos = snapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final EditText description = (EditText) findViewById(R.id.item_description);
        final EditText price = (EditText) findViewById(R.id.item_price);
        final CheckBox offer = (CheckBox) findViewById(R.id.item_offer);
        final CheckBox demand = (CheckBox) findViewById(R.id.item_demand);

        Button chooseImg = (Button) findViewById(R.id.chooseImg);

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demand.setChecked(!demand.isChecked());
            }
        });

        demand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offer.setChecked(!offer.isChecked());
            }
        });

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
            public void onClick(View v) {

                String priceVal = price.getText().toString();
                String descriptionVal = description.getText().toString();
                if (priceVal.equals("") || descriptionVal.equals("") || !(offer.isChecked() || demand.isChecked()))
                    Toast.makeText(AddItem.this, "Merci de bien vouloir remplir tous les champs", Toast.LENGTH_SHORT).show();
                else{
                    if (offer.isChecked()) {
                        writeArticle(descriptionVal, priceVal, true);
                    } else if (demand.isChecked()) {
                        writeArticle(descriptionVal, priceVal, false);
                    }
                    finish();
                }

        }
    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            uploadFile();
        }
    }

    protected void writeArticle(String description, String price, Boolean offer ) {
        Article article = new Article(description, price, userInfos.tel, userInfos.prenom,userInfos.nom, urlPict, geoloc.getLongitude(), geoloc.getLatitude());
        Date d = new Date();
        String idObj = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssS");
        idObj = format.format(d);
        if(offer){
            mDatabase.child("offer").child(idObj).setValue(article);
        }else{
            mDatabase.child("demand").child(idObj).setValue(article);
        }
    }
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssS");
            String idObj = format.format(new Date());

            StorageReference riversRef = storageRef.child("images/"+idObj+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            urlPict = taskSnapshot.getDownloadUrl().toString();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded , url is : "+urlPict, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Log.e("Pict upload issue","Error while uploading file");
            //you can display an error toast
        }
    }
}
