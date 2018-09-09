package com.arif.friendchat.View;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResetPasswordActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    @BindView(R.id.email) EditText inputEmail;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root));
        unbinder= ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

    }
    @OnClick(R.id.login)
    public void Login()
    {
        overridePendingTransition(R.anim.stand_by, R.anim.right_to_left);
        finish();
    }
@OnClick(R.id.btn_reset_password)
public void resetPassword()
{

    String email = inputEmail.getText().toString().trim();

    if (TextUtils.isEmpty(email)) {
        Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
        return;
    }

    progressBar.setVisibility(View.VISIBLE);
    auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.stand_by, R.anim.left_to_right);
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
}

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.stand_by, R.anim.right_to_left);
        finish();
        super.onBackPressed();
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
}