package com.arif.friendchat.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.arif.friendchat.R;


public class PushActivity extends Activity {
    private final int ACTION_LOGIN = 1;
    private int mAction = 0;
    private String mToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        getData();
        goToChatScreen();
        finish();
    }

    String room = "";

    private void getData() {
        Intent i = getIntent();
        if (i == null) {
            return;
        }
        if (i.hasExtra(PushActivity.class.getSimpleName())) {
            room = i.getStringExtra(PushActivity.class.getSimpleName());
        }
    }

    private void goToChatScreen() {
        if (room.length() == 0) {
            startSplash();
            return;
        }
        int roomType = -1;
        if (room.contains("job-consult")) {
           // roomType = FireBaseRoomEntity.ROOM_TYPE_JOB_CONSULT_CHAT;

        }
        if (roomType == -1) {
            startSplash();
            return;
        }



        Intent intent = null;
        intent = new Intent(this, ChatActivity.class);
          // BaseActivity.className = JobConsultationActivity_.class.getSimpleName();
//        switch (roomType) {
//            case FireBaseRoomEntity.ROOM_TYPE_JOB_CONSULT_CHAT:
//                intent = new Intent(this, JobConsultationActivity_.class);
//                BaseActivity.className = JobConsultationActivity_.class.getSimpleName();
//                break;
//            case FireBaseRoomEntity.ROOM_TYPE_LIVING_CHAT:
//                intent = new Intent(this, HouseActivity_.class);
//                BaseActivity.className = HouseActivity_.class.getSimpleName();
//                break;
//            case FireBaseRoomEntity.ROOM_TYPE_BUILDING_GROUP:
//                intent = new Intent(this, BuildingGroupChatActivity_.class);
//                BaseActivity.className = BuildingGroupChatActivity_.class.getSimpleName();
//                break;
//            case FireBaseRoomEntity.ROOM_TYPE_STAFF_CHAT:
//                intent = new Intent(this, JobConsultationSingleChatActivity_.class);
//                intent.putExtra(JobConsultationSingleChatActivity_.EXTRA_FIREBASE_ROOM_PUSH,
//                        room);
//                BaseActivity.className = JobConsultationSingleChatActivity_.class.getSimpleName();
//                break;
//        }
        if (intent != null) {
//            setLastReadRoom(fireBaseRoomEntity);
//            ((MainActivity) mActivity).setVisibleForUnreadMessageView(false, fireBaseRoomEntity);
//            mDetailRoom = fireBaseRoomEntity.getId() + "";
            startActivity(intent);
            overridePendingTransition(R.anim.right_to_left, R.anim.stand_by);
        }
    }

    private void startSplash() {
        startActivity(new Intent(this, SpashActivity.class));
    }

}
