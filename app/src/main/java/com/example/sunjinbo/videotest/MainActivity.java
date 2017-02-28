package com.example.sunjinbo.videotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onVideoViewClick(View view) {
        startActivity(new Intent(MainActivity.this, VideoViewActivity.class));
    }

    public void onSurfaceViewClick(View view) {
        startActivity(new Intent(MainActivity.this, SurfaceViewActivity.class));
    }

    public void onAudioRecordClick(View view) {
        startActivity(new Intent(MainActivity.this, AudioRecordActivity.class));
    }

    public void onAudioTrackClick(View view) {
        startActivity(new Intent(MainActivity.this, AudioTrackActivity.class));
    }

    public void onIntentClick(View view) {
        startActivity(new Intent(MainActivity.this, IntentActivity.class));
    }

    public void onCameraClick(View view) {
        startActivity(new Intent(MainActivity.this, CameraActivity.class));
    }

    public void onMediaRecordClick(View view) {
        startActivity(new Intent(MainActivity.this, MediaRecordActivity.class));
    }

    public void onMediaEffectClick(View view) {
        startActivity(new Intent(MainActivity.this, MediaEffectActivity.class));
    }
}
