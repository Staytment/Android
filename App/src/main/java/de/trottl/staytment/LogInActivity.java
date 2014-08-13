package de.trottl.staytment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class LogInActivity extends Activity {
    Button btnLogIn_wo, btnLogIn_fb, btnLogin_gp, btnLogin_tw;
    WebView webview;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences shPref = getSharedPreferences("Staytment", Context.MODE_PRIVATE);
        String Auth_Email = shPref.getString("Email", null);

        if (!checkOnlineState()) {
            CustomToast customToast = new CustomToast(getApplicationContext(), getString(R.string.no_connectivity));
            customToast.show();
        }

        if (Auth_Email != null) {
            Intent MainAct = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MainAct);

            CustomToast cToast = new CustomToast(getApplicationContext(), getString(R.string.logged_in_successfully));
            cToast.show();

            finish();
        }

        webview = (WebView) findViewById(R.id.webview);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new JavaScriptInstance(this.getApplicationContext()), "HTML");

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance();

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("callback?error=")) {
                    Log.i("oAuth", "Error, access denied");
                    pDialog.dismiss();
                }

                return false;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                if (url.contains("callback?code=")) {
                    Log.i("Auth", "Call javascript code");
                    view.loadUrl("javascript:window.HTML.showContent(document.getElementsByTagName('pre')[0].innerHTML)");
                    pDialog.dismiss();
                    Intent MainAct = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(MainAct);

                    CustomToast cToast = new CustomToast(getApplicationContext(), getString(R.string.logged_in_successfully));
                    cToast.show();

                    finish();
                } else if (url.contains("about:blank")) {
                    Log.i("AboutBlank", "Blank Page was loaded");
                    pDialog.dismiss();
                } else {
                    Log.i("Auth", "No callback code");
                    webview.setVisibility(View.VISIBLE);
                    pDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webview.setVisibility(View.GONE);
            }
        });

        btnLogIn_wo = (Button) findViewById(R.id.btn_login_ex);
        btnLogIn_fb = (Button) findViewById(R.id.btnFb);
        btnLogin_gp = (Button) findViewById(R.id.btnGplus);
        btnLogin_tw = (Button) findViewById(R.id.btnTw);

        btnLogIn_wo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainAct = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainAct);

                finish();
            }
        });

        btnLogIn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = ProgressDialog.show(LogInActivity.this, "", "Please Wait...");
                webview.loadUrl("http://api.staytment.com/auth/facebook");
            }
        });

        btnLogin_gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = ProgressDialog.show(LogInActivity.this, "", "Please Wait...");
                webview.loadUrl("http://api.staytment.com/auth/google");
            }
        });

        btnLogin_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disabled because of no implementation in api
                //pDialog = ProgressDialog.show(LogInActivity.this, "", "Please Wait...");
                //webview.loadUrl("http://api.staytment.com/auth/twitter");
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (webview.getVisibility() == View.VISIBLE) {
            webview.loadUrl("about:blank");
            webview.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public boolean checkOnlineState() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
