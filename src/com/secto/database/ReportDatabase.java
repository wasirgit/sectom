package com.secto.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.secto.model.ImageDto;
import com.secto.utils.Utils;

public class ReportDatabase extends SQLiteOpenHelper {
	String TAG = "ReportDatabase";
	public static final String DATABASE_NAME = "REPORT_DB";
	public static final String TABLE_IMAGES = "images_table";

	public static final String IMAGES_ID = "ID";
	public static final String IMAGES_TITLE = "images_title";
	public static final String IMAGES_PATH = "images_path";

	public static String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES
			+ " (" + IMAGES_ID + " INTEGER PRIMARY KEY ," + IMAGES_PATH+ " TEXT ,"+ IMAGES_TITLE
			+ " TEXT )";

	public ReportDatabase(Context context) {
		super(context, DATABASE_NAME, null, EmployeeDatabase.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_IMAGES_TABLE);
		} catch (Exception e) {
			Utils.errorLog(TAG, "Oncreate" + e.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
		onCreate(db);
	}
	public void removeDb(){
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(TABLE_IMAGES, null, null);
		} catch (Exception e) {
			Utils.errorLog(TAG, " removeRow ==" + e.toString());
		}
		if (db != null)
			db.close();
	}

	public void addImages(ImageDto imageDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(IMAGES_PATH, imageDto.getImagepath());
		values.put(IMAGES_TITLE, imageDto.getTitle());
		try {
			db.insert(TABLE_IMAGES, null, values);
		} catch (Exception e) {

		}
		db.close();
	}
	
	public void updateImages(ImageDto imageDto) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(IMAGES_PATH, imageDto.getImagepath());
		try {
			db.update(TABLE_IMAGES, values, IMAGES_PATH + " = ?",
					new String[] { String.valueOf("" + imageDto.getImagepath()) });
		} catch (Exception e) {
			Utils.errorLog(TAG, "updateImages " + e.toString());
		}
		db.close();
	}
	
	public ArrayList<ImageDto> getImagesList() {
		ArrayList<ImageDto> imagesList = new ArrayList<ImageDto>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT " + IMAGES_PATH +" , "+IMAGES_TITLE+ " FROM "
					+ TABLE_IMAGES;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					ImageDto imageDto = new ImageDto();
					imageDto.setImagepath(cursor.getString(0));
					imageDto.setTitle(cursor.getString(1));
					imagesList.add(imageDto);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Utils.errorLog(TAG, e.toString());
		}
		return imagesList;
	}

}
