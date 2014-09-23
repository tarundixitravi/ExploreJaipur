package com.girnar.explorejaipur.google;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServices {

	Activity context;
	int result;
	public GooglePlayServices(Activity a) {
		this.context = a;
	}
	
	public void showGooglePlayServiceAlertDialog(int b) {
		result=b;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Do you want to proceed ?").setTitle(
				"Google Play Services problem !");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						GooglePlayServicesUtil.getErrorDialog(result,context, 32).show();
						dialog.dismiss();
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						context.finish();
						
					}
				});
		builder.show();
	}

}
