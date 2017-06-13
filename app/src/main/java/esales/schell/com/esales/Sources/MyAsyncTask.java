package esales.schell.com.esales.Sources;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.os.AsyncTask;

import esales.schell.com.esales.MainActivity.ShowMapsActivity;

/**
 * Created by Admin on 13-06-2017.
 */

public class MyAsyncTask extends AsyncTask<Void, Void, Void>  {
    private Context ContextAsync;
    Dialog progress;
    public GPSTracker gpsTracker;
    private String providerAsync;
    double   latAsync=0.0;
    double lonAsync=0.0;
    String thikanaAsync="Scan sms for location";
    String AddressAsync="";

    public MyAsyncTask (Context context , GPSTracker gpsTracker){
        this.ContextAsync = context;
        this.gpsTracker = gpsTracker;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(ContextAsync, "Loading data", "Please wait...");

    }


    @Override
    protected Void doInBackground(Void... arg0) {
       /* // TODO Auto-generated method stub
        locationManagerAsync = (LocationManager) ContextAsync.getSystemService(ContextAsync.LOCATION_SERVICE);


        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
        providerAsync = locationManagerAsync.getBestProvider(criteria, false);


        if (locationManagerAsync.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            providerAsync = LocationManager.GPS_PROVIDER;
        } else if (locationManagerAsync.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            providerAsync = LocationManager.NETWORK_PROVIDER;
            *//*AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("GPS is disabled in the settings!");
            alert.setMessage("It is recomended that you turn on your device's GPS and restart the app so the app can determine your location more accurately!");
            alert.setPositiveButton("OK", null);
            alert.show();*//*
        } else if (locationManagerAsync.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            providerAsync = LocationManager.PASSIVE_PROVIDER;
            //Toast.makeText(ContextAsync, "Switch On Data Connection!!!!", Toast.LENGTH_LONG).show();
        }

        location = locationManagerAsync.getLastKnownLocation(providerAsync);
        // Initialize the location fields
        if (location != null) {
            //  System.out.println("Provider " + provider + " has been selected.");
            latAsync = location.getLatitude();
            lonAsync = location.getLongitude();

        } else {
            //Toast.makeText(ContextAsync, " Locationnot available", Toast.LENGTH_SHORT).show();
        }



        List<Address> addresses = null;
        GeocoderAsync = new Geocoder(ContextAsync, Locale.getDefault());
        try {
            addresses = GeocoderAsync.getFromLocation(latAsync, lonAsync, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getCountryName();
            AddressAsync = Html.fromHtml(
                    address + ", " + city + ",<br>" + country).toString();
        } catch (Exception e) {
            e.printStackTrace();
            AddressAsync = "Refresh for the address";
        }*/

        latAsync = gpsTracker.getLatitude();
        lonAsync = gpsTracker.getLatitude();


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        super.onPostExecute(result);
        progress.dismiss();

        Intent intentAsync = new Intent(ContextAsync,ShowMapsActivity.class);
        intentAsync.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intentAsync.putExtra("calculated_Lat", latAsync);
        intentAsync.putExtra("calculated_Lon", lonAsync);

        ContextAsync.startActivity(intentAsync);

    }



   /* @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        locationManagerAsync.requestLocationUpdates(providerAsync, 0, 0, this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }*/
}