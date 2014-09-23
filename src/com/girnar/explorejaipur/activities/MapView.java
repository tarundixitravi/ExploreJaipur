package com.girnar.explorejaipur.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.google.GooglePlayServices;
import com.girnar.explorejaipur.gps.GPSTracker;
import com.girnar.explorejaipur.network.NetworkTracker;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapView extends FragmentActivity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "MapView";
	private Intent intent;
	private double siteLatitude, siteLongitude;
	private LatLng place;
	private GoogleMap siteMap;
	private GPSTracker gps;
	private ImageView buttonHome, buttonBack;
	private RadioButton radioButton;
	private String check_point, gotLog, gotLat, placeName;
	private int image;
	private int result;
	private TextView name;
	private NetworkTracker networkTracker;
	private GoogleAdd googleAdd;
	GooglePlayServices googlePlayServices;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		Log.d(TAG, "On create called");
		networkTracker = new NetworkTracker(this);
		googleAdd = new GoogleAdd(this);
		googlePlayServices = new GooglePlayServices(this);
		gps = new GPSTracker(this);
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("Locate on map");

		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(this);
		buttonHome.setOnClickListener(this);

		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Map View");

		check_point = getIntent().getExtras().getString("check_point");
		gotLog = getIntent().getExtras().getString("longitude");
		gotLat = getIntent().getExtras().getString("latitude");
		placeName = getIntent().getExtras().getString("name");
		image = getIntent().getExtras().getInt("image");

		siteLatitude = Double.parseDouble(gotLat);
		siteLongitude = Double.parseDouble(gotLog);

		place = new LatLng(siteLatitude, siteLongitude);

		radioButton = (RadioButton) findViewById(R.id.button_detail_description_onmap);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.button_map_description_onmap);
		radioButton.setChecked(true);
		radioButton = (RadioButton) findViewById(R.id.button_here_description_onmap);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.button_jp_description_onmap);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		if (gps.hasGPSDevice()) {
			if (gps.checkGPSEnabled()) {
				if (gps.canGetLocationGPS()) {

				} else if (gps.canGetLocationNetwork(new ProgressDialog(this))) {

				} else {
					((RadioButton) findViewById(R.id.button_here_description))
							.setEnabled(false);

				}
			} else {

			}
		} else if (gps.canGetLocationNetwork(new ProgressDialog(this))) {

		} else {
			((RadioButton) findViewById(R.id.button_here_description))
					.setEnabled(false);

		}
		if (networkTracker.isNetworkAvailable()) {
			try {
				result = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(getApplicationContext());
				siteMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
				siteMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				siteMap.addMarker(new MarkerOptions()
						.position(place)
						.title(placeName)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				siteMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 16));
			} catch (Exception e) {
				googlePlayServices.showGooglePlayServiceAlertDialog(result);
			}
		} else {
			networkTracker.showDialog();
		}

	}

	Context context;

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		this.context = context;
		return super.onCreateView(name, context, attrs);

	}

	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.getId() == R.id.button_detail_description_onmap) {
				intent = new Intent();
				if (check_point.equals("monument")) {
					intent = new Intent(MapView.this, MonumentDetail.class);
				} else if (check_point.equals("utility")) {
					intent = new Intent(MapView.this, UtilityDetail.class);
				}
				intent.putExtra("name", placeName);
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_here_description_onmap) {

				intent = new Intent(MapView.this, PathFromHere.class);
				intent.putExtra("longitude", gotLog);
				intent.putExtra("latitude", gotLat);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", check_point);
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_jp_description_onmap) {

				intent = new Intent(MapView.this, PathFromJPJunction.class);
				intent.putExtra("image", image);
				intent.putExtra("longitude", gotLog);
				intent.putExtra("latitude", gotLat);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", check_point);
				startActivity(intent);
				finish();
			}

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.image_sub_back)
			finish();
		else if (v.getId() == R.id.image_sub_home) {
			Intent inentHome = new Intent(this, Homepage.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			inentHome.putExtra("status", "true");
			startActivity(inentHome);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopGps();
	}
}
