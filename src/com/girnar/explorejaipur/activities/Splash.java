package com.girnar.explorejaipur.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.girnar.explorejaipur.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

@SuppressWarnings("javadoc")
public class Splash extends Activity {

	private int _splashTime = 3000;
	private Tracker tracker;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		EasyTracker.getInstance().setContext(getApplicationContext());
		tracker = EasyTracker.getTracker();
		tracker.trackView("Splash");
		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					long lastTime = System.currentTimeMillis();
					long currTime = System.currentTimeMillis();
					long diff = (currTime - lastTime);
					if (diff < _splashTime) {
						sleep(_splashTime - diff);

					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					startActivity(new Intent(Splash.this, Homepage.class));
					finish();
					// stop();
				}
			}
		};
		splashTread.start();

	}

}
