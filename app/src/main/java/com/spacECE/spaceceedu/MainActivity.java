package com.spacECE.spaceceedu;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.Location.LocationService;
import com.spacECE.spaceceedu.VideoLibrary.Topic;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity;
import com.squareup.picasso.Picasso;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    public static Account ACCOUNT=null;
    UserLocalStore userLocalStore;
    DBController dbController;
    int dayNo;
    public final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocalStore = new UserLocalStore(getApplicationContext());

        Log.i("DEVICE TOKEN","In next line");
        //Android ID:
        //Log.i("DEVICE TOKEN : ",Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if(authenticate()){
            getDetails();
        }

        if (firstStart) {
            //causing crash on first boot
//            FirebaseMessaging.getInstance().getToken()
//                    .addOnCompleteListener(new OnCompleteListener<String>() {
//                        @Override
//                        public void onComplete(@NonNull Task<String> task) {
//                            if (!task.isSuccessful()) {
//                                Log.w("FCM TOKEN : ", "Fetching FCM registration token failed", task.getException());
//                                return;
//                            }
//                            Log.d("FCM TOKEN : ", task.getResult());
//                            sendTokenToServer(task.getResult());
//                        }
//                    });

            prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        //Firebase Cloud Messaging for PushNotification
        FirebaseMessaging.getInstance().subscribeToTopic("Notify");

        //Bottom navigation bar
        BottomNavigationView bottomNav = findViewById(R.id.Main_Bottom_Navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        //Navigation Drawer
        drawer = findViewById(R.id.Main_NavView_drawer);

        //Toolbar support for navigationDrawer
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NavigationDrawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(ACCOUNT!=null) {

            Toast.makeText(this, ACCOUNT.getUsername(), Toast.LENGTH_SHORT).show();
            toolbar.setTitle("Hello "+ACCOUNT.getUsername()+" !");
            Toast.makeText(this, ACCOUNT.getUsername(), Toast.LENGTH_SHORT).show();
            NavigationView navigationView = (NavigationView) findViewById(R.id.Main_navView_drawer);

            // get menu from navigationView
            View navHead = navigationView.getHeaderView(0);

            // find MenuItem you want to change
            ImageView nav_camara = navHead.findViewById(R.id.Main_nav_drawer_profile_pic);
            if(!ACCOUNT.getProfile_pic().isEmpty()){
                Picasso.get().load(ACCOUNT.getProfile_pic()).into(nav_camara);
            }
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout,
                    new FragmentMain()).commit();
        }

        GetLists getLists= new GetLists();
        getLists.execute();

        DBController dbController = new DBController(MainActivity.this);

        if(dbController.isNewUser() == 0) {
            Toast.makeText(MainActivity.this,"Ram",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: "+"new User");
            createNotificationChannel();
            sendNotification();
            GetFirstActivity getActivities = new GetFirstActivity();
            getActivities.execute();
        }

        //Starting Location Service
        LocationService locationService = new LocationService();
        locationService.Start(this, this);

    }

    private void sendTokenToServer(String token) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UsefulFunctions.UsingGetAPI("http://educationfoundation.space/ConsultUs/api_token?email="+ACCOUNT.getAccount_id()+"&token="+token);
            }
        });
        thread.start();

    }

    private void getDetails() {
        ACCOUNT = userLocalStore.getLoggedInAccount();
    }

    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

    NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new FragmentMain();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new FragmentProfile();
                            break;
                        case R.id.nav_help:
                            selectedFragment = new FragmentAbout();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(ACCOUNT!=null){
            inflater.inflate(R.menu.options_main_activity_loggedin, menu);
        }else {
            inflater.inflate(R.menu.options_main_activity, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.button_signOut:
                signOut();
                return true;
            case R.id.button_signIn:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void sendNotification(){

        Log.d(TAG, "sendNotification: called");
        //Toast.makeText(ActivitiesListActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, ReminderBroadCastReciever.class);

        //int lastDay = dbController.isNewUser();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        //myEdit.putInt("dayNo", lastDay);

        myEdit.commit();


        //make it 0 if not worked
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 200, intent, 0);

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
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        Log.d(TAG, "sendNotification: "+calendar.getTimeInMillis());
        Log.d(TAG, "sendNotification: tenSeconds "+(time+tenSeconds));

    }

    private void signOut() {
        userLocalStore.clearUserData();
        ACCOUNT = null;
        userLocalStore.setUserLoggedIn(false);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    class GetLists extends AsyncTask<String, Void, JSONObject> {
        final private JSONObject[] apiCall = {null};

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {

                apiCall[0] = UsefulFunctions.UsingGetAPI("http://educationfoundation.space/SpacTube/api_all?uid=1&type=all");
                Log.i("Object Obtained: ", apiCall[0].toString());

                JSONArray jsonArray = null;
                JSONArray recentJsonArray = null;
                JSONArray trendingJsonArray = null;

                try {
                    jsonArray = apiCall[0].getJSONArray("data");
                    recentJsonArray = apiCall[0].getJSONArray("data_recent");
                    trendingJsonArray = apiCall[0].getJSONArray("data_trending");
                    Log.i("API : ", apiCall[0].toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("JSON Error: ", "Key not found in JSON");
                }

                try {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                        Topic newTopic = new Topic(response_element.getString("status"), response_element.getString("title"),
                                response_element.getString("v_id"), response_element.getString("filter"),
                                response_element.getString("length"), response_element.getString("v_url"),
                                response_element.getString("v_date"), response_element.getString("v_uni_no"),
                                response_element.getString("desc"), response_element.getString("cntlike"),
                                response_element.getString("cntdislike"), response_element.getString("views"),
                                response_element.getString("cntcomment"));
                        VideoLibrary_Activity.topicList.add(newTopic);
                        if (response_element.getString("status").equalsIgnoreCase("created")) {
                            VideoLibrary_Activity.paidTopicList.add(newTopic);
                        } else {
                            VideoLibrary_Activity.freeTopicList.add(newTopic);
                        }
                    }

                    Log.i("Paid : ", VideoLibrary_Activity.paidTopicList.toString() + "///" + VideoLibrary_Activity.paidTopicList.size());
                    Log.i("Free : ", VideoLibrary_Activity.freeTopicList.toString() + "///" + VideoLibrary_Activity.freeTopicList.size());

                    for (int i = 0; i < recentJsonArray.length(); i++) {

                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                        Topic newTopic = new Topic(response_element.getString("status"), response_element.getString("title"),
                                response_element.getString("v_id"), response_element.getString("filter"),
                                response_element.getString("length"), response_element.getString("v_url"),
                                response_element.getString("v_date"), response_element.getString("v_uni_no"),
                                response_element.getString("desc"), response_element.getString("cntlike"),
                                response_element.getString("cntdislike"), response_element.getString("views"),
                                response_element.getString("cntcomment"));
                        VideoLibrary_Activity.recentTopicList.add(newTopic);
                    }
                    Log.i("Recent : ", VideoLibrary_Activity.recentTopicList.toString());

                    for (int i = 0; i < trendingJsonArray.length(); i++) {

                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                        Topic newTopic = new Topic(response_element.getString("status"), response_element.getString("title"),
                                response_element.getString("v_id"), response_element.getString("filter"),
                                response_element.getString("length"), response_element.getString("v_url"),
                                response_element.getString("v_date"), response_element.getString("v_uni_no"),
                                response_element.getString("desc"), response_element.getString("cntlike"),
                                response_element.getString("cntdislike"), response_element.getString("views"),
                                response_element.getString("cntcomment"));
                        VideoLibrary_Activity.topicList.add(newTopic);
                        VideoLibrary_Activity.trendingTopicList.add(newTopic);
                    }
                    Log.i("TRENDING : ", VideoLibrary_Activity.trendingTopicList.toString());

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                    Log.i("JSON Object : ", "Key not found");
                }
                VideoLibrary_Activity.ArrayDownloadCOMPLETED[0] = true;
                Log.i("VIDEOS:::::", VideoLibrary_Activity.topicList.toString());

                return null;
            }    catch (RuntimeException runtimeException){
                Log.i("RUNTIME EXCEPTION:::", "Server did not respons");
            }
            return null;
        }
    }

    class GetFirstActivity extends AsyncTask<String,Void,JSONObject>{

        final private JSONObject[] apiCall = {null};

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {

                apiCall[0] = UsefulFunctions.UsingGetAPI("http://educationfoundation.space/spacece/api/spaceactive_activities.php?ano=1");
                Log.d(TAG, "Object Obtained "+apiCall[0].toString());

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                ActivityData activityData = gson.fromJson(apiCall[0].toString(),ActivityData.class);
                Log.d(TAG, "doInBackground: activity_dev_domain "+activityData.getData().get(0).getActivityDevDomain());
                //List<Data> list = activityData.getData();

                DBController dbController = new DBController(MainActivity.this);
                dbController.insertRecord(activityData);
                Log.d(TAG, "doInBackground: "+dbController.isNewUser());

            }catch (RuntimeException runtimeException){
                Log.d(TAG, "RUNTIME EXCEPTION:::, Server did not respons");
            }

            return null;
        }
    }

}