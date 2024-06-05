package com.spacECE.spaceceedu;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.Location.LocationService;
import com.spacECE.spaceceedu.Utils.Notification;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.spacECE.spaceceedu.VideoLibrary.Topic;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity;

import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
    protected void onPostResume() {
        super.onPostResume();
        SetAccountDetails();


    }

    private void SetAccountDetails() {
        if(ACCOUNT!=null) {
            toolbar.setTitle("Hello "+ACCOUNT.getUsername()+" !");
            NavigationView navigationView = (NavigationView) findViewById(R.id.Main_navView_drawer);

            // get menu from navigationView
            View navHead = navigationView.getHeaderView(0);

            // find MenuItem you want to change
            ImageView nav_camara = navHead.findViewById(R.id.Main_nav_drawer_profile_pic);

            //https connection doesn't work as of now use http

            try {
                Picasso.get().load(ACCOUNT.getProfile_pic().replace("https://","http://")).into(nav_camara);
            } catch (Exception e) {
                e.printStackTrace();
            }


            invalidateOptionsMenu();




        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //disabled night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userLocalStore = new UserLocalStore(getApplicationContext());
        //FirebaseApp.initializeApp(this);

        Log.i("DEVICE TOKEN","In next line");
        //Android ID:
        //Log.i("DEVICE TOKEN : ",Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if(authenticate()){
            getDetails();
        }

        if (firstStart) {
            //causing crash on first boot TODO
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
        //FirebaseMessaging.getInstance().subscribeToTopic("Notify");

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

        //puts account details
        SetAccountDetails();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout,
                    new FragmentMain()).commit();
        }

        DBController dbController = new DBController(MainActivity.this);

        if(dbController.isNewUser() == 0) {
            //sending first notification to user who just installed
            Log.d(TAG, "onCreate: "+"new User");
            createNotificationChannel();
            sendNotification();
            GetFirstActivity getActivities = new GetFirstActivity();
            getActivities.execute();
        }

        //Starting Location Service
        //more info in the respective class
        LocationService locationService = new LocationService();
        locationService.Start(this, this);


    }


    private void sendTokenToServer(String token) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //this is not working right now but this is to know when someone installs the app for the first time
                UsefulFunctions.UsingGetAPI("http://43.205.45.96/ConsultUs/api_token?email="+ACCOUNT.getAccount_id()+"&token="+token);
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 200, intent, PendingIntent.FLAG_IMMUTABLE);

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

    class GetFirstActivity extends AsyncTask<String,Void,JSONObject>{

        final private JSONObject[] apiCall = {null};

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {

                apiCall[0] = UsefulFunctions.UsingGetAPI("http://43.205.45.96/api/spaceactive_activities.php?ano=1");
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