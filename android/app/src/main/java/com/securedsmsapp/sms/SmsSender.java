package com.securedsmsapp.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.google.gson.Gson;
import com.securedsmsapp.common.Result;

import java.util.ArrayList;

public class SmsSender {
    public static final String TAG = "com.securedsmsapp.sms";
    public static void sendTextSms(ReactApplicationContext reactApplicationContext, String address, String text, Promise promise){
        try {
            SmsManager smsManager = MySmsManager.getInstance().getCurr();
            final ArrayList<String> parts = smsManager.divideMessage(text);

            final ArrayList<PendingIntent> sentIntents = new ArrayList<>();
            final ArrayList<PendingIntent> deliveryIntents = new ArrayList<>();

            // Broadcast receiver for sent status
            BroadcastReceiver sentStatusReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int resultCode = getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        System.out.println("SMS_SENT_SUCCESS");
                        promise.resolve(new Gson().toJson(new Result(0, "SMS_SENT_SUCCESS")));
                    } else {
                        System.out.println("SMS_SENT_FAILED");
                        promise.reject("SMS not sent.", "Error code: " + resultCode);
                    }
                    reactApplicationContext.unregisterReceiver(this);
                }
            };

            // Broadcast receiver for delivery status
            BroadcastReceiver deliveryStatusReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int resultCode = getResultCode();
                    if (resultCode == Activity.RESULT_OK) {
                        System.out.println(TAG + "SMS delivered");
                    } else {
                        System.out.println(TAG + "SMS not delivered");
                    }
                    reactApplicationContext.unregisterReceiver(this);
                }
            };

            reactApplicationContext.registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
            reactApplicationContext.registerReceiver(deliveryStatusReceiver, new IntentFilter("SMS_DELIVERED"));

            for (int i = 0; i < parts.size(); i++) {
                sentIntents.add(PendingIntent.getBroadcast(reactApplicationContext, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_MUTABLE));
                deliveryIntents.add(PendingIntent.getBroadcast(reactApplicationContext, 0, new Intent("SMS_DELIVERED"), PendingIntent.FLAG_MUTABLE));
            }

            smsManager.sendMultipartTextMessage(address, null, parts, sentIntents, deliveryIntents);
        } catch (Exception ex) {
            promise.reject("SMS send failed.", ex.getMessage());
        }
    }

}
