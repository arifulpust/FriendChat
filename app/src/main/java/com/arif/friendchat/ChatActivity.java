package com.arif.friendchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arif.friendchat.constant.AppData;
import com.arif.friendchat.constant.Constant;
import com.arif.friendchat.requesthandler.DataController;
import com.arif.friendchat.requesthandler.RemoteDataController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements DataController{
RemoteDataController remoteDataController;
ImageView audio,video;
    private DatabaseReference chatMessage_1;
    private DatabaseReference chatMessage_2;
    private FirebaseDatabase mFirebaseInstance;
    EditText message;
    MessageAdapter messageAdapter;
    User user;
    ListView list_of_messages;
    List<ChatMessage>messages=new ArrayList<>();
    Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        audio=(ImageView)findViewById(R.id.audio);
        video=(ImageView)findViewById(R.id.video);
        message = (EditText)findViewById(R.id.input);
        list_of_messages = (ListView) findViewById(R.id.list_of_messages);
        user=(User)getIntent().getSerializableExtra("user");
        initializer();
        messageAdapter=new MessageAdapter(getApplicationContext(), messages);
        list_of_messages.setAdapter(messageAdapter);
      //  mUserId = firebaseRef.getAuth().getUid();
Log.e("user",""+gson.toJson(user));
        remoteDataController=new RemoteDataController(this,getApplicationContext());
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(message.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Write some thing!", Toast.LENGTH_SHORT).show();
                    return;
                }
                createChatMessage();
            }
        });

       // https://github.com/hieuapp/android-firebase-chat.git

    }
    String userEmail;
    private void initializer() {
         userEmail= AppData.getData(AppData.email,getApplicationContext());
        mFirebaseInstance = FirebaseDatabase.getInstance();
        String Uid=AppData.getData(AppData.Acess_Toten,getApplicationContext());
        chatMessage_1 = mFirebaseInstance.getReference(Constant.FireBase.KEY_CHAT).child(Constant.FireBase.KEY_ROOM_MESSAGES).child(Uid);
        chatMessage_2 = mFirebaseInstance.getReference(Constant.FireBase.KEY_CHAT).child(Constant.FireBase.KEY_ROOM_MESSAGES).child(user.Uid);

    }
private void createChatMessage()
{


    HashMap<String, Object> values = new HashMap<>();
    values.put(Constant.FireBase.KEY_EMAIL, userEmail);
    values.put(Constant.FireBase.KEY_MESSAGE, message.getText().toString().trim());
    values.put(Constant.FireBase.KEY_TIMESTAMP,System.currentTimeMillis());

    chatMessage_1.push().setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
                Log.e("chatMessage_1","call");
            }else
            {
                Log.e("chatMessage_1","fail");
            }
        }
    });
//    chatMessage_2.push().setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
//        @Override
//        public void onComplete(@NonNull Task<Void> task) {
//            if(task.isSuccessful())
//            {
//                Log.e("chatMessage_2","call");
//            }else
//            {
//                Log.e("chatMessage_2","fail");
//            }
//        }
//    });

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            messages = new ArrayList();
            try {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatMessage message = ds.getValue(ChatMessage.class);
                    messages.add(message);

                    Log.e("onDataChange",gson.toJson(message)+"\n") ;
                }
            }
     catch ( Exception e)
     {
         Log.e("Exception",""+e.getMessage());
     }
            messageAdapter.SetData(messages);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    chatMessage_1.addListenerForSingleValueEvent(eventListener);

}
    String url = Constant.FCM_URL;
    Map<String, String> params;

    @Override
    public void DataReceivedFromDataController(String response, int tag) {

    }

    @Override
    public void errorReceivedFromDataController(String error, int tag) {

    }


}
