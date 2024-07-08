package com.example.test7;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoLectureActivity extends AppCompatActivity {

    private ExoPlayer player1, player2, player3, player4, player5;
    private PlayerView playerView1, playerView2, playerView3, playerView4, playerView5;
    private Button openVideoButton1, openVideoButton2, openVideoButton3, openVideoButton4, openVideoButton5;

    private String[] videoUrls = {
            "https://www.youtube.com/watch?v=NfurkrZEn3Q",
            "https://www.youtube.com/watch?v=0iRbD5rM5qc",
            "https://www.youtube.com/watch?v=6YRvAgq3VjU",
            "https://www.youtube.com/watch?v=OQY0kNTIaBM",
            "https://www.youtube.com/watch?v=8i-LTHO4lrg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_lecture);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerView1 = findViewById(R.id.playerView1);
        playerView2 = findViewById(R.id.playerView2);
        playerView3 = findViewById(R.id.playerView3);
        playerView4 = findViewById(R.id.playerView4);
        playerView5 = findViewById(R.id.playerView5);

        openVideoButton1 = findViewById(R.id.openVideoButton1);
        openVideoButton2 = findViewById(R.id.openVideoButton2);
        openVideoButton3 = findViewById(R.id.openVideoButton3);
        openVideoButton4 = findViewById(R.id.openVideoButton4);
        openVideoButton5 = findViewById(R.id.openVideoButton5);

        // Initialize players
        initializePlayer();

        // Set click listeners for buttons
        setButtonClickListeners();
    }

    private void initializePlayer() {
        player1 = new ExoPlayer.Builder(this).build();
        player2 = new ExoPlayer.Builder(this).build();
        player3 = new ExoPlayer.Builder(this).build();
        player4 = new ExoPlayer.Builder(this).build();
        player5 = new ExoPlayer.Builder(this).build();

        playerView1.setPlayer(player1);
        playerView2.setPlayer(player2);
        playerView3.setPlayer(player3);
        playerView4.setPlayer(player4);
        playerView5.setPlayer(player5);

        MediaItem mediaItem1 = MediaItem.fromUri(Uri.parse(videoUrls[0]));
        MediaItem mediaItem2 = MediaItem.fromUri(Uri.parse(videoUrls[1]));
        MediaItem mediaItem3 = MediaItem.fromUri(Uri.parse(videoUrls[2]));
        MediaItem mediaItem4 = MediaItem.fromUri(Uri.parse(videoUrls[3]));
        MediaItem mediaItem5 = MediaItem.fromUri(Uri.parse(videoUrls[4]));

        player1.setMediaItem(mediaItem1);
        player2.setMediaItem(mediaItem2);
        player3.setMediaItem(mediaItem3);
        player4.setMediaItem(mediaItem4);
        player5.setMediaItem(mediaItem5);

        player1.prepare();
        player2.prepare();
        player3.prepare();
        player4.prepare();
        player5.prepare();
    }

    private void setButtonClickListeners() {
        openVideoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo(videoUrls[0]);
            }
        });

        openVideoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo(videoUrls[1]);
            }
        });

        openVideoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo(videoUrls[2]);
            }
        });

        openVideoButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo(videoUrls[3]);
            }
        });

        openVideoButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo(videoUrls[4]);
            }
        });
    }

    private void openYouTubeVideo(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        player1.play();
        player2.play();
        player3.play();
        player4.play();
        player5.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player1.pause();
        player2.pause();
        player3.pause();
        player4.pause();
        player5.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player1 != null) {
            player1.release();
            player1 = null;
        }
        if (player2 != null) {
            player2.release();
            player2 = null;
        }
        if (player3 != null) {
            player3.release();
            player3 = null;
        }
        if (player4 != null) {
            player4.release();
            player4 = null;
        }
        if (player5 != null) {
            player5.release();
            player5 = null;
        }
    }
}