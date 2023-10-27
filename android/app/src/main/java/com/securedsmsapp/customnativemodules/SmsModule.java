package com.securedsmsapp.customnativemodules;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.google.gson.Gson;
import com.securedsmsapp.sms.Conversations;
import com.securedsmsapp.sms.Sms;
import com.securedsmsapp.sms.SmsSender;

public class SmsModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactApplicationContext;
    public SmsModule(ReactApplicationContext reactApplicationContext){
        super(reactApplicationContext);
        this.reactApplicationContext = reactApplicationContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "SmsModule";
    }

    @ReactMethod
    public void sendTextSms(String address, String text, Promise promise){
        SmsSender.sendTextSms(reactApplicationContext, address, text, promise);
    }

    @ReactMethod
    public void getSmsConversationsList(Promise promise){
        try{
            ReadableArray conversationsList = Conversations.getAllThreads(this.reactApplicationContext);
            promise.resolve(conversationsList);
        }catch (Exception e){
            promise.reject("ERROR: ", e);
        }
    }

    @ReactMethod
    public void getSmsByThreadId(String threadId, Promise promise){
        try{
            ReadableArray allSmsOfThread = Sms.getAllSmsByThreadId(this.reactApplicationContext, threadId);
            promise.resolve(allSmsOfThread);
        }catch (Exception e){
            promise.reject("ERROR: ", e);
        }
    }

    @ReactMethod
    public void getSmsByAddress(String address, Promise promise){
        try{
            ReadableArray allSmsOfAddress = Sms.getAllSmsByAddress(this.reactApplicationContext, address);
            promise.resolve(allSmsOfAddress);
        }catch (Exception e){
            promise.reject("ERROR: ", e);
        }
    }

}
