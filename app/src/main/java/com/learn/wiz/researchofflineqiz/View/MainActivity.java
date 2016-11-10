package com.learn.wiz.researchofflineqiz.View;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.learn.wiz.researchofflineqiz.R;
import com.learn.wiz.researchofflineqiz.helper.DatabaseHelper;
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

    //File downloaded file name
    private static String file_name =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this); //api after 23+ need to ask user's Permission
        // show progress bar button
        Utilities.db = new DatabaseHelper(getApplicationContext()); //initialed db

        txt_downloadURL = (EditText) findViewById(R.id.downloadURL);

        btnShowProgress = (Button) findViewById(R.id.btnProgressBar);
        // Image view to show image after downloading

        myWebView = (WebView) findViewById(R.id.webView);

        //Go to Test Page
        Button btnGo=(Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadHTMLData.class);
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
                new DownloadFileFromURL().execute(file_name=txt_downloadURL.getText().toString());
            }
        });
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

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                file_name = new  File(url.getPath().toString()).getName();
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lenghtOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(String.format("/sdcard/%s",file_name=changeToDownloadedFileName(file_name)));

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            String filePath = String.format("file://%s/%s",Environment.getExternalStorageDirectory(),file_name);

            myWebView.loadUrl(filePath);
            try{
//                File f=new File(Environment.getExternalStorageDirectory().toString(),file_name);
//                if(f.exists()){
//                    Log.i("file  is exists", "file ");
//                }

            } catch (Exception e){
                Log.i(e.toString(), "onPostExecute: ");
            }
        }

        public String changeToDownloadedFileName(String fileName){

                int num = 0;
                File photo = new File(Environment.getExternalStorageDirectory(), fileName);
                String fileExtension =  getFileExtension(fileName);
                fileName = getfileNameWithoutExtension(fileName);
            while(photo.exists()) {
                    num=num+1;
                    fileName = fileName + num +fileExtension;
                    photo = new File(Environment.getExternalStorageDirectory(), fileName);
            }
            return fileName+fileExtension;
        }

        //Filename without Extension
        public String getfileNameWithoutExtension(String fileName){
            int pos = fileName.lastIndexOf(".");        //return last position of "."
            int endIndex=fileName.length()-1;
            if (pos > 0) {
                fileName = fileName.substring(0, pos);
            }
            return fileName;
        }

        //get FileExtension
        public String getFileExtension(String fileName){
            int pos = fileName.lastIndexOf(".");        //return last position of "."
            if (pos > 0) {
                fileName = fileName.substring(pos, fileName.length());
            }
            return fileName;
        }


    }
}

