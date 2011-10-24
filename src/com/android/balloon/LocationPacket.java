package com.android.balloon;


public class LocationPacket {
	private String run_id = "0";
	private String device_name = "CM_phone";
	
	//secret is a pseudo password to prevent people from flooding the server with data.
	private String secret = "rip_pimp_c";
	private float speed;
	private double altitude;
	private double latitude;
	private double longitude;
	private double accuracy;
	private long time;
	
	
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setRun_id(String run_id) {
		this.run_id = run_id;
	}
	

}
