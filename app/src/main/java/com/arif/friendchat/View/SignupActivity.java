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
import com.arif.friendchat.Entity.User;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.arif.friendchat.constant.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignupActivity extends AppCompatActivity {
    public String TAG="SignupActivity";

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    @BindView(R.id.confirm_password) EditText confirm_password;
    @BindView(R.id.email) EditText inputEmail;
    @BindView(R.id.password) EditText inputPassword;
    @BindView(R.id.name) EditText inputName;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private String userId;
    String email,password,name;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root));

        unbinder= ButterKnife.bind(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();




    }
@OnClick(R.id.sign_up_button)
public void signUp()
{

    name = inputName.getText().toString().trim();
    email = inputEmail.getText().toString().trim();
    password = inputPassword.getText().toString().trim();
 String    password_confirm = confirm_password.getText().toString().trim();
    if (TextUtils.isEmpty(name)) {
        Toast.makeText(getApplicationContext(), "Enter User Name address!", Toast.LENGTH_SHORT).show();
        return;
    }
    if (TextUtils.isEmpty(email)) {
        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
        return;
    }

    if (TextUtils.isEmpty(password)) {
        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        return;
    }

    if (password.length() < 6) {
        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        return;
    }
    if (!password.equals(password_confirm)) {
        Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_SHORT).show();
        return;
    }


    progressBar.setVisibility(View.VISIBLE);
    //create user
    auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    Uid=task.getResult().getUser().getUid();
                    AppData.saveData(AppData.Acess_Toten,Uid,getApplicationContext());
                    AppData.saveData(AppData.email,email,getApplicationContext());

                    if (!task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // addToFirebaseUserList();
                        getNewToken();

                    }
                }
            });
}
    @OnClick(R.id.sign_in_button)
    public void Login()
    {
        overridePendingTransition(R.anim.stand_by, R.anim.right_to_left);
        finish();
    }

    @OnClick(R.id.btn_reset_password)
    public void resetPassword()
    {
        finish();
        startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
        overridePendingTransition(R.anim.right_to_left, R.anim.stand_by);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
String Uid;
    private void getNewToken()
    {



        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String token = getString(R.string.msg_subscribed);
                        Log.e("token",""+token);
                        if (!task.isSuccessful()) {

                            Toast.makeText(SignupActivity.this, getString(R.string.msg_subscribe_failed), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            getToken();

                        }


                    }
                });
    }

    @Override
    public void onBackPressed() {

        overridePendingTransition(R.anim.stand_by, R.anim.right_to_left);
        finish();
        super.onBackPressed();
    }

    private void createUser(String token) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name,email, Uid,token);
        AppData.saveData(AppData.Acess_Toten,userId,getApplicationContext());
AppData.saveData(AppData.user_info,new Gson().toJson(user),getApplicationContext());
        mFirebaseDatabase.child(userId).setValue(user);

        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.right_to_left, R.anim.stand_by);
        finish();
    }

    /**
     * User data change listener
     */
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
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
                        createUser(token);
                    }
                });
    }

}