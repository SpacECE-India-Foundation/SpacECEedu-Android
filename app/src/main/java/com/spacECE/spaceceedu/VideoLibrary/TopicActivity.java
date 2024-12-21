package com.spacECE.spaceceedu.VideoLibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
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
        Thread thread = new Thread(() -> UsefulFunctions.UsingGetAPI("http://43.205.45.96/SpacTube/api_UpdateViews.php?vid=" + finalV_id));
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
                add_comment(comment, MainActivity.ACCOUNT.getAccount_id()+"", finalV_id);
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

        if (like_status_main) {
            set_like_dislike_btn_status(0, 0);
        } else {
            set_like_dislike_btn_status(1, 0);
            dislike_status_main = false;
            like_status_main = true;
        }
        Thread thread = new Thread(() -> UsefulFunctions.UsingGetAPI("http://43.205.45.96/SpacTube/api_likeVideo.php?uid=" + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + finalV_id));
        thread.start();

        new Thread(() -> {
            try {
                thread.join(); // Wait for the API call thread to finish
                runOnUiThread(() -> update_likes_dislike_count_onUI(finalV_id)); // Update UI on the main thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleDislikeButton(String finalV_id) {


        if (dislike_status_main) {
            set_like_dislike_btn_status(0, 0);
        } else {
            set_like_dislike_btn_status(0, 1);
            like_status_main = false;
            dislike_status_main = true;
        }
        Thread thread = new Thread(() -> UsefulFunctions.UsingGetAPI("http://43.205.45.96/SpacTube/api_dislikeVideo.php?uid=" + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + finalV_id));
        thread.start();

        new Thread(() -> {
            try {
                thread.join(); // Wait for the API call thread to finish
                runOnUiThread(() -> update_likes_dislike_count_onUI(finalV_id)); // Update UI on the main thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void update_comments_list(String vid_id) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://43.205.45.96/SpacTube/api_getAllComments.php?vid=" + vid_id;

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
                        JSONArray commentsArray = jsonObject.getJSONArray("comments");

                        for (int i = 0; i < commentsArray.length(); i++) {
                            JSONObject commentObject = commentsArray.getJSONObject(i);
                            String comment = commentObject.getString("u_comment");
                            String date = commentObject.getString("Date");


                            commentsList.add(URLDecoder.decode(comment));
                            datesList.add(date.split(" ")[0]);
                        }
                        custom_comments_adapter adapter = new custom_comments_adapter(TopicActivity.this, commentsList.toArray(new String[0]), datesList.toArray(new String[0]));
                        runOnUiThread(() -> comments_listview.setAdapter(adapter));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    void update_likes_dislike_count_onUI(String vid_id) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://43.205.45.96/SpacTube/api_getEachCount.php?vid=" + vid_id;

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
                        runOnUiThread(() -> tv_like.setText(likes+" Likes"));
                        runOnUiThread(() -> tv_dislike.setText(dislikes+" Dislikes"));
                        runOnUiThread(() -> tv_views.setText(views+" Views "));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(TopicActivity.this, "Failed to load likes and dislikes", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    void add_comment(String comment, String uid, String vid_id) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://43.205.45.96/SpacTube/api_commentVideo.php";

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


                runOnUiThread(() -> update_comments_list(vid_id));
            }
        });
    }

    private void update_like_dislike(String vid_id) {
        //updating count
        //updating color
        final String[] like_status = new String[1];
        final String[] dislike_status = new String[1];

        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://43.205.45.96/SpacTube/api_extractlike.php?uid=" + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + vid_id)
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
                    .url("http://43.205.45.96/SpacTube/api_getDisLike.php?uid=" + MainActivity.ACCOUNT.getAccount_id() + "&vid=" + vid_id)
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

    void set_like_dislike_btn_status(int l, int dl) {
        if (l == 0) {
            b_likeVideo.setImageResource(R.drawable.ic_baseline_thumb_up_24);
        } else {
            b_likeVideo.setImageResource(R.drawable.ic_baseline_thumb_up_highlighted);
        }
        if (dl == 0) {
            b_dislikeVideo.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24);
        } else {
            b_dislikeVideo.setImageResource(R.drawable.ic_baseline_thumb_down_highlighted);
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
