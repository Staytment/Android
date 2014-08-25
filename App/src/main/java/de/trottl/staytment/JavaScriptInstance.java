package de.trottl.staytment;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONObject;

public class JavaScriptInstance {
    Context cntx;

    JavaScriptInstance(Context cntx) {
        this.cntx = cntx;
    }

    @JavascriptInterface
    public void showContent(String cont) {
        try {
            JSONObject json = new JSONObject(cont);
            Log.i("JSON", "JSON parsed successfully");
            setPreferences(json.optString("provider"),
                    json.optLong("identifier"),
                    json.optString("email"),
                    json.optString("name"),
                    json.optString("picture"),
                    json.optString("apiKey"));

            Log.i("JSON", "Email: " + json.optString("email"));
        } catch (Throwable t) {
            Log.e("JSON", "Can't parse JSON Object");
        }
    }

    @JavascriptInterface
    private void setPreferences(String provider, Long identifier, String email, String name, String picture, String apikey) {
        SharedPreferences shPref = this.cntx.getSharedPreferences("Staytment", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.putString("Provider", provider);
        editor.putLong("Identifier", identifier);
        editor.putString("Email", email);
        editor.putString("Name", name);
        editor.putString("Picture", picture);
        editor.putString("Apikey", apikey);
        editor.apply();
    }
}
