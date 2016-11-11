package com.learn.wiz.researchofflineqiz.View;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.learn.wiz.researchofflineqiz.R;
import com.learn.wiz.researchofflineqiz.helper.DatabaseHelper;
import com.learn.wiz.researchofflineqiz.helper.DecompressZip;
import com.learn.wiz.researchofflineqiz.helper.DownloadFile;
import com.learn.wiz.researchofflineqiz.helper.ExternalStorage;
import com.learn.wiz.researchofflineqiz.helper.Utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    // button to show progress dialog
    Button btnShowProgress;

    // Progress Dialog
    private ProgressDialog pDialog;
    ImageView my_image;
    WebView myWebView;

    //URL textbox
    EditText txt_downloadURL;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url=null;

    // QizID for zip sub foler
    String quizID = null;


    //File path to parse by Intent
    String filePath =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this); //api after 23+ need to ask user's Permission
        // show progress bar button
        Utilities.db = new DatabaseHelper(getApplicationContext()); //initialed db

        txt_downloadURL = (EditText) findViewById(R.id.downloadURL);

        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);

        myWebView = (WebView) findViewById(R.id.webView);

        //Go to Test Page
        Button btnGo=(Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadHTMLData.class);
                intent.putExtra("filePath",filePath);
                startActivity(intent);
            }
        });

        /**
         * Show Progress bar click event
         * */
        btnShowProgress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting new Async Task
                new DownloadTask().execute(file_url=txt_downloadURL.getText().toString());
            }
        });

    }

    /**
     * Background task to download and unpack .zip file in background.
     */
    private class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = (String) params[0];

            try {
               return downloadAllAssets(url);
            } catch ( Exception e ) { return e.toString(); }

         //   return "";
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgress();
          //  String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),file_name);

            if ( result == null ) { return; }
            else { myWebView.loadUrl(filePath=String.format("file://%s/%s/%s",result,quizID,Utilities.file_name));}
            // something went wrong, post a message to user - you could use a dialog here or whatever
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG ).show();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // File Download
    //////////////////////////////////////////////////////////////////////////

    /**
     * Download .zip file specified by url, then unzip it to a folder in external storage.
     *
     * @param url
     */
    private String downloadAllAssets( String url ) {
        // Temp folder for holding asset during download
        File zipDir =  ExternalStorage.getSDCacheDir(this, "tmp");
        // File path to store .zip file before unzipping
        File zipFile = new File( zipDir.getPath() + "/temp.zip" );
        // Folder to hold unzipped output

        String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(url);
        //quizID is folder Name with QizID
        quizID = URLUtil.guessFileName(url,null,"").replace("."+fileExtenstion,"");
        File outputDir = ExternalStorage.getSDCacheDir( this,"Quiz/"+quizID);

        try {
            DownloadFile.download( url, zipFile, zipDir );
            unzipFile( zipFile, outputDir );
            return outputDir.toString();
        } finally {
            zipFile.delete();
        }

    }

    //////////////////////////////////////////////////////////////////////////
    // Zip Extraction
    //////////////////////////////////////////////////////////////////////////

    /**
     * Unpack .zip file.
     *
     * @param zipFile
     * @param destination
     */
    protected void unzipFile( File zipFile, File destination ) {
        DecompressZip decomp = new DecompressZip( zipFile.getPath(),
                destination.getPath() + File.separator );
        decomp.unzip();
    }

    protected void showProgress( ) {
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Progress");
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate( true );
        pDialog.setCancelable( false );
        pDialog.show();
    }

    protected void dismissProgress() {
        // You can't be too careful.
        if (pDialog != null && pDialog.isShowing() && pDialog.getWindow() != null) {
            try {
                pDialog.dismiss();
            } catch ( IllegalArgumentException ignore ) { ; }
        }
        pDialog = null;
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }
}

