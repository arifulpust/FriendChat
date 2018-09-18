package com.arif.friendchat.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arif.friendchat.Entity.Data;
import com.arif.friendchat.Entity.FCMPushMessage;
import com.arif.friendchat.Entity.User;
import com.arif.friendchat.Interface.DataController;
import com.arif.friendchat.R;
import com.arif.friendchat.Utils.RemoteDataController;
import com.arif.friendchat.constant.Account;
import com.arif.friendchat.constant.Constant;
import com.google.gson.Gson;

import java.util.List;

public class IncomingCallActivity extends Activity implements DataController {
    String TAG=getClass().getSimpleName();
Gson gson=new Gson();
    Data push_data=new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            push_data = (Data) b.getSerializable(Constant.PUST_DATA);
        }

        Log.e(TAG,gson.toJson(push_data));
    }


    public void acceptCall(View view){
        Intent intent = new Intent(IncomingCallActivity.this, AudionVideoChattingActivity.class);
        intent.putExtra(Constant.PUST_DATA,push_data);
        startActivity(intent);
    }

    /**
     * Publish a hangup command if rejecting call.
     * @param view
     */
    public void rejectCall(View view){
        RemoteDataController remoteDataController=new RemoteDataController(this,getApplicationContext());
        Account.initializeUserInfo(getApplicationContext());
     User my_info= Account.user;
        FCMPushMessage fcmPushMessage=new FCMPushMessage();
       Data data=new Data();
        data.message="i am busy now";
        data.type=Constant.REJECT;
        fcmPushMessage.data= data;
        Log.e("FCMPushMessage",""+gson.toJson(fcmPushMessage));
        remoteDataController.FCMPushMessage(Constant.FCM_URL,fcmPushMessage,101);
  finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    public void DataReceivedFromDataController(String response, int tag) {

    }

    @Override
    public void errorReceivedFromDataController(String error, int tag) {

    }
}
