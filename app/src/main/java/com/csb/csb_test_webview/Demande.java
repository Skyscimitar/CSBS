package com.csb.csb_test_webview;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.csb.csb_test_webview.R;
import com.google.android.gms.location.ActivityRecognition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.csb.csb_test_webview.R;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * Created by Danny on 07/06/2017.
 */

public class Demande extends ListFragment {
    comunicate cm;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("csb");
    private ArrayList<Article> article_data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_view, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText medit = (EditText) getActivity().findViewById(R.id.editText2);
        DatabaseReference ref = mDatabase.child("csb").child("demande");
        ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Activity  activity = getActivity();
                Article article = dataSnapshot.getValue(Article.class);
                Log.i("bite", "bite");
                article_data.add(article);
                List<Article> articles = article_data;
                Article article_dat[] = new Article[articles.size()];
                article_dat = articles.toArray(article_dat);
                final Article article_data[] = article_dat;
                ArticleAdapter offerAdapter = new ArticleAdapter(activity, R.layout.offer_view,article_data);
                final ListView listView = (ListView) activity.findViewById(android.R.id.list);
                listView.setAdapter(offerAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        comunicate cm;
                        cm = (comunicate) activity;
                        cm.sendData(article_data[position]);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("DB demande",error.toString());
            }
        });



    }

}
