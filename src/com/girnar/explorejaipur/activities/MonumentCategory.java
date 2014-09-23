package com.girnar.explorejaipur.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.custom.CustomAdapterCategory;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.gps.GPSTracker;
import com.girnar.explorejaipur.sqlite.ExploreJaipurDatabase;
import com.girnar.explorejaipur.sqlite.MonumentsSqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class MonumentCategory extends Activity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "MonumentCategory";
	private Intent intent;
	private Location locationA, locationB;
	private CustomAdapterCategory adepter;
	private List<MonumentsSqliteData> monumentsSqliteDatas;
	private ExploreJaipurDatabase exploreJaipurDatabase;
	private MonumentsSqliteData searchObject;
	private int image;
	private RadioButton radioButton;
	private GPSTracker gps;
	private GoogleAdd googleAdd;
	private String CategoryForDatabaseSearch;
	private TextView  name;
	private ImageView buttonHome, buttonBack;
	private ListView monumentList;
	private String title;
	private ArrayList<String> placeId = new ArrayList<String>();
	private ArrayList<String> placeName = new ArrayList<String>();
	private ArrayList<String> placeNameForSorting = new ArrayList<String>();
	private ArrayList<String> distance = new ArrayList<String>();
	private ArrayList<Float> distanceFromCurrentLocation = new ArrayList<Float>();
	private ArrayList<Float> distanceFromCurrentLocationForSorting = new ArrayList<Float>();
	private Double monumentLatitude, monumentLongitude, currentLatitude,
			currentLongitude; 
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		Log.d(TAG, "On create called");
		googleAdd = new GoogleAdd(this);
		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(this);
		buttonHome.setOnClickListener(this);
		
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Monument Category");

		image = getIntent().getExtras().getInt("image");
		title = getIntent().getExtras().getString("name");

		name = (TextView) findViewById(R.id.text_sub_headind);
		if (title.equals("historical")) {
			name.setText("Historical places");
			CategoryForDatabaseSearch = "Historical Monument";
		} else if (title.equals("temple")) {
			name.setText("Temples");
			CategoryForDatabaseSearch = "Temple";
		} else if (title.equals("picnic")) {
			name.setText("Picnic Spots");
			CategoryForDatabaseSearch = "Picnic Spots";
		} else if (title.equals("shopingmalls")) {
			name.setText("Malls");
			CategoryForDatabaseSearch = "Shopping Mall";
		} else if (title.equals("cinema")) {
			name.setText("Cinema Halls");
			CategoryForDatabaseSearch = "Cinema Halls";
		}

		monumentList = (ListView) findViewById(R.id.list_jaipur_bus_root);
		radioButton = (RadioButton) findViewById(R.id.button_namewise_category);
		radioButton
		.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		radioButton.setChecked(true);
		radioButton = (RadioButton) findViewById(R.id.button_nearby_category);
		radioButton
				.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
		


		gps = new GPSTracker(this);

			if  (gps.canGetLocationNetwork(new ProgressDialog(this))) {
				afterLocationFind();
			} else if(gps.hasGPSDevice()) {
				if (gps.checkGPSEnabled()) {
					if (gps.canGetLocationGPS()) {

						afterLocationFind();
					} 
						
						else if (gps.canGetLocationNetwork(new ProgressDialog(this))) {
							
							afterLocationFind();
						} else {
					//	gps.canNotGetLocation();
							Toast.makeText(getApplicationContext(),"Please check your network settings" ,Toast.LENGTH_LONG).show();
						finish();
					}
				} else {
					gps.showSettingsAlert();
				}
			} else {
			//gps.canNotGetLocation();
				Toast.makeText(getApplicationContext(),"Please check your network settings" ,Toast.LENGTH_LONG).show();	
			finish();
		}

	}


	private void afterLocationFind() {
		try {
			currentLatitude = gps.getLatitude();
			currentLongitude = gps.getLongitude();
			searchObject = new MonumentsSqliteData();
			searchObject.setCategory(CategoryForDatabaseSearch);
			exploreJaipurDatabase = new ExploreJaipurDatabase(this);
			monumentsSqliteDatas = exploreJaipurDatabase
					.retriveSelectedCategoryMonument(searchObject);
			for (MonumentsSqliteData monument : monumentsSqliteDatas) {
				placeId.add(monument.getMonumentId());
				placeName.add(monument.getName());
				distance.add(monument.getDistance() + " km");
				monumentLatitude = Double.parseDouble(monument.getLatitude());
				monumentLongitude = Double.parseDouble(monument.getLongitude());
				placeNameForSorting.add(monument.getName());
				locationA = new Location("Location A");
				locationA.setLatitude(currentLatitude);
				locationA.setLongitude(currentLongitude);
				locationB = new Location("Location B");
				locationB.setLatitude(monumentLatitude);
				locationB.setLongitude(monumentLongitude);
				distanceFromCurrentLocation
						.add(locationA.distanceTo(locationB) / 1000);
				distanceFromCurrentLocationForSorting.add(locationA
						.distanceTo(locationB) / 1000);
			}

			monumentList = (ListView) findViewById(R.id.list_jaipur_bus_root);
			adepter = new CustomAdapterCategory(MonumentCategory.this,
					placeName, image);
			monumentList.setAdapter(adepter);
			monumentList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					intent = new Intent(MonumentCategory.this,
							MonumentDetail.class);
					intent.putExtra("name", (String) placeName.get(arg2));
					intent.putExtra("image", image);
					startActivity(intent);
				}
			});
			

		} catch (Exception e) {
			gps.canNotGetLocation();
		}
		
	}
	 CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.getId() == R.id.button_nearby_category) {
				
				
				CustomAdapterCategory adepter = new CustomAdapterCategory(
						MonumentCategory.this, placeName, image);
				monumentList.setAdapter(adepter);
				monumentList
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(
									AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
								intent = new Intent(
										MonumentCategory.this,
										MonumentDetail.class);
								intent.putExtra("name",
										(String) placeName.get(arg2));
								intent.putExtra("image", image);
								startActivity(intent);
							}
						});
			} 
			else if (buttonView.getId() == R.id.button_namewise_category) {
				
				String temp1;
				float temp2;
               
				int size = placeName.size();
				for (int i = 0; i < size; i++) {
					for (int j = 1; j < size; j++) {
						if (distanceFromCurrentLocationForSorting
								.get(j) < distanceFromCurrentLocationForSorting
								.get(j - 1)) {
									

							temp2 = distanceFromCurrentLocationForSorting
									.get(j - 1);
							distanceFromCurrentLocationForSorting.set(
									j - 1,
									distanceFromCurrentLocationForSorting
											.get(j));
							distanceFromCurrentLocationForSorting.set(
									j, temp2);

							temp1 = placeNameForSorting.get(j - 1);
							placeNameForSorting.set(j - 1,
									placeNameForSorting.get(j));
							placeNameForSorting.set(j, temp1);

						}
					}

				}
				
				CustomAdapterCategory adepter = new CustomAdapterCategory(
						MonumentCategory.this, placeNameForSorting,
						image);
				monumentList.setAdapter(adepter);
				monumentList
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(
									AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
								intent = new Intent(
										MonumentCategory.this,
										MonumentDetail.class);
								intent.putExtra("name",
										(String) placeNameForSorting
												.get(arg2));
								intent.putExtra("image", image);
								startActivity(intent);
							}
						});
				
			} 

		}
	};
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.image_sub_back)
			finish();
		else if (v.getId() == R.id.image_sub_home) {
			Intent inentHome;
			inentHome = new Intent(this, Homepage.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
