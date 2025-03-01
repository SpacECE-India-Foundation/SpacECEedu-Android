package com.spacECE.spaceceedu.VideoLibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TopicActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    private ImageView b_likeVideo;
    private ImageView b_dislikeVideo;
    TextView discrip_view;
    private ImageView b_share;
    private ImageView b_comment;

    private TextView tv_title;
    private TextView tv_like;
    private TextView tv_dislike;
    private TextView tv_views;
    boolean like_status_main = false, dislike_status_main = false;
    ListView comments_listview;

    private ArrayList<String> commentsList = new ArrayList<>();
    private ArrayList<String> datesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        // Initialize UI elements
        initializeUI();

        // Get values from the previous activity
        Bundle extras = getIntent().getExtras();
        String name = "No topic";
        String discription = "No ID";
        String v_url = "Video ID missing";
        String v_id = "Unknown";
        String like_count = "Unknown";
        String dislike_count = "Unknown";
        String views = "unknown";

        if (extras != null) {
            name = extras.getString("topic_name");
            discription = extras.getString("discrp");
            v_url = extras.getString("v_url");
            v_id = extras.getString("v_id");
            like_count = extras.getString("like_count");
            dislike_count = extras.getString("dislike_count");
            views = extras.getString("views");
        }

        // Set initial data to views
        setInitialData(name, discription, v_url, like_count, dislike_count, views);

        String finalV_id = v_id;

        // Update like/dislike status if user is logged in
        if (MainActivity.ACCOUNT != null) {
            // Ensure buttons are in their default (non-selected) state
            like_status_main = false;
            dislike_status_main = false;
            set_like_dislike_btn_status(0, 0); // Deselect both like and dislike by default
            update_like_dislike(finalV_id);
        }


        // Update comments list
        update_comments_list(finalV_id);

        // Increase view count
        increaseViewCount(finalV_id);

        // Setup YouTube player
        setupYouTubePlayer(v_url);

        // Setup button listeners
        setupButtonListeners(finalV_id, name, v_url);
    }

    private void increaseViewCount(String finalV_id) {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String updateViewsUrl = config.getString("SPACETUBE_UPDATEVIEWS");
                Thread thread = new Thread(() -> UsefulFunctions.UsingGetAPI( baseUrl+updateViewsUrl+ finalV_id));
                thread.start();


                //updating the like and dislike incase the user has liked/disliked
                //something then pressed back and again came to same video
                new Thread(() -> {
                    try {
                        thread.join();
                        runOnUiThread(() -> update_likes_dislike_count_onUI(finalV_id));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    private void initializeUI() {
        comments_listview = findViewById(R.id.Topics_comments_listview);
        b_comment = findViewById(R.id.Topics_Button_Comment);
        b_share = findViewById(R.id.Topic_Button_Share);
        b_likeVideo = findViewById(R.id.Topic_Button_LikeVideo);
        b_dislikeVideo = findViewById(R.id.Topic_Button_DislikeVideo);
        tv_dislike = findViewById(R.id.Topic_TextView_dislikeCount);
        tv_like = findViewById(R.id.Topic_TextView_likeCount);
        tv_views = findViewById(R.id.Topic_TextView_viewCount);
        discrip_view = findViewById(R.id.Topic_TextView_Description);
        tv_title = findViewById(R.id.Topic_TextView_Title);
    }

    private void setInitialData(String name, String description, String v_url, String like_count, String dislike_count, String views) {
        discrip_view.setText(description);
        tv_like.setText(like_count + " Likes");
        tv_dislike.setText(dislike_count + " Dislikes");
        tv_views.setText(views + " Views");
        tv_title.setText(name);
    }

    private void setupYouTubePlayer(String v_url) {
        youTubePlayerView = findViewById(R.id.YoutubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(v_url, 0);
            }
        });
    }

    private void setupButtonListeners(String finalV_id, String name, String v_url) {
        b_likeVideo.setOnClickListener(view -> {
            if (MainActivity.ACCOUNT == null) {
                Toast.makeText(TopicActivity.this, "Sign-in to Like Video", Toast.LENGTH_SHORT).show();
            } else {
                handleLikeButton(finalV_id);
            }
        });

        b_dislikeVideo.setOnClickListener(view -> {
            if (MainActivity.ACCOUNT == null) {
                Toast.makeText(TopicActivity.this, "Sign-in to Dislike Video", Toast.LENGTH_SHORT).show();
            } else {
                handleDislikeButton(finalV_id);
            }
        });

        b_comment.setOnClickListener(view -> {
            EditText commentText = findViewById(R.id.Topic_EditText_Comment);
            String comment = URLEncoder.encode(commentText.getText().toString());

            if (MainActivity.ACCOUNT == null) {
                Toast.makeText(TopicActivity.this, "Sign-in To Comment", Toast.LENGTH_SHORT).show();
            } else {
                add_comment(comment, MainActivity.ACCOUNT.getAccount_id(), finalV_id);
                Log.d("Comment API", "Comment: " + comment+" UID: "+MainActivity.ACCOUNT.getAccount_id()+" VID: "+finalV_id);
            }
            commentText.setText("");
        });

        b_share.setOnClickListener(view -> {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.putExtra(Intent.EXTRA_SUBJECT, "SpaceTube");
            myIntent.putExtra(Intent.EXTRA_TEXT, "Hey!, check this out this video on " + name + " by SpacECE: https://www.youtube.com/watch?v=" + v_url);
            startActivity(Intent.createChooser(myIntent, "Share Using"));
        });
    }

    private void handleLikeButton(String finalV_id) {
        if (MainActivity.ACCOUNT == null) {
            Toast.makeText(TopicActivity.this, "Sign-in to Like Video", Toast.LENGTH_SHORT).show();
        } else {
            if (like_status_main) {
                set_like_dislike_btn_status(0, 0); // Deselect like and dislike
                like_status_main = false; // Reset like status
            } else {
                set_like_dislike_btn_status(1, 0); // Select like, deselect dislike
                dislike_status_main = false; // Reset dislike status
                like_status_main = true; // Set like status
            }

            try {
                JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                if (config != null) {
                    String baseUrl = config.getString("BASE_URL");
                    String likevideoUrl = config.getString("SPACETUBE_LIKEVIDEO");
                    // Make the API call asynchronously using OkHttp's enqueue method
                    OkHttpClient client = new OkHttpClient();
                    String url = baseUrl + likevideoUrl + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + finalV_id;
                    Request request = new Request.Builder().url(url).build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Request Failed", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                Log.d("API not responding","not responding");
                                throw new IOException("Unexpected code " + response);
                            }
                            // After the request finishes, update the like and dislike count
                            String responseBody = response.body().string();
                            Log.d("API_RESPONSE", responseBody); // Print response

                            runOnUiThread(() -> update_likes_dislike_count_onUI(finalV_id));
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR:::", "Failed to load API URLs");
            }
        }
    }
    private void handleDislikeButton(String finalV_id) {
        if (MainActivity.ACCOUNT == null) {
            Toast.makeText(TopicActivity.this, "Sign-in to Dislike Video", Toast.LENGTH_SHORT).show();
        } else {
            if (dislike_status_main) {
                set_like_dislike_btn_status(0, 0); // Deselect like and dislike
                dislike_status_main = false; // Reset dislike status
            } else {
                set_like_dislike_btn_status(0, 1); // Select dislike, deselect like
                like_status_main = false; // Reset like status
                dislike_status_main = true; // Set dislike status
            }

            try {
                JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                if (config != null) {
                    String baseUrl= config.getString("BASE_URL");
                    String dislikevideoUrl = config.getString("SPACETUBE_DISLIKEVIDEO");

                    OkHttpClient client = new OkHttpClient();
                    String url = baseUrl+ dislikevideoUrl + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + finalV_id;
                    Request request = new Request.Builder().url(url).build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Request Failed", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                Log.d("===================","not responding");
                                throw new IOException("Unexpected code " + response);

                            }
                            // After the request finishes, update the like and dislike count
                            runOnUiThread(() -> update_likes_dislike_count_onUI(finalV_id));
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR:::", "Failed to load API URLs");
            }
        }
    }

    void update_comments_list(String vid_id) {
        final TextView tv_no_comments = findViewById(R.id.tv_no_comments);

        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl = config.getString("BASE_URL");
                String getAllCommentsUrl = config.getString("SPACETUBE_GETALLCOMMENTS");
                OkHttpClient client = new OkHttpClient();
                String url = baseUrl + getAllCommentsUrl + vid_id;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("Comment API", "Network Failure: " + e.getMessage(), e);
                        runOnUiThread(() -> tv_no_comments.setText("Network Error")); // Update TextView
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("Comment API", "Response Code: " + response.code());
                        if (!response.isSuccessful()) {
                            String errorMessage = response.body().string();
                            Log.e("Comment API", "HTTP Error: " + response.code() + ", Message: " + errorMessage);
                            runOnUiThread(() -> tv_no_comments.setText("HTTP Error: " + response.code())); // Update TextView
                            return;
                        }

                        final String responseData = response.body().string();
                        Log.d("Comment API", "Response Data: " + responseData);

                        runOnUiThread(() -> {
                            commentsList.clear();
                            datesList.clear();

                            try {
                                JSONObject jsonObject = new JSONObject(responseData);
                                String status = jsonObject.getString("status");
                                Log.d("Comment API Status:", status);

                                if (status.equals("true")) {
                                    if (jsonObject.has("comments") && jsonObject.get("comments") instanceof JSONArray) {
                                        JSONArray commentsArray = jsonObject.getJSONArray("comments");
                                        Log.d("Comments Array Length:", String.valueOf(commentsArray.length()));

                                        for (int i = 0; i < commentsArray.length(); i++) {
                                            JSONObject commentObject = commentsArray.getJSONObject(i);
                                            String comment = commentObject.getString("u_comment");
                                            String date = commentObject.getString("date");

                                            commentsList.add(comment);
                                            datesList.add(date.split(" ")[0]);
                                        }

                                        if (commentsList.isEmpty()) {
                                            tv_no_comments.setText("No comments"); //Show "No comments" if list is empty
                                        } else {
                                            tv_no_comments.setText(""); //Clear the text if comments are available
                                            custom_comments_adapter adapter = new custom_comments_adapter(TopicActivity.this, commentsList.toArray(new String[0]), datesList.toArray(new String[0]));
                                            comments_listview.setAdapter(adapter);
                                        }
                                    } else {
                                        Log.e("Comment API", "No comments array found in JSON response");
                                        tv_no_comments.setText("No comments"); // Update TextView
                                    }
                                } else {
                                    Log.e("Comment API", "Status: " + status);
                                    tv_no_comments.setText("No comments"); // Update TextView
                                }
                            } catch (JSONException e) {
                                Log.e("Comment API", "JSON Exception: " + e.getMessage(), e);
                                tv_no_comments.setText("No comments"); // Update TextView
                            }
                        });
                    }
                });
            }
        } catch (JSONException e) {
            Log.e("Comment API", "Error loading API URLs: " + e.getMessage(), e);
            runOnUiThread(() -> tv_no_comments.setText("No comments")); // Update TextView
        }
    }

    void update_likes_dislike_count_onUI(String vid_id) {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl = config.getString("BASE_URL");
                String getEachCountUrl = config.getString("SPACETUBE_GETEACHCOUNT");
                OkHttpClient client = new OkHttpClient();
                String url = baseUrl + getEachCountUrl + vid_id;

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Request Failed", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        commentsList.clear();
                        datesList.clear();
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            String status = jsonObject.getString("status");

                            if (status.equals("true")) {
                                String likes = jsonObject.getString("cntlike");
                                String dislikes = jsonObject.getString("cntdislike");
                                String views = jsonObject.getString("views");
                                runOnUiThread(() -> tv_like.setText(likes + " Likes"));
                                runOnUiThread(() -> tv_dislike.setText(dislikes + " Dislikes"));
                                runOnUiThread(() -> tv_views.setText(views + " Views "));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Failed to load likes and dislikes", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    void add_comment(String comment, String uid, String vid_id) {
        try {

            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                Log.d("Failed","failed to push due to some reason");
                String baseUrl= config.getString("BASE_URL");
                String commentVideoUrl = config.getString("SPACETUBE_COMMENTVIDEO");
                OkHttpClient client = new OkHttpClient();
                String url = baseUrl+commentVideoUrl;
                Log.d("Comment API URL:", url+"?uid="+uid+"&vid="+vid_id);

                RequestBody formBody = new FormBody.Builder()
                        .add("uid", uid)
                        .add("vid", vid_id)
                        .add("u_comment", comment)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Network Error", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        //check if correct from server side
                        String responseBody = response.body().string();
                        Log.d("Comment API Response:", responseBody);

                        runOnUiThread(() -> update_comments_list(vid_id));
                    }
                });
            }

        }catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    private void update_like_dislike(String vid_id) {
        //updating count
        //updating color
        final String[] like_status = new String[1];
        final String[] dislike_status = new String[1];
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String likeCountUrl = config.getString("SPACETUBE_LIKECOUNT");
                String dislikeCountUrl = config.getString("SPACETUBE_DISLIKECOUNT");

                new Thread(() -> {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl + likeCountUrl + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + vid_id)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        String resp = response.body().string();
                        JSONObject jsonObject = new JSONObject(resp);
                        like_status[0] = jsonObject.getString("status");

                        runOnUiThread(() -> {
                            if (like_status[0].equals("true")) {
                                set_like_dislike_btn_status(1, 0);
                                like_status_main = true;
                            } else {
                                like_status_main = false;
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }).start();

                new Thread(() -> {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(baseUrl + dislikeCountUrl + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + vid_id)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        String resp = response.body().string();
                        JSONObject jsonObject = new JSONObject(resp);
                        dislike_status[0] = jsonObject.getString("status");

                        runOnUiThread(() -> {
                            if (dislike_status[0].equals("true")) {
                                set_like_dislike_btn_status(0, 1);
                                dislike_status_main = true;
                            } else {
                                dislike_status_main = false;
                            }
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    void set_like_dislike_btn_status(int like, int dislike) {
        if (like == 0) {
            b_likeVideo.setImageResource(R.drawable.ic_baseline_thumb_up_24); // Set to default like icon
            like_status_main = false;  // Ensure like status is false
        } else {
            b_likeVideo.setImageResource(R.drawable.ic_baseline_thumb_up_highlighted); // Set to selected like icon
            like_status_main = true;   // Ensure like status is true
        }

        if (dislike == 0) {
            b_dislikeVideo.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24); // Set to default dislike icon
            dislike_status_main = false; // Ensure dislike status is false
        } else {
            b_dislikeVideo.setImageResource(R.drawable.ic_baseline_thumb_down_highlighted); // Set to selected dislike icon
            dislike_status_main = true;  // Ensure dislike status is true
        }
    }

    public class custom_comments_adapter extends ArrayAdapter<String> {
        String[] m_comments;
        String[] m_date;
        Context mContext;

        public custom_comments_adapter(Context context, String[] arComments, String[] arDate) {
            super(context, R.layout.item_comments_topic_spacetube, R.id.item_comments_text, arComments);
            m_comments = arComments;
            m_date = arDate;
            mContext = context;
        }

        @SuppressLint("ResourceAsColor")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            VHolder vholder;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.item_comments_topic_spacetube, parent, false);

                vholder = new VHolder(row);
                row.setTag(vholder);
            } else {
                vholder = (VHolder) row.getTag();
            }

            vholder.txt_comment.setText(m_comments[position]);
            vholder.txt_date.setText(m_date[position]);

            return row;
        }
    }

    public class VHolder {
        TextView txt_comment, txt_date;

        public VHolder(View r) {
            txt_comment = r.findViewById(R.id.item_comments_text);
            txt_date = r.findViewById(R.id.item_comments_date);
        }
    }
}