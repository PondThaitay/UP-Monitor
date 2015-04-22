package com.cm_smarthome.myservice;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.widget.SwipeRefreshLayout;


public class MainActivity2Activity extends ActionBarActivity {

    MyService myService = new MyService();

    Context context = this;

    Sqlite mySqlite = new Sqlite(context);

    private Button btnStop;

    private TextView tvUsername;

    private String jsonResult;
    private String url = "http://up-ictmonitor.info/checkapp.php";
    private String Username;
    private String Status;

    private ListView listView;
    
    private SwipeRefreshLayout swipeLayout;

    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        listView = (ListView) findViewById(R.id.listView1);

        btnStop = (Button) findViewById(R.id.btnStop);

        tvUsername = (TextView) findViewById(R.id.Username);

        String arrData[] = mySqlite.SelectData("1");

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        accessWebService();
                        Toast.makeText(getBaseContext(), "อัพเดทข้อมูลเวลาสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });

        Username = arrData[1];
        tvUsername.setText("คุณ : " + Username);
        accessWebService();
        Intent intent = new Intent(MainActivity2Activity.this, MyService.class);
        startService(intent);
        
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeLayout.setColorScheme(android.R.color.holo_blue_dark,android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        accessWebService();
                        Toast.makeText(getBaseContext(), "อัพเดทข้อมูลสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ออกจากระบบสำเร็จ", Toast.LENGTH_SHORT).show();

                mySqlite.UpdateData("1", "2");

                Intent i = new Intent(MainActivity2Activity.this, MainActivity.class);
                startActivity(i);

                Intent intent = new Intent(MainActivity2Activity.this, MyService.class);
                stopService(intent);
            }
        });
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("username", Username));

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

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
            return null;
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
                Toast.makeText(getApplicationContext(), "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }
    }// end async task

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url});
    }

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("emp_info");

            if (jsonMainNode.length() > 0) {

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    String Name_Site = jsonChildNode.optString("Name_Site");
                    String Name_url = jsonChildNode.optString("Name_url");
                    String Date = jsonChildNode.optString("Date");


                        if(Name_Site.equals("NULL")){
                            String outPut = "ไม่มีเว็ปไซต์ที่ไม่สามารถติดต่อได้";
                            employeeList.add(createEmployee("employees", outPut));

                    }else {
                        String outPut = "ชื่อเว็ปไซต์ : " + Name_Site + "\n" + "URL : " + Name_url + "\n"
                                + "เวลา : " + Date + "\n" + "สถานะ : ไม่สามารถเข้าถึงได้"; //0 normal  1 Flase
                        employeeList.add(createEmployee("employees", outPut));
                    }
                }
            } else {
                Toast.makeText(context, "ไม่มีเว็ปไซต์ล้มเหลว", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
                android.R.layout.simple_list_item_1,
                new String[]{"employees"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> createEmployee(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }
}
