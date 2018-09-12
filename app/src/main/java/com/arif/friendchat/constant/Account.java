package com.arif.friendchat.constant;

import android.content.Context;
import android.util.Log;

import com.arif.friendchat.Entity.User;
import com.google.gson.Gson;

/**
 * Created by HP on 2/7/2018.
 */

public class Account {
   public static User user;
    public  static void initializeUserInfo(Context context)
    {
        String userdata=AppData.getData(AppData.user_info,context);
        Log.e("Account",""+userdata);
        Gson gson =new Gson();
         user=gson.fromJson(userdata,User.class);


    }
}
