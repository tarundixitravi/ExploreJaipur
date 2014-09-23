package com.girnar.explorejaipur.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.constants.Constants;
import com.girnar.explorejaipur.custom.CustomSpinner;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.girnar.explorejaipur.jsondata.DialogShow;
import com.girnar.explorejaipur.jsondata.JsonDataJaipurBus;
import com.girnar.explorejaipur.jsondata.JsonDataMonument;
import com.girnar.explorejaipur.jsondata.JsonDataUtility;
import com.girnar.explorejaipur.jsondata.JsonParser;
import com.girnar.explorejaipur.jsondata.MyException;
import com.girnar.explorejaipur.jsonflag.JsonFlagExploreJaipur;
import com.girnar.explorejaipur.network.NetworkTracker;
import com.girnar.explorejaipur.sqlite.ExploreJaipurDatabase;
import com.girnar.explorejaipur.sqlite.ExploreJaipurFlagSqliteData;
import com.girnar.explorejaipur.sqlite.JaipurBusSqliteData;
import com.girnar.explorejaipur.sqlite.MonumentsSqliteData;
import com.girnar.explorejaipur.sqlite.UtilitySqliteData;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

/**
 * The Class Homepage.
 */
public class Homepage extends Activity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "Homepage";

	private ImageView buttonCityMap, buttonUtility, buttonBus, buttonMonunent,
			searchButton;
	private ArrayList<String> name = new ArrayList<String>();
	private ArrayList<String> id = new ArrayList<String>();
	private AutoCompleteTextView autoCompleteTextView;
	private NetworkTracker networkTracker;
	private GoogleAdd googleAdd;
	private JsonDataJaipurBus jsonObjectJaipurBus;
	private JsonDataUtility jsonObjectUtility;
	private JsonDataMonument jsonObjectMonument;
	private JsonFlagExploreJaipur jsonObjectExploreJaipur;

	private TextView heading;
	private ProgressDialog progressDialog, dialog;
	private ExploreJaipurDatabase exploreJaipurDatabase;
	private List<ExploreJaipurFlagSqliteData> exploreJaipurFlagSqliteData;
	private boolean flagMonumentCheck = false, flagUtilityCheck = false,
			flagJaipurBusCheck = false;
	private boolean searchOpen = false;

	SharedPreferences sharedPreferences;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		Log.d(TAG, "On create called");
		networkTracker = new NetworkTracker(this);
		googleAdd = new GoogleAdd(this);
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Home Page");

		exploreJaipurDatabase = new ExploreJaipurDatabase(this);
		exploreJaipurFlagSqliteData = exploreJaipurDatabase
				.retriveExploreJaipurFlag();

		dialog = new ProgressDialog(this);

		buttonCityMap = (ImageView) findViewById(R.id.button_home_page_map);
		buttonBus = (ImageView) findViewById(R.id.button_home_page_bus);
		buttonUtility = (ImageView) findViewById(R.id.button_home_page_utility);
		buttonMonunent = (ImageView) findViewById(R.id.button_home_page_monument);
		buttonBus.setOnClickListener(this);
		buttonMonunent.setOnClickListener(this);
		buttonCityMap.setOnClickListener(this);
		buttonUtility.setOnClickListener(this);
		searchButton = (ImageView) findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		heading = (TextView) findViewById(R.id.text_heading_header);

		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete_header);
		autoCompleteTextView.setThreshold(1);
		autoCompleteTextView.setLines(1);

		autoCompleteTextView.setVisibility(View.GONE);
		try {
			taskAfterUpdate();
			getIntent().getExtras().getString("status").equals(null);
		} catch (Exception e) {
			if (networkTracker.isNetworkAvailable()) {
				new MyTaskExploreJaipurFlag().execute();
			} else {
				networkTracker.showDialog();
			}
		}
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				{
					try {
						String Serachid = id.get(name
								.indexOf(autoCompleteTextView.getText()
										.toString()));
						if ((Serachid).startsWith("vp")) {
							Intent intent = new Intent(Homepage.this,
									MonumentDetail.class);
							intent.putExtra("name", autoCompleteTextView
									.getText().toString());
							if (Serachid.startsWith("vph"))
								intent.putExtra("image", R.drawable.historical1);
							else if (Serachid.startsWith("vpc"))
								intent.putExtra("image", R.drawable.cimema);
							else if (Serachid.startsWith("vpt"))
								intent.putExtra("image", R.drawable.tample);
							else if (Serachid.startsWith("vpp"))
								intent.putExtra("image", R.drawable.picnic);
							else if (Serachid.startsWith("vps"))
								intent.putExtra("image", R.drawable.mall);
							startActivity(intent);
						} else if (Serachid.startsWith("ut")) {
							Intent intent = new Intent(Homepage.this,
									UtilityDetail.class);
							intent.putExtra("name", autoCompleteTextView
									.getText().toString());

							if (Serachid.startsWith("utp"))
								intent.putExtra("image", R.drawable.icon_police);
							else if (Serachid.startsWith("utr"))
								intent.putExtra("image", R.drawable.icon_train);
							else if (Serachid.startsWith("uth"))
								intent.putExtra("image", R.drawable.icon_hotel);
							else if (Serachid.startsWith("uto"))
								intent.putExtra("image",
										R.drawable.icon_hospital);

							startActivity(intent);
						}
						autoCompleteTextView.setText("");
						autoCompleteTextView.setVisibility(View.GONE);
						searchButton.setImageResource(R.drawable.search);
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								autoCompleteTextView.getWindowToken(), 0);

						heading.setVisibility(View.VISIBLE);
						searchOpen = false;

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Sorry! No such item found", Toast.LENGTH_LONG)
								.show();
					}
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_home_page_bus) {
			Intent in = new Intent(getApplicationContext(), JaipurBus.class);
			startActivity(in);
		} else if (v.getId() == R.id.button_home_page_map) {
			Intent intent = new Intent(Homepage.this, CityMap.class);
			startActivity(intent);
		} else if (v.getId() == R.id.button_home_page_monument) {
			Intent intent = new Intent(getApplicationContext(), Monuments.class);
			startActivity(intent);
		} else if (v.getId() == R.id.button_home_page_utility) {
			Intent intent = new Intent(getApplicationContext(), Utilities.class);
			startActivity(intent);
		} else if (v.getId() == R.id.search_button) {
			if (!searchOpen) {
				autoCompleteTextView.setVisibility(View.VISIBLE);
				searchButton.setImageResource(R.drawable.cancel_button);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(autoCompleteTextView,
						InputMethodManager.SHOW_IMPLICIT);
				heading.setVisibility(View.GONE);
				searchOpen = true;
			} else if (searchOpen) {
				autoCompleteTextView.setVisibility(View.GONE);
				searchButton.setImageResource(R.drawable.search);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						autoCompleteTextView.getWindowToken(), 0);
				heading.setVisibility(View.VISIBLE);
				autoCompleteTextView.setText("");
				searchOpen = false;

			}
		}
	}

	/**
	 * This method starts the AsynkTask for get Data online.
	 */

	private class MyTaskExploreJaipurFlag extends
			AsyncTask<String, String, String> {
		String output = null;
		ObjectMapper mapper = new ObjectMapper();

		public MyTaskExploreJaipurFlag() {
		}

		@Override
		protected void onPreExecute() {
			Homepage.this.progressDialog = ProgressDialog.show(Homepage.this,
					"", "Please wait, while retrieving data...", true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				try {
					output = new JsonParser()
							.getJSONFromUrl(Constants.API_URL_FLAG_EXPLORE_JAIPUR);
				} catch (MyException e) {
					e.printStackTrace();
				}

				if (output != null)
					jsonObjectExploreJaipur = mapper.readValue(output,
							JsonFlagExploreJaipur.class);

			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (output != null) {
				try {
					flagJaipurBusCheck = !(jsonObjectExploreJaipur.getData()
							.getUpdateTableStatus().get(0).getFlag())
							.equals(exploreJaipurFlagSqliteData.get(0)
									.getFlag());
					flagMonumentCheck = !(jsonObjectExploreJaipur.getData()
							.getUpdateTableStatus().get(1).getFlag())
							.equals(exploreJaipurFlagSqliteData.get(1)
									.getFlag());
					flagUtilityCheck = !(jsonObjectExploreJaipur.getData()
							.getUpdateTableStatus().get(2).getFlag())
							.equals(exploreJaipurFlagSqliteData.get(2)
									.getFlag());

				} catch (Exception e) {
					flagJaipurBusCheck = true;
					flagMonumentCheck = true;
					flagUtilityCheck = true;
				}
				getData();
			} else {
				dataNotReceived();
				networkTracker.internetNotActive();
			}
		}
	}

	public void getData() {

		if (flagJaipurBusCheck) {
			new MyTaskJaipurBus().execute();
		} else if (flagMonumentCheck) {
			new MyTaskMomument().execute();
		} else if (flagUtilityCheck) {
			new MyTaskUtility().execute();
		} else {
			taskAfterUpdate();
		}

		if (flagJaipurBusCheck || flagMonumentCheck || flagUtilityCheck) {
			updateExploreJaipurFlagTable();
		}
	}

	private class MyTaskJaipurBus extends AsyncTask<String, String, String> {
		String output = null;
		ObjectMapper mapper = new ObjectMapper();

		public MyTaskJaipurBus() {
		}

		@Override
		protected void onPreExecute() {
			DialogShow.showProgressDialog(dialog,
					"Please wait, while retrieving data...");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				output = new JsonParser()
						.getJSONFromUrl(Constants.API_URL_JAIPUR_BUS);
				if (output != null)
					jsonObjectJaipurBus = mapper.readValue(output,
							JsonDataJaipurBus.class);
			} catch (MyException e) {
				e.printStackTrace();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			DialogShow.closeProgressDialog(dialog);
			if (output != null) {
				if (flagMonumentCheck) {
					new MyTaskMomument().execute();
				} else if (flagUtilityCheck) {
					new MyTaskUtility().execute();
				} else {
					taskAfterUpdate();
				}
				updateJaipuBusTable();
			} else {
				dataNotReceived();
				networkTracker.internetNotActive();
			}

		}
	}

	private class MyTaskMomument extends AsyncTask<String, String, String> {
		String output = null;
		ObjectMapper mapper = new ObjectMapper();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DialogShow.showProgressDialog(dialog,
					"Please wait, while retrieving data...");
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				output = new JsonParser()
						.getJSONFromUrl(Constants.API_URL_MONUMENT);
				if (output != null)
					jsonObjectMonument = mapper.readValue(output,
							JsonDataMonument.class);
			} catch (MyException e) {
				e.printStackTrace();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			DialogShow.closeProgressDialog(dialog);
			if (output != null) {
				if (flagUtilityCheck) {
					new MyTaskUtility().execute();
				} else {
					taskAfterUpdate();
				}
				updateMonumentTable();
			} else {
				dataNotReceived();
				networkTracker.internetNotActive();
			}
		}
	}

	private class MyTaskUtility extends AsyncTask<String, String, String> {
		String output = null;
		ObjectMapper mapper = new ObjectMapper();

		public MyTaskUtility() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			DialogShow.showProgressDialog(dialog,
					"Please wait, while retrieving data...");
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				output = new JsonParser()
						.getJSONFromUrl(Constants.API_URL_UTILITY);
				if (output != null)
					jsonObjectUtility = mapper.readValue(output,
							JsonDataUtility.class);
			} catch (MyException e) {
				e.printStackTrace();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			DialogShow.closeProgressDialog(dialog);
			if (output != null) {
				updateUtilityTable();
				taskAfterUpdate();
			} else {
				dataNotReceived();
				networkTracker.internetNotActive();
			}
		}
	}

	private void updateExploreJaipurFlagTable() {
		exploreJaipurDatabase.deleteExploreJaipurFlagdata();
		for (int i = 0; i < jsonObjectExploreJaipur.getData()
				.getUpdateTableStatus().size(); i++) {
			ExploreJaipurFlagSqliteData exploreJaipurFlagSqliteData = new ExploreJaipurFlagSqliteData();
			exploreJaipurFlagSqliteData.setId(jsonObjectExploreJaipur.getData()
					.getUpdateTableStatus().get(i).getId());
			exploreJaipurFlagSqliteData.setTableName(jsonObjectExploreJaipur
					.getData().getUpdateTableStatus().get(i).getTable_name());
			exploreJaipurFlagSqliteData.setFlag(jsonObjectExploreJaipur
					.getData().getUpdateTableStatus().get(i).getFlag());
			exploreJaipurDatabase
					.insertExploreJaipurFlag(exploreJaipurFlagSqliteData);

		}
	}

	private void updateJaipuBusTable() {
		exploreJaipurDatabase.deleteJaipurBusdata();
		for (int i = 0; i < jsonObjectJaipurBus.getData().getJaipurBus().size(); i++) {
			JaipurBusSqliteData jaipurBusSqliteData = new JaipurBusSqliteData();
			jaipurBusSqliteData.setJaipurBusId(jsonObjectJaipurBus.getData()
					.getJaipurBus().get(i).getJaipurBusId());
			jaipurBusSqliteData.setRouteNo(jsonObjectJaipurBus.getData()
					.getJaipurBus().get(i).getRouteNo());
			jaipurBusSqliteData.setStartStand(jsonObjectJaipurBus.getData()
					.getJaipurBus().get(i).getStartStand());
			jaipurBusSqliteData.setEndStand(jsonObjectJaipurBus.getData()
					.getJaipurBus().get(i).getEndStand());
			jaipurBusSqliteData.setIntermediateStands(jsonObjectJaipurBus
					.getData().getJaipurBus().get(i).getIntermidiate());
			jaipurBusSqliteData.setColor(jsonObjectJaipurBus.getData()
					.getJaipurBus().get(i).getColor());
			jaipurBusSqliteData.setRadialCircular(jsonObjectJaipurBus.getData()
					.getJaipurBus().get(i).getRadialCircular());
			exploreJaipurDatabase.insertJaipurBus(jaipurBusSqliteData);
		}
	}

	private void updateMonumentTable() {
		exploreJaipurDatabase.deleteMonumentdata();
		for (int i = 0; i < jsonObjectMonument.getData().getMonument().size(); i++) {
			MonumentsSqliteData monumentsSqliteData = new MonumentsSqliteData();
			monumentsSqliteData.setMonumentId(jsonObjectMonument.getData()
					.getMonument().get(i).getMonumentId());
			monumentsSqliteData.setName(jsonObjectMonument.getData()
					.getMonument().get(i).getName());
			monumentsSqliteData.setCategory(jsonObjectMonument.getData()
					.getMonument().get(i).getCategory());
			monumentsSqliteData.setLocation(jsonObjectMonument.getData()
					.getMonument().get(i).getLocation());
			monumentsSqliteData.setLatitude(jsonObjectMonument.getData()
					.getMonument().get(i).getLatitude());
			monumentsSqliteData.setLongitude(jsonObjectMonument.getData()
					.getMonument().get(i).getLongitude());
			monumentsSqliteData.setDescription(jsonObjectMonument.getData()
					.getMonument().get(i).getDescription());
			monumentsSqliteData.setDistance(jsonObjectMonument.getData()
					.getMonument().get(i).getDistance());
			monumentsSqliteData.setDistanceBusStand(jsonObjectMonument
					.getData().getMonument().get(i).getDistanceBusStand());
			exploreJaipurDatabase.insertMonument(monumentsSqliteData);

		}

	}

	private void updateUtilityTable() {
		exploreJaipurDatabase.deleteUtilitydata();
		for (int i = 0; i < jsonObjectUtility.getData().getUtility().size(); i++) {
			UtilitySqliteData utilitySqliteData = new UtilitySqliteData();
			utilitySqliteData.setUtilityId(jsonObjectUtility.getData()
					.getUtility().get(i).getUtilityId());
			utilitySqliteData.setName(jsonObjectUtility.getData().getUtility()
					.get(i).getName());
			utilitySqliteData.setCategory(jsonObjectUtility.getData()
					.getUtility().get(i).getCategory());
			utilitySqliteData.setLocation(jsonObjectUtility.getData()
					.getUtility().get(i).getLocation());
			utilitySqliteData.setLatitude(jsonObjectUtility.getData()
					.getUtility().get(i).getLatitude());
			utilitySqliteData.setLongitude(jsonObjectUtility.getData()
					.getUtility().get(i).getLongitude());
			utilitySqliteData.setDescription(jsonObjectUtility.getData()
					.getUtility().get(i).getDescription());
			utilitySqliteData.setDistance(jsonObjectUtility.getData()
					.getUtility().get(i).getDistance());
			utilitySqliteData.setDistanceBusStand(jsonObjectUtility.getData()
					.getUtility().get(i).getDistanceBusStand());
			exploreJaipurDatabase.insertUtility(utilitySqliteData);

		}
	}

	private void taskAfterUpdate() {
		ExploreJaipurDatabase exploreJaipurDatabase = new ExploreJaipurDatabase(
				Homepage.this);
		List<MonumentsSqliteData> monumentsSqliteDatas = exploreJaipurDatabase
				.retriveMonument();
		List<UtilitySqliteData> utilitySqliteDatas = exploreJaipurDatabase
				.retriveUtility();
		name.clear();
		for (MonumentsSqliteData monument : monumentsSqliteDatas) {
			name.add(monument.getName());
			id.add(monument.getMonumentId());
		}
		for (UtilitySqliteData utility : utilitySqliteDatas) {
			name.add(utility.getName());
			id.add(utility.getUtilityId());
		}

		CustomSpinner adapter = new CustomSpinner(Homepage.this, name);

		autoCompleteTextView.setAdapter(adapter);

	}
	private void dataNotReceived(){
		exploreJaipurDatabase.deleteExploreJaipurFlagdata();

	}

}