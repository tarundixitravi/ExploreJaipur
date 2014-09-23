package com.girnar.explorejaipur.activities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.girnar.explorejaipur.sqlite.MonumentsSqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

/**
 * The Class MonumentDetail.
 */
public class MonumentDetail extends Activity {
	private Tracker tracker;
	private final String TAG = "MonumentDetail";
	private Intent intent;
	private List<MonumentsSqliteData> monumentsSqliteDatas;
	private ExploreJaipurDatabase exploreJaipurDatabase;
	private MonumentsSqliteData searchObject;
	private RadioButton radioButton;
	private int image;
	private NetworkTracker networkTracker;
	private GoogleAdd googleAdd;
	private GPSTracker gps;
	private ImageView monumentImage, buttonHome, buttonBack;
	private ProgressDialog progressDialog;
	private TextView nameView, categoryView, descriptionView,
			distanceFromRailwayStationView, distanceFromBusStandView,
			distanceFromHereView, locationView, name;
	private Double sourceLatitude, sourceLongitude, destinationLatitude,
			destinationLongitude;
	private String placeName, category, distanceFromRailwayStation,
			description, latitude, longitude, location, monumentName,
			distanceFromBusStand, monumentId;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		networkTracker = new NetworkTracker(this);
		googleAdd = new GoogleAdd(this);
		gps = new GPSTracker(this);
		Log.d(TAG, "On create called");
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("Detail");

		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);

		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Monument Detail");

		monumentName = getIntent().getExtras().getString("name");
		image = getIntent().getExtras().getInt("image");

		monumentImage = (ImageView) findViewById(R.id.image_description);
		monumentImage.setImageResource(image);

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

		searchObject = new MonumentsSqliteData();
		searchObject.setName(monumentName);
		exploreJaipurDatabase = new ExploreJaipurDatabase(this);
		monumentsSqliteDatas = exploreJaipurDatabase
				.retriveSelectedMonument(searchObject);
		for (MonumentsSqliteData monument : monumentsSqliteDatas) {
			monumentId = monument.getMonumentId();
			placeName = monument.getName();
			category = monument.getCategory();
			description = monument.getDescription();
			latitude = monument.getLatitude();
			destinationLatitude = Double.parseDouble(latitude);
			longitude = monument.getLongitude();
			destinationLongitude = Double.parseDouble(longitude);
			location = monument.getLocation();
			distanceFromRailwayStation = monument.getDistance();
			distanceFromBusStand = monument.getDistanceBusStand();
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
		distanceFromHereView = (TextView) findViewById(R.id.text_here_description);
		descriptionView = (TextView) findViewById(R.id.text_description_details);
		descriptionView.setText(description);

		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		buttonHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(MonumentDetail.this, Homepage.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("status", "true");
				startActivity(intent);
				finish();
			}
		});

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
			networkTracker.showDialog();
		}

	}

	void findLocation() {
		try {
			sourceLatitude = gps.getLatitude();
			sourceLongitude = gps.getLongitude();
			new DownloadImage()
					.execute("http://explorejaipur.girnarsoft.com/images/"
							+ monumentId + ".png");
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
				intent = new Intent(MonumentDetail.this, MapView.class);
				intent.putExtra("maptype", "sitemap");
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.putExtra("name", placeName);
				intent.putExtra("image", image);
				intent.putExtra("check_point", "monument");
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_here_description) {
				intent = new Intent(MonumentDetail.this, PathFromHere.class);
				intent.putExtra("maptype", "path_from_here");
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.putExtra("name", placeName);
				intent.putExtra("image", image);
				intent.putExtra("check_point", "monument");
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_jp_description) {
				intent = new Intent(MonumentDetail.this,
						PathFromJPJunction.class);
				intent.putExtra("maptype", "path_from_junction");
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				intent.putExtra("name", placeName);
				intent.putExtra("image", image);
				intent.putExtra("check_point", "monument");
				startActivity(intent);
				finish();
			}
		}
	};

	public class DownloadImage extends AsyncTask<String, Integer, Drawable> {
		@Override
		protected void onPreExecute() {
			MonumentDetail.this.progressDialog = ProgressDialog.show(
					MonumentDetail.this, "",
					"Please wait, fetching details...", true);
			super.onPreExecute();
		}

		@Override
		protected Drawable doInBackground(String... arg0) {
			return downloadImage(arg0[0]);
		}

		protected void onPostExecute(Drawable image) {

			if (image != null) {
				// progressDialog.dismiss();
				downloadPath();
				monumentImage.setImageDrawable(image);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private Drawable downloadImage(String urlget) {
		URL url;
		InputStream in;
		BufferedInputStream buf;
		try {
			url = new URL(urlget);
			in = url.openStream();
			buf = new BufferedInputStream(in);
			Bitmap bMap = BitmapFactory.decodeStream(buf);
			if (in != null) {
				in.close();
			}
			if (buf != null) {
				buf.close();
			}
			return new BitmapDrawable(bMap);
		} catch (Exception e) {
			// progressDialog.dismiss();
			handler.sendMessage(new Message());
			return null;
		}
	}

	void downloadPath() {
		if (networkTracker.isNetworkAvailable()) {
			new DistanceFinder().execute();
		} else {
			networkTracker.showDialog();
		}
	}

	class DistanceFinder extends AsyncTask<Void, Void, Void> {
		String responseStr;
		String url;
		float iDistance;
		String iDistanceFloat;
		boolean flag = true;

		@Override
		protected void onPreExecute() {

			/*
			 * MonumentDetail.this.progressDialog2 = ProgressDialog.show(
			 * MonumentDetail.this, "", "Fetching details, Please wait...",
			 * true);
			 */
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
					Toast.makeText(MonumentDetail.this,
							"Distance not received, please retry",
							Toast.LENGTH_LONG).show();
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
			} catch (JSONException e) {
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
				distanceFromHereView.setText("Unable to found");
				Toast.makeText(MonumentDetail.this,
						"Unable to find distance from here", Toast.LENGTH_LONG)
						.show();
			}

			super.onPostExecute(result);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(MonumentDetail.this, "Please connect to internet",
					Toast.LENGTH_LONG).show();
			finish();

		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gps.stopGps();
	}
}
