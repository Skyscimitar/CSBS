package com.csb.csb_test_webview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import static com.firebase.ui.storage.R.styleable.View;

/**
 * Created by Danny on 06/06/2017.
 */

public class ExtendedItem extends Fragment {
    Integer count =1;
    Article article;
    Geoloc geoloc;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.extended_view, container, false);

        geoloc = Geoloc.getInstance(getActivity());

        TextView descriptionExtendedView = (TextView) view.findViewById(R.id.descriptionExtendedView);
        TextView prixExtendedView = (TextView) view.findViewById(R.id.prixExtendedView);
        TextView distanceExtendedView = (TextView) view.findViewById(R.id.distanceExtendedView);
        TextView nomVendeurExtendedView = (TextView) view.findViewById(R.id.nomVendeurExtendedView);
        descriptionExtendedView.setText(article.getNom());
        nomVendeurExtendedView.setText("De " + article.getSellerName() + " " + article.getSellerSurname());
         distanceExtendedView.setText(geoloc.getDistanceUser(
                Double.valueOf(article.getLatitude()),
                Double.valueOf(article.getLongitude())
        ));
        prixExtendedView.setText(article.getPrix().toString() + "€");
        ImageView img = (ImageView) view.findViewById(R.id.imgView);



        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(android.view.View.VISIBLE);
        Log.e("e",article.getStorageReference());
        Glide.with(this)
                .load(article.getStorageReference())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(android.view.View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(android.view.View.GONE);
                        return false;
                    }
                })
                .into(img);
        Button button = (Button)view.findViewById(R.id.call);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + article.getTelephone()));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                }else {     //have got permission
                    try{
                        startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getActivity(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button button1 = (Button)view.findViewById(R.id.text);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
                    {
                        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

                        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                        sendIntent.setData(Uri.parse("smsto:"+ article.getTelephone()));
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "text");

                        if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                        // any app that support this intent.
                        {
                            sendIntent.setPackage(defaultSmsPackageName);
                        }
                        startActivity(sendIntent);

                    }
                    else // For early versions, do what worked for you before.
                    {
                        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address",article.getTelephone());
                        startActivity(smsIntent);
                    }
                }
                catch(android.content.ActivityNotFoundException ex){
                    Toast.makeText(getActivity(),"No Application found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    public void incrementData(Article  article){
        this.article = article;
    }


}
