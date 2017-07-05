package com.csb.csb_test_webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class AddItem extends AppCompatActivity {
    String token;
    String cityName;
    Uri filePath;
    int PICK_IMAGE_REQUEST = 111;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://csbandroid-2ef3f.appspot.com");

    //private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        /*
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            Task<Location> getLocation = mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // ...
                            } else {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("Adress",addresses.toString());
                                if (addresses.size() > 0)
                                {
                                    cityName = addresses.get(0).getLocality();
                                }
                                else
                                {
                                    cityName = "Unknown";
                                }
                            }
                        }
                    });
        }*/
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
                    DataSender ds = new DataSender(description.getText().toString(), price.getText().toString(), "typeItem=1", token );
                    ds.execute();
                }else{
                    DataSender ds = new DataSender(description.getText().toString(), price.getText().toString(), "typeItem=2", token );
                    ds.execute();
                }

        }
    });
    }
}
