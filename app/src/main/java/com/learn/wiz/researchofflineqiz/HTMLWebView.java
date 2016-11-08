package com.learn.wiz.researchofflineqiz;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

public class HTMLWebView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmlweb_view);
        String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
                "<table><tr><td><input type='text' id='apple' value='1234'/></td><td>b</td></tr></table></html>";


        Element document = Jsoup.parse(html);
        String a= document.getElementById("apple").val();
        WebView myWebView=(WebView)findViewById(R.id.webView);

        File f=new File(Environment.getExternalStorageDirectory().toString(),"/sampleQizForm.html");
        if(f.exists()){
            Log.i("file  is exists", "file ");
            try{
                org.jsoup.nodes.Document doc = Jsoup.parse(f,null);
                String z= doc.getElementById("text").val();
                Toast.makeText(HTMLWebView.this, z, Toast.LENGTH_LONG).show();
                Log.i(z, "I got it");

            } catch (Exception e){
                Log.i(e.toString(), "onPostExecute: ");

            }
        } else{
            Log.i("not exists", "file");
        }

        String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),"sampleQizForm.html");

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(filePath);



//        myWebView.addJavascriptInterface(new Object()
//        {
//            @JavascriptInterface
//            public void performClick()
//            {
//                Toast.makeText(HTMLWebView.this, "OK click button from WebView", Toast.LENGTH_LONG).show();
//            }
//        }, "ok");
//
//        Button btnLogin=new Button(this);
//        btnLogin.setOnClickListener(t);
//
//        browser.addJavascriptInterface(btnLogin,"login");
//
//        @Override
//        public void onClick(View v) {
//
//            //do Something
//
//        }

//        myWebView.addJavascriptInterface(new Object()
//        {
//            @JavascriptInterface
//            public void performClick()
//            {
//                Log.d("LOGIN::", "Clicked");
//                Toast.makeText(HTMLWebView.this, "Login clicked", Toast.LENGTH_LONG).show();
//            }
//        }, "submit");
    }




}
