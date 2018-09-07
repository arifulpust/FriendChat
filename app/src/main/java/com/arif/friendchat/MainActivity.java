package com.arif.friendchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arif.friendchat.constant.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MainActivity extends AppCompatActivity {
public String TAG="MainActivity";
    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
            changeEmail, changePassword, sendEmail, remove, signOut;

    private EditText oldEmail, newEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
Gson gson=new Gson();
ListView user_list;
List<User>users=new ArrayList<>();
    UserListAdapter userListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_list=(ListView)findViewById(R.id.user_list);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        auth = FirebaseAuth.getInstance();

        Log.e("auth",""+gson.toJson(auth.getUid()));

        userListAdapter = new UserListAdapter(getApplicationContext(), users);
        user_list.setAdapter(userListAdapter);
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("user",users.get(position));
                startActivity(intent);
            }
        });
        auth = FirebaseAuth.getInstance();

        //get current user

        getAllUer();
        initializer();
        getToken();
    }
    private void getToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast

                        Log.e(TAG,"token-- "+ token);
                    }
                });
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
switch (item.getItemId())
{
    case R.id.setting:
    break;
    case R.id.logout:
        signOut();
        break;
}
        //respond to menu item selection
        return super.onOptionsItemSelected(item);
    }
private void getAllUer()
{
    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            users = new ArrayList();
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                User user = ds.getValue(User.class);
                users.add(user);

                Log.e("onDataChange",gson.toJson(users)+"\n") ;
            }
            userListAdapter.SetData(users);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    mFirebaseDatabase.addListenerForSingleValueEvent(eventListener);


}

private void initializer()
{
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

    btnChangeEmail = (Button) findViewById(R.id.change_email_button);
    btnChangePassword = (Button) findViewById(R.id.change_password_button);
    btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
    btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
    changeEmail = (Button) findViewById(R.id.changeEmail);
    changePassword = (Button) findViewById(R.id.changePass);
    sendEmail = (Button) findViewById(R.id.send);
    remove = (Button) findViewById(R.id.remove);
    signOut = (Button) findViewById(R.id.sign_out);

    oldEmail = (EditText) findViewById(R.id.old_email);
    newEmail = (EditText) findViewById(R.id.new_email);
    password = (EditText) findViewById(R.id.password);
    newPassword = (EditText) findViewById(R.id.newPassword);

    oldEmail.setVisibility(View.GONE);
    newEmail.setVisibility(View.GONE);
    password.setVisibility(View.GONE);
    newPassword.setVisibility(View.GONE);
    changeEmail.setVisibility(View.GONE);
    changePassword.setVisibility(View.GONE);
    sendEmail.setVisibility(View.GONE);
    remove.setVisibility(View.GONE);

    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
    }

    btnChangeEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            oldEmail.setVisibility(View.GONE);
            newEmail.setVisibility(View.VISIBLE);
            password.setVisibility(View.GONE);
            newPassword.setVisibility(View.GONE);
            changeEmail.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);
            sendEmail.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        }
    });

    changeEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !newEmail.getText().toString().trim().equals("")) {
                user.updateEmail(newEmail.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                    signOut();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            } else if (newEmail.getText().toString().trim().equals("")) {
                newEmail.setError("Enter email");
                progressBar.setVisibility(View.GONE);
            }
        }
    });

    btnChangePassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            oldEmail.setVisibility(View.GONE);
            newEmail.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            newPassword.setVisibility(View.VISIBLE);
            changeEmail.setVisibility(View.GONE);
            changePassword.setVisibility(View.VISIBLE);
            sendEmail.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        }
    });

    changePassword.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null && !newPassword.getText().toString().trim().equals("")) {
                if (newPassword.getText().toString().trim().length() < 6) {
                    newPassword.setError("Password too short, enter minimum 6 characters");
                    progressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(newPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            } else if (newPassword.getText().toString().trim().equals("")) {
                newPassword.setError("Enter password");
                progressBar.setVisibility(View.GONE);
            }
        }
    });

    btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            oldEmail.setVisibility(View.VISIBLE);
            newEmail.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            newPassword.setVisibility(View.GONE);
            changeEmail.setVisibility(View.GONE);
            changePassword.setVisibility(View.GONE);
            sendEmail.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
    });

    sendEmail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (!oldEmail.getText().toString().trim().equals("")) {
                auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            } else {
                oldEmail.setError("Enter email");
                progressBar.setVisibility(View.GONE);
            }
        }
    });

    btnRemoveUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                    finish();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }
    });

    signOut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signOut();
        }
    });
}
    //sign out method
    public void signOut() {
        AppData.Clear(getApplicationContext());
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}