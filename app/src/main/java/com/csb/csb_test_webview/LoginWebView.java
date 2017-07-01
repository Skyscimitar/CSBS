package com.csb.csb_test_webview;

import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_web_view);

        WebView webViewLogin = (WebView) findViewById(R.id.webViewLogin);

        webViewLogin.getSettings().setJavaScriptEnabled(true);

        webViewLogin.setWebViewClient(new WebViewAuth(this));

        webViewLogin.loadUrl("http://nicolasfley.fr:7898/auth");
    }
}
