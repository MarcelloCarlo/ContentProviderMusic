package com.marcello.jcgut.contentprovidermusic;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MusicPlayer extends Activity implements View.OnClickListener {

    private Button mPrevious,mPlay,mNext,mPause;
    private ComponentName component;
    private BroadcastReceiver batrecv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        setupViews();
        batrecv = new MusicBroadcastReceiver();
    }

    @Override
    protected void onStart(){
        registerReceiver(batrecv, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    super.onStart();
    }

    @Override
    protected void onStop(){
        unregisterReceiver(batrecv);
        super.onStop();
    }

    public void setupViews(){
        component = new ComponentName(this,MusicService.class);

        mPrevious = findViewById(R.id.btnPrevious);
        mPlay = findViewById(R.id.btnPlay);
        mNext = findViewById(R.id.btnNext);
        mPause = findViewById(R.id.btnPause);

        mPrevious.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPause.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v== mPrevious){
            Intent mIntent = new Intent(MusicService.PREVIOUS_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
        }else if(v == mPlay){
            Intent mIntent = new Intent(MusicService.PLAY_ACTION);
            mIntent.setComponent(component);
            startService(mIntent);
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
