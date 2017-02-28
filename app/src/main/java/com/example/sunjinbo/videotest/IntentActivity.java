package com.example.sunjinbo.videotest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;

public class IntentActivity extends AppCompatActivity {

    private static final int TAKE_PICTURE = 0;
    private static final int RECORD_MEDIA = 1;

    private VideoView mVideoView;
    private ImageView mImageView;
    private Uri mOutputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        mImageView = (ImageView) findViewById(R.id.iv_picture);
        mVideoView = (VideoView) findViewById(R.id.vv_video);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setImageBitmap(thumbnail);
                }
            } else {
                int width = mImageView.getWidth();
                int height = mImageView.getHeight();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mOutputFileUri.getPath(), options);

                int imgWidth = options.outWidth;
                int imgHeight = options.outHeight;

                int scaleFactor = Math.min(imgWidth / width, imgHeight / height);

                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;
                options.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mOutputFileUri.getPath(), options);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageBitmap(bitmap);
            }
        } else if (requestCode == RECORD_MEDIA) {
            mVideoView.setVideoURI(data.getData());
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.start();
        }
    }

    public void onStartClick(View view) {
        mImageView.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);

        switch (view.getId()) {
            case R.id.btn_take_picture:
                takePicture();
                break;

            case R.id.btn_record_media:
                recordMedia();
                break;

            default:
                break;
        }
    }

    private void takePicture() {
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");

        mOutputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);

        startActivityForResult(intent, TAKE_PICTURE);
    }

    private void recordMedia() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        startActivityForResult(intent, RECORD_MEDIA);
    }
}
