package com.learn.wiz.researchofflineqiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PostDataToAndroid extends AppCompatActivity {
    private WebView wv;
    EditText editText;
    TextView textView;
    Button button,buttonClick;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_data_to_android);

        editText = (EditText)findViewById(R.id.titlebar);
        textView = (TextView)findViewById(R.id.txt_html);
        button = (Button)findViewById(R.id.button);
        buttonClick = (Button)findViewById(R.id.buttonClick);

        wv = (WebView) findViewById(R.id.webview);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setUserAgentString("Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)");
        wv.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        wv.setWebChromeClient(new MyChromeClient());
        String filePath = String.format("file://%s/%s", Environment.getExternalStorageDirectory(),"index.html");
        wv.loadUrl(filePath);
        wv.requestFocus();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = editText.getText().toString();
                wv.loadUrl("javascript:sayHelloFromJS('"+txt+"')");
            }
        });
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wv.loadUrl("javascript:(function(){document.getElementById('buttonClick').click();})()");
            }
        });
    }

    class MyJavaScriptInterface
    {
        @SuppressWarnings("unused")
        public void showHTML(final String html)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(html);
                }
            });
        }
    }

    class MyChromeClient extends WebChromeClient{
    }

    class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

}
