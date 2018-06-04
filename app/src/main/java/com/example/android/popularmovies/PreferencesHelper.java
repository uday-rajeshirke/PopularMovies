package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

class PreferencesHelper {

    private final SharedPreferences sharedPreferences;

    PreferencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("DefaultSettings",MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public int getSortingOptionPreferences(String key) {
        return sharedPreferences.getInt(key, 1);
    }

    public void saveSortingOptionPreferences(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
