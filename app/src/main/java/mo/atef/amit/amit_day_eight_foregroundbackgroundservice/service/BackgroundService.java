package mo.atef.amit.amit_day_eight_foregroundbackgroundservice.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.R;

public class BackgroundService extends Service {
    private MediaPlayer musicPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = MediaPlayer.create(this, R.raw.amr);
        musicPlayer.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Music Service started by user.", Toast.LENGTH_LONG).show();
        musicPlayer.start();
        Log.e("Background","Started");
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("BackgroundUnbind","Unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        Log.e("Background","Destroyed");
        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}