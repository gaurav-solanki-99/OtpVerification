package com.example.otpverification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsBroadCastReceiver  extends BroadcastReceiver
{

     public SmsBroadCastReceiverListener smsBroadCastReceiverListener;
    @Override
    public void onReceive(Context context, Intent intent) {

          if(intent.getAction()== SmsRetriever.SMS_RETRIEVED_ACTION)
          {
              Bundle extras=intent.getExtras();

              Status smsRetreiverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);


              switch (smsRetreiverStatus.getStatusCode())
              {
                  case CommonStatusCodes
                          .SUCCESS:
                       Intent messageIntent= extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                       smsBroadCastReceiverListener.onSuccess(messageIntent);
                       break;
                  case CommonStatusCodes.TIMEOUT:
                          smsBroadCastReceiverListener.onFailure();
                          break;


              }
          }

    }

    public interface SmsBroadCastReceiverListener
    {
        void onSuccess(Intent intent);

        void onFailure();
    }
}
