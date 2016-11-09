package com.learn.wiz.researchofflineqiz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadHTMLData extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_on_click_listener);
        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            //Inject JavaScript during runtime
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    view.loadUrl("javascript:" + buildInjection());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),"index.htm");
        webView.loadUrl(filePath);
    }


    @NonNull
    private String buildInjection() throws IOException {
        StringBuilder buf = new StringBuilder();
        InputStream inject = getAssets().open("inject.js");// file from assets
        BufferedReader in = new BufferedReader(new InputStreamReader(inject, "UTF-8"));
        String str;
        while ((str = in.readLine()) != null) {
            buf.append(str);
        }
        in.close();

        return buf.toString();
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
        public void htmlFilledUpData(String json_string) {

            List<String> list = new ArrayList<>();
            try{
                JSONObject json_data = new JSONObject(json_string);
                System.out.println("this is json-data "+json_data.toString());
                Toast.makeText(ReadHTMLData.this, json_data.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e){
                Toast.makeText(ReadHTMLData.this,e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
