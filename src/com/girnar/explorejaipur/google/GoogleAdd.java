package com.girnar.explorejaipur.google;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.girnar.explorejaipur.R;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * The Class GoogleAdd.
 */
public class GoogleAdd {

	Activity context;

	/**
	 * Instantiates a new google add.
	 *
	 * @param context the context
	 */
	public GoogleAdd(Activity context) {
		this.context = context;
	}

	/**
	 * Adds the google ads.
	 */
	public void addGoogleAds() {
		AdView adView = new AdView(context, AdSize.SMART_BANNER, context
				.getResources().getString(R.string.google_ads_id));
		LinearLayout layout = (LinearLayout) context
				.findViewById(R.id.googleads);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest();
		final LinearLayout pBar = (LinearLayout) context
				.findViewById(R.id.gooleaddload);
		pBar.setVisibility(View.VISIBLE);
		// adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adView.setAdListener(new AdListener() {

			@Override
			public void onReceiveAd(Ad arg0) {
				pBar.setVisibility(View.GONE);
			}

			@Override
			public void onPresentScreen(Ad arg0) {
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
			}

			/*
			 * @Override public void onFailedToReceiveAd(Ad arg0, ErrorCode
			 * arg1) { }
			 */
			@Override
			public void onDismissScreen(Ad arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub

			}
		});
		adView.loadAd(adRequest);

	}
}
