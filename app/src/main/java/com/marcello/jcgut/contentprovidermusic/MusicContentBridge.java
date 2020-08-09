package com.marcello.jcgut.contentprovidermusic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MusicContentBridge extends Activity implements View.OnClickListener {
    private Button mPrevious,mPlay,mNext,mPause;
    private ComponentName component;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MusicContentBridge.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(MusicContentBridge.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    /*Toast.makeText(MusicContentBridge.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();*/
                    Intent mIntent = new Intent(MusicService.PLAY_ACTION);
                    mIntent.setComponent(component);
                    startService(mIntent);
                } else {
                    // Permission Denied
                    Toast.makeText(MusicContentBridge.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestPermission();
        super.onCreate(savedInstanceState);
        component = new ComponentName(this, MusicService.class);
        setContentView(R.layout.activity_music_player);
        final TextView playLabel = (TextView) findViewById(R.id.lblCurrentPlay);
        final TextView miscLabel = (TextView) findViewById(R.id.lblMiscString);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String currentPlay = intent.getStringExtra(MusicService.CURRENT_PLAY);
                        String miscString = intent.getStringExtra(MusicService.MISC_INFO);
                        playLabel.setText(currentPlay);
                        miscLabel.setText(miscString);
                    }
                }, new IntentFilter(MusicService.SEND_CURRENT_PLAY)
        );

        mPrevious = findViewById(R.id.btnPrevious);
        mPlay = findViewById(R.id.btnPlay);
        mNext = findViewById(R.id.btnNext);
        mPause = findViewById(R.id.btnPause);

        mPrevious.setOnClickListener( this);
        mPlay.setOnClickListener( this);
        mNext.setOnClickListener( this);
        mPause.setOnClickListener( this);
    }


    @SuppressLint("SetTextI18n")
    public void onClick(View v) {
        if (v== mPrevious){
            Intent mIntent = new Intent(MusicService.PREVIOUS_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }else if(v == mPlay){
            Intent mIntent = new Intent(MusicService.PLAY_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
            if (mPlay.getText().toString().equals("Play")){
                mPlay.setText("Pause");
            } else {
                mPlay.setText("Play");
            }
        }else if(v == mNext) {
            Intent mIntent = new Intent(MusicService.NEXT_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }else{
            Intent mIntent = new Intent(MusicService.PAUSE_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }
    }
    }



