package com.girnar.explorejaipur.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.girnar.explorejaipur.R;
import com.girnar.explorejaipur.google.GoogleAdd;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

/**
 * The Class Utilities.
 */
public class Utilities extends Activity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "Utilities";
	private Intent intent;
	private TextView name;
	private GoogleAdd googleAdd;
	private ImageView buttonHome, buttonBack, buttonHospital, buttonHotel,
			buttonPolice, buttoRailway;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.utilities);
		Log.d(TAG, "On create called");
		googleAdd = new GoogleAdd(this);
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("City Utilities");

		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(this);
		buttonHome.setOnClickListener(this);

		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Utilities");

		buttonHospital = (ImageView) findViewById(R.id.button_hospital_utilities);
		buttonHotel = (ImageView) findViewById(R.id.button_hotel_utilities);
		buttonPolice = (ImageView) findViewById(R.id.button_police_utilities);
		buttoRailway = (ImageView) findViewById(R.id.button_rail_utilities);
		buttonHospital.setOnClickListener(this);
		buttonHotel.setOnClickListener(this);
		buttonPolice.setOnClickListener(this);
		buttoRailway.setOnClickListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.image_sub_back) {
			finish();
		} else if (v.getId() == R.id.image_sub_home) {

			intent = new Intent(this, Homepage.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("status","true");
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.button_hospital_utilities) {
			intent = new Intent(getApplicationContext(), UtilityCategory.class);
			intent.putExtra("name", "hospital");
			intent.putExtra("image", R.drawable.icon_hospital);
			startActivity(intent);
		}

		else if (v.getId() == R.id.button_hotel_utilities) {
			intent = new Intent(getApplicationContext(), UtilityCategory.class);
			intent.putExtra("name", "hotel");
			intent.putExtra("image", R.drawable.icon_hotel);
			startActivity(intent);
		}

		else if (v.getId() == R.id.button_police_utilities) {
			intent = new Intent(getApplicationContext(), UtilityCategory.class);
			intent.putExtra("name", "police");
			intent.putExtra("image", R.drawable.icon_police);
			startActivity(intent);
		} else if (v.getId() == R.id.button_rail_utilities) {
			intent = new Intent(getApplicationContext(), UtilityCategory.class);
			intent.putExtra("name", "rail");
			intent.putExtra("image", R.drawable.icon_train);
			startActivity(intent);
		}

	}

}