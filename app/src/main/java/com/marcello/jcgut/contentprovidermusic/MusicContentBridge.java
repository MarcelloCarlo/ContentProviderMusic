package com.marcello.jcgut.contentprovidermusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public class MusicContentBridge extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ComponentName component = new ComponentName(this, MusicService.class);

        Intent mIntent = new Intent(MusicService.PLAY_ACTION);
        mIntent.setComponent(component);
        startService(mIntent);
        setContentView(R.layout.activity_music_player);
    }
    }



