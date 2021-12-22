package com.spacECE.spaceceedu.Test;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.spacECE.spaceceedu.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class listener extends Service {

    private String url = "http://worldtimeapi.org/api/timezone/Asia/Kolkata";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("hello world", "service");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    notif();

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response;

                    try {
                        response = client.newCall(request).execute();
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();




    }

    private void notif() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("SapceActive - Activity ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(200, builder.build());
        Log.d("hello world", "onReceive:notification sent ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
