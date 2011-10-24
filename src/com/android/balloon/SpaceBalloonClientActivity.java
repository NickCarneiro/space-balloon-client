package com.android.balloon;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Nick Carneiro
 * some location code borrowed from http://hejp.co.uk/android/android-gps-example/
 */
public class SpaceBalloonClientActivity extends Activity implements LocationListener {
	TextView txtInfo;
	static final String tag = "Main"; // for Log

	LocationManager lm;
	StringBuilder sb;
	int noOfFixes = 0;
	String run_id;
    
    
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/*
		 * run id is set when the application is opened and stays the
		 * same for the entire launch
		 */
		Random rn = new Random();
		
		run_id = Integer.toString(rn.nextInt(1000));
		/* get TextView to display the GPS data */
		txtInfo = (TextView) findViewById(R.id.textView1);
		txtInfo.setText("Waiting for location data...");
		
		/* the location manager is the most vital part it allows access
		 * to location and GPS status services */
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	protected void onResume() {
		/*
		 * onResume is is always called after onStart, even if the app hasn't been
		 * paused
		 *
		 * add location listener and request updates every 1000ms or 10m
		 */
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* GPS, as it turns out, consumes battery like crazy */
		lm.removeUpdates(this);
		super.onResume();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v(tag, "Location Changed");

		sb = new StringBuilder(512);

		noOfFixes++;

		/* display some of the data in the TextView */
		LocationPacket packet = new LocationPacket();
		packet.setSpeed(location.getSpeed());
		packet.setAltitude(location.getAltitude());
		packet.setLatitude(location.getLatitude());
		packet.setLongitude(location.getLongitude());
		packet.setAccuracy(location.getAccuracy());
		packet.setTime(location.getTime());
		packet.setRun_id(run_id);
		
		sb.append("No. of Fixes: ");
		sb.append(noOfFixes);
		sb.append('\n');
		sb.append('\n');

		sb.append("Longitude: ");
		sb.append(packet.getLongitude());
		sb.append('\n');

		sb.append("Latitude: ");
		sb.append(packet.getLatitude());
		sb.append('\n');

		sb.append("Altitiude: ");
		sb.append(packet.getAltitude());
		sb.append('\n');
		
		sb.append("Speed: ");
		sb.append(packet.getSpeed());
		sb.append('\n');

		sb.append("Accuracy: ");
		sb.append(packet.getAccuracy());
		sb.append('\n');

		sb.append("Timestamp: ");
		sb.append(packet.getTime());
		sb.append('\n');
		
		//put on screen
		txtInfo.setText(sb.toString());
		
		Gson gson = new Gson();
		String json = gson.toJson(packet);
		//send to server
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://trillworks.com:3011/data");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

			nameValuePairs.add(new BasicNameValuePair("json", json));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			System.out.println(response);

		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		/* this is called if/when the GPS is disabled in settings */
		Log.v(tag, "Disabled");

		/* bring up the GPS settings */
		Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v(tag, "Enabled");
		Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* This is called when the GPS status alters */
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(tag, "Status Changed: Out of Service");
			Toast.makeText(this, "Status Changed: Out of Service",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(tag, "Status Changed: Temporarily Unavailable");
			Toast.makeText(this, "Status Changed: Temporarily Unavailable",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:
			Log.v(tag, "Status Changed: Available");
			Toast.makeText(this, "Status Changed: Available",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onStop() {
		/* may as well just finish since saving the state is not important for this toy app */
		finish();
		super.onStop();
	}
}