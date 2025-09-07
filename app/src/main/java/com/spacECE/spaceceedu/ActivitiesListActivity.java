package com.spacECE.spaceceedu;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitiesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static DBController dbController;
    private Button buttonSetAlarm;
    List<ActivityData> activityDataList;
    ListView listViewActivityData;
    ActivityAdapter activityAdapter;
    DBController dbController2;
    final static String TAG = "ActivitiesListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        dbController = new DBController(this);

        // fetching all the records from SQLite Database
        //activityDataList = dbController.getAll();

        //Log.d(TAG, "onCreate: "+activityDataList.get(0).getData().get(0).getActivityTitle());


        //fetchUsingRetrofit();
        //fetchUsingVolley();

        //setDummyData();
        setDataFromSQLite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dbController.isNewUser() == 1) {
            Log.d(TAG, "onResume: notification created");
            //createNotificationChannel();
            //sendNotification();
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Reminder";
            String description = "New Activity is Available";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivityData activityData = (ActivityData) activityAdapter.getItem(position);
        Log.d(TAG, "onItemClick: "+activityData.getData().get(0).getActivityDevDomain());
        Intent intent = new Intent(getApplicationContext(), ActivityDetailsActivity.class);
        intent.putExtra("EXTRA_ACTIVITY", activityData);
        startActivity(intent);
    }

    public void fetchUsingRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.109.14.4/spacece/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONParser jsonParser = retrofit.create(JSONParser.class);
        Call<List<ActivityData>> call = jsonParser.getActivites();

        call.enqueue(new Callback<List<ActivityData>>() {
            @Override
            public void onResponse(Call<List<ActivityData>> call, Response<List<ActivityData>> response) {
                Log.d(TAG, "onResponse: "+response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(ActivitiesListActivity.this, "Fetched", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onResponse: Fetched");
                }
                listViewActivityData = findViewById(R.id.list_activity);
                activityDataList = response.body();
                activityAdapter = new ActivityAdapter(ActivitiesListActivity.this, activityDataList);
                listViewActivityData.setAdapter(activityAdapter);

            }

            @Override
            public void onFailure(Call<List<ActivityData>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                Log.d(TAG, "onFailure: isExecuted "+call.isExecuted());
                Log.d(TAG, "onFailure: isCancled"+call.isCanceled());
            }

        });
    }

    public void sendNotification(){

        Log.d(TAG, "sendNotification: called");
        //Toast.makeText(ActivitiesListActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ActivitiesListActivity.this, ReminderBroadCastReciever.class);

        int lastDay = dbController.isNewUser();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putInt("dayNo", lastDay);

        myEdit.commit();


        //make it 0 if not worked
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivitiesListActivity.this, 200, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();
        long tenSeconds = 1000 * 10;

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,5);
        calendar.set(Calendar.SECOND,0);

        Log.d(TAG, "sendNotification: "+calendar.getTime());
        //alarmManager.set(AlarmManager.RTC_WAKEUP, time + tenSeconds, pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        Log.d(TAG, "sendNotification: "+calendar.getTimeInMillis());
        Log.d(TAG, "sendNotification: tenSeconds "+(time+tenSeconds));

    }

    public void setDummyData(){
        // setting dummy data
        //Data data1 = new Data("2","INTRODUCTION TO LEARN ON APP"," *SpacECE-LearnOnApp* \\r\\n *Course001-Home as a Learning SPACE*\\r\\n*Day1: Introduction* \\r\\n\\r\\nhttps:\\/\\/youtu.be\\/byLb4z-uqEg\\r\\n\\r\\nThanks and Regards,\\r\\nLearnOnApp\\r\\nSpacECE India Foundation\\\"\\r\\n\\r\\n\"","Day1");

        //List<Data> dataList = new ArrayList<>();
        //dataList.add(data1);

        //ActivityData activityData = new ActivityData("Success",dataList,"found");

        /*activityDataList = new ArrayList<>();
        activityDataList.add(activityData);
        activityDataList.add(activityData);
        activityDataList.add(activityData);*/


        // inserting data onto SQsLiteDatabase
        //insertDataIntoSqlite(activityData);
        /*insertDataIntoSqlite(activityData);
        insertDataIntoSqlite(activityData);
        insertDataIntoSqlite(activityData);
        insertDataIntoSqlite(activityData); */


        activityDataList = dbController.getAll();
        //Log.d(TAG, "setDummyData: "+activityData.getData().get(0).getActivityDay());
        listViewActivityData = findViewById(R.id.list_activity);
        activityAdapter = new ActivityAdapter(this,activityDataList);
        listViewActivityData.setAdapter(activityAdapter);
        listViewActivityData.setOnItemClickListener(this);

    }

    public void setDataFromSQLite(){

        activityDataList = dbController.getAll();

        listViewActivityData = findViewById(R.id.list_activity);
        if(activityDataList.size() > 0) {
            activityAdapter = new ActivityAdapter(this, activityDataList);
            listViewActivityData.setAdapter(activityAdapter);
            listViewActivityData.setOnItemClickListener(this);
        }

    }


    public static void InsertDataIntoSqlite(ActivityData activityData){
        List<ActivityData> activityDataList = dbController.getAll();
        ActivityData lastActivity = activityDataList.get(activityDataList.size()-1);

        if(!(activityData.getData().get(0).getActivityNo() == lastActivity.getData().get(0).getActivityNo())){

        int status = dbController.insertRecord(activityData);
            if(status >0){
                Log.d(TAG, "setDummyData: Data Inserted"+status);
            }
        }
    }
}