package com.girnar.explorejaipur.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.custom.CustomAdapter;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.sqlite.ExploreJaipurDatabase;
import com.girnar.explorejaipur.sqlite.JaipurBusSqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

/**
 * The Class JaipurBus.
 */
public class JaipurBus extends Activity {
	private Tracker tracker;
	private final String TAG = "JaipurBus";
	private int image, busImage;
	private Intent intent;
	private CustomAdapter adepter;
	private List<JaipurBusSqliteData> jaipurBusSqliteDatas;
	private ExploreJaipurDatabase exploreJaipurDatabase;
	private ArrayList<String> jaipurBusId = new ArrayList<String>();
	private ArrayList<String> routeNumber = new ArrayList<String>();
	private ArrayList<String> routeNumber2 = new ArrayList<String>();
	private ArrayList<String> startStand = new ArrayList<String>();
	private ArrayList<String> endStand = new ArrayList<String>();
	private ArrayList<String> routeName = new ArrayList<String>();
	private ImageView buttonHome, buttonBack;
	private ListView busList;
	private TextView buttnFindroute;
	private GoogleAdd googleAdd ;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.jaipur_bus);
		Log.d(TAG, "On create called");
		googleAdd = new GoogleAdd(this);
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Jaipur Bus");
		
		exploreJaipurDatabase = new ExploreJaipurDatabase(this);
		jaipurBusSqliteDatas = exploreJaipurDatabase.retriveJaipurBus();
		for (JaipurBusSqliteData jaipurBus : jaipurBusSqliteDatas) {
			jaipurBusId.add(jaipurBus.getJaipurBusId());
			routeNumber.add(jaipurBus.getRouteNo());
			routeNumber2.add("Route No. " + jaipurBus.getRouteNo());
			startStand.add(jaipurBus.getStartStand());
			endStand.add(jaipurBus.getEndStand());
			routeName.add(jaipurBus.getStartStand() + " to "
					+ jaipurBus.getEndStand());

		}
		busList = (ListView) findViewById(R.id.list_jaipur_bus_root);
		image = R.drawable.bus_black_small;
		busImage = R.drawable.jaipurbus;
		adepter = new CustomAdapter(JaipurBus.this, routeNumber2, routeName,
				image, busImage);
		busList.setAdapter(adepter);
		busList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				intent = new Intent(JaipurBus.this, BusDetails.class);
				intent.putExtra("id", jaipurBusId.get(arg2));
				startActivity(intent);
			}
		});

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
				intent = new Intent(JaipurBus.this, Homepage.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		buttnFindroute = (TextView) findViewById(R.id.text_find_route_jaipur_bus);
		buttnFindroute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(JaipurBus.this, FindRoute.class);
				startActivity(intent);
				intent.putExtra("status","true");

			}
		});
	}

}
