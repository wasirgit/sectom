package com.secto.utils;

import java.util.ArrayList;

import com.secto.database.EmployeeDatabase;
import com.secto.model.SynchDto;
import com.secto.threads.SynchroniseThread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		try {
			ConnectivityManager cm = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE));

			if (cm == null) {
				return;
			}

			Utils.debugLog("NetworkChangeReceiver", "connecting.....");
			if (cm.getActiveNetworkInfo() != null
					&& !cm.getActiveNetworkInfo().isConnected()) {
				Utils.debugLog("NetworkChangeReceiver", "Disconnected.....");
			} else if (cm.getActiveNetworkInfo() != null
					&& cm.getActiveNetworkInfo().isConnected()) {
				Utils.debugLog("NetworkChangeReceiver", "Connected");
				startSynchronize(context);
			}
		} catch (Exception e) {
			Utils.debugLog("NetworkChangeReceiver", "" + e.toString());
		}

	}

	private void startSynchronize(Context context) {
		EmployeeDatabase database = new EmployeeDatabase(context);
		ArrayList<SynchDto> list = database.getSyncData();
		new SynchroniseThread(context,list).execute();
	}

}
