package com.arif.friendchat.constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by bipulkhan on 12/27/16.
 */


public class AppData {

    //keys
    public static String employeeInfo = "employee";
    public static String email = "email";
    public static String Acess_Toten = "Acess_Toten";
    public static String deviceToken = "deviceToken";
    public static String user_id = "user_id";
    public static String user_image = "user_image";
    public static String PCode = "pcode";
    public static String isSocial = "isSocial";

    public static String SoundNotification = "SoundNotification";
    public static String VibrationNotification = "VibrationNotification";
    public static String PopupNotification = "PopupNotification";
    public static String offNotification = "offNotification";


    public static final String MyPREFERENCES = "unitwo" ;


    public static void saveData(String key, String value, Context context){
        if(context!=null) {


            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.commit();
          //  Log.e("saveData"," "+ key+"  "+getData(key,context));


        }
        else
        {
            Log.e("saveData"," null");
        }
    }

    public static String getData(String key, Context context){
        String string="";
        if(context!=null) {
            //Log.e("contex", "not null");
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, 0);
             string = prefs.getString(key, "");
        }
        else
        {
            //Log.e("contex"," null");
        }
        return string;
    }
    public static String Clear(Context context){
        String string="";
        if(context!=null) {
            //Log.e("contex", "not null");
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
        }
        else
        {
            //Log.e("contex"," null");
        }
        return string;
    }
}
