package com.sectom.customlistener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.secto.model.DetailsDto;
import com.secto.ui.ReportActivityFour;
import com.secto.utils.Constants;

public class ReportListener implements OnClickListener {
	DetailsDto detailsDto;
	Context context;

	public ReportListener(DetailsDto detailsDto, Context context) {
		this.detailsDto = detailsDto;
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(context, ""+detailsDto.getReportId(), Toast.LENGTH_SHORT).show();
		switch (detailsDto.getReportId()) {
		case 1:
			Toast.makeText(context, ""+detailsDto.getReportId(), Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(context, ""+detailsDto.getReportId(), Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(context, ""+detailsDto.getReportId(), Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Constants.detailsDto = detailsDto;
			Intent intent = new Intent(context,ReportActivityFour.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			
			break;

		default:
			break;
		}

	}

}
