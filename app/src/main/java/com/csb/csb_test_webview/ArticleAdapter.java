package com.csb.csb_test_webview;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.csb.csb_test_webview.R;

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
            holder.txtview = (TextView) row.findViewById(R.id.txtview);
            row.setTag(holder);
        }else{
            holder = (ArticleHolder) row.getTag();
        }
        Article article = data[position];
        if(article != null)
            holder.txtview.setText(article.toString());

        return row;
    }

    static class ArticleHolder{
        TextView txtview;
    }
}