package com.securedsmsapp.sms;

import android.telephony.SmsManager;

public class MySmsManager {
    private static MySmsManager instance;
    private SmsManager smsManager;

    private MySmsManager() {
        smsManager = SmsManager.getDefault();
    }

    public static MySmsManager getInstance() {
        if (instance == null) {
            instance = new MySmsManager();
        }
        return instance;
    }

    public SmsManager getCurr(){
        return this.smsManager;
    }

    public void setCurrSIM(int simSlotIndex) {
        this.smsManager = SmsManager.getSmsManagerForSubscriptionId(simSlotIndex);
    }
}

