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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        setContentView(R.layout.activity_add_item);
        finalRef = storage.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("csb");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = new ArrayList<String>(locationManager.getProviders(true));
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

        for(String name : names)
            providers.add(locationManager.getProvider(name));

        Criteria critere = new Criteria();

        // Pour indiquer la précision voulue
        // On peut mettre ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision
        critere.setAccuracy(Criteria.ACCURACY_FINE);
        // Est-ce que le fournisseur doit être capable de donner une altitude ?
        critere.setAltitudeRequired(true);
        // Est-ce que le fournisseur doit être capable de donner une direction ?
        critere.setBearingRequired(true);
        // Est-ce que le fournisseur peut être payant ?
        critere.setCostAllowed(false);
        // Pour indiquer la consommation d'énergie demandée
        // Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
        critere.setPowerRequirement(Criteria.POWER_HIGH);
        // Est-ce que le fournisseur doit être capable de donner une vitesse ?
        critere.setSpeedRequired(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission","ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION disable");
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 150, new LocationListener() {
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                @Override
                public void onProviderEnabled(String provider) {
                }
                @Override
                public void onProviderDisabled(String provider) {
                }
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            });
        }
        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        }
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
        Article article = new Article(description, price, userInfos.tel, userInfos.prenom,userInfos.nom, urlPict, longitude, latitude);
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

            StorageReference riversRef = storageRef.child("images/pic.jpg");
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
            //you can display an error toast
        }
    }
}
