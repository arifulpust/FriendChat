package com.arif.friendchat.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arif.friendchat.Adapter.ChatUserAdapter;
import com.arif.friendchat.Entity.User;
import com.arif.friendchat.Interface.OnItemClickListener;
import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.arif.friendchat.View.ChatActivity;
import com.arif.friendchat.View.MainActivity;
import com.arif.friendchat.constant.AppData;
import com.arif.friendchat.constant.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MessageListFragment extends Fragment implements OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.user_list)
    RecyclerView user_list;
    ChatUserAdapter chatUserAdapter;
    List<User>users=new ArrayList<>();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Gson gson=new Gson();
    public static  String Token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.message_layout, container, false);
        unbinder= ButterKnife.bind(this,view);
        CorrectSizeUtil.getInstance(getActivity()).correctSize(view);
        user_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        user_list.setHasFixedSize(true);
        chatUserAdapter=new ChatUserAdapter(getActivity(),users,this);
        user_list.setAdapter(chatUserAdapter);
Log.e("MessageListFragment","call");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        Token= AppData.getData(Constant.Token,getActivity());


         mFirebaseDatabase=mFirebaseDatabase.child(Token);


        getAllUer();
        getToken();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    String email;
    private void getToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("MessageList", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast

                        Log.e("MessageList","token-- "+ token);
                    }
                });
    }
    private void getAllUer() {

        email = AppData.getData(AppData.email, getActivity());
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users = new ArrayList();
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (!user.email.equals(email))
                            users.add(user);

                        Log.e("onDataChange", ds + "\n" + gson.toJson(users) + "\n");
                    }
                    chatUserAdapter.SetData(users);
                } catch (Exception e) {
                    chatUserAdapter.SetData(users);
                    Log.e("Exception--", "" + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onClickItem(int position) {
        Intent intent=new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("user",users.get(position));
        startActivity(intent);
    }
}
