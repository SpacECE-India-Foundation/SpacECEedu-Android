package com.spacECE.spaceceedu;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONObject;

public class ReminderBroadCastReciever extends BroadcastReceiver {

    final String TAG = "ReminderBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            //int dayNo = intent.getExtras().getInt("EXTRA_DAY_NO");
            //Log.d(TAG, "onReceive: Extra "+dayNo);
            DBController dbController = new DBController(context);
            ActivityData lastActivity = dbController.getLastActivity();
            int dayNo = dbController.isNewUser();
            dayNo++;

            Log.d(TAG, "onReceive: day"+dayNo);
            GetRecentActivity getRecentActivity = new GetRecentActivity(dayNo, context);
            getRecentActivity.execute();

            String activityNo = lastActivity.getData().get(0).getActivityNo();
            String activityName = lastActivity.getData().get(0).getActivityName();

            Intent repeatingIntent = new Intent(context,ActivityDetailsActivity.class);
            repeatingIntent.putExtra("EXTRA_ACTIVITY",lastActivity);
            repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,200,repeatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("SpaceActive - Activity "+activityNo)
                    .setContentText(activityName)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(200, builder.build());
            Log.d(TAG, "onReceive:notification sent ");
        }catch (Exception e){
            e.printStackTrace();
        }




    }


    class GetRecentActivity extends AsyncTask<String,Void, JSONObject> {

        final private JSONObject[] apiCall = {null};
        int dayNo;
        final private Context context;


        GetRecentActivity(int dayNo, Context context){
            this.dayNo = dayNo;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                JSONObject config = ConfigUtils.loadConfig(context);
                if (config != null) {
                    String baseUrl= config.getString("BASE_URL");
                    String reminderBroadCastReceiverUrl = config.getString("REMINDER_BROAD_CAST_RECEIVER_URL");

                    apiCall[0] = UsefulFunctions.UsingGetAPI(baseUrl+reminderBroadCastReceiverUrl+dayNo);
                    Log.d(TAG, "Object Obtained "+apiCall[0].toString());

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ActivityData activityData = gson.fromJson(apiCall[0].toString(),ActivityData.class);
                    //ActivitiesListActivity.InsertDataIntoSqlite(activityData);

                }
            }
            catch (RuntimeException runtimeException){
                Log.d(TAG, "RUNTIME EXCEPTION:::, Server did not response");
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR:::", "Failed to load API URLs");
            }

            return null;
        }
    }
}
