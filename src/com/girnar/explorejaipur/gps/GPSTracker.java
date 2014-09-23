package com.girnar.explorejaipur.gps;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.girnar.explorejaipur.activities.UtilityDetail;
import com.girnar.explorejaipur.constants.Constants;

/**
 * The Class GPSTracker.
 */
public class GPSTracker extends Service implements LocationListener {

	private final Activity mContext;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocationFlag = false;
	Location location;
	double latitude;
	double longitude;
	Dialog dialog;
	protected LocationManager locationManager;
	private Criteria criteria;
	ProgressDialog progressDialogGps;
private String provider;
	/**
	 * Instantiates a new gPS tracker.
	 * 
	 * @param context
	 *            the context
	 */
	public GPSTracker(Activity context) {
		this.mContext = context;
		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);
		isGPSEnabled = checkGPSEnabled();
		isNetworkEnabled = checkNetworkEnabled();
	}
	/**
	 *  this method set the criteria to get provider
	 */

	public void setCriteria() {

		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	
	public boolean canGetLocationGPS() {
		boolean b = true;
		setCriteria();
		
	
		provider = locationManager.getBestProvider(criteria, true);
		if (isGPSEnabled) {

			locationManager.requestLocationUpdates(
					provider,
					Constants.MIN_TIME_BW_UPDATES,
					Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(provider);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				} else {
					b = false;
				}
			} else {
				b = false;
			}

		} else {
			b = false;
		}
		return b;
	}

	public boolean canGetLocationNetwork(ProgressDialog progressDialog) {
		boolean b = true;
		
		progressDialogGps=progressDialog;
		if (isNetworkEnabled) {
			
			progressDialogGps = ProgressDialog.show(
					mContext, "", "Fetching details, Please wait...",
					true);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES,
					Constants.MIN_TIME_BW_UPDATES, this);
			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				} else {
					b = false;
				}
			} else {
				b = false;
			}

		} else {
			b = false;
		}
		return b;
	}


	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public boolean checkGPSEnabled() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public boolean checkNetworkEnabled() {
		return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	@SuppressWarnings("unused")
	public boolean hasGPSDevice() {

		final LocationManager mgr = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (mgr == null)
			return false;
		final List<String> providers = mgr.getAllProviders();
		if (providers == null)
			return false;
		return providers.contains(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Show settings alert.
	 * 
	 * @param a
	 *            the a
	 */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle("GPS");
		alertDialog.setCancelable(false);
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
						mContext.finish();
					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						mContext.finish();

					}
				});
		alertDialog.show();
	}

	public void canNotGetLocation() {
		Dialog dialog;
		dialog = new Dialog(mContext) {
			@Override
			public void onBackPressed() {
				mContext.finish();
				super.onBackPressed();
			}
		};
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle("Your location can not be detected");
		dialog.setCancelable(true);
		dialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {

		latitude=location.getLatitude();
		longitude=location.getLongitude();
		if(progressDialogGps != null){
		if(progressDialogGps.isShowing()){
			progressDialogGps.dismiss();
		}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String,
	 * int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	public void stopGps()
	{
		locationManager.removeUpdates(this);
	}
}