package com.arif.friendchat.View;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.arif.friendchat.constant.AppData;
import com.arif.friendchat.constant.Constant;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SpashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root));
        goNextScreen();
    }

    private void goNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SpashEnded();
            }
        }, Constant.SPLASH_TIME);
    }

    private void SpashEnded() {
        Class cl;

        Log.e("SpashEnded",AppData.getData(AppData.Acess_Toten,getApplicationContext())+"");
        if (AppData.getData(AppData.Acess_Toten,getApplicationContext()).trim().length() > 1&&AppData.getData(AppData.Acess_Toten,getApplicationContext())!=null) {
            // has been logged in
//                    if (SharedPreferenceUtil.isSkillCheckComplete(SplashActivity.this)) {
            cl = MainActivity.class;
//                    } else {
//                        cl = SkillCheckActivity_.class;
//                    }
        } else {
            // not logged in

                cl = LoginActivity.class;

        }
        Intent i = new Intent(SpashActivity.this, cl);
        startActivity(i);
        finish();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
