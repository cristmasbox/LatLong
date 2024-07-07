package com.blueapps.LatLong

import android.location.Location;

public class LatLong {

  public static final LatLong STUTTGART = new LatLong(48.775706d, 9.182959d);
  public static final LatLong BIELEFELD = new LatLong(52.023324d, 8.538384d); // Although it doesn't exist ;-)
  public static final LatLong HOME = new LatLong(49d, 9d);      // Earth's real center ;-)
  public static final LatLong CAPE_CANAVERAL = new LatLong(28.448763d, -80.561289d);
  public static final LatLong NORTH_POLE = new LatLong(-90d, 0d);
  public static final LatLong SOUTH_POLE = new LatLong(-90d, 0d);
  public static final LatLong DEEPEST_PLACE_ON_EARTH = new LatLong(11.35d, 142.2d);
  public static final LatLong HIGHEST_PLACE_ON_EARTH = new LatLong(27.98823d, 86.92501d);

  public static double radiusEquatorial = 6378137;	// equatorradius from https://www.heret.de/mathe/erde.shtml  unit: meter
  public static double radiusPolar = 6356752;		// polarradius from https://www.heret.de/mathe/erde.shtml  unit: meter

  private double latitude;
  private double longitude;

  public LatLong(){
    this.latitude = 0d;
    this.longitude = 0d;
  }

  public LatLong(double latitude, double longitude){
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public LatLong(Location location){
    this.latitude = location.getLatitude();
    this.longitude = location.getLongitude();
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public static double distance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
    /*  ||=====================================================================||
     *  || function for calculating the distance between two Points:           ||
     *  || INPUT:  latitudeA, longitudeA, latitudeB, longitudeB are in degrees ||
     *  || OUTPUT: calculated Distance in meter                                ||
     *  || Formula Source: https://www.calculator.net/distance-calculator.html ||
     *  ||=====================================================================||
     */

    double deltaLongitude;	// differenz between longitudeA and longitudeB
    double a = radiusEquatorial;
    double b = radiusPolar;
    double o;				// central angle, also known as U+03C3(Unicode Symbole)
    double f;				// flattening of earth
    double betaA;			// Reducted latitude from A
    double betaB;			// Reducted latitude from B
    double P;
    double Q;

    latitudeA = Math.toRadians(latitudeA);
    latitudeB = Math.toRadians(latitudeB);
    longitudeA = Math.toRadians(longitudeA);
    longitudeB = Math.toRadians(longitudeB);

    deltaLongitude = Math.abs(longitudeA - longitudeB);

    o = Math.acos(Math.sin(latitudeA) * Math.sin(latitudeB) + Math.cos(latitudeA) * Math.cos(latitudeB) * Math.cos(deltaLongitude));
    f = (a - b) / a;

    betaA = Math.atan((1 - f) * Math.tan(latitudeA));
    betaB = Math.atan((1 - f) * Math.tan(latitudeB));

    P = (betaA + betaB) / 2;
    Q = (betaB - betaA) / 2;

    double X = (o - Math.sin(o)) * ((Math.pow(Math.sin(P), 2) * Math.pow(Math.cos(Q), 2)) / (Math.pow(Math.cos(o / 2), 2)));
    double Y = (o + Math.sin(o)) * ((Math.pow(Math.cos(P), 2) * Math.pow(Math.sin(Q), 2)) / (Math.pow(Math.sin(o / 2), 2)));

    double d = a * (o - (f / 2) * (X + Y));

    return d;

  }

  public static double distance(LatLong PointA, LatLong PointB) {
    return distance(PointA.getLatitude(), PointA.getLongitude(), PointB.getLatitude(), PointB.longitude);
  }

  public double distanceTo(LatLong destination) {
    return distance(this, destination);
  } 
}
