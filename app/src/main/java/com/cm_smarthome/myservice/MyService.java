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
    private String Username;
    private String arrData[];

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRunning != false) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if (isRunning) {

                        try {

                            Thread.sleep(100);
                            arrData = mySqlite.SelectData("1");
                            Username = arrData[1];

                            if (Username.equals(null)) {
                                arrData = mySqlite.SelectData("1");
                                Username = arrData[1];

                                g1.C(Username);
                                String STATUS = g1.Flag;
                                String ID = g1.ID;

                                if (STATUS.equals("1")) {
                                    createNotification();
                                    g1.update(ID);
                                } else {
                                    Log.e("GG", "F");
                                }
                            } else {
                                g1.C(Username);
                                String STATUS = g1.Flag;
                                String ID = g1.ID;

                                if (STATUS.equals("1")) {
                                    createNotification();
                                    g1.update(ID);
                                } else {
                                    Log.e("GG", "F");
                                }
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
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

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setLatestEventInfo(this, Title, Message, activity);
        notification.number += 1;

        notification.defaults = Notification.DEFAULT_LIGHTS; // Sound
        notification.defaults = Notification.DEFAULT_VIBRATE; // Vibrate

        notificationManager.notify(1, notification);
    }
}