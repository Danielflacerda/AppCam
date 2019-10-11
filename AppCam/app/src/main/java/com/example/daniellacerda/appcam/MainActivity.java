package com.example.daniellacerda.appcam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    ImageView mImageView;
    VideoView mVideoView;
    Uri videoUri;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final String VIDEO_SAMPLE = "tacoma_narrows";
    static final int REQUEST_IMAGE_CAPTURE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        Button btnVideo = (Button) findViewById(R.id.btnVideo);
        mImageView = (ImageView)findViewById(R.id.imageView);
        mVideoView = (VideoView)findViewById(R.id.videoView);
        frameLayout = (FrameLayout) findViewById(R.id.FrameView);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.setVisibility(View.VISIBLE);
                mVideoView.setVisibility(View.GONE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVideoView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                initializePlayer();
            }
        });
    }

        private void initializePlayer() {
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);
    }

    private Uri getMedia(String mediaName) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + mediaName);
    }

    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            initializePlayer();
            mVideoView.setVideoURI(videoUri);
            mVideoView.start();
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        mImageView.setImageBitmap(bitmap);}
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }
}
