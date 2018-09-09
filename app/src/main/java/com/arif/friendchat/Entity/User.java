package com.arif.friendchat.Entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User  implements Serializable{


    public String email;
    public String token;
    public String name;
    public String Uid;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name,String email, String Uid, String token) {
        this.name = name;
        this.Uid = Uid;
        this.email = email;
        this.token = token;
    }
}
