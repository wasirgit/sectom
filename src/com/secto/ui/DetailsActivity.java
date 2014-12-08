package com.secto.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.secto.database.EmployeeDatabase;
import com.secto.model.CustomerDto;
import com.secto.model.DetailsDto;
import com.secto.model.JobRegisterDto;
import com.secto.model.SiteDto;
import com.secto.model.SynchDto;
import com.secto.model.WorkListDto;
import com.secto.utils.AllUrls;
import com.secto.utils.Constants;
import com.secto.utils.RemoteServer;
import com.secto.utils.StorageClass;
import com.secto.utils.Utils;
import com.secto.wheel.ArrayWheelAdapter;
import com.secto.wheel.OnWheelChangedListener;
import com.secto.wheel.OnWheelScrollListener;
import com.secto.wheel.WheelView;
import com.sectom.customlistener.ReportListener;

public class DetailsActivity extends Activity implements OnClickListener {
	String TAG = "DetailsActivity";
	LinearLayout table_layout_row_sun, table_layout_row_mon,
			table_layout_row_tues, table_layout_row_wed,
			table_layout_row_thurs, table_layout_row_fri, table_layout_row_sat;
	Spinner yearSpinner;
	private ProgressDialog progressDialog;
	private DetailsActivity detailsActivity;
	EmployeeDatabase employeeDatabase;
	int week;
	String employeeId = "";
	TextView weekNo, year, previous, next;
	List<String> spinnerData;
	List<String> spinnerDataActualTable;
	Button searchBtn;
	TextView date_mon, date_tues, date_wed, date_thurs, date_fri, date_sat,
			date_sun;
	int FAILEDVISIT = 0;
	int SAVETAG = 1;
	int COMPLETED = 2;
	private boolean wheelScrolled = false;
	String valueMin = "";
	String valueSec = "";

	String wheelMenu1[] = new String[] { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24" };
	String wheelMenu2[] = new String[] { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
			"28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
			"39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
			"50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_layout);
		detailsActivity = this;
		spinnerData = new ArrayList<String>();
		spinnerDataActualTable = new ArrayList<String>();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		employeeId = prefs.getString(Constants.EMPLOYEE_ID, "");
		employeeDatabase = new EmployeeDatabase(detailsActivity);

		week = Utils.getWeekNo();
		getNoOfWeekOfYear();
		initUI();

		if (Utils.isConnectedToInternet(this)) {
			new LoadinData().execute();
		} else {
			Utils.openErrorDialog(
					"You need internet connection for completing this request.Please connect with internet first.",
					this);
		}
		setSpinnerData(yearSpinner, spinnerData, 0);
		setYear();
		setYear();
	}

	private void setSpinnerData(Spinner spinner, List<String> spinnerData,
			int position) {
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerData);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(dataAdapter);
		spinner.setSelection(position);

	}

	private class LoadinData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(detailsActivity, "",
					"Loading...");
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
			}
			new loadData().execute();
			super.onPostExecute(result);
		}
	}

	private void setYear() {
		year.setText("" + getYear());
	}

	private int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	private void getNoOfWeekOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getYear());
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);

		int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
		int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;

		for (int i = 1; i <= numberOfWeeks; i++) {
			spinnerData.add("" + i);
		}
	}

	private void startLoadingData() {
		try {
			Thread edataThread = new Thread(new Runnable() {
				@Override
				public void run() {
					loadCustomerData();
				}
			});

			edataThread.start();
			edataThread.join();
		} catch (Exception e) {
		}

		try {
			Thread tagList = new Thread(new Runnable() {
				@Override
				public void run() {
					loadJobRegisterListData();
				}
			});

			tagList.start();
			tagList.join();
		} catch (Exception e) {
		}

		try {
			Thread updateUi = new Thread(new Runnable() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						public void run() {

						}
					});
				}
			});
			updateUi.start();
			updateUi.join();
		} catch (Exception e) {
		}
	}

	private class loadData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(detailsActivity, "",
					"Populating Data...");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			StorageClass.getDetailsMap().clear();
			StorageClass.setWorklist(employeeDatabase.getWorkList(week));
			for (int i = 0; i < StorageClass.getWorklist().size(); i++) {
				WorkListDto workListDto = StorageClass.getWorklist().get(i);

				ArrayList<DetailsDto> detailsList = employeeDatabase
						.getActualHourData(employeeId,
								workListDto.getWork_date());
				StorageClass.getDetailsMap().put(workListDto.getWork_date(),
						detailsList);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			addTableRow();
			super.onPostExecute(result);
		}
	}

	private void loadCustomerData() {
		String serverResonse;
		try {
			serverResonse = RemoteServer.sendHTTPPostRequestToServer(
					AllUrls.CUSTOMER, true);
			Utils.debugLog(TAG, "  Customer data serverResonse ="
					+ serverResonse);
			saveCustomertodb(serverResonse);

		} catch (Exception e) {
			Utils.errorLog(TAG, "loadCustomerData  =" + e.toString());
		}
	}

	private void loadJobRegisterListData() {
		String serverResonse;
		try {
			serverResonse = RemoteServer.sendHTTPPostRequestToServer(
					AllUrls.JOB_REGISTER, true);
			Utils.debugLog(TAG, "  Employee data serverResonse ="
					+ serverResonse);
			saveJobRegistertodb(serverResonse);

		} catch (Exception e) {
			Utils.errorLog(TAG, "loadCustomerData  =" + e.toString());
		}
	}

	private void saveCustomertodb(String serverResonse) {
		try {
			JSONObject jsonObject = new JSONObject(serverResonse);
			if (jsonObject.getBoolean(Constants.SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray("dataRows");
				for (int i = 0; i < jsonArray.length(); i++) {
					CustomerDto customerDto = new CustomerDto();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					customerDto.setCustomer_id(jsonObject2
							.getInt(EmployeeDatabase.CUSTOMER_ID));
					customerDto.setCustomer_name(jsonObject2
							.getString(EmployeeDatabase.CUSTOMER_NAME));
					customerDto.setCustomer_full_name(jsonObject2
							.getString(EmployeeDatabase.CUSTOMER_FULL_NAME));
					customerDto.setColor_code(jsonObject2
							.getString(EmployeeDatabase.COLOR_CODE));
					customerDto.setCustomer_status(jsonObject2
							.getInt(EmployeeDatabase.CUSTOMER_STATUS));
					employeeDatabase.addCustomer(customerDto);
					Utils.errorLog(TAG, "Saved saveCustomertodb succesfully...");
				}
			}
		} catch (Exception e) {

			Utils.errorLog(TAG, "saveCustomertodb = " + e.toString());
		}
	}

	private void saveJobRegistertodb(String serverResonse) {
		try {
			JSONObject jsonObject = new JSONObject(serverResonse);
			if (jsonObject.getBoolean(Constants.SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray("dataRows");
				for (int i = 0; i < jsonArray.length(); i++) {
					JobRegisterDto jobRegisterDto = new JobRegisterDto();
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					jobRegisterDto.setJob_register_id(jsonObject2
							.getInt(EmployeeDatabase.JOB_REGISTER_ID));
					jobRegisterDto.setJob_name(jsonObject2
							.getString(EmployeeDatabase.JOB_NAME));
					jobRegisterDto.setJob_number(jsonObject2
							.getInt(EmployeeDatabase.JOB_NUMBER));
					jobRegisterDto.setCustomer_id(jsonObject2
							.getString(EmployeeDatabase.CUSTOMER_ID));
					employeeDatabase.addJobRegister(jobRegisterDto);
					Utils.errorLog(TAG,
							"Saved saveJobRegistertodb succesfully...");
				}
			}
		} catch (Exception e) {
			Utils.errorLog(TAG, "saveJobRegistertodb = " + e.toString());
		}

	}

	private void addTableRow() {

		table_layout_row_sun.removeAllViews();
		table_layout_row_mon.removeAllViews();
		table_layout_row_tues.removeAllViews();
		table_layout_row_wed.removeAllViews();
		table_layout_row_thurs.removeAllViews();
		table_layout_row_fri.removeAllViews();
		table_layout_row_sat.removeAllViews();

		for (String key : StorageClass.getDetailsMap().keySet()) {
			String day = Utils.convertToDay(key);

			if (day.equalsIgnoreCase("Monday")) {
				date_mon.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);

					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);
					TextView tv_site_report = (TextView) viewToLoad.findViewById(R.id.tv_site_report);
					tv_site_report.setOnClickListener(new ReportListener(detailsDto,getApplicationContext()));
//					tv_site_report
					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					Utils.debugLog(TAG,
							"  " + detailsDto.getActual_start_time() + " = "
									+ detailsDto.getActual_end_time());
					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {
						// TODO: handle exception
					}

					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}

					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ "-" + detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_mon.addView(viewToLoad, i);

				}
			} else if (day.equalsIgnoreCase("Tuesday")) {
				date_tues.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);

					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);

					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {
						// TODO: handle exception
					}

					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}

					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_tues.addView(viewToLoad, i);
				}
			} else if (day.equalsIgnoreCase("Wednesday")) {
				date_wed.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);

					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);

					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {

					}
					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}

					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_wed.addView(viewToLoad, i);
				}
			} else if (day.equalsIgnoreCase("Thursday")) {
				date_thurs.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);
					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);

					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {

					}
					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}

					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_thurs.addView(viewToLoad, i);
				}
			} else if (day.equalsIgnoreCase("Friday")) {
				date_fri.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);
					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);

					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {

					}
					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}
					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_fri.addView(viewToLoad, i);
				}

			} else if (day.equalsIgnoreCase("Saturday")) {
				date_sat.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);
					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);
					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);

					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {
					}
					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}
					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_sat.addView(viewToLoad, i);
				}
			} else if (day.equalsIgnoreCase("Sunday")) {
				date_sun.setText(key);
				ArrayList<DetailsDto> detailsDtos = StorageClass
						.getDetailsMap().get(key);
				for (int i = 0; i < detailsDtos.size(); i++) {
					DetailsDto detailsDto = detailsDtos.get(i);
					View viewToLoad = LayoutInflater.from(
							getApplicationContext()).inflate(R.layout.tablerow,
							null);

					Button startTime = (Button) viewToLoad
							.findViewById(R.id.startTime);
					Button endTime = (Button) viewToLoad
							.findViewById(R.id.endTime);
					startTime.setText(detailsDto.getActual_start_time());
					endTime.setText(detailsDto.getActual_end_time());

					Button failed_visit = (Button) viewToLoad
							.findViewById(R.id.failed_visit);
					Button saveBtn = (Button) viewToLoad
							.findViewById(R.id.saveBtn);
					Button completeBtn = (Button) viewToLoad
							.findViewById(R.id.completeBtn);

					TextView customername = (TextView) viewToLoad
							.findViewById(R.id.top_textview);
					TextView sitename = (TextView) viewToLoad
							.findViewById(R.id.tv_sitename);
					TextView tv_site_user_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_user_name);
					TextView tv_site_combination_name = (TextView) viewToLoad
							.findViewById(R.id.tv_site_combination_name);

					float float1 = 0, float2 = 0;
					try {
						float1 = Float.valueOf(detailsDto
								.getActual_start_time());
						float2 = Float.valueOf(detailsDto.getActual_end_time());
					} catch (Exception e) {
					}
					Utils.debugLog(TAG, " float = " + float1 + " = " + float2);
					if (float1 > 0 || float2 > 0) {
						saveBtn.setVisibility(View.GONE);
					} else {
						startTime.setOnClickListener(new ButtonWheelListener(
								startTime));
						endTime.setOnClickListener(new ButtonWheelListener(
								endTime));
						saveBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, saveBtn,
								SAVETAG));
					}
					String status = "";
					status = detailsDto.getUpdated_job_status_name();
					Utils.debugLog(TAG, " status =  " + status + " == "
							+ detailsDto.getSiteUserName());
					if (!status.equalsIgnoreCase(Constants.FAILED_VISIT)) {
						failed_visit.setVisibility(View.VISIBLE);
						failed_visit.setText("failed_visit");
						failed_visit.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, failed_visit,
								FAILEDVISIT));
					} else {
						failed_visit.setVisibility(View.INVISIBLE);
					}
					if (status.equalsIgnoreCase(Constants.COMPLETED)) {
						completeBtn.setVisibility(View.INVISIBLE);
						failed_visit.setVisibility(View.INVISIBLE);
					} else {
						completeBtn.setVisibility(View.VISIBLE);
						completeBtn.setOnClickListener(new ButtonListener(
								detailsDto, startTime, endTime, completeBtn,
								COMPLETED));
					}

					customername.setText(detailsDto.getCustomerName());
					sitename.setText(detailsDto.getSiteName());
					tv_site_user_name.setText(detailsDto.getSiteUserName());
					tv_site_combination_name.setText(detailsDto.getJobNo()
							+ detailsDto.getContactNo()
							+ detailsDto.getServiceNo());

					table_layout_row_sun.addView(viewToLoad, i);
				}
			}
		}
	}

	private void initUI() {
		weekNo = (TextView) findViewById(R.id.weekNo);
		weekNo.setText("Week no: " + week);
		year = (TextView) findViewById(R.id.year);
		table_layout_row_sun = (LinearLayout) findViewById(R.id.table_layout_row_sun);
		table_layout_row_mon = (LinearLayout) findViewById(R.id.table_layout_row_mon);
		table_layout_row_tues = (LinearLayout) findViewById(R.id.table_layout_row_tues);
		table_layout_row_wed = (LinearLayout) findViewById(R.id.table_layout_row_wed);
		table_layout_row_thurs = (LinearLayout) findViewById(R.id.table_layout_row_thurs);
		table_layout_row_fri = (LinearLayout) findViewById(R.id.table_layout_row_fri);
		table_layout_row_sat = (LinearLayout) findViewById(R.id.table_layout_row_sat);

		date_mon = (TextView) findViewById(R.id.date_mon);
		date_tues = (TextView) findViewById(R.id.date_tue);
		date_wed = (TextView) findViewById(R.id.date_wed);
		date_thurs = (TextView) findViewById(R.id.date_thurs);
		date_fri = (TextView) findViewById(R.id.date_fri);
		date_sat = (TextView) findViewById(R.id.date_sat);
		date_sun = (TextView) findViewById(R.id.date_sun);

		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);
		yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
		previous = (TextView) findViewById(R.id.previous);
		next = (TextView) findViewById(R.id.next);
		previous.setOnClickListener(this);
		next.setOnClickListener(this);

	}

	private void setWeekNo(int week) {
		weekNo.setText("Week no: " + week);
	}

	private String getSpinnerSelectedData(Spinner spinner) {
		return spinner.getSelectedItem().toString();
	}

	private class ButtonListener implements Button.OnClickListener {
		DetailsDto detailsDto;
		Button startTime, endTime;
		Button saveBtn;
		int tag = -1;

		public ButtonListener(DetailsDto detailsDto, Button startTime,
				Button endTime, Button saveBtn, int tag) {
			this.detailsDto = detailsDto;
			this.startTime = startTime;
			this.endTime = endTime;
			this.saveBtn = saveBtn;
			this.tag = tag;
		}

		@Override
		public void onClick(View v) {
			if (tag == SAVETAG) {
				if (Utils.isConnectedToInternet(detailsActivity)) {
					employeeDatabase.updateActualTable(startTime.getText()
							.toString().trim(), endTime.getText().toString()
							.trim(), detailsDto.getActual_hour_id());
					new saveSTFTData(startTime.getText().toString().trim(),
							endTime.getText().toString().trim(),
							detailsDto.getActual_hour_id(), saveBtn).execute();
				} else {
					employeeDatabase.updateActualTable(startTime.getText()
							.toString().trim(), endTime.getText().toString()
							.trim(), detailsDto.getActual_hour_id());

					SynchDto synchDto = new SynchDto();
					synchDto.setTag_id(detailsDto.getActual_hour_id());
					synchDto.setActual_start_time(startTime.getText()
							.toString().trim());
					synchDto.setActual_end_time(endTime.getText().toString()
							.trim());

					synchDto.setSynctype(Constants.TYPE_SAVE);
					employeeDatabase.addSynchData(synchDto);

					saveBtn.setText("Saved");
					saveBtn.setVisibility(View.INVISIBLE);

				}

			} else if (tag == FAILEDVISIT) {
				if (Utils.isConnectedToInternet(detailsActivity)) {
					employeeDatabase.updateTagTable("Failed Visit",
							detailsDto.getTag_id());
					new saveData("Failed Visit", detailsDto.getTag_id(),
							saveBtn).execute();
				} else {
					employeeDatabase.updateTagTable("Failed Visit",
							detailsDto.getTag_id());
					saveBtn.setText("Visited");
					saveBtn.setVisibility(View.INVISIBLE);
					SynchDto synchDto = new SynchDto();
					synchDto.setTag_id(detailsDto.getTag_id());
					synchDto.setUpdated_job_status_name("Failed Visit");
					synchDto.setSynctype(Constants.TYPE_FAILEDVISIT_OR_COMPLETE);
					employeeDatabase.addSynchData(synchDto);
				}

			} else if (tag == COMPLETED) {
				if (Utils.isConnectedToInternet(detailsActivity)) {
					employeeDatabase.updateTagTable("Completed",
							detailsDto.getTag_id());
					new saveData("Failed Visit", detailsDto.getTag_id(),
							saveBtn).execute();
				} else {
					employeeDatabase.updateTagTable("Completed",
							detailsDto.getTag_id());
					SynchDto synchDto = new SynchDto();
					synchDto.setTag_id(detailsDto.getTag_id());
					synchDto.setUpdated_job_status_name("Completed");
					synchDto.setSynctype(Constants.TYPE_FAILEDVISIT_OR_COMPLETE);
					saveBtn.setText("Completed");
					saveBtn.setVisibility(View.INVISIBLE);
					employeeDatabase.addSynchData(synchDto);
				}

			}
		}
	}

	private class saveData extends AsyncTask<Void, Void, Void> {
		String serverResonse;
		String value;
		int tagId;
		Button button;

		public saveData(String string, int tagId, Button button) {
			this.value = string;
			this.tagId = tagId;
			this.button = button;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(detailsActivity, "",
					"Saving...");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				serverResonse = RemoteServer.sendHTTPPostRequestToServer(
						AllUrls.FAILED_VISIT_CURL + "" + tagId + "/"
								+ value.replaceAll(" ", "%20"), true);
				Utils.debugLog(TAG, "  serverResonse =" + serverResonse);
			} catch (Exception e) {
				Utils.errorLog(TAG, "loadEmployeeData  =" + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}

			try {
				JSONObject jsonObject = new JSONObject(serverResonse);
				if (jsonObject.getBoolean("success")) {
					button.setVisibility(View.INVISIBLE);
				} else {
					Toast.makeText(getApplicationContext(),
							"Request Failed. Please Try again",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Utils.errorLog(TAG, "saveData = " + e.toString());
			}
			super.onPostExecute(result);
		}
	}

	private class saveSTFTData extends AsyncTask<Void, Void, Void> {
		String serverResonse;
		String st, ft;
		int actualId;
		Button button;

		public saveSTFTData(String st, String ft, int actualId, Button button) {
			this.st = st;
			this.ft = ft;
			this.actualId = actualId;
			this.button = button;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(detailsActivity, "",
					"Saving...");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				List<NameValuePair> postData = new ArrayList<NameValuePair>(3);
				postData.add(new BasicNameValuePair("actual_hour_id", ""
						+ actualId));
				postData.add(new BasicNameValuePair("start_time", "" + st));
				postData.add(new BasicNameValuePair("end_time", "" + ft));
				serverResonse = RemoteServer.sendHTTPPostRequestToServer(
						AllUrls.SAVEURL, postData, true);
				Utils.debugLog(TAG, "  serverResonse =" + serverResonse);
			} catch (Exception e) {
				Utils.errorLog(TAG, "loadEmployeeData  =" + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}

			try {
				button.setVisibility(View.INVISIBLE);
				JSONObject jsonObject = new JSONObject(serverResonse);
				if (jsonObject.getBoolean("success")) {
					Toast.makeText(getApplicationContext(),
							"Successfully saved.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Request Failed. Please Try again",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Utils.errorLog(TAG, "saveData = " + e.toString());
			}
			super.onPostExecute(result);
		}
	}

	private class ButtonWheelListener implements Button.OnClickListener {
		Button button;

		public ButtonWheelListener(Button startTime) {
			this.button = startTime;
		}

		@Override
		public void onClick(View v) {
			showDialogView(button);
		}
	}
	

	WheelView wheelView1, wheelView2;

	private void showDialogView(final Button button) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.whelpopupview, null);
		alertDialogBuilder.setTitle("Set time");
		alertDialogBuilder.setView(dialoglayout);
		wheelView1 = (WheelView) dialoglayout.findViewById(R.id.p1);
		wheelView2 = (WheelView) dialoglayout.findViewById(R.id.p2);
		initWheel1();
		initWheel2();

		alertDialogBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						Utils.debugLog(TAG, valueMin + "." + valueSec);
						button.setText(valueMin + "." + valueSec);
						valueMin = "";
						valueSec = "";
					}
				});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void initWheel1() {
		wheelView1.setViewAdapter(new ArrayWheelAdapter(this, wheelMenu1));
		wheelView1.setVisibleItems(2);
		wheelView1.setCurrentItem(0);
		wheelView1.addChangingListener(changedListener);
		wheelView1.addScrollingListener(scrolledListener);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initWheel2() {
		wheelView2.setViewAdapter(new ArrayWheelAdapter(this, wheelMenu2));
		wheelView2.setVisibleItems(2);
		wheelView2.setCurrentItem(0);
		wheelView2.addChangingListener(changedListener);
		wheelView2.addScrollingListener(scrolledListener);
	}

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		@SuppressWarnings("unused")
		public void onScrollStarts(WheelView wheel) {
			wheelScrolled = true;
		}

		@SuppressWarnings("unused")
		public void onScrollEnds(WheelView wheel) {
			wheelScrolled = false;
			updateStatus();
		}

		@Override
		public void onScrollingStarted(WheelView wheel) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			// TODO Auto-generated method stub

		}
	};

	// Wheel changed listener
	private final OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
				updateStatus();
			}
		}
	};

	private void updateStatus() {
		valueMin = wheelMenu1[wheelView1.getCurrentItem()];
		valueSec = wheelMenu2[wheelView2.getCurrentItem()];
		Utils.debugLog(TAG, valueMin + "." + valueSec);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.previous:
			week = week - 1;
			setWeekNo(week);
			new loadData().execute();
			break;
		case R.id.next:
			week = week + 1;
			setWeekNo(week);
			new loadData().execute();
			break;
		case R.id.searchBtn:
			week = Integer.parseInt(getSpinnerSelectedData(yearSpinner));
			new loadData().execute();
			setWeekNo(week);
			break;

		default:
			break;
		}
	}

}
