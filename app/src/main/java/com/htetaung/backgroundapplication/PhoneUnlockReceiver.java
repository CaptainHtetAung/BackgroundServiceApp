package com.htetaung.backgroundapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by HtetAung on 5/18/18.
 */

public class PhoneUnlockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //catch phone unlock event
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Intent newIntent = new Intent(context, PictureViewActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(newIntent);
        }
    }
}
