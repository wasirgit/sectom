package com.secto.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	private static boolean debugOn = true;

	public Utils() {
	}

	public static void debugLog(String logTag, String s) {
		if (debugOn) {
			Log.d(logTag, logTag + " ->" + s);
		}
	}

	public static void errorLog(String logTag, String s) {
		if (debugOn) {
			Log.e(logTag, logTag + " ->" + s);
		}
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static boolean isConnectedToInternet(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connected = (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable() && conMgr
				.getActiveNetworkInfo().isConnected());
		return connected;
	}

	public static void openErrorDialog(String err_msg, Context context) {
		if (err_msg == null || err_msg.length() < 0) {
			err_msg = "Couldn't Connect With Server. Please Try Again.";
		}
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setMessage(err_msg);
		alert.setCancelable(true);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	public static String convertToDay(String dateString) {
		try {
			Date date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
			String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH)
					.format(date);
			Utils.debugLog("MainActivity", "days name  " + dayOfWeek);
			return dayOfWeek;
		} catch (Exception e) {

		}
		return "";
	}

	public static int getWeekNo() {
		Calendar calendar = Calendar.getInstance();
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		return week;
	}
}
