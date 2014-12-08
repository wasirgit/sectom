package com.secto.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.secto.database.EmployeeDatabase;
import com.secto.model.SynchDto;
import com.secto.utils.AllUrls;
import com.secto.utils.Constants;
import com.secto.utils.RemoteServer;
import com.secto.utils.Utils;

public class SynchroniseThread extends AsyncTask<Void, Void, Void> {
	String TAG ="SynchroniseThread";
	Context context;
	ArrayList<SynchDto> syncList;
	EmployeeDatabase database;
	public SynchroniseThread(Context context, ArrayList<SynchDto> syncList) {
		this.context = context;
		this.syncList = syncList;
		database = new EmployeeDatabase(context);
		Utils.errorLog(TAG, "Synchronize started....");
	}

	@Override
	protected void onPreExecute() {
		Utils.showToast(context, "Synchronizing");
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		for (int i = 0; i < syncList.size(); i++) {
			SynchDto synchDto = syncList.get(i);
			try {
				if(synchDto.getSynctype() == Constants.TYPE_FAILEDVISIT_OR_COMPLETE){
					String serverResonse = RemoteServer.sendHTTPPostRequestToServer(
							AllUrls.FAILED_VISIT_CURL+""+synchDto.getTag_id()+"/"+synchDto.getUpdated_job_status_name().replaceAll(" ", "%20"), true);
					Utils.debugLog(TAG, "  SynchroniseThread ="+ serverResonse);
					JSONObject jsonObject = new JSONObject(serverResonse);
					if(jsonObject.getBoolean("success")){
						database.removeRow(EmployeeDatabase.TABLE_SYNCHRONOUS, EmployeeDatabase.TAG_ID_OR_AH_ID, synchDto.getTag_id());
					}
				}
				else{
					List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
			        postData.add(new BasicNameValuePair("actual_hour_id", "" + synchDto.getTag_id()));
			        postData.add(new BasicNameValuePair("start_time", "" + synchDto.getActual_start_time()));
			        postData.add(new BasicNameValuePair("end_time", "" + synchDto.getActual_end_time()));
					String serverResonse = RemoteServer.sendHTTPPostRequestToServer(
							AllUrls.SAVEURL, postData,true);
					Utils.debugLog(TAG, "  serverResonse ="+ serverResonse);
					
					JSONObject jsonObject = new JSONObject(serverResonse);
					if(jsonObject.getBoolean("success")){
						database.removeRow(EmployeeDatabase.TABLE_SYNCHRONOUS, EmployeeDatabase.TAG_ID_OR_AH_ID, synchDto.getTag_id());
					}
				}
			} catch (Exception e) {
				Utils.errorLog(TAG, " SynchroniseThread ="+e.toString());
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
	}

}
