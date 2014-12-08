package com.secto.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.secto.customadapter.CustomGridViewAdapter;
import com.secto.database.EmployeeDatabase;
import com.secto.database.ReportDatabase;
import com.secto.model.ImageDto;
import com.secto.utils.Constants;
import com.secto.utils.Utils;
import com.setco.interfacelist.Transport;

public class ReportActivityFour extends Activity implements OnClickListener,
		Transport {
	private EditText customerNetwork, lps_stolen;
	private TextView siteNormal_TV, siteName_TV, dateofwork_TV, pu_works_TV,
			ticketNo_TV, oosreportTab, oosreportimageTab;
	private Button saveBtn;
	private LinearLayout oosreportPanel, oosreportimagePanel;
	public static ReportActivityFour reportActivityFour;
	GridView gridView;

	CustomGridViewAdapter customGridAdapter;
	boolean recapture = false;
	int position = 0;
	private Button chooseImaageBtn;
	private ImageView imageView;
	Button saveImaageBtn;
	ArrayList<ImageDto> imageList = new ArrayList<ImageDto>();
	String TAG = "ReportActivityFour";
	ReportDatabase reportDatabase;
	EmployeeDatabase employeeDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reportidfour);
		reportActivityFour = this;
		reportDatabase = new ReportDatabase(getApplicationContext());
		employeeDatabase = new  EmployeeDatabase(getApplicationContext());
		initUI();
		
		loadImageFromDB();
		
		
	
	}

	private void showImage() {
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid,imageList);
		gridView.setAdapter(customGridAdapter);
		
	}

	private void loadImageFromDB() {
		imageList = reportDatabase.getImagesList();
		showImage();
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		reportActivityFour = null;
	}

	private void initUI() {
		customerNetwork = (EditText) findViewById(R.id.customerNetwork);
		lps_stolen = (EditText) findViewById(R.id.lps_stolen);
		siteNormal_TV = (TextView) findViewById(R.id.siteNormal_TV);
		siteName_TV = (TextView) findViewById(R.id.siteName_TV);
		dateofwork_TV = (TextView) findViewById(R.id.dateofwork_TV);
		pu_works_TV = (TextView) findViewById(R.id.pu_works_TV);
		ticketNo_TV = (TextView) findViewById(R.id.ticketNo_TV);
		oosreportPanel = (LinearLayout) findViewById(R.id.oosreportPanel);
		oosreportimagePanel = (LinearLayout) findViewById(R.id.oosreportimagePanel);

		oosreportTab = (TextView) findViewById(R.id.oosreportTab);
		oosreportimageTab = (TextView) findViewById(R.id.oosreportimageTab);
		oosreportTab.setOnClickListener(this);
		oosreportimageTab.setOnClickListener(this);
		oosreportTab.setSelected(true);

		chooseImaageBtn = (Button) findViewById(R.id.chooseImaageBtn);
		chooseImaageBtn.setOnClickListener(this);

		gridView = (GridView) findViewById(R.id.gridView1);
		saveImaageBtn =(Button) findViewById(R.id.saveImaageBtn);
		saveImaageBtn.setOnClickListener(this);
		
		siteNormal_TV.setText(Constants.detailsDto.getSiteUserName());
		siteName_TV.setText(Constants.detailsDto.getSiteName());
		dateofwork_TV.setText(Constants.detailsDto.getDate());
		ticketNo_TV.setText(""+Constants.detailsDto.getTt_number());
	
		int employeeId = employeeDatabase.executeQueryInt("SELECT " + EmployeeDatabase.EMPLOYEE_ID
				+ " FROM " + EmployeeDatabase.TABLE_ACTUAL_HOUR + " WHERE " + EmployeeDatabase.TAG_ID
				+ "='" + Constants.detailsDto.getTag_id() + "'");
		
		String fisrtNameQuery = "SELECT " + EmployeeDatabase.EMPLOYEE_FIRST_NAME + " FROM "
				+ EmployeeDatabase.TABLE_EMPLOYEE + " WHERE " + EmployeeDatabase.EMPLOYEE_ID + "='"
				+ employeeId + "'";
		
		String fisrtName = employeeDatabase.executeQuery(fisrtNameQuery);
		
		String lastNameQuery = "SELECT " + EmployeeDatabase.EMPLOYEE_LAST_NAME + " FROM "
				+ EmployeeDatabase.TABLE_EMPLOYEE + " WHERE " + EmployeeDatabase.EMPLOYEE_ID + "='"
				+ employeeId + "'";
		
		String lastName = employeeDatabase.executeQuery(lastNameQuery);
		pu_works_TV.setText(fisrtName+" "+lastName);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.oosreportTab:
			oosreportPanel.setVisibility(View.VISIBLE);
			oosreportimagePanel.setVisibility(View.GONE);
			oosreportimageTab.setSelected(false);
			oosreportTab.setSelected(true);
			break;
		case R.id.oosreportimageTab:
			oosreportPanel.setVisibility(View.GONE);
			oosreportimagePanel.setVisibility(View.VISIBLE);
			oosreportimageTab.setSelected(true);
			oosreportTab.setSelected(false);
			break;
		case R.id.chooseImaageBtn:
			recapture = false;
			takePictureFromCamera();
			break;
			
		case R.id.saveImaageBtn:
			saveToDataBase();
			break;
		default:
			break;
		}
	}

	private void saveToDataBase() {
		if(imageList.size()>0){
			reportDatabase.removeDb();
		}
		for (int i = 0; i < imageList.size(); i++) {
			ImageDto imageDto = imageList.get(i);
			reportDatabase.addImages(imageDto);
		}
		
		Toast.makeText(getApplicationContext(), "Images Saved succesfully.", Toast.LENGTH_SHORT).show();
	}

	Uri imageUri = null;
	final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	String Path;

	private void takePictureFromCamera() {
		String fileName = System.currentTimeMillis() + ".jpg";
		Utils.errorLog(TAG, "takePictureFromCamera == " + fileName);
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,
				"Image capture by camera");
		imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String imagePath = convertImageUriToFile(imageUri,
						ReportActivityFour.this);
				ImageDto imageDto = new ImageDto();
				imageDto.setTitle(imagePath);
				imageDto.setImagepath(imagePath);
				try {
					if(recapture){
						imageList.remove(position);
						imageList.add(position,imageDto);
					}else{
						imageList.add(imageDto);
					}
				} catch (Exception e) {
					Utils.errorLog(TAG, " onActivityResult saving .... "+e.toString());
				}
				if (customGridAdapter != null) {
					customGridAdapter.notifyDataSetChanged();
				} else {
					customGridAdapter = new CustomGridViewAdapter(this,
							R.layout.row_grid, imageList);
					gridView.setAdapter(customGridAdapter);
				}
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, " Picture was not taken ",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public String convertImageUriToFile(Uri imageUri, Activity activity) {
		Cursor cursor = null;
		try {
			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = getContentResolver().query(imageUri, 
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null // Order-by clause (ascending by name)

					);

			// Get Query Data

			int file_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/

			if (size == 0) {
				Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (cursor.moveToFirst()) {
					Path = cursor.getString(file_ColumnIndex);
					Utils.debugLog(TAG, " image path =" + Path);
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return Path;
	}

	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				ReportActivityFour.this);
		Bitmap mBitmap;

		protected void onPreExecute() {
			Dialog.setMessage(" Loading image from Sdcard..");
			Dialog.show();
		}

		// Call after onPreExecute method
		protected Void doInBackground(String... urls) {

			mBitmap = BitmapFactory.decodeFile(Path);

			return null;
		}

		protected void onPostExecute(Void unused) {
			Dialog.dismiss();
			if (mBitmap != null) {
				imageView.setImageBitmap(mBitmap);
			}
		}
	}

	@Override
	public void recapture(String fileName,int position) 
	{
		this.position = position;
		recapture = true;
		takePictureFromCamera();
	}

}
