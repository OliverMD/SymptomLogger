package com.odownard.symptomlogger;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by olive_000 on 09/09/2015.
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle("Add your Syptoms")
                .setContentText("Remember to add your synptoms and tags for today");
        ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(34567, builder.build());
    }
}
