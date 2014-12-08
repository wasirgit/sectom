package com.secto.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharePreferences {

	public static void saveToSharePreference(Context applicationContext ){
		Editor editor = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit();
	}
	
}
