package com.girnar.explorejaipur.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.sqlite.ExploreJaipurDatabase;
import com.girnar.explorejaipur.sqlite.JaipurBusSqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

/**
 * The Class BusDetails.
 */
public class BusDetails extends Activity {
	private Tracker tracker;
	private final String TAG = "BusDetails";
	private JaipurBusSqliteData searchObject;
	private ExploreJaipurDatabase exploreJaipurDatabase;
	private List<JaipurBusSqliteData> jaipurBusSqliteDatas;
	private ImageView buttonHome, buttonBack, busColor;
	private Intent intent;
	private TextView name, route, busFrom, busTo, busType, intermediateStation;
	private String id, intermediateArray;
	private String routeNumber, startStand, endStand, intermediateStand, color,
			type;
	private GoogleAdd googleAdd;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_details);
		Log.d(TAG, "On create called");
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("Route Details");

		id = getIntent().getExtras().getString("id");
		googleAdd = new GoogleAdd(this);
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Bus Details");

		searchObject = new JaipurBusSqliteData();
		searchObject.setJaipurBusId(id);
		exploreJaipurDatabase = new ExploreJaipurDatabase(this);
		jaipurBusSqliteDatas = exploreJaipurDatabase
				.retriveSelectedJaipurBus(searchObject);

		for (JaipurBusSqliteData jaipurBus : jaipurBusSqliteDatas) {

			routeNumber = jaipurBus.getRouteNo();
			startStand = jaipurBus.getStartStand();
			endStand = jaipurBus.getEndStand();
			intermediateStand = jaipurBus.getIntermediateStands();
		
			type = jaipurBus.getRadialCircular();
			color = jaipurBus.getColor();
		}
		route = (TextView) findViewById(R.id.text_rout_bus_details);
		route.setText("Rout no." + routeNumber);
		busFrom = (TextView) findViewById(R.id.text_from_bus_details);
		busFrom.setText(startStand);
		busTo = (TextView) findViewById(R.id.text_to_bus_details);
		busTo.setText(endStand);
		busType = (TextView) findViewById(R.id.text_type_bus_details);
		busType.setText(type);
		intermediateArray = intermediateStand.replace(",", "\n    - ");
		intermediateArray = "    - " + intermediateArray;
		intermediateStation = (TextView) findViewById(R.id.list_inte_station);
		intermediateStation.setText(intermediateArray);
		busColor = (ImageView) findViewById(R.id.color_stations_bus_details);
		busColor.setBackgroundColor(Color.parseColor("#" + color));

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
				intent.putExtra("status","true");
				startActivity(intent);
				finish();
			}
		});

	}

}
