package com.example.sunjinbo.videotest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AudioRecordActivity extends AppCompatActivity implements Runnable {

    private boolean mIsRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);

        final Button btn = (Button) findViewById(R.id.btn_audio_record);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsRecording) {
                    mIsRecording = false;
                    btn.setText(R.string.start_record_audio);
                } else {
                    mIsRecording = true;
                    btn.setText(R.string.stop_record_audio);
                    new Thread(AudioRecordActivity.this).start();
                }
            }
        });
    }

    private void startRecord() {
        int frequency = 11025;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        File file = new File(Environment.getExternalStorageDirectory(), "raw.pcm");

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int bufferSize = AudioRecord.getMinBufferSize(frequency,
                    channelConfiguration,
                    audioEncoding);

            short[] buffer = new short[bufferSize];

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    frequency,
                    channelConfiguration,
                    audioEncoding,
                    bufferSize);

            audioRecord.startRecording();

            while (mIsRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dataOutputStream.write(buffer[i]);
                }
            }

            audioRecord.stop();
            dataOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startRecord();
    }
}
