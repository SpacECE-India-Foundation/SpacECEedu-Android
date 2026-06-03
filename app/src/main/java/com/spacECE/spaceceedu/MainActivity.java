package com.spacECE.spaceceedu;

import android.Manifest;
import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.Location.LocationService;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.google.firebase.auth.FirebaseAuth;

import com.squareup.picasso.Picasso;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    public static Account ACCOUNT = null;
    UserLocalStore userLocalStore;
    DBController dbController;
    public final String TAG = "MainActivity";
    private LocationService locationService;

    private static final String INSTAGRAM_URL = "https://www.instagram.com/spac.ece/";
    private static final String YOUTUBE_URL = "https://www.youtube.com/@SpacECE";
    private static final String FACEBOOK_URL = "https://www.facebook.com/SpacECE/";
    private static final String LINKEDIN_URL = "https://www.linkedin.com/company/spacecein/";
    private static final String TWITTER_URL = "https://x.com/ece_spac";
    private static final String ABOUT_US_URL = "https://www.spacece.in/about-us";
    private static final String TERMS_AND_CONDITIONS_URL = "https://www.spacece.co/terms-and-conditions";
    private static final String PRIVACY_POLICY_URL = "https://www.spacece.co/privacy-policy";
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.spacece.milestonetracker";
    private static final String MAP_LOCATION = "geo:0,0?q=SpacECE INDIA FOUNDATION, CHANDRALOK NAGARI, C602, opp. Muktai Garden, Ganesh Nagar, Dhayari, Pune, Maharashtra 411041";
    private static final String CONTACT_US_EMAIL = "contact@spacece.in";


    @Override
    protected void onPostResume() {
        super.onPostResume();
        SetAccountDetails();
    }

    private void SetAccountDetails() {
        if (toolbar != null) {
            if (ACCOUNT != null && ACCOUNT.getUsername() != null && !ACCOUNT.getUsername().isEmpty() && !ACCOUNT.getUsername().equalsIgnoreCase("null")) {
                toolbar.setTitle("Hi " + ACCOUNT.getUsername() + " !");
            } else {
                toolbar.setTitle("Hi!");
            }
        }

        if (ACCOUNT != null) {
            NavigationView navigationView = findViewById(R.id.Main_navView_drawer);

            if (navigationView != null) {
                View navHead = navigationView.getHeaderView(0);
                ImageView nav_camara = navHead.findViewById(R.id.Main_nav_drawer_profile_pic);

                if (nav_camara != null && ACCOUNT.getProfile_pic() != null && !ACCOUNT.getProfile_pic().isEmpty()) {
                    try {
                        Picasso.get().load(ACCOUNT.getProfile_pic().replace("https://", "http://")).into(nav_camara);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userLocalStore = new UserLocalStore(getApplicationContext());
        dbController = DBController.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (authenticate()) {
            getDetails();
        }

        if (firstStart) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w("FCM TOKEN : ", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Log.d("FCM TOKEN : ", task.getResult());
                        sendTokenToServer(task.getResult());
                    });

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("Notify");

        BottomNavigationView bottomNav = findViewById(R.id.Main_Bottom_Navigation);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new FragmentMain();
                    if (toolbar != null) toolbar.setVisibility(View.VISIBLE);
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new FragmentProfile();
                    if (toolbar != null) toolbar.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_help) {
                    selectedFragment = new FragmentAbout();
                    if (toolbar != null) toolbar.setVisibility(View.GONE);
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout,
                            selectedFragment).commit();
                }
                return true;
            });
        }

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.Main_navView_drawer);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (drawer != null && toolbar != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        SetAccountDetails();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout,
                    new FragmentMain()).commit();
            if (toolbar != null) toolbar.setVisibility(View.VISIBLE);
        }

        View loginFooter = findViewById(R.id.nav_login_button_container);
        if (loginFooter != null) {
            loginFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ACCOUNT == null) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Already Logged In", Toast.LENGTH_SHORT).show();
                    }
                    if (drawer != null) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
            });
        }

        new Thread(() -> {
            if (dbController.isNewUser() == 0) {
                runOnUiThread(() -> {
                    createNotificationChannel();
                    sendNotification();
                });
                runGetFirstActivityTask();
            }
        }).start();

        locationService = new LocationService();
        locationService.Start(this, this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });
    }

    private void runGetFirstActivityTask() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                JSONObject apiCall = UsefulFunctions.UsingGetAPI("https://hustle-7c68d043.mileswebhosting.com/spacece/api/spaceactive_activities.php?ano=1");
                if (apiCall != null) {
                    Gson gson = new GsonBuilder().create();
                    ActivityData activityData = gson.fromJson(apiCall.toString(), ActivityData.class);
                    dbController.insertRecord(activityData);
                }
            } catch (Exception e) {
                Log.e(TAG, "runGetFirstActivityTask Error", e);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (locationService != null) {
            locationService.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void sendTokenToServer(String token) {
        if (ACCOUNT != null) {
            new Thread(() -> UsefulFunctions.UsingGetAPI("https://hustle-7c68d043.mileswebhosting.com/ConsultUs/api_token?email=" + ACCOUNT.getAccount_id() + "&token=" + token)).start();
        }
    }

    private void getDetails() {
        ACCOUNT = userLocalStore.getLoggedInAccount();
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Main_Fragment_layout, new FragmentMain()).commit();
            if (toolbar != null) toolbar.setVisibility(View.VISIBLE);
        } else {
            if (itemId == R.id.nav_about) {
                openUrl(ABOUT_US_URL);
            } else if (itemId == R.id.nav_terms) {
                openUrl(TERMS_AND_CONDITIONS_URL);
            } else if (itemId == R.id.nav_privacy) {
                openUrl(PRIVACY_POLICY_URL);
            } else if (itemId == R.id.nav_instagram) {
                openUrl(INSTAGRAM_URL);
            } else if (itemId == R.id.nav_youtube) {
                openUrl(YOUTUBE_URL);
            } else if (itemId == R.id.nav_facebook) {
                openUrl(FACEBOOK_URL);
            } else if (itemId == R.id.nav_linkedin) {
                openUrl(LINKEDIN_URL);
            } else if (itemId == R.id.nav_twitter) {
                openUrl(TWITTER_URL);
            } else if (itemId == R.id.nav_contact) {
                openEmail();
            } else if (itemId == R.id.nav_rate) {
                openUrl(PLAY_STORE_URL);
            } else if (itemId == R.id.nav_location) {
                openMap();
            }
        }

        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(MAP_LOCATION));
        startActivity(intent);
    }

    private void openEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + CONTACT_US_EMAIL));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (ACCOUNT != null) {
            inflater.inflate(R.menu.options_main_activity_loggedin, menu);
        } else {
            inflater.inflate(R.menu.options_main_activity, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.button_signOut) {
            signOut();
            return true;
        } else if (itemId == R.id.button_signIn) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify", "Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("New Activity is Available");
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }
    }

    public void sendNotification() {
        Intent intent = new Intent(MainActivity.this, ReminderBroadCastReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 200, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 5);
            calendar.set(Calendar.SECOND, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        userLocalStore.clearUserData();
        ACCOUNT = null;
        userLocalStore.setUserLoggedIn(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void openDrawer() {
        if (drawer != null) {
            drawer.openDrawer(GravityCompat.START);
        }
    }
}
