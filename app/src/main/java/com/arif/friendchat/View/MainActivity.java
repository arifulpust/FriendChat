package com.arif.friendchat.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arif.friendchat.Fragment.AlertFragment;
import com.arif.friendchat.Fragment.HomeFragment;
import com.arif.friendchat.Fragment.MessageListFragment;
import com.arif.friendchat.Fragment.MyPageFragment;
import com.arif.friendchat.R;
import com.arif.friendchat.Entity.User;
import com.arif.friendchat.Adapter.UserListAdapter;
import com.arif.friendchat.Utils.ColorTheme;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.arif.friendchat.constant.AppData;
import com.arif.friendchat.constant.Constant;
import com.arif.friendchat.web_rtc.ConnectActivity;
import com.arif.friendchat.web_rtc.SettingsActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.arif.friendchat.R.drawable.gradient;

public class MainActivity extends AppCompatActivity {
public String TAG="MainActivity";

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
Gson gson=new Gson();
ListView user_list;
    public static final int FRAGMENT_HOME = 1;
    public static final int FRAGMENT_CHAT = 2;
    public static final int FRAGMENT_ALERT = 3;
    public static final int FRAGMENT_MY_PAGE = 4;
List<User>users=new ArrayList<>();
    UserListAdapter userListAdapter;
    public static  String Token;
    public static Drawable background = (Drawable) ColorTheme.makeGradientColor(ColorTheme.getColor("#80DEEA"),ColorTheme.getColor("#006064"));
    private String mCurrentFragmentTag = "";
    private ArrayList<String> mArrFragmentTag = new ArrayList<>();


    @BindView(R.id.txt_header_title) TextView txt_header_title;
    @BindView(R.id.rlt_footer_home)
    RelativeLayout rlt_footer_home;
    @BindView(R.id.rlt_footer_chat)
    RelativeLayout rlt_footer_chat;
    @BindView(R.id.rlt_footer_alert)
    RelativeLayout rlt_footer_alert;
    @BindView(R.id.rlt_footer_mypage)
    RelativeLayout rlt_footer_mypage;
    @BindView(R.id.timeLineIcon)
    AppCompatImageView timeLineIcon;
    @BindView(R.id.chatIcon)
    AppCompatImageView chatIcon;
    @BindView(R.id.alertIcon)
    AppCompatImageView alertIcon;
    @BindView(R.id.myPageIcon)
    AppCompatImageView myPageIcon;
    @BindView(R.id.btn_footer_home)
    AppCompatImageButton btn_footer_home;
    @BindView(R.id.btn_footer_chat)
    AppCompatImageButton btn_footer_chat;
    @BindView(R.id.btn_footer_alert)
    AppCompatImageButton btn_footer_alert;
    @BindView(R.id.btn_footer_mypage)
    AppCompatImageButton btn_footer_mypage;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // user_list=(ListView)findViewById(R.id.user_list);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        Token=AppData.getData(Constant.Token,getApplicationContext());
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.root));
        unbinder= ButterKnife.bind(this);
        clickFooter(1);
       auth = FirebaseAuth.getInstance();
    }

    private final String KEY_FRAGMENT_LIST = "fragmentlist";
    private final String KEY_FOOTER_BUTTON = "footerbutton";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putStringArrayList(KEY_FRAGMENT_LIST, mArrFragmentTag);
            outState.putInt(KEY_FOOTER_BUTTON, Integer.parseInt(mCurrentFragmentTag));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mArrFragmentTag = savedInstanceState.getStringArrayList(KEY_FRAGMENT_LIST);
        mCurrentFragmentTag = savedInstanceState.getInt(KEY_FOOTER_BUTTON) + "";
        if (mArrFragmentTag != null && (mCurrentFragmentTag + "").length() > 0) {
            setFooterState(Integer.parseInt(mCurrentFragmentTag));
            setHeader(Integer.parseInt(mCurrentFragmentTag));
        } else {
            // ignore
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    @OnClick(R.id.btn_setting)
    public void setting()
    {
        signOut();
//        Intent   intent = new Intent(MainActivity.this, SettingsActivity.class);
//        startActivity(intent);
    }
    @OnClick(R.id.btn_footer_home)
    public void showHomeFragment() {
        clickFooter(FRAGMENT_HOME);
    }

    @OnClick(R.id.btn_footer_chat)
    public void showChatFragment() {
        clickFooter(FRAGMENT_CHAT);
//        setVisibleForUnreadMessageView(false);
    }
    @OnClick(R.id.btn_footer_alert)
    public void showAlertFragment() {
        clickFooter(FRAGMENT_ALERT);
    }

    @OnClick(R.id.btn_footer_mypage)
    public void showMyPageFragment() {
        clickFooter(FRAGMENT_MY_PAGE);
    }
    private void clickFooter(int footer) {
        if (footer == FRAGMENT_MY_PAGE) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

            showFragment(footer);
            setHeader( footer );


//        if (!btn_footer_center_chat.isSelected()) {
        setFooterState(footer);
//        }

    }

    public void showFragment(int fragment) {

        Fragment newFragment = getSupportFragmentManager().findFragmentByTag("" + fragment);
        if (newFragment == null) {

            switch (fragment) {
                case FRAGMENT_HOME:

                    newFragment = new HomeFragment();

                    break;
                case FRAGMENT_CHAT:
                    newFragment = new MessageListFragment();
                    break;

                case FRAGMENT_ALERT:
                    newFragment = new AlertFragment();

                    break;
                case FRAGMENT_MY_PAGE:
                    newFragment = new MyPageFragment();

                    break;
            }
            mArrFragmentTag.add(fragment + "");
            hideAllFragment(fragment + "");

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.container, newFragment, fragment + "");
            transaction.addToBackStack(null);
            transaction.commit();
        }



    }
    private void hideAllFragment(String newFragmentTag) {
        for (String tag : mArrFragmentTag) {
            Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
            if (f != null) {
                if (tag.equals(newFragmentTag)) {

                } else {
                    getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag(tag)).commit();
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
           // window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
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
    String email;
private void getAllUer() {

    email = AppData.getData(AppData.email, getApplicationContext());
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
                userListAdapter.SetData(users);
            } catch (Exception e) {
                userListAdapter.SetData(users);
                Log.e("Exception--", "" + e.getMessage());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}
    //sign out method
    public void signOut() {
        AppData.Clear(getApplicationContext());
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    private void setFooterState(int fragment) {
        //  timeLineText  chatIcon  footer_chat_text_selector alertIcon  footer_atert_text_selector myPageIcon footer_atert_text_selector
        btn_footer_home.setSelected(fragment == FRAGMENT_HOME);
        rlt_footer_home.setSelected(fragment == FRAGMENT_HOME);
        btn_footer_chat.setSelected(fragment == FRAGMENT_CHAT);
        rlt_footer_chat.setSelected(fragment == FRAGMENT_CHAT);



        btn_footer_alert.setSelected(fragment == FRAGMENT_ALERT);
        rlt_footer_alert.setSelected(fragment == FRAGMENT_ALERT);
        btn_footer_mypage.setSelected(fragment == FRAGMENT_MY_PAGE);
        rlt_footer_mypage.setSelected(fragment == FRAGMENT_MY_PAGE);



        if(fragment==FRAGMENT_HOME)
        {
            timeLineIcon.setColorFilter(ColorTheme.getColor(ColorTheme.ColorPrimary));
        }
        else
        {
            timeLineIcon.setColorFilter(ColorTheme.getColor(ColorTheme.Gray));
        }
        if(fragment==FRAGMENT_CHAT)
        {
            chatIcon.setColorFilter(ColorTheme.getColor(ColorTheme.ColorPrimary));

        }
        else
        {
            chatIcon.setColorFilter(ColorTheme.getColor(ColorTheme.Gray));

        }
        if(fragment==FRAGMENT_ALERT)
        {
            alertIcon.setColorFilter(ColorTheme.getColor(ColorTheme.ColorPrimary));

        }
        else
        {
            alertIcon.setColorFilter(ColorTheme.getColor(ColorTheme.Gray));

        }
        if(fragment==FRAGMENT_MY_PAGE)
        {
            myPageIcon.setColorFilter(ColorTheme.getColor(ColorTheme.ColorPrimary));

        }
        else
        {
            myPageIcon.setColorFilter(ColorTheme.getColor(ColorTheme.Gray));

        }



    }
    private void setHeader(int footer) {
        String title = "";
        switch (footer) {
            case FRAGMENT_HOME:
                title = getResources().getString(R.string.title_timeline);
                break;
            case FRAGMENT_CHAT:
                title = getResources().getString(R.string.title_chat);
                break;
            case FRAGMENT_ALERT:
                title = getResources().getString(R.string.title_alert);
                break;
            case FRAGMENT_MY_PAGE:
                title = getResources().getString(R.string.title_my_page);
                break;
        }
        setHeaderTitle(title);
    }
    public void setHeaderTitle(String title) {
        txt_header_title.setText(title);
    }
}