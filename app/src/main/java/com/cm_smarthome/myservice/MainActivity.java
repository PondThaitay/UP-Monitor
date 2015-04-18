package com.cm_smarthome.myservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    MyService myService = new MyService();

    Sqlite mySqlite = new Sqlite(this);

    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    Context context = this;

    get g1 = new get();

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mySqlite.getWritableDatabase();

        mySqlite.InsertData();

        String arrData[] = mySqlite.SelectData("1");

        if(arrData[2].equals("1")){
            Intent intent = new Intent(this, MainActivity2Activity.class);
            startActivity(intent);
        }

        cd = new ConnectionDetector(context);
        isInternetPresent = cd.isConnectingToInternet();

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);

        if (!isInternetPresent) {
            showAlertDialog(context, "เกิดข้อผิดพลาด", "กรุณาเชื่อต่อกับเครือข่าย", false);
        }

        findViewById(R.id.start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternet();
            }
        });
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public void CheckInternet() {

        cd = new ConnectionDetector(context);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            showAlertDialog(context, "เกิดข้อผิดพลาด", "กรุณาเชื่อต่อกับเครือข่าย", false);
        } else {
            g1.CheckLogin(username.getText().toString().toLowerCase(), password.getText().toString());

            if (g1.getSatatusLogin().equals("Yes")) {

                mySqlite.UpdateData("1", username.getText().toString(), "1");

                Toast.makeText(context, "เข้าสู่ระบบสำเร็จ", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, MainActivity2Activity.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง", Toast.LENGTH_LONG).show();
            }
        }
    }


}