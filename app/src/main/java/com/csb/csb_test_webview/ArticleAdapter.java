package com.csb.csb_test_webview;


import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Danny on 06/06/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article>{
    Context context;
    int layoutResourceId;
    Article data[] = null;

    public ArticleAdapter(Context context, int layoutResourceId, Article[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ArticleHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ArticleHolder();
            holder.descriptionOfferView = (TextView) row.findViewById(R.id.descriptionOfferView);
            holder.prixOfferView = (TextView) row.findViewById(R.id.prixOfferView);
            holder.distanceOfferView = (TextView) row.findViewById(R.id.distanceOfferView);
            holder.nomVendeurOfferView = (TextView) row.findViewById(R.id.nomVendeurOfferView);
            holder.offerViewLayer = (ConstraintLayout) row.findViewById(R.id.offerViewLayer);
            holder.offerViewLayer.setVisibility(View.VISIBLE);
            row.setTag(holder);
        }else{
            holder = (ArticleHolder) row.getTag();
        }
        Article article = data[position];
        if(article != null) {
            holder.descriptionOfferView.setText(article.getNom());
            holder.prixOfferView.setText("prix : " + article.getPrix().toString() + "€");
            holder.nomVendeurOfferView.setText("Vendu par " + article.getSellerName() + " " + article.getSellerSurname());
            holder.distanceOfferView.setText("distance : " + article.getLatitude().toString() +"m");
        }

        return row;
    }

    static class ArticleHolder{
        TextView descriptionOfferView;
        TextView prixOfferView;
        TextView distanceOfferView;
        TextView nomVendeurOfferView;
        ConstraintLayout offerViewLayer;
    }
}