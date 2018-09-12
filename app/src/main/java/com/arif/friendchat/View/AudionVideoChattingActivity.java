package com.arif.friendchat.View;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arif.friendchat.Entity.User;
import com.arif.friendchat.R;
import com.arif.friendchat.constant.Constant;




public class AudionVideoChattingActivity extends AppCompatActivity {
    private static final int CONNECTION_REQUEST = 1;
    private static final int RC_CALL = 111;
    String roomID;
    String chatType;
    String from;
    User user;

    private String stdByChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audion_video_chatting);
        from = getIntent().getStringExtra(Constant.FROM);
        roomID = getIntent().getStringExtra(Constant.ROOMID);
        chatType = getIntent().getStringExtra(Constant.CHAT_TYPE);

        if (from.equals("me")) {
            Log.e("AudionVideoChattingA","outGoingCall");
            outGoingCall();
        }else
        {
            Log.e("inCommingCall","outGoingCall");
            inCommingCall();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    private void dispatchIncomingCall(String userId){

    }

    private void inCommingCall()
{

}
    private void outGoingCall()
    {



    }



}
