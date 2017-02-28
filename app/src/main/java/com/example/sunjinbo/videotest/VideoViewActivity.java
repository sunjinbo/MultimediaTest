package com.example.sunjinbo.videotest;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        final VideoView videoView = (VideoView)findViewById(R.id.video_view);
        videoView.setKeepScreenOn(true);
        videoView.setVideoPath("http://msoftdl.360.cn/mobilesafe/shouji360/360VR/video/vr/AntVR.mp4");
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
}
