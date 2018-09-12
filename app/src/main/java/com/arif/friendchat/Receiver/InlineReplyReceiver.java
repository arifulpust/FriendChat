package com.arif.friendchat.Receiver;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.arif.friendchat.Fragment.MessageListFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;


/**
 * Created by WIN-HAIVM on 12/12/17.
 */

public class InlineReplyReceiver extends BroadcastReceiver {
    public static final String KEY_REPLY = "keyreply";
    public static final String REPLY_ACTION = "replyaction";
    public static final String KEY_MESSAGE_ID = "messageid";
    public static final String KEY_ROOM = "room";
    public static final String KEY_NOTIFICATION_ID = "notificationid";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            CharSequence message = getReplyMessage(intent);
            int notificationID = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);
//            String title = intent.getStringExtra(KEY_MESSAGE_ID);
            String messageOriginal = intent.getStringExtra(KEY_MESSAGE_ID);
            String room = intent.getStringExtra(KEY_ROOM);

//            Toast.makeText(context, "Room ID: " + notificationID + "\nMessage: " + messageOriginal,
//                    Toast.LENGTH_SHORT).show();
            replyChat(context, message.toString(), room, messageOriginal);

            updateNotification(context, notificationID);

            Intent i = new Intent(MessageListFragment.class.getSimpleName());
            i.putExtra(KEY_ROOM, room);
            context.sendBroadcast(i);
        }
    }

    @SuppressWarnings("NewApi")
    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_REPLY);
        }
        return null;
    }

    private void updateNotification(Context context, int notificationID) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationID);
    }

    private void replyChat(Context context, String message, String roomID, String messageOriginal) {
        //FireBaseMessageEntity firebaseMessage = createFirebaseTextMessage(context, message);
     //   pushMessageToFireBase(context, firebaseMessage, roomID);
        setRead(context, messageOriginal);

    }


//    private FireBaseMessageEntity createFirebaseTextMessage(Context context, String text) {
//        FireBaseMessageEntity firebaseMessage = ChatFragment.createBaseFirebaseMessage(context);
//        firebaseMessage.setType(Constant.FireBase.VALUE_DEFAULT);
//        firebaseMessage.setMessage(text);
//        return firebaseMessage;
//    }

//    private void pushMessageToFireBase(Context context, final FireBaseMessageEntity message, String roomID) {
//        if (!Utils.isNetworkConnected(context)) {
//            String title = context.getString(R.string.err_title);
//            String errMessage = context.getString(R.string.err_no_network);
//            Toast.makeText(context, errMessage, Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        initChat(roomID);
//
//       // Map<String, Object> values = message.toMap();
//     //   mDBReferenceChat.push().setValue(values);
//
//      //  Map<String, Object> valuesUnread = message.toMapUnread();
//       // mDBReferenceChatUnread.updateChildren(valuesUnread);
//    }

    private final boolean isServerReturnMessageIdOnPush = false;
    private void setRead(Context c, String messageOriginal) {
        if(!isServerReturnMessageIdOnPush){
            return;
        }

        String mMyID = "";
        String mMyName = "";
//        if (mMyID.length() == 0) {
//         //   mMyID = Utils.getFirebaseUserIdFromUserId(SharedPreferenceUtil.getUserID
//                    (c));
//         //   mMyName = SharedPreferenceUtil.getName(c);
//        }

//        message.setRead(FireBaseMessageEntity.READ_READ);

       // FireBaseReadEntity rMessage = new FireBaseReadEntity();

       // rMessage.setName(mMyName);
       // rMessage.setId(mMyID);
       // rMessage.setAvatar(SharedPreferenceUtil.getIconPath(c));

     //   Map<String, Object> readMessage = rMessage.toMap();

       // mDBReferenceChat.child(messageOriginal).child(Constant.FireBase.KEY_READ_USERS).child(mMyID).updateChildren(readMessage);
    }

    private DatabaseReference mDBReference = null;
    private DatabaseReference mDBReferenceChat = null;
    private DatabaseReference mDBReferenceChatUnread = null;

    private void initChat(String roomID) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference();//"server/saving-data/fireblog"
       // mDBReference = ref.child(Constant.FireBase.KEY_CHAT);
      //  mDBReferenceChat = mDBReference.child(Constant.FireBase.KEY_ROOM_MESSAGES).child(roomID);
       // mDBReferenceChatUnread = mDBReference.child(Constant.FireBase.KEY_ROOM_UNREAD).child(roomID);
    }
}
