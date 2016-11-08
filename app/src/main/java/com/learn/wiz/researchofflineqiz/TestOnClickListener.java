package com.learn.wiz.researchofflineqiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestOnClickListener extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_on_click_listener);
        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),"formsample.html");
        webView.loadUrl(filePath);
    }

    public class WebAppInterface {

        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void nextScreen(String pro_cat_id) {
//            startActivity(new Intent(mContext,
//                    MainActivity.class));
            List<String> list = new ArrayList<>();
            try{
                JSONObject json_data = new JSONObject(pro_cat_id);
//                String status = json_data.getString("hidden1");
//                JSONArray jsonArr = new JSONArray(status);
//
//                for (int i = 0; i < jsonArr.length(); i++)
//                {
//                    JSONObject jsonObj = jsonArr.getJSONObject(i);
//
//                    System.out.println(jsonObj);
//                }
                System.out.println("this is json-data "+json_data.toString());

            } catch (Exception e){

            }
           Toast.makeText(TestOnClickListener.this, list.toString(), Toast.LENGTH_LONG).show();
        }





    }


}
