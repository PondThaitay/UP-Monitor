package com.cm_smarthome.myservice;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdminPond on 21/3/2558.
 */
public class get {

    protected String statusLogin;

    private String jsonResult;
    private String url = "http://up-ictmonitor.info/checkapp.php";

    protected String Flag;
    protected String ID;

    //CheckLogin
    public void CheckLogin(String username, String password) {

        String url = "http://up-ictmonitor.info/loginapp1.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        String resultServer = getHttpPost(url, params);

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            statusLogin = c.getString("status");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getSatatusLogin() {
        return statusLogin;
    }
    //end CheckLogin

    public void update(String input) {
        String url = "http://up-ictmonitor.info/updateapp.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();//คำอธิบายอยู่ด้านล่าง
        params.add(new BasicNameValuePair("monitor", input));//คำอธิบายอยู่ด้านล่าง
        String resultServer = getHttpPost(url, params);//คำอธิบายอยู่ด้านล่าง

        //ส่งไป PHP แล้ว PHP รับ $strWhere = $_POST["ID"]; เผื่อแอพต้อง login
        //เพื่อแจ้งเตือนระบุคนไปเลยแต่ถ้าไม่มีการระบุก็ มะต้องแก้ก่อได้ หื้อ มัน ส่งไป เฉยๆๆ มะ ได้ เอา ไป Query
        //ถ้าต้องการแก้ แก้ Method getHttpPost ตวยเนอะ ฮาทำเผื่อคิงต้องการเฉยๆๆ

        JSONObject c;
        try {
            c = new JSONObject(resultServer);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getHttpPost(String url, List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public void C(String username) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", username));

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpResponse response = httpclient.execute(httppost);
            jsonResult = inputStreamToString(
                    response.getEntity().getContent()).toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("emp_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                Flag = jsonChildNode.optString("flag");
                ID = jsonChildNode.optString("Monitor_ID");
            }
            String Username = username;
            Log.e("Username", Username);
            Log.e("Flag", Flag);
            Log.e("ID", ID);


        } catch (JSONException e) {
            Log.e("Error 2.1", e.toString());
        }


    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            Log.e("Error 1", e.toString());
        }
        return answer;
    }
}
