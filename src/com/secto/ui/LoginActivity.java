package com.secto.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.secto.database.EmployeeDatabase;
import com.secto.model.ActualHourDto;
import com.secto.model.EmployeeDto;
import com.secto.model.SiteDto;
import com.secto.model.SynchDto;
import com.secto.model.TagListDto;
import com.secto.model.WorkListDto;
import com.secto.utils.AllUrls;
import com.secto.utils.Constants;
import com.secto.utils.RemoteServer;
import com.secto.utils.Utils;

public class LoginActivity extends Activity implements OnClickListener {

	String TAG = "LoginActivity";
	private LinearLayout login_layout;
	EditText userNameField, passwordField;
	ImageButton loginBtn;
	LoginActivity loginActivity;
	EmployeeDatabase employeeDatabase;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		loginActivity = this;
		employeeDatabase = new EmployeeDatabase(loginActivity);
		initUI();
		if (Utils.isConnectedToInternet(this)) {

			new LoadinData().execute();
		} else {
			Utils.openErrorDialog(
					"You need internet connection for completing this request.Please connect with internet first.",
					this);
		}
	}

	private class LoadinData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(loginActivity, "",
					"Initializing...");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			startLoadingData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
				login_layout.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}

	}

	private void startLoadingData() {
//		try {
//			Thread syncThread = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					syncWithServer();
//				}
//			});
//
//			syncThread.start();
//			syncThread.join();
//		} catch (Exception e) {
//		}

		try {
			Thread edataThread = new Thread(new Runnable() {
				@Override
				public void run() {
					loadEmployeeData();
				}
			});
			edataThread.start();
			edataThread.join();
		} catch (Exception e) {
		}

		try {
			Thread allDataList = new Thread(new Runnable() {
				@Override
				public void run() {
					loadAllData();
				}
			});

			allDataList.start();
			allDataList.join();
		} catch (Exception e) {
		}

		// if (employeeDatabase.getTotalActualHourRecords() < 1) {
		// try {
		// Thread actualHourData = new Thread(new Runnable() {
		// @Override
		// public void run() {
		// loadActualHourData();
		// }
		// });
		//
		// actualHourData.start();
		// actualHourData.join();
		// } catch (Exception e) {
		// }
		// }

		try {
			Thread workList = new Thread(new Runnable() {
				@Override
				public void run() {
					loadWorkListData();
				}
			});
			workList.start();
			workList.join();
		} catch (Exception e) {
		}

		try {
			Thread updateUi = new Thread(new Runnable() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						public void run() {
							login_layout.setVisibility(View.VISIBLE);
						}
					});
				}
			});
			updateUi.start();
			updateUi.join();
		} catch (Exception e) {
		}

	}

	private void syncWithServer() {
		employeeDatabase = new EmployeeDatabase(this);
		ArrayList<SynchDto> syncList = employeeDatabase.getSyncData();
		if (syncList.size() > 0) {
			String serverResonse;
			try {
				serverResonse = RemoteServer.sendHTTPPostRequestToServer(
						AllUrls.SYNCURL, true);
				Utils.debugLog(TAG, "  synchronizing.... =" + serverResonse);
			} catch (Exception e) {
				Utils.errorLog(TAG, "loadWorkListData  =" + e.toString());
			}
		}
	}

	private void loadEmployeeData() {
		String serverResonse;
		try {
			serverResonse = RemoteServer.sendHTTPPostRequestToServer(
					AllUrls.EMPLOYEELIST_URL, true);
			Utils.debugLog(TAG, "  Employee data serverResonse ="
					+ serverResonse);
			saveEmployeeDataintodb(serverResonse);

		} catch (Exception e) {
			Utils.errorLog(TAG, "loadEmployeeData  =" + e.toString());
		}

	}

	private void loadAllData() {
		String serverResonse;
		try {
			serverResonse = RemoteServer.sendHTTPPostRequestToServer(
					AllUrls.ALLURL, true);
			Utils.debugLog(TAG, "  All Data serverResonse =" + serverResonse);
			saveAllDataListintodb(serverResonse);
		} catch (Exception e) {
			Utils.errorLog(TAG, "loadAllData  =" + e.toString());
		}

	}

	private void loadWorkListData() {

		String serverResonse;
		try {
			serverResonse = RemoteServer.sendHTTPPostRequestToServer(
					AllUrls.WORKLIST, true);
			Utils.debugLog(TAG, "  workList hour serverResonse ="
					+ serverResonse);
			saveWorkListintodb(serverResonse);
		} catch (Exception e) {
			Utils.errorLog(TAG, "loadWorkListData  =" + e.toString());
		}
	}

	private void initUI() {
		login_layout = (LinearLayout) findViewById(R.id.login_layout);
		loginBtn = (ImageButton) findViewById(R.id.loginBtn);
		loginBtn.setOnClickListener(this);
		userNameField = (EditText) findViewById(R.id.userNameField);
		passwordField = (EditText) findViewById(R.id.passwordField);
	}

	private void showToast(String text) {
		Toast.makeText(loginActivity, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.loginBtn:
			if (userNameField.getText().toString().length() < 3) {
				showToast("Please give PPS Number first.");
				return;
			}
			if (passwordField.getText().toString().length() < 3) {
				showToast("Please give password first.");
				return;
			}
			new LoginTask().execute();
			break;
		// Intent intent = new Intent(loginActivity,DetailsActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
		default:
			break;
		}
	}

	private class LoginTask extends AsyncTask<Void, Void, Void> {
		boolean islooged;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(loginActivity, "",
					"Signing in...");
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				// TODO: handle exception
			}
			employeeDatabase = new EmployeeDatabase(loginActivity);
			islooged = employeeDatabase.loginTask(userNameField.getText()
					.toString(), passwordField.getText().toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			if (islooged) {
				String query = "select " + EmployeeDatabase.EMPLOYEE_ID
						+ " from " + EmployeeDatabase.TABLE_EMPLOYEE
						+ " where " + EmployeeDatabase.EMPLOYEE_PPS_NUMBER
						+ " = " + "'" + userNameField.getText().toString()
						+ "'";
				String employeeId = employeeDatabase.executeQuery(query);
				Editor editor = PreferenceManager.getDefaultSharedPreferences(
						getApplicationContext()).edit();
				editor.putString(Constants.EMPLOYEE_ID, employeeId);
				editor.commit();
				Utils.debugLog(TAG, employeeId);
				Intent intent = new Intent(loginActivity, DetailsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				showToast("PPS Number or password is not correct. Please again.");
			}
		}

	}

	private void saveEmployeeDataintodb(String serverResonse) {
		try {
			JSONObject jsonObject = new JSONObject(serverResonse);
			if (jsonObject.getBoolean(Constants.SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray("dataRows");
				for (int i = 0; i < jsonArray.length(); i++) {
					EmployeeDto employeeDto = new EmployeeDto();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					employeeDto.setEmployee_id(jsonObject2
							.getInt(Constants.EMPLOYEE_ID));
					employeeDto.setBillable_hours_id(jsonObject2
							.getString(Constants.BILLABLE_HOURS_ID));
					employeeDto.setEmployee_type(jsonObject2
							.getInt(Constants.EMPLOYEE_TYPE));
					employeeDto.setEmployee_date_of_birth(jsonObject2
							.getString(Constants.EMPLOYEE_DATE_OF_BIRTH));
					employeeDto.setEmployee_status(jsonObject2
							.getInt(Constants.EMPLOYEE_STATUS));
					employeeDto.setEmployee_first_name(jsonObject2
							.getString(Constants.EMPLOYEE_FIRST_NAME));
					employeeDto.setEmployee_last_name(jsonObject2
							.getString(Constants.EMPLOYEE_LAST_NAME));
					employeeDto.setEmployee_pps_number(jsonObject2
							.getString(Constants.EMPLOYEE_PPS_NUMBER));
					employeeDto.setPassword(jsonObject2
							.getString(Constants.PASSWORD));
					employeeDatabase.addEmployee(employeeDto);
					Utils.errorLog(TAG, "Saved succesfully...");
				}
			}
		} catch (Exception e) {

			Utils.errorLog(TAG, "saveEmployeeDataintodb = " + e.toString());
		}
	}

	private void saveAllDataListintodb(String serverResonse) {

		try {
			JSONObject jsonObject = new JSONObject(serverResonse);
			if (jsonObject.getBoolean(Constants.SUCCESS)) {
				JSONArray actual_hour_table_dataArray = jsonObject
						.getJSONArray("actual_hour_table_data");
				for (int i = 0; i < actual_hour_table_dataArray.length(); i++) {
					ActualHourDto actualHourDto = new ActualHourDto();
					JSONObject jsonObject2 = actual_hour_table_dataArray
							.getJSONObject(i);
					actualHourDto.setActual_hour_id(jsonObject2
							.getInt(Constants.ACTUAL_HOUR_ID));
					actualHourDto.setTag_id(jsonObject2
							.getInt(Constants.TAG_ID));
					actualHourDto.setJob_number(jsonObject2
							.getString(Constants.JOB_NUMBER));
					actualHourDto.setContact_number(jsonObject2
							.getString(Constants.CONTACT_NUMBER));
					actualHourDto.setService_number(jsonObject2
							.getString(Constants.SERVICE_NUMBER));
					actualHourDto.setEmployee_id(jsonObject2
							.getInt(Constants.EMPLOYEE_ID));
					actualHourDto.setActual_start_time(jsonObject2
							.getString(Constants.ACTUAL_START_TIME));
					actualHourDto.setActual_end_time(jsonObject2
							.getString(Constants.ACTUAL_END_TIME));
					actualHourDto.setWork_date(jsonObject2
							.getString(Constants.WORK_DATE));
					actualHourDto
							.setYear(jsonObject2.getString(Constants.YEAR));
					actualHourDto.setWeek_no(jsonObject2
							.getString(Constants.WEEK_NO));

					employeeDatabase.addActualHour(actualHourDto);
					Utils.errorLog(TAG, "actual hour Saved succesfully...");
				}

				JSONArray tag_table_dataArray = jsonObject
						.getJSONArray("tag_table_data");
				for (int i = 0; i < tag_table_dataArray.length(); i++) {
					TagListDto tagListDto = new TagListDto();
					JSONObject jsonObject2 = tag_table_dataArray
							.getJSONObject(i);
					tagListDto.setTag_id(jsonObject2.getInt(Constants.TAG_ID));
					tagListDto.setJob_number(jsonObject2
							.getString(Constants.JOB_NUMBER));
					tagListDto.setContact_number(jsonObject2
							.getString(Constants.CONTACT_NUMBER));
					tagListDto.setService_number(jsonObject2
							.getString(Constants.SERVICE_NUMBER));
					tagListDto.setSite_username(jsonObject2
							.getString(Constants.SITE_USERNAME));
					tagListDto.setUpdated_job_status_name(jsonObject2
							.getString(Constants.UPDATED_JOB_STATUS_NAME));
					tagListDto.setReport_id(jsonObject2
							.getString(Constants.REPORT_ID));

					employeeDatabase.addTag(tagListDto);
					Utils.errorLog(TAG, "Saved tagList succesfully...");
				}

				JSONArray site_table_dataArray = jsonObject
						.getJSONArray("site_table_data");
				for (int i = 0; i < site_table_dataArray.length(); i++) {
					SiteDto siteDto = new SiteDto();
					JSONObject jsonObject2 = site_table_dataArray
							.getJSONObject(i);
					siteDto.setSite_id(jsonObject2
							.getInt(EmployeeDatabase.SITE_ID));
					siteDto.setSite_operator_id(jsonObject2
							.getInt(EmployeeDatabase.SITE_OPERATOR_ID));
					siteDto.setService_provider_id(jsonObject2
							.getInt(EmployeeDatabase.SERVICE_PROVIDER_ID));
					siteDto.setSite_type_id(jsonObject2
							.getInt(EmployeeDatabase.SITE_TYPE_ID));
					siteDto.setSite_name(jsonObject2
							.getString(EmployeeDatabase.SITE_NAME));
					siteDto.setSite_username(jsonObject2
							.getString(EmployeeDatabase.SITE_USERNAME));
					siteDto.setLat(jsonObject2.getString(EmployeeDatabase.LAT));
					siteDto.setLng(jsonObject2.getString(EmployeeDatabase.LNG));
					employeeDatabase.addSite(siteDto);
					Utils.errorLog(TAG, "Saved saveSitetodb succesfully...");
				}
			}
		} catch (Exception e) {
			Utils.errorLog(TAG, "saveAllDataListintodb = " + e.toString());
		}
	}

	private void saveWorkListintodb(String serverResonse) {
		try {
			JSONObject jsonObject = new JSONObject(serverResonse);
			if (jsonObject.getBoolean(Constants.SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray("dataRows");
				for (int i = 0; i < jsonArray.length(); i++) {
					WorkListDto workListDto = new WorkListDto();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					workListDto.setWork_list_id(jsonObject2
							.getInt(Constants.WORK_LIST_ID));
					workListDto.setWeek_no(jsonObject2
							.getInt(Constants.WEEK_NO));
					workListDto.setWork_date(jsonObject2
							.getString(Constants.WORK_DATE));
					workListDto.setYear(jsonObject2.getInt(Constants.YEAR));
					employeeDatabase.addWorkList(workListDto);
					Utils.errorLog(TAG, "Work List Saved succesfully...");
				}
			}
		} catch (Exception e) {
			Utils.errorLog(TAG, "saveWorkListintodb = " + e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
