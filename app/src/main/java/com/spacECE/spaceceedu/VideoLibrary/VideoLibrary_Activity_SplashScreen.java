package com.spacECE.spaceceedu.VideoLibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideoLibrary_Activity_SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        VideoLibrary_Activity.freeTopicList.clear();
        VideoLibrary_Activity.paidTopicList.clear();
        VideoLibrary_Activity.trendingTopicList.clear();


        GetLists getLists = new GetLists();
        getLists.execute();


    }


    class GetLists extends AsyncTask<String, Void, JSONObject> {
        final private JSONObject[] apiCall = {null};

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                if (config != null) {
                    String baseUrl = config.getString("BASE_URL");
                    String freeUrl = config.getString("SPACETUBE_FREE");
                    String trendingUrl = config.getString("SPACETUBE_TRENDING");
                    String paidUrl = config.getString("SPACETUBE_PAID");

//                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://13.126.66.91/spacece/SpacTube/api_all.php?uid=1&type=free");
                    apiCall[0] = UsefulFunctions.UsingGetAPI(baseUrl + freeUrl);
                    JSONArray jsonArray_free = apiCall[0].optJSONArray("data");


//                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://13.126.66.91/spacece/SpacTube/api_all.php?uid=1&type=trending");
                    apiCall[0] = UsefulFunctions.UsingGetAPI(baseUrl + trendingUrl);
                    JSONArray JsonArray_trending = apiCall[0].optJSONArray("data");


//                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://13.126.66.91/spacece/SpacTube/api_all.php?uid=1&type=paid");
                    apiCall[0] = UsefulFunctions.UsingGetAPI(baseUrl + paidUrl);
                    JSONArray JsonArray_paid = apiCall[0].optJSONArray("data");

                    try {
                        if(jsonArray_free!=null) {
                            for (int i = 0; i < jsonArray_free.length(); i++) {
                                JSONObject response_element = new JSONObject(String.valueOf(jsonArray_free.getJSONObject(i)));

                                Topic newTopic = new Topic(response_element.getString("status"),
                                        response_element.getString("title"),
                                        response_element.getString("v_id"),
                                        response_element.getString("filter"),
                                        response_element.getString("length"),
                                        response_element.getString("v_url"),
                                        response_element.getString("v_date"),
                                        response_element.getString("v_uni_no"),
                                        response_element.getString("v_desc"),
                                        response_element.getString("cntlike"),
                                        response_element.getString("cntdislike"),
                                        response_element.getString("views"),
                                        response_element.getString("cntcomment"));

                                VideoLibrary_Activity.freeTopicList.add(newTopic);
                            }
                        }

                        if(JsonArray_paid!=null) {
                            for (int i = 0; i < JsonArray_paid.length(); i++) {
                                JSONObject response_element = new JSONObject(String.valueOf(JsonArray_paid.getJSONObject(i)));

                                Topic newTopic = new Topic(response_element.getString("status"),
                                        response_element.getString("title"),
                                        response_element.getString("v_id"),
                                        response_element.getString("filter"),
                                        response_element.getString("length"),
                                        response_element.getString("v_url"),
                                        response_element.getString("v_date"),
                                        response_element.getString("v_uni_no"),
                                        response_element.getString("v_desc"),
                                        response_element.getString("cntlike"),
                                        response_element.getString("cntdislike"),
                                        response_element.getString("views"),
                                        response_element.getString("cntcomment"));

                                VideoLibrary_Activity.paidTopicList.add(newTopic);
                            }
                        }

                        if(JsonArray_trending!=null) {
                            for (int i = 0; i < JsonArray_trending.length(); i++) {
                                JSONObject response_element = new JSONObject(String.valueOf(JsonArray_trending.getJSONObject(i)));

                                Topic newTopic = new Topic(response_element.getString("status"),
                                        response_element.getString("title"),
                                        response_element.getString("v_id"),
                                        response_element.getString("filter"),
                                        response_element.getString("length"),
                                        response_element.getString("v_url"),
                                        response_element.getString("v_date"),
                                        response_element.getString("v_uni_no"),
                                        response_element.getString("v_desc"),
                                        response_element.getString("cntlike"),
                                        response_element.getString("cntdislike"),
                                        response_element.getString("views"),
                                        response_element.getString("cntcomment"));

                                VideoLibrary_Activity.trendingTopicList.add(newTopic);
                            }
                        }

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        Log.i("JSON Object : ", "Key not found");
                    }
                VideoLibrary_Activity.ArrayDownloadCOMPLETED[0] = true;
//                Log.i("VIDEOS:::::", VideoLibrary_Activity.topicList.toString());

                Intent intent = new Intent(getApplicationContext(), VideoLibrary_Activity.class);
                startActivity(intent);
                finish();

                return null;
                }
            }
            catch (RuntimeException runtimeException) {
                Log.i("RUNTIME EXCEPTION:::", "Server did not respond");
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR:::", "Failed to load API URLs");
            }
            return null;
        }
    }

}
