package com.marcello.jcgut.contentprovidermusic;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MusicService extends Service {

    String[] mCursorCols = new String[]{
            "audio._id AS _id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION};

    private MediaPlayer mMediaPlayer;
    private Cursor mCursor;
    private int mPlayPosition = 0;

    public static final String PLAY_ACTION = "com.marcello.jcgut.contentprovidermusic.PLAY_ACTION";
    public static final String PAUSE_ACTION =  "com.marcello.jcgut.contentprovidermusic.PAUSE_ACTION";
    public static final String NEXT_ACTION = "om.marcello.jcgut.contentprovidermusic.NEXT_ACTION";
    public static final String PREVIOUS_ACTION = "om.marcello.jcgut.contentprovidermusic.PREVIOUS_ACTION";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mMediaPlayer = new MediaPlayer();

        Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        mCursor = getContentResolver().query(MUSIC_URI,mCursorCols,"duration > 10000",null,null);
    }

    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);

        String action = intent.getAction();

        switch (action) {
            case PLAY_ACTION:
                play();
                break;
            case PAUSE_ACTION:
                pause();
                break;
            case NEXT_ACTION:
                next();
                break;
            case PREVIOUS_ACTION:
                previous();
                break;
        }
    }

    public void play(){
        inite();
    }

    public void pause(){
        stopSelf();
    }

    public void previous(){
        if(mPlayPosition == 0){
            mPlayPosition = mCursor.getCount() - 1;
        }else {
            mPlayPosition--;
        }
        inite();
    }

    public void next() {
        if (mPlayPosition == mCursor.getCount() - 1){
            mPlayPosition = 0;
        }else {
            mPlayPosition++;
        }
        inite();
    }

    public void inite(){
        mMediaPlayer.reset();
        String dataSource = getDateByPosition(mCursor, mPlayPosition);
        String info = getInfoByPosition(mCursor, mPlayPosition);

        Toast.makeText(getApplicationContext(),info,Toast.LENGTH_SHORT).show();

        try{
            mMediaPlayer.setDataSource(dataSource);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }catch (IllegalArgumentException ea){
            ea.printStackTrace();
        } catch (IOException ea){
            ea.printStackTrace();
        }
    }

    public String getDateByPosition(Cursor c, int position){
        c.moveToPosition(position);
        int dataColumn = c.getColumnIndex(MediaStore.Audio.Media.DATA);
        String data = c.getString(dataColumn);
        return data;
    }

    public String getInfoByPosition(Cursor c, int position){
        c.moveToPosition(position);
        int titleColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistColumn = c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        String info = c.getString(artistColumn)+" "+c.getString(titleColumn);

        return info;
    }



    public void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
    }


}