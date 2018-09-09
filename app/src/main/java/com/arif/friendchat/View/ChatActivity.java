package com.arif.friendchat.View;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arif.friendchat.Adapter.ChatAdapter;
import com.arif.friendchat.Entity.ChatMessage;
import com.arif.friendchat.R;
import com.arif.friendchat.Entity.User;
import com.arif.friendchat.constant.AppData;
import com.arif.friendchat.constant.Constant;
import com.arif.friendchat.Interface.DataController;
import com.arif.friendchat.Utils.RemoteDataController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
    //MessageAdapter messageAdapter;
    ChatAdapter messageAdapter;
    User user;
    RecyclerView list_of_messages;
    List<ChatMessage>messages=new ArrayList<>();
    Gson gson=new Gson();
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        audio=(ImageView)findViewById(R.id.audio);
        video=(ImageView)findViewById(R.id.video);
        message = (EditText)findViewById(R.id.input);
        list_of_messages = (RecyclerView) findViewById(R.id.list_of_messages);
        user=(User)getIntent().getSerializableExtra("user");
        email=AppData.getData(AppData.email,getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_of_messages.setHasFixedSize(true);
        list_of_messages.setLayoutManager(linearLayoutManager);
        initializer();
        messageAdapter=new ChatAdapter(getApplicationContext(), messages,email);
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

        Log.e("Uid",""+Uid);
        chatMessage_1 = mFirebaseInstance.getReference(Constant.FireBase.KEY_CHAT).child(Constant.FireBase.KEY_ROOM_MESSAGES).child(Uid);
        chatMessage_2 = mFirebaseInstance.getReference(Constant.FireBase.KEY_CHAT).child(Constant.FireBase.KEY_ROOM_MESSAGES).child(user.Uid);
        getData();
    }
private void createChatMessage()
{


    HashMap<String, Object> values = new HashMap<>();
    values.put(Constant.FireBase.KEY_EMAIL, userEmail);
    values.put(Constant.FireBase.KEY_MESSAGE, message.getText().toString().trim());
    values.put(Constant.FireBase.KEY_TIMESTAMP,System.currentTimeMillis());
Log.e("values",""+values);
    chatMessage_1.push().setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
                Log.e("chatMessage_1","call");
                message.setText("");
                getData();
            }else
            {
                Log.e("chatMessage_1","fail");
            }
        }
    });

    chatMessage_2.push().setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
                Log.e("chatMessage_2","call");
            }else
            {
                Log.e("chatMessage_2","fail");
            }
        }
    });



}
    Map<String, String> params;

    @Override
    public void DataReceivedFromDataController(String response, int tag) {

    }

    @Override
    public void errorReceivedFromDataController(String error, int tag) {

    }
private void getData()
{
    chatMessage_1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            loadData(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
        }
    });


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e("onChildAdded","call");
            //loadData(dataSnapshot);

            // ...
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Log.e("onChildChanged","call");
            loadData(dataSnapshot);
            // ...
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    chatMessage_1.addChildEventListener(childEventListener);



}
private void loadData(DataSnapshot dataSnapshot )
{
    messages = new ArrayList();
    try {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            ChatMessage message = ds.getValue(ChatMessage.class);
            messages.add(message);

            Log.e("onDataChange",ds.getValue()+"\n"+gson.toJson(message)+"\n") ;
        }
    }
    catch ( Exception e)
    {
        Log.e("Exception mess",""+e.getMessage());
    }
    messageAdapter.SetData(messages);
    list_of_messages.smoothScrollToPosition(messages.size()-1);
}

    public void Back(View view) {
        finish();
    }
}
