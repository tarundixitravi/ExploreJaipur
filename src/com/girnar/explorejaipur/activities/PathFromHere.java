package com.girnar.explorejaipur.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

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
import com.google.android.gms.maps.model.PolylineOptions;

public class PathFromHere extends FragmentActivity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "PathFromHere";
	private Intent intent;
	private boolean check1 = true;
	private ProgressDialog progressDialog;
	private TextView header;
	private LatLng place;
	private GoogleMap pathFromHere;
	private RadioButton radioButton;
	private String check_point, gotLog, gotLat, placeName, url;

	private int image;
	private GPSTracker gps;
	private ImageView buttonHome, buttonBack;
	private NetworkTracker networkTracker;
	private GoogleAdd googleAdd;
	GooglePlayServices googlePlayServices;
	private int result;
	private double siteLatitude, siteLongitude, sourceLatitude,
			sourceLongitude, destinationLatitude, destinationLongitude;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		Log.d(TAG, "On create called");
		networkTracker = new NetworkTracker(this);
		googleAdd = new GoogleAdd(this);
		googlePlayServices = new GooglePlayServices(this);
		header = (TextView) findViewById(R.id.text_sub_headind);
		header.setText("Path on map");
		gps = new GPSTracker(this);
		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(this);
		buttonHome.setOnClickListener(this);

		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Path From Here");

		check_point = getIntent().getExtras().getString("check_point");
		image = getIntent().getExtras().getInt("image");
		gotLog = getIntent().getExtras().getString("longitude");
		gotLat = getIntent().getExtras().getString("latitude");
		placeName = getIntent().getExtras().getString("name");

		siteLatitude = Double.parseDouble(gotLat);
		siteLongitude = Double.parseDouble(gotLog);
		destinationLatitude = siteLatitude;
		destinationLongitude = siteLongitude;

		place = new LatLng(siteLatitude, siteLongitude);

		radioButton = (RadioButton) findViewById(R.id.button_detail_description_onmap);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.button_map_description_onmap);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton = (RadioButton) findViewById(R.id.button_here_description_onmap);
		radioButton.setChecked(true);
		radioButton = (RadioButton) findViewById(R.id.button_jp_description_onmap);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);

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
						gps.canNotGetLocation();
						finish();
					}
				} else {
					gps.showSettingsAlert();
				}
			} else {
				gps.canNotGetLocation();
				finish();
			}

		} else {
			networkTracker.showDialog();
		}

	}

	void findLocation() {
		try {
			sourceLatitude = gps.getLatitude();
			sourceLongitude = gps.getLongitude();

			try {
				result = GooglePlayServicesUtil
						.isGooglePlayServicesAvailable(getApplicationContext());
				pathFromHere = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
				pathFromHere
						.addMarker(new MarkerOptions()
								.position(
										new LatLng(sourceLatitude,
												sourceLongitude))
								.title("Your Location")
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
				pathFromHere.addMarker(new MarkerOptions().position(place)
						.title(placeName));
				pathFromHere.moveCamera(CameraUpdateFactory.newLatLngZoom(
						place, 10));
				pathFromHere.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				new connectAsyncTask().execute();
			} catch (Exception e) {
				googlePlayServices.showGooglePlayServiceAlertDialog(result);
			}

		} catch (Exception e) {
			gps.canNotGetLocation();
			finish();
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
					intent = new Intent(PathFromHere.this, MonumentDetail.class);
				} else if (check_point.equals("utility")) {
					intent = new Intent(PathFromHere.this, UtilityDetail.class);
				}
				intent.putExtra("name", placeName);
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_map_description_onmap) {

				intent = new Intent(PathFromHere.this, MapView.class);
				intent.putExtra("image", image);
				intent.putExtra("longitude", gotLog);
				intent.putExtra("latitude", gotLat);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", check_point);
				startActivity(intent);
				finish();
			} else if (buttonView.getId() == R.id.button_jp_description_onmap) {

				intent = new Intent(PathFromHere.this, PathFromJPJunction.class);
				intent.putExtra("longitude", gotLog);
				intent.putExtra("latitude", gotLat);
				intent.putExtra("name", placeName);
				intent.putExtra("check_point", check_point);
				intent.putExtra("image", image);
				startActivity(intent);
				finish();
			}

		}
	};

	private class connectAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			PathFromHere.this.progressDialog = ProgressDialog.show(
					PathFromHere.this, "", "Fetching route, Please wait...",
					true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			url = makeURL(sourceLatitude, sourceLongitude, destinationLatitude,
					destinationLongitude);
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			if (check1) {
				progressDialog.hide();
				if (result != null) {
					drawPath(result);
				} else {
					Toast.makeText(PathFromHere.this, "Unable to find path",
							Toast.LENGTH_LONG).show();
				}
			}
			super.onPostExecute(result);
		}
	}

	public class JSONParser {

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		public JSONParser() {
		}

		public String getJSONFromUrl(String url) {
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = (HttpResponse) httpClient
						.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				json = sb.toString();
				is.close();
			} catch (Exception e) {
				check1 = false;
				progressDialog.hide();
				Toast.makeText(PathFromHere.this, "Unable to find path",
						Toast.LENGTH_LONG).show();
			}
			return json;

		}
	}

	public void drawPath(String result) {

		try {

			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes
					.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);

			for (int z = 0; z < list.size() - 1; z++) {
				LatLng src = list.get(z);
				LatLng dest = list.get(z + 1);
				pathFromHere.addPolyline(new PolylineOptions()
						.add(new LatLng(src.latitude, src.longitude),
								new LatLng(dest.latitude, dest.longitude))
						.width(2).color(Color.RED).geodesic(true));

			}

		} catch (JSONException e) {
			Dialog dlg = new Dialog(PathFromHere.this);
			dlg.setCancelable(true);
			dlg.setTitle("Unfortunatly Path was not drawn, Please try again");
			dlg.show();
		}
	}

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	public String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	@Override
	public void onClick(View v) {
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
