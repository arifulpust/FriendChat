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
import com.arif.friendchat.R;
import com.arif.friendchat.Receiver.InlineReplyReceiver;
import com.arif.friendchat.View.AudionVideoChattingActivity;
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
              //  Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(remoteMessage.getData());
                Data fcmPushMessage=gson.fromJson(object.toString(),Data.class);
                Log.e("JSON_OBJECT", object.toString());
                Log.e("fcmPushMessage", gson.toJson(fcmPushMessage));
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    postNormalNotification(fcmPushMessage, 1);
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    postHistoryNotification( fcmPushMessage, 1, false);
                } else {
                    postHistoryNotification( fcmPushMessage, 1, true);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private PendingIntent getClickIntent(String room) {
        Intent intent = new Intent(this, PushActivity.class);
        intent.putExtra(PushActivity.class.getSimpleName(), room);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        return pIntent;

    }

    private void postNormalNotification(Data  data, int notiID) {
        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(data.type)
                .setContentText(data.message)
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


//        String replyLabel = "reply";
//        android.app.RemoteInput remoteInput = new android.app.RemoteInput.Builder(InlineReplyReceiver.KEY_REPLY)
//                .setLabel(replyLabel)
//                .build();

        Intent intent = new Intent(this, AudionVideoChattingActivity.class);
        //intent.setAction(Constant.ROOMID);
        intent.putExtra(Constant.ROOMID, data.user.Uid);
        intent.putExtra(Constant.CHAT_TYPE, data.type);
        intent.putExtra(Constant.FROM, data.user.name);

        intent.putExtra(InlineReplyReceiver.KEY_MESSAGE_ID, data.message);
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis() % 2000000000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.InboxStyle ibs = new Notification.InboxStyle();



        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.chat : R.drawable.chat)

                .setContentTitle(data.type)
                .setContentText(data.message)
                .setContentIntent(getClickIntent(""))
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

//        if (isReply) { // API >= 24
//            Notification.Action replyAction = new Notification.Action.Builder(
//                    R.drawable.chat, replyLabel, pIntent)
//                    .addRemoteInput(remoteInput)
//                    .setAllowGeneratedReplies(true)
//                    .build();
//            mBuilder.addAction(replyAction); // reply action from step b above
//        }

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

    public static final int NOTIFICATION_JOB_CONSULT_ID = -1;
    public static final int NOTIFICATION_LIVING_ID = -2;
    public static final int NOTIFICATION_BUILDING_GROUP = -3;

    public static int parseRoomChatToNotificationID(String from) {
        // userxxx_staffxxx, userxxx_job-consult, userxxx_living
        int res = 0;
        try {
            final String staff = "staff";
            if (from.contains(staff)) {
                String id = from.substring(from.indexOf(staff) + staff.length(), from.length());
                res = Integer.parseInt(id);
            } else if (from.contains("living")) {
                return NOTIFICATION_LIVING_ID;
            } else if (from.contains("consult")) {
                return NOTIFICATION_JOB_CONSULT_ID;
            }else if (from.contains("building")) {
                return NOTIFICATION_BUILDING_GROUP;
            } else {
                // case unknown
                res = -(int) (System.currentTimeMillis() % 2000000000) - 4; // <= -4 meaning unknown, then make stand alone notification
            }
        } catch (Exception e) {
            res = -(int) (System.currentTimeMillis() % 2000000000) - 4; // <= -4 meaning unknown, then make stand alone notification
        }
        return res;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
