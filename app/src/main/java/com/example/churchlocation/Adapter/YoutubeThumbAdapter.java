package com.example.churchlocation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.churchlocation.Activity.YouTubeLoad;
import com.example.churchlocation.Model.TubeThumbs;
import com.example.churchlocation.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class YoutubeThumbAdapter extends RecyclerView.Adapter<YoutubeThumbAdapter.VideoViewHolder> {
    private Context context;
    private ArrayList<TubeThumbs> youtubeThumbAdapters;

    private String devKey = "AIzaSyDBBTUH0D6eOuRvLkynRtuBirQwYU6pl9Q";


    public YoutubeThumbAdapter(Context context, ArrayList<TubeThumbs> youtubeThumbAdapters) {
        this.context = context;
        this.youtubeThumbAdapters = youtubeThumbAdapters;
    }

    @NonNull
    @Override
    public YoutubeThumbAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tube_thumb, parent, false);

        context = parent.getContext();

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final YoutubeThumbAdapter.VideoViewHolder holder, final int position) {

        holder.youTubeThumbnailView.initialize(devKey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(youtubeThumbAdapters.get(position).getVideoId());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("Arraysss", youtubeThumbAdapters.get(position).getVideoId());

                context.startActivity(new Intent(view.getContext(), YouTubeLoad.class)
                .putExtra("video_id", youtubeThumbAdapters.get(position).getVideoId()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeThumbAdapters.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        YouTubeThumbnailView youTubeThumbnailView;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            youTubeThumbnailView = itemView.findViewById(R.id.video_thumb);
        }
    }
}
