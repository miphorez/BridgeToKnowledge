package com.cezia.bridgetoknowledge;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ScrollView;

 class BookPartPosition {

     private static final String APP_PREFERENCES_LAST_PART = "lastpart";
     private static final long PREF_LAST_PART = 0;

     private static final String APP_PREFERENCES_LAST_PART_POSITION = "lastpartposition";
     private static final int PREF_LAST_PART_POSITION = 0;

    private final ScrollView scrollView;
    private final Context context;

    BookPartPosition(Context context, ScrollView scrollView) {
        this.scrollView = scrollView;
        this.context = context;
    }

     int loadPrefLastBookPosition(){
         int pos = 0;
         SharedPreferences prefSettings = context.getSharedPreferences(BookPartFont.APP_PREFERENCES, Context.MODE_PRIVATE);
         if (prefSettings.contains(APP_PREFERENCES_LAST_PART_POSITION)) {
             pos = prefSettings.getInt(APP_PREFERENCES_LAST_PART_POSITION, PREF_LAST_PART_POSITION);
         }
         return pos;
     }

     void savePrefLastPartPosition() {
         SharedPreferences prefSettings = context.getSharedPreferences(BookPartFont.APP_PREFERENCES, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = prefSettings.edit();
         editor.putInt(APP_PREFERENCES_LAST_PART_POSITION, scrollView.getScrollY());
         editor.commit();
     }

     static long loadPrefLastPart(Context context) {
         long lastPart = PREF_LAST_PART;
         SharedPreferences prefSettings = context.getSharedPreferences(BookPartFont.APP_PREFERENCES, Context.MODE_PRIVATE);
         if (prefSettings.contains(APP_PREFERENCES_LAST_PART)) {
             lastPart = prefSettings.getLong(APP_PREFERENCES_LAST_PART, PREF_LAST_PART);
         }
         return lastPart;
     }

     void savePrefLastPart(long lastPart) {
         SharedPreferences prefSettings = context.getSharedPreferences(BookPartFont.APP_PREFERENCES, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = prefSettings.edit();
         editor.putLong(APP_PREFERENCES_LAST_PART, lastPart);
         editor.commit();
     }
}
