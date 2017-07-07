package com.csb.csb_test_webview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by theOne on 07/07/2017.
 */

public class Geoloc {
    /** Constructeur privé */
    private LocationManager locationManager;
    private ArrayList<LocationProvider> providers;
    private ArrayList<String> names;
    private Double longitude;
    private Double latitude;
    private Context mContext;
    private Boolean haveBeenInitialised;

    private Geoloc(Context mContext_) {
        mContext = mContext_;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        providers = new ArrayList<LocationProvider>();
        names = new ArrayList<String>(locationManager.getProviders(true));
        longitude = 0.;
        latitude = 0.;

        Criteria critere = new Criteria();
        // Pour indiquer la précision voulue
        // On peut mettre ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision
        critere.setAccuracy(Criteria.ACCURACY_FINE);
        // Est-ce que le fournisseur doit être capable de donner une altitude ?
        critere.setAltitudeRequired(true);
        // Est-ce que le fournisseur doit être capable de donner une direction ?
        critere.setBearingRequired(false);
        // Est-ce que le fournisseur peut être payant ?
        critere.setCostAllowed(false);
        // Pour indiquer la consommation d'énergie demandée
        // Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
        critere.setPowerRequirement(Criteria.POWER_MEDIUM);
        // Est-ce que le fournisseur doit être capable de donner une vitesse ?
        critere.setSpeedRequired(false);

        for(String name : names)
            providers.add(locationManager.getProvider(name));

        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission","ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION disable");
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 150, new LocationListener() {
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
    }

    /** Instance unique pré-initialisée */
    private static Geoloc geoloc = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static Geoloc getInstance(Context mContext_)
    {
        if(geoloc == null) geoloc = new Geoloc(mContext_);
        return geoloc;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getDistanceUser(double latitudeEx,double longitudeEx){
        double distance = distance(latitude, longitude, latitudeEx, longitudeEx);
        boolean isK = false;
        if(distance > 2){
            distance /= 1000;
            isK = true;
        }
        distance = (double)Math.round(distance * 1000d) / 1000d;
        return String.valueOf(distance) + (isK ? "km" : "m");
    }

    // credits to http://www.geodatasource.com/developers/java
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        String unit = "M";
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
