package com.example.sunjinbo.videotest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AudioTrackActivity extends AppCompatActivity {

    private boolean mIsPlaying = false;
    private AudioTrack mAudioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_track);

        final Button btn = (Button) findViewById(R.id.btn_audio_play);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPlaying) {
                    mIsPlaying = false;
                    btn.setText(R.string.start_play_audio);
                    stopPlay();
                } else {
                    mIsPlaying = true;
                    btn.setText(R.string.stop_play_audio);
                    startPlay();
                }
            }
        });
    }

    private void startPlay() {
        int frequency = 11025;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        File file = new File(Environment.getExternalStorageDirectory(), "raw.pcm");
        if (!file.exists()) {
            Toast.makeText(this, this.getString(R.string.media_file_not_found), Toast.LENGTH_SHORT).show();
            return;
        }

        int audioLength = (int) (file.length());
        short[] audio = new short[audioLength];

        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while (dataInputStream.available() > 0) {
                audio[i] = dataInputStream.readShort();
                i++;
            }

            dataInputStream.close();

            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    frequency,
                    channelConfiguration,
                    audioEncoding,
                    audioLength,
                    AudioTrack.MODE_STREAM);

            mAudioTrack.play();
            mAudioTrack.write(audio, 0, audioLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlay() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
            mAudioTrack = null;
        }
    }
}
