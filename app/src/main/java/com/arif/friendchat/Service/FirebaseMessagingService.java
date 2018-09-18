package com.arif.friendchat.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.arif.friendchat.Entity.Data;
import com.arif.friendchat.Entity.FCMPushMessage;
import com.arif.friendchat.Entity.User;
import com.arif.friendchat.R;

import com.arif.friendchat.View.AudionVideoChattingActivity;
import com.arif.friendchat.View.IncomingCallActivity;
import com.arif.friendchat.View.PushActivity;
import com.arif.friendchat.constant.Constant;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    public Context context = this;
Gson gson=new Gson();
   public User user;
    // flag for network status
    public  String Tag="FirebaseMessagingService";

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getData()+"  "+remoteMessage.getData().size() );
        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try
            {
                Data data=new Data();
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(remoteMessage.getData());
                String user_str=object.getString("user");
                String message=object.getString("message");
                String type=object.getString("type");
                user=gson.fromJson(user_str,User.class);
                data.user=user;
                data.type=type;
                data.message=message;
                Log.e("JSON_OBJECT", object.toString());
                Log.e("fcmPushMessage", gson.toJson(data));
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    postNormalNotification(data, 1);
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    postHistoryNotification( data, 1, false);
                } else {
                    postHistoryNotification( data, 1, true);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private PendingIntent getClickIntent(Data data) {
        Intent intent = new Intent(this, IncomingCallActivity.class);
        intent.putExtra(Constant.ROOMID, data.channel);

        intent.putExtra(Constant.CHAT_TYPE, data.type);
        intent.putExtra(Constant.FROM, data.user.name);


        //intent.putExtra(PushActivity.class.getSimpleName(), room);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        return pIntent;

    }

    private void postNormalNotification(Data  data, int notiID) {

        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(data.type)
                .setContentText(data.message)
                .setContentIntent(getClickIntent(data))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.message))
                .setSmallIcon(R.mipmap.ic_launcher)
               // .setContentIntent(getClickIntent(room))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        n.defaults |= Notification.DEFAULT_VIBRATE;
        n.defaults |= Notification.DEFAULT_SOUND;

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);
        mNotificationManager.notify(notiID, n);
    }

    private final int MAX_NOTI_LINE = 6;

    @SuppressWarnings("NewApi") // 20 or above
    private void postHistoryNotification(Data data, int notiID, boolean isReply) {



        Intent intent = new Intent(this, IncomingCallActivity.class);


        intent.putExtra(Constant.PUST_DATA, data);
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis() % 2000000000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.InboxStyle ibs = new Notification.InboxStyle();



        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.chat : R.drawable.chat)

                .setContentTitle(data.type)
                .setContentIntent(pIntent)
                .setContentText(data.message)
                .setContentIntent(getClickIntent(data))
                .setStyle(ibs)
//                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
//                .setStyle(big)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("room");
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("room", data.type, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Notification n = mBuilder.build();
        n.defaults |= Notification.DEFAULT_VIBRATE;
        n.defaults |= Notification.DEFAULT_SOUND;

        mNotificationManager.notify(notiID, n);

    }





}
