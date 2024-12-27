package com.spacECE.spaceceedu;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.Location.LocationService;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
/////////////////////////////////////////////////
import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    public static Account ACCOUNT=null;
    UserLocalStore userLocalStore;
    NavigationView navigationView;
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

            // get menu from navigationView
            View navHead = navigationView.getHeaderView(0);

            // find MenuItem you want to change
            ImageView nav_camara = navHead.findViewById(R.id.Main_nav_drawer_profile_pic);
            TextView nav_name = navHead.findViewById(R.id.Main_Nav_TextView_UserName);

            //https connection doesn't work as of now use http

            nav_name.setText(ACCOUNT.getUsername());
            nav_name.setTextSize(20);
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.Signout);
            item.setVisible(true);

            try {
                if(nav_camara==null){
                    Picasso.get().load(ACCOUNT.getUsername().replace("https://","http://")).into((Target)nav_name);
                }
                else {
                    //Picasso.get().load(ACCOUNT.getProfile_pic().replace("https://", "http://")).into(nav_camara);
                    Picasso.get().load("https://drive.google.com/uc?id=1euwFojCEAStP9mXVaUH3eLZDx39nW7eg").into(nav_camara);
                    Picasso.get().load(ACCOUNT.getUsername().replace("https://", "http://")).into((Target) nav_name);
                }
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
        navigationView = (NavigationView) findViewById(R.id.Main_navView_drawer);

        try {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment=null;
                    switch (item.getItemId()) {
                        case R.id.Home:
                            selectedFragment=new FragmentMain();
                            break;
                        case R.id.payments:         //changes made for solving the payment option
                            selectedFragment=new FragmentPayment();
                            break;
                        case R.id.profile:
                            selectedFragment=new FragmentProfile();
                            break;
                        case R.id.AboutUs:
                            selectedFragment=new FragmentAbout();
                            break;
                        case R.id.Signout:
                            signOut();
                            break;
                        default:
                            selectedFragment = new FragmentMain();
                            break;
                    }
                    drawer.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout,
                            selectedFragment).commit();
                    return true;
                }
            });
        }catch (Exception e){
            Log.e("onCreate: ","Error" );
        }
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
                try {
                    JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                    if (config != null) {
                        String baseUrl= config.getString("BASE_URL");
                        String consultApiTokenUrl = config.getString("CONSULT_APITOKEN");

                        //this is not working right now but this is to know when someone installs the app for the first time
                        UsefulFunctions.UsingGetAPI(baseUrl+consultApiTokenUrl+ACCOUNT.getAccount_id()+"&token="+token);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.i("ERROR:::", "Failed to load API URLs");
                }
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
                        default:
                            selectedFragment = new FragmentMain();
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

        // Restart the activity to reflect the changes
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    ///////////////////////////////////////////////////////////////////////////////////
//    class GetFirstActivity extends AsyncTask<String,Void,JSONObject>{
//
//        final private JSONObject[] apiCall = {null};
//
//        @Override
//        protected JSONObject doInBackground(String... strings) {
//
//            try {
//                JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
//                if (config != null) {
//                    String baseUrl= config.getString("BASE_URL");
//                    String spaceActiveActivityUrl = config.getString("SPACEACTIVE_SPACEACTIVITY");
//
//                    apiCall[0] = UsefulFunctions.UsingGetAPI(baseUrl+spaceActiveActivityUrl);
//                    Log.d(TAG, "Object Obtained "+apiCall[0].toString());
//
//                    GsonBuilder gsonBuilder = new GsonBuilder();
//                    Gson gson = gsonBuilder.create();
//                    ActivityData activityData = gson.fromJson(apiCall[0].toString(),ActivityData.class);
//                    Log.d(TAG, "doInBackground: activity_dev_domain "+activityData.getData().get(0).getActivityDevDomain());
//                    //List<Data> list = activityData.getData();
//
//                    DBController dbController = new DBController(MainActivity.this);
//                    dbController.insertRecord(activityData);
//                    Log.d(TAG, "doInBackground: "+dbController.isNewUser());
//                }
//
//            }catch (RuntimeException runtimeException){
//                Log.d(TAG, "RUNTIME EXCEPTION:::, Server did not respons");
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                Log.i("ERROR:::", "Failed to load API URLs");
//            }
//
//
//            return null;
//        }
//    }
/////////////////////////////////////////////////////////////////////////////////////////


    public class GetFirstActivity {
        private static final String TAG = "GetFirstActivity";

        private final ExecutorService executorService = Executors.newSingleThreadExecutor();
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        public void execute() {
            executorService.execute(() -> {
                JSONObject apiResponse = null;

                try {
                    // Load configuration
                    JSONObject config = ConfigUtils.loadConfig(MainActivity.this.getApplicationContext());
                    if (config != null) {
                        String baseUrl = config.getString("BASE_URL");
                        String spaceActiveActivityUrl = config.getString("SPACEACTIVE_SPACEACTIVITY");
                        String fullUrl = baseUrl + spaceActiveActivityUrl;

                        // Log the full URL
                        Log.d(TAG, "Testing API call to: " + fullUrl);

                        // Check if the URL is reachable (optional, for debugging)
                        if (!isUrlReachable(fullUrl)) {
                            Log.e(TAG, "URL is not reachable: " + fullUrl);
                            return;
                        }

                        // Make API call
                        apiResponse = UsefulFunctions.UsingGetAPI(fullUrl);
                        Log.d(TAG, "Object Obtained: " + apiResponse.toString());

                        // Parse API response
                        Gson gson = new GsonBuilder().create();
                        ActivityData activityData = gson.fromJson(apiResponse.toString(), ActivityData.class);
                        Log.d(TAG, "Activity Dev Domain: " + activityData.getData().get(0).getActivityDevDomain());

                        // Save to database
                        DBController dbController = new DBController(MainActivity.this);
                        dbController.insertRecord(activityData);
                        Log.d(TAG, "Is new user: " + dbController.isNewUser());
                    } else {
                        Log.e(TAG, "Config is null. Unable to proceed.");
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "Runtime Exception: Server did not respond", e);
                } catch (Exception e) {
                    Log.e(TAG, "Error: Failed to load API URLs", e);
                }

                // Handle UI updates on the main thread
                final JSONObject finalApiResponse = apiResponse;
                mainHandler.post(() -> {
                    if (finalApiResponse != null) {
                        Log.d(TAG, "UI update with API response");
                        // Update UI here if necessary
                    } else {
                        Log.d(TAG, "API response is null, no UI update");
                    }
                });
            });
        }

        public void shutDown() {
            executorService.shutdown();
        }

        private boolean isUrlReachable(String url) {
            try {
                URL testUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(3000); // Timeout in milliseconds
                connection.setReadTimeout(3000);
                int responseCode = connection.getResponseCode();
                return (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT);
            } catch (IOException e) {
                Log.e(TAG, "Error checking URL reachability: " + e.getMessage());
                return false;
            }
        }
    }
}