package com.android.balloon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import android.R.id;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.Toast;

public class BalloonService extends Service {
	
	

	private static final String tag = "BalloonService";
	private LocationManager lm;
	private LocationListener locationListener;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "Ballon Service Created", Toast.LENGTH_LONG).show();
		Log.d(tag, "Balloon Service Created");
		
	}
	
	


	@Override
	public void onDestroy() {
		//stop getting location updates
		lm.removeUpdates(locationListener);

		Toast.makeText(this, "Balloon Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(tag, "onDestroy");
	
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "BalloonService Started", Toast.LENGTH_LONG).show();
		Log.d(tag, "BallonService Started");
		
		/*
		 * The run id is set when the application is opened and stays the
		 * same for the entire launch
		 */
		Random rand = new Random();
		String run_id = "winston";
		int random_int = rand.nextInt(1000);
		String run_id_string = run_id + random_int;
		
		/* the location manager is the most vital part it allows access
		 * to location and GPS status services */
		locationListener = new BalloonLocationListener(run_id_string);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10, locationListener);

		
	}

	
	
}
	

