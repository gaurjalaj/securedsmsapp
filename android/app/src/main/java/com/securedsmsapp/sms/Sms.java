package com.securedsmsapp.sms;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.securedsmsapp.common.Result;

public class Sms {
    @SuppressLint("Range")
    public static ReadableMap getLastSmsForThread(ReactApplicationContext reactApplicationContext, String threadId) {
        WritableMap lastSmsMap = Arguments.createMap();
        String selection = "thread_id=?";
        String[] selectionArgs = new String[]{threadId};
        String sortOrder = "date DESC LIMIT 1";

        ContentResolver contentResolver = reactApplicationContext.getContentResolver();
        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, selection, selectionArgs, sortOrder);

        if (cursor != null && cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);
                lastSmsMap.putString(columnName, columnValue);
            }
            cursor.close();
        }

        return lastSmsMap;
    }

    @SuppressLint("Range")
    public static ReadableArray getAllSmsByThreadId(ReactApplicationContext reactApplicationContext, String threadId) {
        WritableArray allSms = Arguments.createArray();
        String selection = "thread_id=?";
        String[] selectionArgs = new String[]{threadId};
        String sortOrder = "date DESC";

        ContentResolver contentResolver = reactApplicationContext.getContentResolver();
        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                WritableMap smsMap = Arguments.createMap();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    smsMap.putString(columnName, columnValue);
                }
                allSms.pushMap(smsMap);
            }
            cursor.close();
        }

        return allSms;
    }

    @SuppressLint("Range")
    public static ReadableArray getAllSmsByAddress(ReactApplicationContext reactApplicationContext, String address) {
        WritableArray allSms = Arguments.createArray();
        String selection = "address=?";
        String[] selectionArgs = new String[]{address};
        String sortOrder = "date DESC";

        ContentResolver contentResolver = reactApplicationContext.getContentResolver();
        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                WritableMap smsMap = Arguments.createMap();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    smsMap.putString(columnName, columnValue);
                }
                allSms.pushMap(smsMap);
            }
            cursor.close();
        }

        return allSms;
    }

    public static Result deleteSmsById(String id) {
        return null;
    }
}
