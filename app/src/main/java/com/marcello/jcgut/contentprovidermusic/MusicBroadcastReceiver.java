package com.marcello.jcgut.contentprovidermusic;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.widget.Toast;

public class MusicBroadcastReceiver extends BroadcastReceiver {

    public MusicBroadcastReceiver(){
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context ctx, Intent intent){

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if ((usbCharge||acCharge)&&isCharging) {
            Toast.makeText(ctx, "Charger Connected, Music Started",
                    Toast.LENGTH_SHORT).show();

            Vibrator musicvib = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            musicvib.vibrate(500);
            Intent intent1 = new Intent(ctx, MusicContentBridge.class);
            ctx.startActivity(intent1);

    }

}}