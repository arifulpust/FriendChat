package com.arif.friendchat.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.arif.friendchat.constant.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
public  static  String TAG="LoginActivity";
    private FirebaseAuth auth;

    String email;
Gson gson=new Gson();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.email) EditText inputEmail;
    @BindView(R.id.password) EditText inputPassword;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root));
        unbinder= ButterKnife.bind(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

    }
    @OnClick(R.id.btn_signup)
    public void signUp()
    {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));

        overridePendingTransition(R.anim.right_to_left, R.anim.stand_by);

    }
    @OnClick(R.id.btn_reset_password)
    public void forgotPassword()
    {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
        overridePendingTransition(R.anim.right_to_left, R.anim.stand_by);
    }
@OnClick(R.id.btn_login)
public void login()
{
    email = inputEmail.getText().toString();
    final String password = inputPassword.getText().toString();
    if (TextUtils.isEmpty(email)) {
        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
        return;
    }
    if (TextUtils.isEmpty(password)) {
        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        return;
    }
    progressBar.setVisibility(View.VISIBLE);
    //authenticate user
    auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        // there was an error
                        if (password.length() < 6) {
                            inputPassword.setError(getString(R.string.minimum_password));
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("signInWithEmailAndPass",""+gson.toJson(task.getResult().getUser()));
                        AppData.saveData(AppData.email,email,getApplicationContext());
                        AppData.saveData(AppData.Acess_Toten,   task.getResult().getUser().getUid(),getApplicationContext());
                        getToken();

                    }
                }
            });
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
                        updateToken(token);

                    }
                });
    }
    String keyValue="" ;
private void updateToken(final String token)
{
    mFirebaseDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot ds : dataSnapshot.getChildren())
            {
                keyValue= ds.getKey().toString();

                Log.e("ds",keyValue+"\n"+ds);
            }
            try {


                mFirebaseDatabase.child(keyValue).child("token").setValue(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.stand_by);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}