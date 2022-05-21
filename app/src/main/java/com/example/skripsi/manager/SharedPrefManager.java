package com.example.skripsi.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.skripsi.Lactivity;
import com.example.skripsi.model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "sharedpref";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ID = "keyid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context){ mCtx = context; }
    public static  synchronized SharedPrefManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //menyimpan data ke sharedpref ketika login
    public void userLogin(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_NAME,user.getName());
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_GENDER,user.getGender());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply();
    }
    // mengecek apakah ada user login atau tidak
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null)!= null;
    }
    //untuk mengambil data user
    public User getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID,-1),
                sharedPreferences.getString(KEY_NAME,null),
                sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_GENDER,null),
                sharedPreferences.getString(KEY_PHONE,null)
        );
    }
    //untuk logout
    public void logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(mCtx,Lactivity.class);
        mCtx.startActivity(intent);
    }



}
