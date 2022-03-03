package com.example.learningproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.learningproject.R;

import java.io.IOException;

public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback,MediaPlayer.OnPreparedListener {


    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private static final String VIDEO_PATH="http://mic.daytan.edu.vn:83/FINAL.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        surfaceView= (SurfaceView) findViewById(R.id.surface_view);
        surfaceHolder=surfaceView.getHolder();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
     mediaPlayer=new MediaPlayer();
     mediaPlayer.setDisplay(surfaceHolder);
     try {
         {
             mediaPlayer.setDataSource(VIDEO_PATH);
             mediaPlayer.prepare();
             mediaPlayer.setOnPreparedListener(SurfaceViewActivity.this);
             mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

         }
     } catch (IOException e) {
         e.printStackTrace();
     }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

    }
}