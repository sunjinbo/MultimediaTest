package com.example.sunjinbo.videotest;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.hardware.Camera;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MediaRecordActivity extends AppCompatActivity implements SurfaceHolder.Callback,
    MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {

    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private SurfaceView mSurfaceView;
    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);

        mStartButton = (Button) findViewById(R.id.btn_record_media);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFixedSize(400, 300);
    }

    @Override
    public void onResume() {
        super.onResume();
        mStartButton.setText(getString(R.string.start_record_media));
        initCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.startFaceDetection();

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRecordingHint(true);
            mCamera.setParameters(parameters);

            initMediaRecorder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaRecorder();
        releaseCamera();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {

    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }

    public void onStartClick(View view) {
        if (TextUtils.equals(mStartButton.getText(), getText(R.string.start_record_media))) {
            mStartButton.setText(getString(R.string.stop_record_media));
            if (mMediaRecorder != null) {
                mMediaRecorder.start();
            }
        } else {
            mStartButton.setText(getString(R.string.start_record_media));
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
            }
        }
    }

    private void initMediaRecorder() {

        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        CamcorderProfile profile = null;

        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_HIGH)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        }

        if (profile != null) {
            mMediaRecorder.setProfile(profile);
        }

        String outputFile = Environment.getExternalStorageDirectory().getPath() + "/myvideo.mp4";
        mMediaRecorder.setOutputFile(outputFile);

        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            mCamera.lock();
        }
    }

    private void initCamera() {
        mCamera = Camera.open();
    }

    private void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopFaceDetection();
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
