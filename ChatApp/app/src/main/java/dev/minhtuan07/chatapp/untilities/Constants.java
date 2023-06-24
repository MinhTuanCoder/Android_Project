package dev.minhtuan07.chatapp.untilities;

import java.util.HashMap;

public class Constants
{
    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME= "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";

    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public  static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static HashMap<String,String> remoteMsgHeaders = null;

    public static HashMap<String,String> getremoteMsgHeaders(){
        if(remoteMsgHeaders==null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    //   AIzaSyCri6XPT94E14zq7JanhR_XtMhqUrztj9c
                   // BJSEqti-UN5T3uevx58K7l5nvgg22tnSTpFOMjOTGM-ZdpwwGHUz8vUebqMb2YM1PvyE31htbaNy9X7tsSg-O8k
                    "key=AAAA1K1_neg:APA91bEZFQtZuoTAGsfNYap0x1HkocwlxUE5WqtVxC-R1NEna9fQ7v2Xx9ol_31MwLkH7mmpSsnhgcH4COkV5ZrPXjqFA0e4S87-YoE0AaPZ-n5P1gdS0QfCuvagxb7E3Q-T6gcg5utb"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders ;
    }



}
