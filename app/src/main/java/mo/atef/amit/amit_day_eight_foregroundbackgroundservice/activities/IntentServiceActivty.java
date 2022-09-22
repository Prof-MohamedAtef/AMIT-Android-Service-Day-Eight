package mo.atef.amit.amit_day_eight_foregroundbackgroundservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.R;
import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.databinding.ActivityIntentServiceActivtyBinding;
import mo.atef.amit.amit_day_eight_foregroundbackgroundservice.service.MyWebRequestIntentService;

public class IntentServiceActivty extends AppCompatActivity {


    ActivityIntentServiceActivtyBinding binding;
    private MyWebServiceReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intent_service_activty);

        // define BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(MyWebServiceReceiver.PROCESS_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        receiver = new MyWebServiceReceiver();
        registerReceiver(receiver, intentFilter);

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent msgIntent=new Intent(IntentServiceActivty.this, MyWebRequestIntentService.class);

                msgIntent.putExtra(MyWebRequestIntentService.REQUEST_STRING, "https://www.google.com");
                startService(msgIntent);

                msgIntent.putExtra(MyWebRequestIntentService.REQUEST_STRING,"https://www.youtube.com");
                startService(msgIntent);

                msgIntent.putExtra(MyWebRequestIntentService.REQUEST_STRING,"https://www.linkedin.com");
                startService(msgIntent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class MyWebServiceReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.as400samplecode.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String responseString= intent.getStringExtra(MyWebRequestIntentService.RESPONSE_STRING);
            String responseMessage= intent.getStringExtra(MyWebRequestIntentService.RESPONSE_MESSAGE);

            binding.response.setText(responseString);
            binding.myWebView.getSettings().setJavaScriptEnabled(true);

            try {
                binding.myWebView.loadData(URLEncoder.encode(responseMessage, "utf-8").replaceAll("\\+", " "), "text/html", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}