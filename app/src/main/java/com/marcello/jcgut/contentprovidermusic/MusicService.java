package com.marcello.jcgut.contentprovidermusic;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;


public class MusicService extends Service {

    String[] mCursorCols = new String[]{
            "audio._id AS _id", MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION};

    Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private MediaPlayer mMediaPlayer;
    private Cursor mCursor;
    private int mPlayPosition = 0;

    public static final String PLAY_ACTION = "com.marcello.jcgut.contentprovidermusic.PLAY_ACTION";
    public static final String PAUSE_ACTION =  "com.marcello.jcgut.contentprovidermusic.PAUSE_ACTION";
    public static final String NEXT_ACTION = "om.marcello.jcgut.contentprovidermusic.NEXT_ACTION";
    public static final String PREVIOUS_ACTION = "om.marcello.jcgut.contentprovidermusic.PREVIOUS_ACTION";

    public static final String
            SEND_CURRENT_PLAY = MusicService.class.getName() + "LocationBroadcast",
            CURRENT_PLAY = "current_play",MISC_INFO = "misc_info";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

       /* PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(singleton(Manifest.permission.READ_EXTERNAL_STORAGE), new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permissions Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(DeniedPermissions deniedPermissions) {
                String deniedPermissionsText = "Denied: " + Arrays.toString(deniedPermissions.toArray());
                Toast.makeText(getApplicationContext(), deniedPermissionsText, Toast.LENGTH_SHORT).show();

                for (DeniedPermission deniedPermission : deniedPermissions) {
                    if(deniedPermission.shouldShowRationale()) {
                        // Display a rationale about why this permission is required
                    }
                }
            }
        });*/


        mMediaPlayer = new MediaPlayer();
        mCursor = getContentResolver().query(MUSIC_URI,mCursorCols,"duration > 10000",null,null);

    }

    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);

        String action = intent.getAction();

        assert action != null;
        switch (action) {
            case PLAY_ACTION:
                if (mMediaPlayer.isPlaying()){
                    pause();
                } else {
                    play();
                }
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
        initPlay();
    }

    public void pause(){
        mMediaPlayer.pause();
        //stopSelf();
    }

    public void previous(){
        if(mPlayPosition == 0){
            mPlayPosition = mCursor.getCount() - 1;
        }else {
            mPlayPosition--;
        }
        initPlay();
    }

    public void next() {
        if (mPlayPosition == mCursor.getCount() - 1){
            mPlayPosition = 0;
        }else {
            mPlayPosition++;
        }
        initPlay();
    }

    public void initPlay(){

        mMediaPlayer.reset();
        String dataSource = getDateByPosition(mCursor, mPlayPosition);
        String info = getInfoByPosition(mCursor, mPlayPosition);

        Intent intent = new Intent(SEND_CURRENT_PLAY);
        intent.putExtra(CURRENT_PLAY, info);
        intent.putExtra(MISC_INFO, dataSource);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        /*Toast.makeText(getApplicationContext(),info,Toast.LENGTH_SHORT).show();*/

        try{
            mMediaPlayer.setDataSource(dataSource);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }catch (IllegalArgumentException | IOException ea){
            ea.printStackTrace();
        }
    }

    public String getDateByPosition(Cursor c, int position){
        c.moveToPosition(position);
        int dataColumn = c.getColumnIndex(MediaStore.Audio.Media.DATA);
        return c.getString(dataColumn);
    }

    public String getInfoByPosition(Cursor c, int position){
        c.moveToPosition(position);
        int titleColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistColumn = c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int albumColumn = c.getColumnIndex(MediaStore.Audio.Media.ALBUM);

        return c.getString(artistColumn)+" - "+c.getString(titleColumn)+" ("+c.getString(albumColumn)+")";
    }



    public void onDestroy(){
        super.onDestroy();
        mMediaPlayer.release();
    }


}