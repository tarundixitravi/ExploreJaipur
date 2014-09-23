package com.girnar.explorejaipur.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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
 * The Class Monuments.
 */
public class Monuments extends Activity implements OnClickListener {
	private Tracker tracker;
	private final String TAG = "Monuments";
	private Intent intent;
	private ImageView buttonHome, buttonBack;
	private ImageView buttonHistorical, buttonTample, buttonPicnic, buttonMall,
			buttonCineplex;
	private TextView name;
	private GoogleAdd googleAdd;
	ProgressDialog progressDialog;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.monuments);
		 googleAdd = new GoogleAdd(this);
		googleAdd.addGoogleAds();
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Monuments");
		Log.d(TAG, "On create called");
		name = (TextView) findViewById(R.id.text_sub_headind);
		name.setText("Monuments");

		buttonHome = (ImageView) findViewById(R.id.image_sub_home);
		buttonBack = (ImageView) findViewById(R.id.image_sub_back);
		buttonBack.setOnClickListener(this);
		buttonHome.setOnClickListener(this);

		buttonHistorical = (ImageView) findViewById(R.id.button_historical_monuments);
		buttonTample = (ImageView) findViewById(R.id.button_tamples_monuments);
		buttonPicnic = (ImageView) findViewById(R.id.button_picnic_monuments);
		buttonMall = (ImageView) findViewById(R.id.button_mallls_monuments);
		buttonCineplex = (ImageView) findViewById(R.id.button_cineplex_monuments);
		buttonHistorical.setOnClickListener(this);
		buttonTample.setOnClickListener(this);
		buttonPicnic.setOnClickListener(this);
		buttonMall.setOnClickListener(this);
		buttonCineplex.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.image_sub_back) {
			finish();
		} else if (v.getId() == R.id.image_sub_home) {
			intent = new Intent(this, Homepage.class)
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("status", "true");
			startActivity(intent);
			finish();
		}

		else if (v.getId() == R.id.button_historical_monuments) {
			intent = new Intent(getApplicationContext(), MonumentCategory.class);
			intent.putExtra("name", "historical");
			intent.putExtra("image", R.drawable.historical1);
			startActivity(intent);
		} else if (v.getId() == R.id.button_tamples_monuments) {
			intent = new Intent(getApplicationContext(), MonumentCategory.class);
			intent.putExtra("name", "temple");
			intent.putExtra("image", R.drawable.tample);
			startActivity(intent);
		} else if (v.getId() == R.id.button_picnic_monuments) {
			intent = new Intent(getApplicationContext(), MonumentCategory.class);
			intent.putExtra("name", "picnic");
			intent.putExtra("image", R.drawable.picnic);
			startActivity(intent);
		} else if (v.getId() == R.id.button_mallls_monuments) {
			intent = new Intent(getApplicationContext(), MonumentCategory.class);
			intent.putExtra("name", "shopingmalls");
			intent.putExtra("image", R.drawable.mall);
			startActivity(intent);
		} else if (v.getId() == R.id.button_cineplex_monuments) {
			intent = new Intent(getApplicationContext(), MonumentCategory.class);
			intent.putExtra("name", "cinema");
			intent.putExtra("image", R.drawable.cimema);
			startActivity(intent);
		}

	}

}
