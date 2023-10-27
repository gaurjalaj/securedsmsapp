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
import com.google.gson.Gson;
import com.securedsmsapp.common.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Conversations {
    @SuppressLint("Range")
    public static ReadableArray getAllThreads(ReactApplicationContext reactApplicationContext) {
        WritableArray conversationList = Arguments.createArray();
        ContentResolver contentResolver = reactApplicationContext.getContentResolver();
        Cursor cursor = contentResolver.query(Telephony.Sms.Conversations.CONTENT_URI, null, null, null, "date DESC");

        if (cursor != null) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(5); // You can adjust the number of threads as needed

            // Create a list of conversations and their corresponding CompletableFutures
            List<ConversationData> conversationDataList = new ArrayList<>();

            while (cursor.moveToNext()) {
                String threadId = cursor.getString(cursor.getColumnIndex("thread_id"));
                WritableMap conversation = Arguments.createMap();
                conversation.putString("thread_id", threadId);
                conversation.putString("snippet", cursor.getString(cursor.getColumnIndex("snippet")));

                // Store the conversation and its CompletableFuture
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    ReadableMap lastSms = Sms.getLastSmsForThread(reactApplicationContext, threadId);
                    conversation.putMap("last_sms", lastSms);
                }, executor);

                // Store conversation and corresponding CompletableFuture in a data structure
                conversationDataList.add(new ConversationData(conversation, future));
            }

            // Wait for all CompletableFuture tasks to complete
            CompletableFuture<Void> allOf = CompletableFuture.allOf(
                    conversationDataList.stream()
                            .map(data -> data.future)
                            .toArray(CompletableFuture[]::new)
            );

            try {
                allOf.get(); // Wait for all tasks to finish
            } catch (InterruptedException | ExecutionException e) {
                // Handle exceptions
                e.printStackTrace();
            }

            // Add conversations to the result list
            conversationDataList.forEach(data -> conversationList.pushMap(data.conversation));

            cursor.close();
            executor.shutdown();
        }

        return conversationList;
    }

    public static Result deleteConversationByThreadId(String threadId) {
        return null;
    }

    // Data structure to associate conversations with their CompletableFuture
    private static class ConversationData {
        WritableMap conversation;
        CompletableFuture<Void> future;

        ConversationData(WritableMap conversation, CompletableFuture<Void> future) {
            this.conversation = conversation;
            this.future = future;
        }
    }

}
