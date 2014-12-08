package com.secto.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.secto.model.ActualHourDto;
import com.secto.model.CustomerDto;
import com.secto.model.DetailsDto;
import com.secto.model.EmployeeDto;
import com.secto.model.JobRegisterDto;
import com.secto.model.JobStatusDto;
import com.secto.model.JobTypeDto;
import com.secto.model.SiteDto;
import com.secto.model.SynchDto;
import com.secto.model.TagListDto;
import com.secto.model.WorkListDto;
import com.secto.utils.Utils;

public class EmployeeDatabase extends SQLiteOpenHelper {
	String TAG = "EmployeeDatabase";
	// Database Version
	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "EMPLOYEE_TABLE";

	public static final String TABLE_EMPLOYEE = "Eemployee";
	public static final String TABLE_TAG_LIST = "tag_list";
	public static final String TABLE_ACTUAL_HOUR = "actual_hour";
	public static final String TABLE_WORK_LIST = "work_list";
	public static final String TABLE_CUSTOMER = "customer";
	public static final String TABLE_JOBREGISTER = "jobregister";
	public static final String TABLE_SITE = "site";

	public static final String TABLE_SYNCHRONOUS = "synchronous";

	public static final String TABLE_JOBTYPE = "jobtype";
	public static final String TABLE_JOBSTATUS = "jobstatus";

	public static final String WORK_LIST_ID = "work_list_id";

	// employee table data
	public static final String EMPLOYEE_ID = "employee_id";
	public static final String BILLABLE_HOURS_ID = "billable_hours_id";
	public static final String EMPLOYEE_TYPE = "employee_type";
	public static final String EMPLOYEE_DATE_OF_BIRTH = "employee_date_of_birth";
	public static final String EMPLOYEE_STATUS = "employee_status";
	public static final String EMPLOYEE_FIRST_NAME = "employee_first_name";
	public static final String EMPLOYEE_LAST_NAME = "employee_last_name";
	public static final String EMPLOYEE_PPS_NUMBER = "employee_pps_number";
	public static final String PASSWORD = "password";

	// taglist table data
	public static final String TAG_ID = "tag_id";
	public static final String JOB_NUMBER = "job_number";
	public static final String CONTACT_NUMBER = "contact_number";
	public static final String SERVICE_NUMBER = "service_number";
	public static final String SITE_USERNAME = "site_username";
	public static final String UPDATED_JOB_STATUS_NAME = "updated_job_status_name";
	public static final String REPORT_ID = "report_id";

	public static final String DESCRIPTION_ID = "description_id";
	public static final String MEN_REQUIRED = "men_required";
	public static final String INVOICE_VALUE = "invoice_value";
	public static final String PO_NUMBER = "po_number";
	public static final String TT_NUMBER = "tt_number";
	public static final String CR_NUMBER = "cr_number";
	public static final String DEFECT_NO = "defect_no";
	public static final String INVOICE_NO = "invoice_no";
	public static final String PO_REQUEST_NO = "po_request_no";
	public static final String PO_REQUEST_DATE = "po_request_date";
	// actual hour table data
	public static final String ACTUAL_HOUR_ID = "actual_hour_id";
	public static final String ACTUAL_START_TIME = "actual_start_time";
	public static final String ACTUAL_END_TIME = "actual_end_time";
	public static final String WORK_DATE = "work_date";
	public static final String YEAR = "year";
	public static final String WEEK_NO = "week_no";

	// customer table data
	public static final String CUSTOMER_ID = "customer_id";
	public static final String CUSTOMER_NAME = "customer_name";
	public static final String CUSTOMER_FULL_NAME = "customer_full_name";
	public static final String COLOR_CODE = "color_code";
	public static final String CUSTOMER_STATUS = "customer_status";

	// job register table
	public static final String JOB_REGISTER_ID = "job_register_id";
	public static final String JOB_NAME = "job_name";
	// site table information
	public static final String SITE_ID = "site_id";
	public static final String SITE_OPERATOR_ID = "site_operator_id";
	public static final String SERVICE_PROVIDER_ID = "service_provider_id";
	public static final String SITE_TYPE_ID = "site_type_id";
	public static final String SITE_NAME = "site_name";
	public static final String LNG = "lng";
	public static final String LAT = "lat";

	public static final String TAG_ID_OR_AH_ID = "tag_idor_actualhourid";
	public static final String SYNCTYPE = "synctype";
	// job type table

	public static final String JOB_TYPE_ID = "job_type_id";
	public static final String JOB_TYPE_NAME = "job_type_name";
	// / job status table
	public static final String JOB_STATUS_ID = "job_status_id";
	public static final String JOB_COMPLETE_DATE = "job_complete_date";

	public static String CREATE_EMPLOYEE_TABLE = "CREATE TABLE "
			+ TABLE_EMPLOYEE + " (" + EMPLOYEE_ID + " INTEGER PRIMARY KEY ,"
			+ BILLABLE_HOURS_ID + " INTEGER ," + EMPLOYEE_TYPE + " INTEGER ,"
			+ EMPLOYEE_DATE_OF_BIRTH + " TEXT ," + EMPLOYEE_STATUS
			+ " INTEGER ," + EMPLOYEE_FIRST_NAME + " TEXT ,"
			+ EMPLOYEE_LAST_NAME + " TEXT ," + EMPLOYEE_PPS_NUMBER + " TEXT ,"
			+ PASSWORD + " TEXT )";

	public static String CREATE_TAGLIST_TABLE = "CREATE TABLE "
			+ TABLE_TAG_LIST + " (" + TAG_ID + " INTEGER PRIMARY KEY ,"
			+ JOB_NUMBER + " TEXT ," + CONTACT_NUMBER + " INTEGER ,"
			+ SERVICE_NUMBER + " TEXT ," + SITE_USERNAME + " TEXT ,"
			+ UPDATED_JOB_STATUS_NAME + " TEXT ," + REPORT_ID + " TEXT ,"
			 + DESCRIPTION_ID + " TEXT ,"
			+ MEN_REQUIRED + " TEXT ," + INVOICE_VALUE + " TEXT ," + PO_NUMBER
			+ " TEXT ," + TT_NUMBER + " TEXT ," + CR_NUMBER + " TEXT ,"
			+ DEFECT_NO + " TEXT ," + INVOICE_NO + " TEXT ," + PO_REQUEST_NO
			+ " TEXT ," + PO_REQUEST_DATE + " TEXT )";

	public static String CREATE_ACTUAL_HOUR_TABLE = "CREATE TABLE "
			+ TABLE_ACTUAL_HOUR + " (" + ACTUAL_HOUR_ID
			+ " INTEGER PRIMARY KEY ," + TAG_ID + " INTEGER ," + JOB_NUMBER
			+ " TEXT ," + CONTACT_NUMBER + " TEXT ," + SERVICE_NUMBER
			+ " TEXT ," + EMPLOYEE_ID + " INTEGER ," + ACTUAL_START_TIME
			+ " TEXT ," + ACTUAL_END_TIME + " TEXT ," + WORK_DATE + " TEXT ,"
			+ YEAR + " INTEGER ," + WEEK_NO + " INTEGER ," + REPORT_ID
			+ " TEXT )";

	public static String CREATE_WORK_LIST_TABLE = "CREATE TABLE "
			+ TABLE_WORK_LIST + " (" + WORK_LIST_ID + " INTEGER PRIMARY KEY ,"
			+ WEEK_NO + " INTEGER ," + WORK_DATE + " TEXT ," + YEAR
			+ " INTEGER )";

	public static String CREATE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER
			+ " (" + CUSTOMER_ID + " INTEGER PRIMARY KEY ," + CUSTOMER_NAME
			+ " TEXT ," + CUSTOMER_FULL_NAME + " TEXT ," + COLOR_CODE
			+ " TEXT ," + CUSTOMER_STATUS + " INTEGER )";

	public static String CREATE_JOBREGISTER = "CREATE TABLE "
			+ TABLE_JOBREGISTER + " (" + JOB_REGISTER_ID
			+ " INTEGER PRIMARY KEY ," + JOB_NAME + " TEXT ," + JOB_NUMBER
			+ " TEXT ," + CUSTOMER_ID + " INTEGER )";

	public static String CREATE_SITE_TABLE = "CREATE TABLE " + TABLE_SITE
			+ " (" + SITE_ID + " INTEGER PRIMARY KEY ," + SITE_OPERATOR_ID
			+ " INTEGER ," + SERVICE_PROVIDER_ID + " INTEGER ," + SITE_TYPE_ID
			+ " INTEGER ," + SITE_NAME + " TEXT ," + SITE_USERNAME + " TEXT ,"
			+ LNG + " TEXT ," + LAT + " TEXT )";

	public static String CREATE_JOBTYPE_TABLE = "CREATE TABLE " + TABLE_JOBTYPE
			+ " (" + JOB_TYPE_ID + " INTEGER PRIMARY KEY ," + JOB_TYPE_NAME
			+ " TEXT )";

	public static String CREATE_JOBSTATUS_TABLE = "CREATE TABLE "
			+ TABLE_JOBSTATUS + " (" + JOB_STATUS_ID + " INTEGER PRIMARY KEY ,"
			+ JOB_TYPE_ID + " INTEGER ," + TAG_ID + " INTEGER ,"
			+ JOB_COMPLETE_DATE + " TEXT )";

	public static String CREATE_SYNCHRONOUS_TABLE = "CREATE TABLE "
			+ TABLE_SYNCHRONOUS + " (" + TAG_ID_OR_AH_ID
			+ " INTEGER PRIMARY KEY ," + UPDATED_JOB_STATUS_NAME + " TEXT ,"
			+ ACTUAL_START_TIME + " TEXT ," + SYNCTYPE + " INTEGER ,"
			+ ACTUAL_END_TIME + " TEXT )";

	public EmployeeDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_EMPLOYEE_TABLE);
			db.execSQL(CREATE_TAGLIST_TABLE);
			db.execSQL(CREATE_ACTUAL_HOUR_TABLE);
			db.execSQL(CREATE_WORK_LIST_TABLE);
			db.execSQL(CREATE_CUSTOMER);
			db.execSQL(CREATE_JOBREGISTER);
			db.execSQL(CREATE_SITE_TABLE);

			db.execSQL(CREATE_JOBTYPE_TABLE);
			db.execSQL(CREATE_JOBSTATUS_TABLE);
			db.execSQL(CREATE_SYNCHRONOUS_TABLE);
		} catch (Exception e) {
			Utils.errorLog(TAG, "Oncreate" + e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTUAL_HOUR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_LIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBREGISTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBTYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBSTATUS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCHRONOUS);
		onCreate(db);
	}

	public void addEmployee(EmployeeDto employeeDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(EMPLOYEE_ID, employeeDto.getEmployee_id());
		values.put(BILLABLE_HOURS_ID, employeeDto.getBillable_hours_id());
		values.put(EMPLOYEE_TYPE, employeeDto.getEmployee_type());
		values.put(EMPLOYEE_DATE_OF_BIRTH,
				employeeDto.getEmployee_date_of_birth());
		values.put(EMPLOYEE_STATUS, employeeDto.getEmployee_status());
		values.put(EMPLOYEE_FIRST_NAME, employeeDto.getEmployee_first_name());
		values.put(EMPLOYEE_LAST_NAME, employeeDto.getEmployee_last_name());
		values.put(EMPLOYEE_PPS_NUMBER, employeeDto.getEmployee_pps_number());
		values.put(PASSWORD, employeeDto.getPassword());
		try {
			if (!isExistsValue(db, TABLE_EMPLOYEE, EMPLOYEE_ID,
					employeeDto.getEmployee_id())) {
				db.insert(TABLE_EMPLOYEE, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public boolean loginTask(String ppsNumber, String passWord) {
		SQLiteDatabase db = this.getWritableDatabase();
		String Query = "Select * from " + TABLE_EMPLOYEE + " where "
				+ EMPLOYEE_PPS_NUMBER + " = " + "'" + ppsNumber + "'" + " AND "
				+ PASSWORD + " = " + "'" + passWord + "'";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor.getCount() <= 0) {
			return false;
		}
		return true;
	}

	public void addTag(TagListDto tagListDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TAG_ID, tagListDto.getTag_id());
		values.put(JOB_NUMBER, tagListDto.getJob_number());
		values.put(CONTACT_NUMBER, tagListDto.getContact_number());
		values.put(SERVICE_NUMBER, tagListDto.getService_number());
		values.put(SITE_USERNAME, tagListDto.getSite_username());
		values.put(UPDATED_JOB_STATUS_NAME,
				tagListDto.getUpdated_job_status_name());
		values.put(REPORT_ID, tagListDto.getReport_id());

		values.put(DESCRIPTION_ID, tagListDto.getDescription_id());
		values.put(MEN_REQUIRED, tagListDto.getMen_required());
		values.put(INVOICE_VALUE, tagListDto.getInvoice_value());
		values.put(PO_NUMBER, tagListDto.getPo_number());
		values.put(TT_NUMBER, tagListDto.getTt_number());
		values.put(CR_NUMBER, tagListDto.getCr_number());
		values.put(DEFECT_NO, tagListDto.getDefect_no());
		values.put(INVOICE_NO, tagListDto.getInvoice_no());
		values.put(PO_REQUEST_NO, tagListDto.getPo_request_no());
		values.put(PO_REQUEST_DATE, tagListDto.getPo_request_date());

		try {
			if (!isExistsValue(db, TABLE_TAG_LIST, TAG_ID,
					tagListDto.getTag_id())) {
				db.insert(TABLE_TAG_LIST, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public void addActualHour(ActualHourDto actualHourDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ACTUAL_HOUR_ID, actualHourDto.getActual_hour_id());
		values.put(TAG_ID, actualHourDto.getTag_id());
		values.put(JOB_NUMBER, actualHourDto.getJob_number());
		values.put(CONTACT_NUMBER, actualHourDto.getContact_number());
		values.put(SERVICE_NUMBER, actualHourDto.getService_number());
		values.put(EMPLOYEE_ID, actualHourDto.getEmployee_id());
		values.put(ACTUAL_START_TIME, actualHourDto.getActual_start_time());

		values.put(ACTUAL_END_TIME, actualHourDto.getActual_end_time());
		values.put(WORK_DATE, actualHourDto.getWork_date());
		values.put(YEAR, actualHourDto.getYear());
		values.put(WEEK_NO, actualHourDto.getWeek_no());

		try {
			if (!isExistsValue(db, TABLE_ACTUAL_HOUR, ACTUAL_HOUR_ID,
					actualHourDto.getActual_hour_id())) {
				db.insert(TABLE_ACTUAL_HOUR, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public void addWorkList(WorkListDto workListDto) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(WORK_LIST_ID, workListDto.getWork_list_id());
			values.put(WEEK_NO, workListDto.getWeek_no());
			values.put(WORK_DATE, workListDto.getWork_date());
			values.put(YEAR, workListDto.getYear());

			try {
				if (!isExistsValue(db, TABLE_WORK_LIST, WORK_LIST_ID,
						workListDto.getWork_list_id())) {
					db.insert(TABLE_WORK_LIST, null, values);
				}
			} catch (Exception e) {

			}

			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, e.toString());

		}

	}

	public void addCustomer(CustomerDto customerDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CUSTOMER_ID, customerDto.getCustomer_id());
		values.put(CUSTOMER_NAME, customerDto.getCustomer_name());
		values.put(CUSTOMER_FULL_NAME, customerDto.getCustomer_full_name());
		values.put(COLOR_CODE, customerDto.getColor_code());
		values.put(CUSTOMER_STATUS, customerDto.getCustomer_status());

		try {
			if (!isExistsValue(db, TABLE_CUSTOMER, CUSTOMER_ID,
					customerDto.getCustomer_id())) {
				db.insert(TABLE_CUSTOMER, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public void addJobRegister(JobRegisterDto jobRegisterDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(JOB_REGISTER_ID, jobRegisterDto.getJob_register_id());
		values.put(JOB_NAME, jobRegisterDto.getJob_name());
		values.put(JOB_NUMBER, jobRegisterDto.getJob_number());
		values.put(CUSTOMER_ID, jobRegisterDto.getCustomer_id());

		try {
			if (!isExistsValue(db, TABLE_JOBREGISTER, JOB_REGISTER_ID,
					jobRegisterDto.getJob_register_id())) {
				db.insert(TABLE_JOBREGISTER, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public void addSite(SiteDto siteDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SITE_ID, siteDto.getSite_id());
		values.put(SITE_OPERATOR_ID, siteDto.getSite_operator_id());
		values.put(SERVICE_PROVIDER_ID, siteDto.getService_provider_id());
		values.put(SITE_TYPE_ID, siteDto.getSite_type_id());
		values.put(SITE_NAME, siteDto.getSite_name());
		values.put(SITE_USERNAME, siteDto.getSite_username());
		values.put(LNG, siteDto.getLng());
		values.put(LAT, siteDto.getLat());

		try {
			if (!isExistsValue(db, TABLE_SITE, SITE_ID, siteDto.getSite_id())) {
				db.insert(TABLE_SITE, null, values);
			}
		} catch (Exception e) {

		}
		db.close();
	}

	public void addJobType(JobTypeDto jobTypeDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(JOB_TYPE_ID, jobTypeDto.getJob_type_id());
		values.put(JOB_TYPE_NAME, jobTypeDto.getJob_type_name());

		try {
			if (!isExistsValue(db, TABLE_JOBTYPE, JOB_TYPE_ID,
					jobTypeDto.getJob_type_id())) {
				db.insert(TABLE_JOBTYPE, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public void addJobStatus(JobStatusDto jobStatusDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(JOB_STATUS_ID, jobStatusDto.getJob_status_id());
		values.put(JOB_TYPE_ID, jobStatusDto.getJob_status_id());
		values.put(TAG_ID, jobStatusDto.getTag_id());
		values.put(JOB_COMPLETE_DATE, jobStatusDto.getJob_complete_date());

		try {
			if (!isExistsValue(db, TABLE_JOBSTATUS, JOB_STATUS_ID,
					jobStatusDto.getJob_status_id())) {
				db.insert(TABLE_JOBSTATUS, null, values);
			}
		} catch (Exception e) {

		}

		db.close();
	}

	public void updateRow(String sql, ContentValues cv, String where) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(sql, cv, where, null);

	}

	public void updateRow(String sql) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.rawQuery(sql, null);
	}

	public void updateActualTable(String actual_start_time,
			String actual_end_time, int actual_hour_id) {
		Utils.debugLog(TAG, " updateActualTable " + actual_start_time + " "
				+ actual_end_time + " " + actual_hour_id);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ACTUAL_START_TIME, actual_start_time);
		values.put(ACTUAL_END_TIME, actual_end_time);
		try {
			db.update(TABLE_ACTUAL_HOUR, values, ACTUAL_HOUR_ID + " = ?",
					new String[] { String.valueOf("" + actual_hour_id) });
		} catch (Exception e) {
			Utils.errorLog(TAG, "updateActualTable " + e.toString());
		}
		db.close();

	}

	public void updateFailedVisit(String satuesname, int tag_id) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UPDATED_JOB_STATUS_NAME, satuesname);

		try {
			db.update(TABLE_ACTUAL_HOUR, values, TAG_ID + " = ?",
					new String[] { String.valueOf("" + tag_id) });
		} catch (Exception e) {
			Utils.errorLog(TAG, "updateActualTable " + e.toString());
		}
		db.close();

	}

	public void updateTagTable(String satuesname, int tag_id) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UPDATED_JOB_STATUS_NAME, satuesname);

		try {
			db.update(TABLE_TAG_LIST, values, TAG_ID + " = ?",
					new String[] { String.valueOf("" + tag_id) });
		} catch (Exception e) {
			Utils.errorLog(TAG, "updateTagTable " + e.toString());
		}
		db.close();

	}

	public int getTotalEmployeeRecords() {
		int recods = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {

			db = this.getWritableDatabase();
			String selectQuery = "SELECT  count("
					+ EmployeeDatabase.EMPLOYEE_ID + ") FROM " + TABLE_EMPLOYEE;

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				recods = cursor.getInt(0);
			}
			cursor.close();
			db.close();

		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}

		return recods;
	}

	public int getTotalCount(String sql) {
		int recods = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {

			db = this.getWritableDatabase();
			String selectQuery = sql;

			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				recods = cursor.getInt(0);
			}
			cursor.close();
			db.close();

		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}

		return recods;
	}

	public int getTotalTagRecords() {
		int recods = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String selectQuery = "SELECT  count(" + EmployeeDatabase.TAG_ID
					+ ") FROM " + TABLE_TAG_LIST;
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				recods = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}
		return recods;
	}

	public int getTotalActualHourRecords() {
		int recods = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String selectQuery = "SELECT  count("
					+ EmployeeDatabase.ACTUAL_HOUR_ID + ") FROM "
					+ TABLE_ACTUAL_HOUR;
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				recods = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}
		return recods;
	}

	public int getTotalWorkList() {
		int recods = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String selectQuery = "SELECT  count("
					+ EmployeeDatabase.WORK_LIST_ID + ") FROM "
					+ TABLE_WORK_LIST;
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				recods = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}
		return recods;
	}

	public String executeQuery(String query) {
		String result = "";
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String selectQuery = query;
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				result = cursor.getString(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}
		return result;
	}

	public int executeQueryInt(String query) {
		int result = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String selectQuery = query;
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				result = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}
		return result;
	}

	public int executeQueryInt(String tableName, String query) {
		int result = 0;
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String selectQuery = query;
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				result = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, " getTotalRecords " + e.toString());
			cursor.close();
			db.close();
		}
		return result;
	}

	public HashMap<String, DetailsDto> getDetails() {
		HashMap<String, DetailsDto> map = new HashMap<String, DetailsDto>();

		return map;
	}
	
	

	public ArrayList<WorkListDto> getWorkList(int weekNo) {
		ArrayList<WorkListDto> worklist = new ArrayList<WorkListDto>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_WORK_LIST + " WHERE "
					+ WEEK_NO + "='" + weekNo + "'";

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					WorkListDto workListDto = new WorkListDto();
					workListDto.setWork_list_id(cursor.getInt(0));
					workListDto.setWeek_no(cursor.getInt(1));
					workListDto.setWork_date(cursor.getString(2));
					workListDto.setYear(cursor.getInt(3));

					worklist.add(workListDto);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, e.toString());
		}
		return worklist;
	}

	public ArrayList<WorkListDto> getActualHour() {
		ArrayList<WorkListDto> worklist = new ArrayList<WorkListDto>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT " + JOB_NUMBER + " , " + TAG_ID
					+ " FROM " + TABLE_ACTUAL_HOUR;

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					ActualHourDto actualHourDto = new ActualHourDto();
					actualHourDto.setJob_number(cursor.getString(0));
					actualHourDto.setTag_id(cursor.getInt(1));
					String selectQuery1 = "SELECT " + CUSTOMER_ID + " FROM "
							+ TABLE_JOBREGISTER + " WHERE " + JOB_NUMBER + "='"
							+ cursor.getString(0) + "'";
					int customerId = executeQueryInt(TABLE_JOBREGISTER,
							selectQuery1);
					String query = "SELECT " + CUSTOMER_NAME + " FROM "
							+ TABLE_CUSTOMER + " WHERE " + JOB_NUMBER + "='"
							+ customerId + "'";
					int customerName = executeQueryInt(TABLE_CUSTOMER, query);
					// worklist.add(workListDto);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, e.toString());
		}
		return worklist;
	}

	public void addSynchData(SynchDto synchDto) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(TAG_ID_OR_AH_ID, synchDto.getTag_id());
			values.put(UPDATED_JOB_STATUS_NAME,
					synchDto.getUpdated_job_status_name());
			values.put(ACTUAL_START_TIME, synchDto.getActual_start_time());
			values.put(SYNCTYPE, synchDto.getSynctype());
			values.put(ACTUAL_END_TIME, synchDto.getActual_end_time());

			if (!isExistsValue(db, TABLE_SYNCHRONOUS, TAG_ID_OR_AH_ID,
					synchDto.getTag_id())) {
				db.insert(TABLE_SYNCHRONOUS, null, values);
			} else {
				db.update(TABLE_SYNCHRONOUS, values, TAG_ID_OR_AH_ID + " = ?",
						new String[] { "" + synchDto.getTag_id() });
			}

			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, "addSynchData ==" + e.toString());
		}
	}

	public void removeRow(String tableName, String keyName, int keyValue) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(tableName, keyName + "=?", new String[] { "" + keyName });
		} catch (Exception e) {
			Utils.errorLog(TAG, " removeRow ==" + e.toString());
		}
		if (db != null)
			db.close();
	}

	public ArrayList<SynchDto> getSyncData() {
		ArrayList<SynchDto> syncList = new ArrayList<SynchDto>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT " + TAG_ID_OR_AH_ID + " , "
					+ UPDATED_JOB_STATUS_NAME + " , " + ACTUAL_START_TIME
					+ " , " + ACTUAL_END_TIME + " , " + SYNCTYPE + " FROM "
					+ TABLE_SYNCHRONOUS;

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					SynchDto synchDto = new SynchDto();
					synchDto.setTag_id(cursor.getInt(0));
					synchDto.setUpdated_job_status_name(cursor.getString(1));
					synchDto.setActual_start_time(cursor.getString(2));
					synchDto.setActual_end_time(cursor.getString(3));
					synchDto.setSynctype(cursor.getInt(3));
					syncList.add(synchDto);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, e.toString());
		}
		return syncList;
	}
	
//	public ArrayList<DetailsDto> getoosReport(){
//		ArrayList<DetailsDto> detailsDtos = new ArrayList<DetailsDto>();
//		String selectQuery1 = "SELECT " + SITE_USERNAME + " FROM "
//				+ TABLE_TAG_LIST + " WHERE " + TAG_ID + "='"
//				+ tagId + "'";
//		
//		
//		return detailsDtos;
//	}

	public ArrayList<DetailsDto> getActualHourData(String emplloyeeId,
			String work_date) {
		ArrayList<DetailsDto> detailsDtos = new ArrayList<DetailsDto>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT " + ACTUAL_HOUR_ID + " , " + TAG_ID
					+ " , " + JOB_NUMBER + " , " + ACTUAL_START_TIME + " , "
					+ ACTUAL_END_TIME + " FROM " + TABLE_ACTUAL_HOUR
					+ " WHERE " + EMPLOYEE_ID + "='" + emplloyeeId + "'"
					+ " AND " + WORK_DATE + "=" + "'" + work_date + "'";
			Utils.debugLog(TAG, "getActualHourData =" + selectQuery);
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					int actual_hour_id = cursor.getInt(0);
					int tagId = cursor.getInt(1);
					String jobnumber = cursor.getString(2);
					String actual_start_time = cursor.getString(3);
					String actual_end_time = cursor.getString(3);

					String selectQuery1 = "SELECT " + JOB_NUMBER + " FROM "
							+ TABLE_TAG_LIST + " WHERE " + TAG_ID + "='"
							+ tagId + "'";
					String jobNo = executeQuery(selectQuery1);
					String selectQuery2 = "SELECT " + CONTACT_NUMBER + " FROM "
							+ TABLE_TAG_LIST + " WHERE " + TAG_ID + "='"
							+ tagId + "'";
					String contactNo = executeQuery(selectQuery2);
					String selectQuery3 = "SELECT " + SITE_USERNAME + " FROM "
							+ TABLE_TAG_LIST + " WHERE " + TAG_ID + "='"
							+ tagId + "'";
					String siteUserName = executeQuery(selectQuery3);
					String selectQuery4 = "SELECT " + SERVICE_NUMBER + " FROM "
							+ TABLE_TAG_LIST + " WHERE " + TAG_ID + "='"
							+ tagId + "'";
					String serviceNo = executeQuery(selectQuery4);

					String updated_job_status_nameQ = "SELECT "
							+ UPDATED_JOB_STATUS_NAME + " FROM "
							+ TABLE_TAG_LIST + " WHERE " + TAG_ID + "='"
							+ tagId + "'";
					String updated_job_status_name = executeQuery(updated_job_status_nameQ);

					String selectQuery5 = "SELECT " + CUSTOMER_ID + " FROM "
							+ TABLE_JOBREGISTER + " WHERE " + JOB_NUMBER + "='"
							+ jobNo + "'";
					String customerId = executeQuery(selectQuery5);

					String selectQuery6 = "SELECT " + CUSTOMER_FULL_NAME
							+ " FROM " + TABLE_CUSTOMER + " WHERE "
							+ CUSTOMER_ID + "='" + customerId + "'";
					String customerName = executeQuery(selectQuery6);

					String selectQuery7 = "SELECT " + SITE_NAME + " FROM "
							+ TABLE_SITE + " WHERE " + SITE_USERNAME + "='"
							+ siteUserName + "'";
					String siteName = executeQuery(selectQuery7);

					int reportId = executeQueryInt("SELECT " + REPORT_ID
							+ " FROM " + TABLE_TAG_LIST + " WHERE " + TAG_ID
							+ "='" + tagId + "'");
					int tt_number = executeQueryInt("SELECT " + TT_NUMBER
							+ " FROM " + TABLE_TAG_LIST + " WHERE " + TAG_ID
							+ "='" + tagId + "'");
					
					
				

					DetailsDto detailsDto = new DetailsDto();
					detailsDto.setJobNo(jobNo);
					detailsDto.setContactNo(contactNo);
					detailsDto.setSiteUserName(siteUserName);
					detailsDto.setCustomerName(customerName);
					detailsDto.setSiteName(siteName);
					detailsDto.setServiceNo(serviceNo);
					detailsDto.setActual_start_time(actual_start_time);
					detailsDto.setActual_end_time(actual_end_time);
					detailsDto
							.setUpdated_job_status_name(updated_job_status_name);
					detailsDto.setTag_id(tagId);
					detailsDto.setActual_hour_id(actual_hour_id);
					detailsDto.setReportId(reportId);
					detailsDto.setDate(work_date);
					
					detailsDtos.add(detailsDto);
					detailsDto.setTt_number(tt_number);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, e.toString());
		}
		return detailsDtos;
	}

	private boolean isExistsValue(SQLiteDatabase db, String tableName,
			String keyName, int keyValue) {
		boolean isexists;
		String selectQuery = "SELECT  * FROM " + tableName + " WHERE "
				+ keyName + "='" + keyValue + "'";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			isexists = false;
		} else {
			isexists = true;
		}
		cursor.close();
		return isexists;
	}
}
