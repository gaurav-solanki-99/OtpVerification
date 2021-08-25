package com.example.otpverification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    public static  final  int REQ_USER_CONSENT=200;
    SmsBroadCastReceiver smsBroadCastReceiver;
    EditText etOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etOtp=(EditText)findViewById(R.id.etOtpVerify);
        startSmarUserConsent();
    }

    private void startSmarUserConsent()
    {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_USER_CONSENT)
        {
            if((resultCode==RESULT_OK)&&(data!=null))
            {
                String message= data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }


        }
    }

    private void getOtpFromMessage(String message) {

        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher=pattern.matcher(message);

        if(matcher.find())
        {
            etOtp.setText(matcher.group(0));
        }

    }

    private  void registerBroadCastReceiver()
    {
        smsBroadCastReceiver = new SmsBroadCastReceiver();
        smsBroadCastReceiver.smsBroadCastReceiverListener=new SmsBroadCastReceiver.SmsBroadCastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent,REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        };


        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadCastReceiver,intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerBroadCastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadCastReceiver);
    }
}