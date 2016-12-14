package com.learn.wiz.researchofflineqiz.Model;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by student on 8/9/16.
 */
public class JSONParser {
    static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        } catch (Exception e) {
            Log.e("readStream Exception", StackTrace.trace(e));
        }
        return(sb.toString());
    }

    public static String getStream(String url) {
        InputStream is = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();
            is = conn.getInputStream();
        } catch (UnsupportedEncodingException e) {
            Log.e("getStream Exception", StackTrace.trace(e));
        } catch (Exception e) {
            Log.e("getStream Exception", StackTrace.trace(e));
        }
        return readStream(is);
    }

    public static String postStream(String url, String data) {
        InputStream is = null;
        String result=null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setFixedLengthStreamingMode(data.getBytes().length);
            conn.setRequestProperty("dataType","json");
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(data.getBytes());
            os.flush();
            is = conn.getInputStream();
            result= readStream(is);
        } catch (UnsupportedEncodingException e) {
            Log.e("postStream Exception", StackTrace.trace(e));
        } catch (Exception e) {
            Log.e("postStream Exception", StackTrace.trace(e));
        }
        return result;
    }

    public static JSONObject getJSONFromUrl(String url) {
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(getStream(url));
        } catch (JSONException e) {
            Log.e("Exception", StackTrace.trace(e));
        }
        return jObj;
    }


    public static JSONArray getJSONArrayFromUrl(String url) {
        JSONArray jArray = null;
        try {
            String dragData=getStream(url);
            dragData=dragData.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>","");
            dragData=dragData.replace("<string xmlns=\"http://tempuri.org/\">","");
            dragData=dragData.replace("</string>","");

            jArray = new JSONArray(dragData);
        } catch (JSONException e) {
            Log.e("Exception", StackTrace.trace(e));
        }
        return jArray;
    }

    public static String fromPojoToJson(Question question) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("question_id", question.getQuestion_id()); // Set the first name/pair
            jsonObj.put("status", question.getStatus());

            JSONArray jsonArr = new JSONArray();

            for (Answer ans : question.getAnswers() ) {
                JSONObject answer = new JSONObject();
                answer.put("answer_id", ans.getAnswer_id());
                answer.put("answer_name", ans.answer_name);
                answer.put("value", ans.value);
                jsonArr.put(answer);
            }
            jsonObj.put("answers", jsonArr);
            return jsonObj.toString();

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }

        return null;

    }

}
