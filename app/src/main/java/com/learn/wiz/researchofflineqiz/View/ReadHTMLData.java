package com.learn.wiz.researchofflineqiz.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.learn.wiz.researchofflineqiz.Controller.QuestionController;
import com.learn.wiz.researchofflineqiz.Model.Answer;
import com.learn.wiz.researchofflineqiz.Model.Question;
import com.learn.wiz.researchofflineqiz.R;
import com.learn.wiz.researchofflineqiz.helper.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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

<<<<<<< Updated upstream:app/src/main/java/com/learn/wiz/researchofflineqiz/View/ReadHTMLData.java
        //sampleQizForm.html is the html form that want to test
        String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),"sampleQizForm.html");
=======
        String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),"Qiz1024.html");
>>>>>>> Stashed changes:app/src/main/java/com/learn/wiz/researchofflineqiz/ReadHTMLData.java
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
            List<Question> list = new ArrayList<>();
            try{
                JSONObject json_data = new JSONObject(json_string);
                Iterator iterator = json_data.keys();
                JSONArray jArray = null;
                while(iterator.hasNext()) {
                    //    System.out.println("This is iterated string "+json_data.get(iterator.next().toString()));
                    String question_key =iterator.next().toString();
                    jArray =new JSONArray(json_data.get(question_key).toString());
                    System.out.println("This is JArray string "+jArray.toString());

                //    List<Answer> list_answers =new ArrayList<>();
                    Question q = new Question(question_key);

                    //get Answer values as [key:value]
                    for(int i=0;i<jArray.length();i++){
                        JSONObject data = new JSONObject(jArray.get(i).toString());
//                        System.out.println("This is data object "+data.toString());
//                        System.out.println("This is data keys "+data.keys().next().toString());
//                        System.out.println("This is value "+ data.get(data.keys().next().toString()).toString());
                        new Answer(data.keys().next().toString(),data.get(data.keys().next().toString()).toString(),q);
                    }
                    list.add(q);

                }

                createQuestions(list);
               // System.out.println("this is json-data "+json_data.toString());
             //   Toast.makeText(ReadHTMLData.this, list.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReadHTMLData.this, Sqlite.class);
                startActivity(intent);
            } catch (Exception e){
                Toast.makeText(ReadHTMLData.this,e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public void createQuestions(List<Question> questions){

        for (Question question:
                questions) {
            Utilities.db.createQuestion(question);
            for (Answer answer:
                    question.getAnswers()){
                Utilities.db.createAnswer(answer);
            }
        }

        System.out.println("Finished Created Questions and answers ");
    }

}
