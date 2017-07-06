package com.csb.csb_test_webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;

import com.csb.csb_test_webview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItem extends AppCompatActivity {
    String token;
    double longitude;
    double latitude;
    Uri filePath;
    int PICK_IMAGE_REQUEST = 111;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://csbandroid-2ef3f.appspot.com");
    LocationManager locationManager;


    //private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = new ArrayList<String>(locationManager.getProviders(true));
        longitude = 0;
        latitude = 0;

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
        latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        final EditText description = (EditText) findViewById(R.id.item_description);
        final EditText price = (EditText) findViewById(R.id.item_price);
        final CheckBox offer = (CheckBox) findViewById(R.id.item_offer);
        final CheckBox demand = (CheckBox) findViewById(R.id.item_demand);

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
            public void onClick(View v) {
                if (filePath != null) {
                    StorageReference childRef = storageRef.child( /* TODO Convention */"image.jpg");
                    UploadTask uploadTask = childRef.putFile(filePath);
                } else {
                    Toast.makeText(AddItem.this, "Sélectionnez une image", Toast.LENGTH_SHORT).show();
                }
                String priceVal = price.getText().toString();
                String descriptionVal = price.getText().toString();
                if (priceVal.equals("") || descriptionVal.equals(""))
                    Toast.makeText(AddItem.this, "Merci de bien vouloir remplir tous les champs", Toast.LENGTH_SHORT).show();
                else{
                    if (offer.isChecked()) {
                        DataSender ds = new DataSender(descriptionVal, priceVal, "typeItem=1", token, storageRef /* Il faut peut être mettre childRef, à tester */);
                        ds.execute();
                    } else if (demand.isChecked()) {
                        DataSender ds = new DataSender(descriptionVal, priceVal, "typeItem=2", token, storageRef /* Il faut peut être mettre childRef, à tester */);
                        ds.execute();
                    }
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
