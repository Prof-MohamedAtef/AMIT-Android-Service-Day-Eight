package mo.atef.amit.amit_day_eight_foregroundbackgroundservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.R;
import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.databinding.ActivityBackgroundServiceBinding;
import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.service.BackgroundService;

public class MusicBackgroundServiceActivity extends AppCompatActivity {

    ActivityBackgroundServiceBinding binding;
    private Intent backgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_background_service);
        backgroundService=new Intent(MusicBackgroundServiceActivity.this, BackgroundService.class);
        binding.btnLaunchMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startService(backgroundService);
                    }
                }).start();
                binding.btnLaunchMusic.setText("Start");
            }
        });
        
        
        binding.btnStopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(backgroundService);
                binding.btnLaunchMusic.setText("Stop");
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}