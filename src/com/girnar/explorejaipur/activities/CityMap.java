package com.girnar.explorejaipur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.google.GooglePlayServices;
import com.girnar.explorejaipur.network.NetworkTracker;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class CityMap extends FragmentActivity {
	int check;
	private Tracker tracker;
	private final String TAG = "CityMap";
	private ImageView buttonHome, buttonBack;
	private Intent intent;
	private TextView name;
	private GoogleMap map;
	double latitude, longitude;
	public static final LatLng JAIPUR_LATLNG = new LatLng(26.916675, 75.817119);
	private GoogleAdd googleAdd ;
	private NetworkTracker networkTracker;
	GooglePlayServices googlePlayServices;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.citymap);
		Log.d(TAG, "On create called");
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("City Map");
	 googleAdd = new GoogleAdd(this);
 networkTracker = new NetworkTracker(this);
 googlePlayServices = new GooglePlayServices(this);
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("City Map");

		if (networkTracker.isNetworkAvailable()) {
			try {
				check = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(getApplicationContext());
				map = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.citymap_jaipur)).getMap();
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				map.setMyLocationEnabled(true);
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(JAIPUR_LATLNG,
						12));
			} catch (Exception e) {
				googlePlayServices.showGooglePlayServiceAlertDialog(check);
			}
		} else {
			networkTracker.showDialog();
		}

		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), Homepage.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("status", "true");
				startActivity(intent);
				finish();
			}
		});
	}
	
}
