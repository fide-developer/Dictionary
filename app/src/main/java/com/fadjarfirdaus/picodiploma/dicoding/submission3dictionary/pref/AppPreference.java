package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    private static final String PREFS_NAME = "loadDataPref";
    private static final String APP_FIRST_DOWNLOAD_ENGLISH = "app_first_download_english";
    private static final String APP_FIRST_DOWNLOAD_INDONESIA = "app_first_download_indonesia";
    private static final String STATE = "en";
    private SharedPreferences prefs;

    public AppPreference(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public void setEnglishDownload(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_FIRST_DOWNLOAD_ENGLISH, input);
        editor.apply();
    }
    public void setIndonesianDownload(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_FIRST_DOWNLOAD_INDONESIA, input);
        editor.apply();
    }
    public Boolean getFirstDlEnglish() {
        return prefs.getBoolean(APP_FIRST_DOWNLOAD_ENGLISH, true);
    }

    public Boolean getFirstDlIndonesia() {
        return prefs.getBoolean(APP_FIRST_DOWNLOAD_INDONESIA, true);
    }
    public void setState(String state){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(STATE, state);
        editor.apply();
    }
    public String getState(){
        return prefs.getString(STATE,"en");
    }
}
