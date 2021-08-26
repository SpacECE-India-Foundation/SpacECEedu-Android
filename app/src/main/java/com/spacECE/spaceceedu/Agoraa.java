package com.spacECE.spaceceedu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class Agoraa extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agoraa);

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            Log.i("Agora  ::","Permissions Granted");
            initAgoraEngineAndJoinChannel();
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        joinChannel();
    }

    private void joinChannel() {
        String accessToken = getString(R.string.agora_access_token);
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "006464ff3e49fb3409494c0956edcec52e7IAB+DDmhDfV6A4vogtIE0UGwzmwIClst6qgqbegCOpDPJHfBAaIAAAAAEACLgpZhqa4oYQEAAQCkrihh")) {
            accessToken = ""; // default, no token
            Log.i("Agora :: ", " JoinChannel ");
        }
        // Call the joinChannel method to join a channel.
        // The uid is not specified. The SDK will assign one automatically.
        mRtcEngine.joinChannel(accessToken, "SpacECE", "Extra Optional Data", 0);
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i("Agora", "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        // Listen for the onUserOffline callback.
        // This callback occurs when the remote user leaves the channel or drops offline.
        @Override
        public void onUserOffline(final int uid, final int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    onRemoteUserLeft(uid, reason);
                }
            });
        }

        // Listen for the onUserMuterAudio callback.
        // This callback occurs when a remote user stops sending the audio stream.
        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    onRemoteUserVoiceMuted(uid, muted);
                }
            });
        }
    };

    // Call the create method to initialize RtcEngine.
    private void initializeAgoraEngine() {
        try {
            Log.i("Agora :: ", " InitializeAgoraEngine ");
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        } catch (Exception e) {
            Log.e("AGORA  ::: ", Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }
}