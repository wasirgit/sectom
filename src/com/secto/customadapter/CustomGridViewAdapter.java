package com.secto.customadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.secto.model.ImageDto;
import com.secto.ui.R;
import com.secto.ui.ReportActivityFour;
import com.setco.interfacelist.Transport;

public class CustomGridViewAdapter extends ArrayAdapter<ImageDto> {
	Context context;
	int layoutResourceId;
	ArrayList<ImageDto> data = new ArrayList<ImageDto>();
	Transport transport;

	public CustomGridViewAdapter(Context context, int layoutResourceId,
			ArrayList<ImageDto> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (EditText) row.findViewById(R.id.item_text);
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			holder.recaptureBtn = (Button) row.findViewById(R.id.recaptureBtn);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		// AQuery aQuery = new AQuery(row);
		final ImageDto imageDto = data.get(position);
		Bitmap bitmap = BitmapFactory.decodeFile(imageDto.getImagepath());
		holder.imageItem.setImageBitmap(bitmap);

		holder.imageItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});

		holder.recaptureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				transport = ReportActivityFour.reportActivityFour;
				transport.recapture(imageDto.getImagepath(),position);
			}
		});
		// aQuery.id(holder.imageItem).image("http://rpwworld.com/sectoms/uploads/oos_report_image/237926491_1417360758.jpg");
		return row;
	}

	static class RecordHolder {
		EditText txtTitle;
		ImageView imageItem;
		Button recaptureBtn;

	}
}