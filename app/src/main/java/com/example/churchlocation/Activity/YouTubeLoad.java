package com.example.churchlocation.Activity;

import android.os.Bundle;

import com.example.churchlocation.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubeLoad extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    private String devKey = "AIzaSyDBBTUH0D6eOuRvLkynRtuBirQwYU6pl9Q";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_load);

        final String videoID;
        videoID = getIntent().getExtras().getString("video_id");

        youTubePlayerView = findViewById(R.id.youtube_vid_player);

        youTubePlayerView.initialize(devKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoID);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}
