package com.android.balloon;



import java.util.Random;




import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author Nick Carneiro
 * some location code borrowed from http://hejp.co.uk/android/android-gps-example/
 */
public class SpaceBalloonClientActivity extends Activity implements OnClickListener {
	Button buttonStart, buttonStop;
	TextView txtInfo;
	private String device_name = "Nick's Phone";
	static final String tag = "SpaceBalloonClientActivity"; // for Log

	StringBuilder sb;
	int noOfFixes = 0;
    
    
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		buttonStart = (Button) findViewById(R.id.buttonStart);
	    buttonStop = (Button) findViewById(R.id.buttonStop);

	    buttonStart.setOnClickListener(this);
	    buttonStop.setOnClickListener(this);
		
		/* get TextView to display the GPS data */
		
		
	}
	
	public void onClick(View src) {
	    switch (src.getId()) {
	    case R.id.buttonStart:
	      Log.d(tag, "onClick: starting BalloonService");
	      startService(new Intent(this, BalloonService.class));
	      break;
	    case R.id.buttonStop:
	      Log.d(tag, "onClick: stopping BalloonService");
	      stopService(new Intent(this, BalloonService.class));
	      break;
	    }
	  }
	
	@Override
	protected void onResume() {
		/*
		 * onResume is is always called after onStart, even if the app hasn't been
		 * paused
		 *
		 * add location listener and request updates every 1000ms or 10m
		 */
		Log.d(tag, "onResume event fired");
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* GPS, as it turns out, consumes battery like crazy */
		//lm.removeUpdates(this);
		Log.d(tag, "onPause event fired");
		super.onResume();
	}

	

	@Override
	protected void onStop() {
		/* may as well just finish since saving the state is not important for this toy app */
		Log.d(tag, "onStop event fired");
		finish();
		super.onStop();
	}
}