package com.csb.csb_test_webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by theOne on 03/06/2017.
 */

public class WebViewAuth extends WebViewClient {
    Context context;
    WebViewAuth(Activity context_){
        context = context_;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri parsedUrl = Uri.parse(url);
        String state = parsedUrl.getQueryParameter("state");
        String code = parsedUrl.getQueryParameter("code");
        Log.i("info","code : "+ code);
        Log.i("info","state : "+ state);
        Log.i("info","url" + parsedUrl.toString());
        if (state != null && code != null){
            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra("token", code);
            context.startActivity(intent);
            return true;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        return false;
    }
}
