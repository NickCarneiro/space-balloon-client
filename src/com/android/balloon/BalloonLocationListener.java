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

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class BalloonLocationListener implements LocationListener {
	static final String tag = "BalloonLocationListener"; // for Log
	StringBuilder sb;
	int noOfFixes = 0;
	//server checks this value to make sure we aren't getting fake requests from ne'erdowells.
	private String secret = "rip_pimp_c";

	BalloonLocationListener(String run_id){
		this.run_id = run_id;
	}
	//supposed to be a generated value to allow tracking of multiple balloons.
	private String run_id = "winston";
	
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
		packet.setSecret(secret);
		
		packet.setRun_id(run_id);
		
		Gson gson = new Gson();
		String json = gson.toJson(packet);
		Log.v(tag, "Sending this JSON: " + json);
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
		Log.v(tag, "GPS is disabled. Turn it back on.");

		/* bring up the GPS settings */
		Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		//startActivity(intent);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v(tag, "Provider Enabled");

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* This is called when the GPS status alters */
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(tag, "Status Changed: Out of Service");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(tag, "Status Changed: Temporarily Unavailable");
			break;
		case LocationProvider.AVAILABLE:
			Log.v(tag, "Status Changed: Available");
			break;
		}
	}

}
