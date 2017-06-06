package esales.schell.com.esales.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import esales.schell.com.esales.Adapter.CustomerListAdapter;
import esales.schell.com.esales.Interface.CustomerNameInterface;
import esales.schell.com.esales.Interface.RetrofitMaps;
import esales.schell.com.esales.Model.MapRouteModel.Example;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.GPSTracker;
import esales.schell.com.esales.Sources.RecyclerItemClickListener;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ShowMapsActivity extends FragmentActivity implements OnMapReadyCallback,CustomerNameInterface {

    private GoogleMap mMap;
    public double lat,log;
    public Spinner custSpiiner;
    public LinearLayout custome_Toolbar;
    public ImageView backBtn;
    public ArrayList<String> custNameList = new ArrayList<>();
    public PopupWindow popupWindow;
    public RecyclerView recyelerCustomerList;
    public CustomerListAdapter adapter;
    public Button rechedBtn;
    public SearchView serchTxt;
    public Context context;
    public boolean flag = true;
    public GPSTracker gpsTracker;
    public String sourceLat="";
    public String sourceLog="";

    public LatLng origin;
    public LatLng dest;
    public LatLng point;
    public ArrayList<LatLng> MarkerPoints;
    public Polyline line;
    public String ShowDistanceDuration = "";
    public MarkerOptions options;
    public Button dayEndBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = ShowMapsActivity.this;
        MarkerPoints = new ArrayList<>();
        gpsTracker = new GPSTracker(context,ShowMapsActivity.this);
        //custome_Toolbar = (LinearLayout)findViewById(R.id.custome_bar);
       // backBtn = (ImageView)custome_Toolbar.findViewById(R.id.backbtn);
        rechedBtn = (Button)findViewById(R.id.rechecdbtn);
        dayEndBtn = (Button)findViewById(R.id.day_endBtn);

        dayEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(ShowMapsActivity.this,
                        "1")));
            }
        });


        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        }
        else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }


        sourceLat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
        sourceLog =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));



       // custSpiiner = (Spinner)custome_Toolbar.findViewById(R.id.customer_name_spinner);

       /* backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        Intent i = getIntent();
        if (i!=null)
        {
            lat = i.getDoubleExtra("lat",-34);
            log = i.getDoubleExtra("log",151);
        }


       /* rechedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sourceLat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
                sourceLog =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));


                    if (!rechedBtn.getText().toString().equalsIgnoreCase("Reached")) {

                        mMap.clear();
                        MarkerPoints.clear();
                        MarkerPoints = new ArrayList<>();



                    }else
                        {

                        }

            }
        });*/

        //spinner work

     /*   if (custNameList.size()>0)
        {
            custNameList.clear();
        }



        //change spinner arrow color

        custSpiiner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> updaetTypeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.customizespinner,
                custNameList);
        updaetTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        custSpiiner.setAdapter(updaetTypeAdapter);*/

    }


    private void callPopup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.popup, null);

        popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT,    RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.setAnimationStyle(R.style.animationName);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        ((Button) popupView.findViewById(R.id.saveBtn))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {


                        rechedBtn.setText("Restart");
                        flag = false;

                        double dstLat = gpsTracker.getLatitude();
                        double dstLog = gpsTracker.getLongitude();

                      /*  double dstLat = 27.1767;
                        double dstLog = 78.0081;*/


                        double srcLat = Double.parseDouble(sourceLat);
                        double srcLog = Double.parseDouble(sourceLog);

                        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(ShowMapsActivity.this, String.valueOf(dstLat))));
                        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(ShowMapsActivity.this, String.valueOf(dstLog))));

                        Log.e("checking srclat is :" ,srcLat + " null");
                        Log.e("checking srcLog is :" ,srcLog + " null");
                        Log.e("checking dstLat is :" ,dstLat + " null");
                        Log.e("checking dstLog is :" ,dstLog + " null");



                        origin = new LatLng(srcLat,srcLog);
                        dest = new LatLng(dstLat,dstLog);

                        point = new LatLng(dstLat,dstLog);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17.0f);
                        mMap.animateCamera(cameraUpdate);


                        // working to shown root and calculate the distance


                        // check condition ,condition is true then clear all points and String
                        if (MarkerPoints.size() > 1) {
                            mMap.clear();
                            MarkerPoints.clear();
                            MarkerPoints = new ArrayList<>();
                            ShowDistanceDuration = "";
                        }


                        mMap.addMarker(options.position(dest).title("Customer Place"));
                        /**
                         * For the start location, the color of marker is GREEN and
                         * for the end location, the color of marker is RED.
                         */
                        if (MarkerPoints.size() == 1) {
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        } else if (MarkerPoints.size() == 2) {
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }

                        // add marker

                        mMap.addMarker(options);

                        // Checks, whether start and end locations are captured
                        if (MarkerPoints.size() >= 2) {
                            origin = MarkerPoints.get(0);
                            dest = MarkerPoints.get(1);
                        }



                        build_retrofit_and_get_response("driving");


                        popupWindow.dismiss();



                    }

                });

        ((Button) popupView.findViewById(R.id.cancelbtutton))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        popupWindow.dismiss();
                    }
                });

        serchTxt = (SearchView)popupView.findViewById(R.id.editserchview);
         recyelerCustomerList= (RecyclerView)popupView.findViewById(R.id.recyeler_customer_list);


        if (custNameList.size()>0)
        {
            custNameList.clear();
        }
        custNameList.add("Himanshu");
        custNameList.add("Rahul singh updya");
        custNameList.add("Pradeep Jaiswal");
        custNameList.add("Harsh jain");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal dffgfdghghgfhgfhffhjfhjhgjgujgjgjhgj");


        adapter = new CustomerListAdapter(context,custNameList,ShowMapsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowMapsActivity.this);
        recyelerCustomerList.setLayoutManager(mLayoutManager);
        recyelerCustomerList.setItemAnimator(new DefaultItemAnimator());
        recyelerCustomerList.setAdapter(adapter);

        serchTxt.setIconified(false);
        //The above line will expand it to fit the area as well as throw up the keyboard

        //To remove the keyboard, but make sure you keep the expanded version:
        serchTxt.clearFocus();

        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("query",query + "-----------");
                adapter.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("Text",newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        serchTxt.setOnQueryTextListener(onQueryTextListener);

      /*  recyelerCustomerList.addOnItemTouchListener(
                new RecyclerItemClickListener(ShowMapsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        Toast.makeText(ShowMapsActivity.this, "Customer Name is "+ custNameList.get(position), Toast.LENGTH_SHORT).show();
                    }
                })
        );*/



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, log);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        Log.e("checking lat is :", sourceLat);

        double sourceInnerLat = Double.parseDouble(sourceLat);
        double sourceInnerLog = Double.parseDouble(sourceLog);
        point = new LatLng(sourceInnerLat,sourceInnerLog);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17.0f);
        mMap.animateCamera(cameraUpdate);


        //add point in a array list
        MarkerPoints.add(point);

        //show marker points -----------
        options = new MarkerOptions();
        mMap.addMarker(options.position(point).title("Customer Place"));


        rechedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sourceLat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
                sourceLog =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));

                if (flag)
                {


                    Log.e("checked buttn name",rechedBtn.getText().toString());
                    if (rechedBtn.getText().toString().equalsIgnoreCase("Reached")) {


                        callPopup();

                    }else
                        {
                            mMap.clear();
                            MarkerPoints.clear();
                            MarkerPoints = new ArrayList<>();
                        }



                }
                else {


                    flag = true;
                    rechedBtn.setText("Reached");

                    // again

                    mMap.clear();
                    MarkerPoints.clear();
                    MarkerPoints = new ArrayList<>();


                    // again



                    String innerSrc  = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
                    String innersrclog =UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));


                    double sourceInnerLat = Double.parseDouble(innerSrc);
                    double sourceInnerLog = Double.parseDouble(innersrclog);
                    point = new LatLng(sourceInnerLat,sourceInnerLog);


                    //add point in a array list
                    MarkerPoints.add(point);

                    //show marker points -----------
                    options = new MarkerOptions();
                    mMap.addMarker(options.position(point).title("Customer Place"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17.0f);
                    mMap.animateCamera(cameraUpdate);


                  /*  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(ShowMapsActivity.this, String.valueOf(28.4960))));
                    UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(ShowMapsActivity.this, String.valueOf(77.4022))));
*/

                    /*Intent intent = getIntent();
                    finish();
                    startActivity(intent);*/

                }
            }
        });
    }

    @Override
    public void getCustomerName(String name) {

        Toast.makeText(context, "Customer Name is "+ name, Toast.LENGTH_SHORT).show();
    }

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, type);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        ShowDistanceDuration = distance ;

                        Toast.makeText(getApplicationContext(), "My Calculate Distance is  -" + distance + "Time - " + time , Toast.LENGTH_LONG).show();

                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> list = decodePoly(encodedString);
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(20)
                                .color(Color.RED)
                                .geodesic(true)
                        );
                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    // Checking if Google Play Services Available or not
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }
}
