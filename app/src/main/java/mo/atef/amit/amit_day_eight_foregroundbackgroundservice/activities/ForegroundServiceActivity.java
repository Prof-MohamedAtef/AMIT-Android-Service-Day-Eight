package mo.atef.amit.amit_day_eight_foregroundbackgroundservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.R;
import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.databinding.ActivityMainBinding;
import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.service.ForegroundService;

public class ForegroundServiceActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private Intent foregroundIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        foregroundIntent=new Intent(ForegroundServiceActivity.this, ForegroundService.class);
        binding.buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startService();
                    }
                }).start();
            }
        });

        binding.buttonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stopService_();
                    }
                }).start();
            }
        });
    }

    private void stopService_() {
        stopService(foregroundIntent);
    }

    private void startService() {
        foregroundIntent.putExtra("foreground","Foreground Service is running");
        ContextCompat.startForegroundService(getApplicationContext(), foregroundIntent);
    }
}