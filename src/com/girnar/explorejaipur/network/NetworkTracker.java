package com.girnar.explorejaipur.network;

import android.app.Activity;
import android.app.Dialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The Class NetworkTracker.
 */
public class NetworkTracker {

	Activity context;
	Dialog dlg;

	/**
	 * Instantiates a new network tracker.
	 * 
	 * @param context
	 *            the context
	 */
	public NetworkTracker(Activity context) {
		this.context = context;
	}

	/**
	 * Checks if is network available.
	 * 
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		@SuppressWarnings("static-access")
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Show dialog.
	 */
	public void showDialog() {
		Dialog internetNotConnectedDialog = new Dialog(context) {
			@Override
			public void onBackPressed() {
				context.finish();
				super.onBackPressed();
			}
		};
		internetNotConnectedDialog.setCanceledOnTouchOutside(false);
		internetNotConnectedDialog.setTitle("Please connect to internet");
		internetNotConnectedDialog.setCancelable(true);
		internetNotConnectedDialog.show();

	}

	public void internetNotActive() {
		dlg = new Dialog(context) {
			@Override
			public void onBackPressed() {
				context.finish();
				super.onBackPressed();
			}
		};
		dlg.setCanceledOnTouchOutside(false);
		dlg.setTitle("Internet connection is not active or too slow");
		dlg.setCancelable(true);
		dlg.show();
	}
}
