package com.girnar.explorejaipur.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.gps.GPSTracker;
import com.girnar.explorejaipur.network.NetworkTracker;
import com.girnar.explorejaipur.sqlite.ExploreJaipurDatabase;
import com.girnar.explorejaipur.sqlite.UtilitySqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class UtilityDetail extends Activity {
	private Tracker tracker;
	private final String TAG = "UtilityDetail";
	private Intent intent;
	private List<UtilitySqliteData> utilitySqliteDatas;
	private ExploreJaipurDatabase exploreJaipurDatabase;
	private UtilitySqliteData searchObject;
	private RadioButton radioButton;
	private int image;

	private ImageView buttonHome, buttonBack;
	private GPSTracker gps;
	private NetworkTracker networkTracker;
	private GoogleAdd googleAdd;
	private ProgressDialog progressDialog;
	private TextView name, nameView, categoryView, descriptionView,
			locationView, distanceFromHereView, distanceFromRailwayStationView,
			distanceFromBusStandView;
	private String placeName, category, distanceFromRailwayStation,
			description, location, latitude, longitude, utilityName,
			distanceFromBusStand;
	private Double sourceLatitude, sourceLongitude, destinationLatitude,
			destinationLongitude;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		Log.d(TAG, "On create called");
		networkTracker = new NetworkTracker(this);
		googleAdd = new GoogleAdd(this);
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("Detail");
		gps = new GPSTracker(this);
		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);

		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Utility Detail");

		utilityName = getIntent().getExtras().getString("name");
		image = getIntent().getExtras().getInt("image");

		ImageView utilityImage = (ImageView) findViewById(R.id.image_description);
		utilityImage.setImageResource(image);

		descriptionView = (TextView) findViewById(R.id.text_description_details);
		descriptionView.setText(description);
		radioButton = (RadioButton) findViewById(R.id.button_detail_description);
		radioButton.setChecked(true);
		radioButton = (RadioButton) findViewById(R.id.button_map_description);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.button_here_description);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.button_jp_description);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);

		searchObject = new UtilitySqliteData();
		searchObject.setName(utilityName);
		exploreJaipurDatabase = new ExploreJaipurDatabase(this);
		utilitySqliteDatas = exploreJaipurDatabase
				.retriveSelectedUtility(searchObject);
		for (UtilitySqliteData utility : utilitySqliteDatas) {
			placeName = utility.getName();
			category = utility.getCategory();
			distanceFromRailwayStation = utility.getDistance();
			distanceFromBusStand = utility.getDistanceBusStand();
			location = utility.getLocation();
			description = utility.getDescription();
			latitude = utility.getLatitude();
			destinationLatitude = Double.parseDouble(latitude);
			longitude = utility.getLongitude();
			destinationLongitude = Double.parseDouble(longitude);
		}
		nameView = (TextView) findViewById(R.id.text_name_description);
		nameView.setText(placeName);
		categoryView = (TextView) findViewById(R.id.text_category_description);
		categoryView.setText(category);
		locationView = (TextView) findViewById(R.id.text_address_description);
		locationView.setText(location);
		distanceFromRailwayStationView = (TextView) findViewById(R.id.text_Junction_description);
		distanceFromRailwayStationView.setText(distanceFromRailwayStation
				+ " km");
		distanceFromBusStandView = (TextView) findViewById(R.id.text_busstand_description);
		distanceFromBusStandView.setText(distanceFromBusStand + " km");
		descriptionView = (TextView) findViewById(R.id.text_description_details);
		descriptionView.setText(description);
		distanceFromHereView = (TextView) findViewById(R.id.text_here_description);
		if (networkTracker.isNetworkAvailable()) {
			if (gps.canGetLocationNetwork(new ProgressDialog(this))) {
				findLocation();
			} else if (gps.hasGPSDevice()) {
				if (gps.checkGPSEnabled()) {
					if (gps.canGetLocationGPS()) {

						findLocation();
					} else if (gps.canGetLocationNetwork(new ProgressDialog(
							this))) {
						findLocation();
					} else {
						((RadioButton) findViewById(R.id.button_here_description))
								.setEnabled(false);
						distanceFromHereView.setTextColor(Color.RED);
						distanceFromHereView
								.setText("Unable to find your location yet");
					}

				} else {
					gps.showSettingsAlert();
				}
			} else {
				((RadioButton) findViewById(R.id.button_here_description))
						.setEnabled(false);
				distanceFromHereView.setTextColor(Color.RED);
				distanceFromHereView
						.setText("Unable to find your location yet");
			}

		} else {
			distanceFromHereView.setTextColor(Color.RED);
			distanceFromHereView.setText("Unable to find your location ");
		}

		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		buttonHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(UtilityDetail.this, Homepage.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("status", "true");
				startActivity(intent);
				finish();
			}
		});
	}

	void findLocation() {
		try {
			sourceLatitude = gps.getLatitude();
			sourceLongitude = gps.getLongitude();
			new DistanceFinder().execute();
		} catch (Exception e) {
			((RadioButton) findViewById(R.id.button_here_description))
					.setEnabled(false);
			distanceFromHereView.setTextColor(Color.RED);
			distanceFromHereView.setText("Unable to find your location yet");

		}

	}

	private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.getId() == R.id.button_map_description) {
				intent = new Intent(UtilityDetail.this, MapView.class);
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", "utility");
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_here_description) {

				intent = new Intent(UtilityDetail.this, PathFromHere.class);
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", "utility");
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_jp_description) {

				intent = new Intent(UtilityDetail.this,
						PathFromJPJunction.class);
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", "utility");
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			}

		}
	};

	class DistanceFinder extends AsyncTask<Void, Void, Void> {
		String responseStr;
		String url;
		float iDistance;
		String iDistanceFloat;
		boolean flag = true;

		@Override
		protected void onPreExecute() {
			UtilityDetail.this.progressDialog = ProgressDialog.show(
					UtilityDetail.this, "", "Fetching details, Please wait...",
					true);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			StringBuilder urlString = new StringBuilder();
			urlString
					.append("http://maps.googleapis.com/maps/api/directions/json");
			urlString.append("?origin=");// from
			urlString.append(Double.toString(sourceLatitude));
			urlString.append(",");
			urlString.append(Double.toString(sourceLongitude));
			urlString.append("&destination=");// to
			urlString.append(Double.toString(destinationLatitude));
			urlString.append(",");
			urlString.append(Double.toString(destinationLongitude));
			urlString.append("&sensor=false&mode=driving&alternatives=true");
			url = urlString.toString();
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					entity.writeTo(out);
					out.close();
					responseStr = out.toString();
				} else {
				}
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}

			try {
				final JSONObject json = new JSONObject(responseStr);
				JSONArray routeArray = json.getJSONArray("routes");
				JSONObject routes = routeArray.getJSONObject(0);
				JSONArray legs = routes.getJSONArray("legs");
				JSONObject steps = legs.getJSONObject(0);
				JSONObject distance = steps.getJSONObject("distance");
				iDistance = (float) (distance.getInt("value"));
				DecimalFormat myFormatter = new DecimalFormat("##0.0");
				iDistanceFloat = myFormatter.format(iDistance / 1000);
			} catch (Exception e) {
				flag = false;

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			progressDialog.dismiss();
			if (flag) {
				distanceFromHereView.setText(iDistanceFloat + " km");
			} else {
				distanceFromHereView.setTextColor(Color.RED);
				distanceFromHereView.setText("Unable to found");
				Toast.makeText(UtilityDetail.this,
						"Unable to find distance from here", Toast.LENGTH_LONG)
						.show();
			}

			super.onPostExecute(result);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopGps();
	}
}
