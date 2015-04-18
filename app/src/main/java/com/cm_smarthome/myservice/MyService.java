package com.cm_smarthome.myservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    get g1 = new get();
    Sqlite mySqlite = new Sqlite(this);

    protected String mX = "";
    private static final String TAG = "HelloService";

    protected boolean isRunning = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                while (isRunning != false) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if (isRunning) {
                        g1.getS();
                        String XD = g1.x;

                        String arrData[] = mySqlite.SelectData("1");
                        String Username = arrData[1];

                        g1.C(Username);
                        String STATUS = g1.Flag;

                        if (STATUS.equals("1")) {
                            createNotification();
                            //g1.update();
                        }
                        Log.i(TAG, "Service running" + mX);
                    }
                }

                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }

    public void createNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification(android.R.drawable.ic_dialog_info,
                "ข้อความใหม่! จาก UP-Monitor", System.currentTimeMillis());

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        String Title = "ข้อความใหม่! จาก UP-Monitor";
        String Message = "มีบางเว็ปไซต์ของคุณได้เกิดการล้มเหลว";

        Intent intent = new Intent(this, MainActivity2Activity.class);

        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, Title, Message, activity);
        notification.number += 1;

        notification.defaults = Notification.DEFAULT_LIGHTS; // Sound
        notification.defaults = Notification.DEFAULT_VIBRATE; // Vibrate

        notificationManager.notify(1, notification);
    }
}