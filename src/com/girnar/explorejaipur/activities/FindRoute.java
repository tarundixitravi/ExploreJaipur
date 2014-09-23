package com.girnar.explorejaipur.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.custom.CustomAdapter;
import com.girnar.explorejaipur.custom.CustomSpinner;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.sqlite.ExploreJaipurDatabase;
import com.girnar.explorejaipur.sqlite.JaipurBusSqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

/**
 * The Class FindRoute.
 */
public class FindRoute extends Activity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "FindRoute";
	private Spinner spinnerFrom;
	private TextView name;
	private ArrayList<String> routeNumber = new ArrayList<String>();
	private ArrayList<String> startStand = new ArrayList<String>();
	private ArrayList<String> endStand = new ArrayList<String>();
	private ArrayList<String> intermediateStand = new ArrayList<String>();
	private ArrayList<String> stands = new ArrayList<String>();
	private ArrayList<String> jaipurBusId = new ArrayList<String>();
	private ArrayList<String> routeFind = new ArrayList<String>();
	private ArrayList<String> routeFindName = new ArrayList<String>();
	private ArrayList<Integer> routeFindIndex = new ArrayList<Integer>();
	private String[][] sta;
	private ImageView buttonHome, buttonBack;
	private Intent intent;
	private Button buttnFindRoute;
	private String[] stand;
	private GoogleAdd googleAdd;
	private ListView busList;
	private List<JaipurBusSqliteData> jaipurBusSqliteDatas;
	ExploreJaipurDatabase exploreJaipurDatabase;

	CustomAdapter adepter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_route);
		Log.d(TAG, "On create called");
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("Find Your Bus");
		 googleAdd = new GoogleAdd(this);
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Find Route");

		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(this);
		buttonHome.setOnClickListener(this);

		exploreJaipurDatabase = new ExploreJaipurDatabase(this);
		jaipurBusSqliteDatas = exploreJaipurDatabase.retriveJaipurBus();
		Set<String> s = new HashSet<String>();
		int j = 0;
		sta = new String[jaipurBusSqliteDatas.size()][];
		for (JaipurBusSqliteData jaipurBus : jaipurBusSqliteDatas) {
			jaipurBusId.add(jaipurBus.getJaipurBusId());
			routeNumber.add(jaipurBus.getRouteNo());
			startStand.add(jaipurBus.getStartStand());
			endStand.add(jaipurBus.getEndStand());
			intermediateStand.add(jaipurBus.getIntermediateStands());
			stand = ((jaipurBus.getIntermediateStands()).split(","));
			int l = stand.length;
			sta[j] = new String[l];
			for (int i = 0; i < l; i++) {
				s.add(stand[i]);
				sta[j][i] = stand[i];
			}
			j++;
		}
		stands = new ArrayList<String>(s);
		stands.add(" ");
		Collections.sort(stands);
		spinnerFrom = (Spinner) findViewById(R.id.spinner_from_find_route);
		buttnFindRoute = (Button) findViewById(R.id.button_find_find_route);
		busList = (ListView) findViewById(R.id.list_result_find_route);
		CustomSpinner arrayAdapter = new CustomSpinner(FindRoute.this, stands);
		spinnerFrom.setAdapter(arrayAdapter);
		spinnerFrom.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		buttnFindRoute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				routeFind.clear();
				routeFindIndex.clear();
				routeFindName.clear();
				String from = spinnerFrom.getSelectedItem().toString();

				for (int i = 0; i < jaipurBusSqliteDatas.size(); i++) {
					int l = sta[i].length;
					Boolean fromMatched = false;
					for (int j = 0; j < l; j++) {
						if (sta[i][j].equalsIgnoreCase(from))
							fromMatched = true;
					}
					if (fromMatched) {
						
						routeFindIndex.add(i);
						routeFind.add(routeNumber.get(i));
						routeFindName.add(startStand.get(i) + " to "
								+ endStand.get(i));

					}
				}
				if (routeFind.size() == 0)
					Toast.makeText(FindRoute.this,
							"No bus avilabale for this route",
							Toast.LENGTH_LONG).show();
				adepter = new CustomAdapter(FindRoute.this, routeFind,
						routeFindName, R.drawable.bus_black_small,
						R.drawable.jaipurbus);
				busList.setAdapter(adepter);
			}
		});
		busList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				intent = new Intent(FindRoute.this, BusDetails.class);
				intent.putExtra("id", jaipurBusId.get(routeFindIndex.get(arg2)));
				startActivity(intent);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.image_sub_back)
			finish();
		else if (v.getId() == R.id.image_sub_home) {
			intent = new Intent(this, Homepage.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("status","true");
			startActivity(intent);
			finish();
		}
	}

}