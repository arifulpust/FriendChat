package com.arif.friendchat.constant;

/**
 * Created by HP on 2/7/2018.
 */

public class Constant {
    public static String FCM_TOKEN="key=AIzaSyCCNrhCeJcvOBp22shEO47A9dHC3HrKo20";
    public static int SPLASH_TIME=2000;
    public static final String YOUTUBE_API_KEY = "AIzaSyDTFQyk0NH1n7w5PM7MenvgVSY8cmFnfT8";
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public static class FireBase {

        public static final String KEY_ID = "id";

        // Key root note
        public static final String KEY_CHAT = "chat";
        public static final String KEY_ROOM_MESSAGES = "room-messages";
        public static final String KEY_ROOM_UNREAD = "staff-unread-rooms";
        public static final String KEY_ROOM_TYPING = "room-typing-signal";
        public static final String KEY_USERS = "users";
        public static final String KEY_READ_USERS = "read_users";

        // Key message note
        public static final String KEY_IMAGE = "image";
        public static final String KEY_FILE = "file";
        public static final String KEY_AVATAR = "avatar";
        public static final String KEY_AVATAR_URL = "avatarUrl";
        public static final String KEY_UPDATED_AT = "updatedAt";
        public static final String KEY_MESSAGE = "message";
        public static final String KEY_NAME = "name";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_TIMESTAMP = "timestamp";
        public static final String KEY_TYPE = "type";
        public static final String KEY_USER_ID = "userId";
        public static final String KEY_READ = "read";

        // Key user note
        public static final String KEY_ROOMS = "rooms";

        // Key room note
        public static final String KEY_ACTIVE = "active";

        // Value
        public static final String VALUE_USER = "user";
        public static final String VALUE_STAFF = "staff";
        public static final String VALUE_ROOM_ID_JOB_CONSULT_CHAT = "job-consult";
        public static final String VALUE_ROOM_ID_LIVING_CHAT = "living";
        public static final String VALUE_ROOM_ID_BUILDING_GROUP = "room_building";
        public static final String VALUE_DEFAULT = "default";
        public static final String VALUE_IMAGE = "image";

        // Regex
        public static final String REGEX_CREATE_ROOM_ID = "_";

    }
}
