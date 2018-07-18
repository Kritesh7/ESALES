package esales.schell.com.esales.MainActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esales.schell.com.esales.Interface.RetrofitMaps;
import esales.schell.com.esales.Model.MapRouteModel.Example;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.AppController;
import esales.schell.com.esales.Sources.SettingConstant;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class ShowEmployeDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public ImageView backImg;
    public TextView vehicleTypeTxt, startAtTimeTxt, sourceNameTxt, reachedAtTimeTxt, destinationNameTxt,
            travelDistanceTxt, alloweRateTxt, totalAmountTxt, travelRemarkTxt, feedSourceTxt, addDateTxt;
    public String textpIdString = "", authCodeString = "", StartLattitude = "", StartLongitude = "", EndLattitude = "",
            EndLongitude = "", sourceLat = "", sourceLog = "" , empName = "";
    public String employDetailUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppEmployeeTravelExpenseDetail";
    public LatLng origin;
    public LatLng dest;
    public LatLng point;
    public ArrayList<LatLng> MarkerPoints;
    public MarkerOptions options;
    public Polyline line;
    public  SupportMapFragment mapFragment;
    public LinearLayout travellRemarkLay;
    public FloatingActionButton floatingActionButton;

    public android.support.design.widget.AppBarLayout mainLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_details_user_activity);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_900));
        }

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar_maps);
        backImg = (ImageView) toolbar.findViewById(R.id.maps_back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        travellRemarkLay = (LinearLayout)findViewById(R.id.travel_remark_layout);
        vehicleTypeTxt = (TextView) findViewById(R.id.vehicle_tpe_txt);
        startAtTimeTxt = (TextView) findViewById(R.id.start_time_txt);
        sourceNameTxt = (TextView) findViewById(R.id.source_name_txt);
        reachedAtTimeTxt = (TextView) findViewById(R.id.reached_time_txt);
        destinationNameTxt = (TextView) findViewById(R.id.destination_name_txt);
        travelDistanceTxt = (TextView) findViewById(R.id.travel_distance_txt);
        alloweRateTxt = (TextView) findViewById(R.id.rate_txt);
        totalAmountTxt = (TextView) findViewById(R.id.total_amout_txt);
        travelRemarkTxt = (TextView) findViewById(R.id.travel_remark_txt);
        feedSourceTxt = (TextView) findViewById(R.id.fedd_source_txt);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.collapsefloatingbutton);
        mainLay = (android.support.design.widget.AppBarLayout)findViewById(R.id.main_content);


        empName = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getEmployName(ShowEmployeDetailActivity.this)));
        authCodeString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ShowEmployeDetailActivity.this)));
        sourceLat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowEmployeDetailActivity.this)));
        sourceLog = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowEmployeDetailActivity.this)));

        Intent i = getIntent();
        if (i != null) {
            textpIdString = i.getStringExtra("TExpID");
        }

        MarkerPoints = new ArrayList<>();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("First");


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mainLay.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        double startLat = Double.parseDouble(sourceLat);
        double startlong = Double.parseDouble(sourceLog);

        LatLng sydney = new LatLng(startLat, startlong);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Log.e("checking lat is :", sourceLat);

        double sourceInnerLat = Double.parseDouble(sourceLat);
        double sourceInnerLog = Double.parseDouble(sourceLog);
        point = new LatLng(sourceInnerLat,sourceInnerLog);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17.0f);
        mMap.animateCamera(cameraUpdate);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17.0f);
                mMap.animateCamera(cameraUpdate);

            }
        });


        //add point in a array list
        MarkerPoints.add(point);

        //show marker points -----------
        options = new MarkerOptions();
       // mMap.addMarker(options.position(point).title(empName));
        getEmployDetails(authCodeString, textpIdString);
    }

    private void build_retrofit_and_get_response(String type,double dstlat,double dstlog) {



        point = new LatLng(dstlat, dstlog);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17.0f);
        mMap.animateCamera(cameraUpdate);


        // working to shown root and calculate the distance


        // check condition ,condition is true then clear all points and String
        if (MarkerPoints.size() > 1) {
            mMap.clear();
            MarkerPoints.clear();
            MarkerPoints = new ArrayList<>();
        }



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
        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, type);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(retrofit.Response<Example> response, Retrofit retrofit) {

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
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

    // API work
    public void getEmployDetails(final String authcode, final String TexpId )
    {
        final ProgressDialog pDialog = new ProgressDialog(ShowEmployeDetailActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, employDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Employ Detail", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            final Toast toast = Toast.makeText(ShowEmployeDetailActivity.this, MsgNotification, Toast.LENGTH_LONG);
                            View view = toast.getView();
                            view.setBackgroundResource(R.drawable.button_rounded_shape);
                            TextView text = (TextView) view.findViewById(android.R.id.message);
                            text.setTextColor(Color.parseColor("#ffffff"));
                            text.setPadding(20, 20, 20, 20);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 2000);

                        }else
                        {

                            String VehicleTypeName = jsonObject.getString("VehicleTypeName");
                            String StartAtTime = jsonObject.getString("StartAtTime");
                            String SourceName = jsonObject.getString("SourceName");
                            String ReachedAtTime = jsonObject.getString("ReachedAtTime");
                            String DestinationName = jsonObject.getString("DestinationName");
                            String TravelledDistance = jsonObject.getString("TravelledDistance");
                            String AllowedRate = jsonObject.getString("AllowedRate");
                            String TotalAmount = jsonObject.getString("TotalAmount");
                            String TravelRemark = jsonObject.getString("TravelRemark");
                            String FeedSource = jsonObject.getString("FeedSource");
                            StartLattitude = jsonObject.getString("StartLattitude");
                            StartLongitude = jsonObject.getString("StartLongitude");
                            EndLattitude = jsonObject.getString("EndLattitude");
                            EndLongitude = jsonObject.getString("EndLongitude");



                            Log.e("checking total Amount",TotalAmount + " null");
                            if (TravelRemark.equalsIgnoreCase(""))
                            {
                                travellRemarkLay.setVisibility(View.GONE);
                            }

                            vehicleTypeTxt.setText(VehicleTypeName);
                            startAtTimeTxt.setText(StartAtTime);
                            sourceNameTxt.setText(SourceName);
                            reachedAtTimeTxt.setText(ReachedAtTime);
                            destinationNameTxt.setText(DestinationName);
                            travelDistanceTxt.setText(TravelledDistance);
                            alloweRateTxt.setText(AllowedRate);
                            totalAmountTxt.setText(TotalAmount);
                            travelRemarkTxt.setText(TravelRemark);
                            feedSourceTxt.setText(FeedSource);


                            if (!FeedSource.equalsIgnoreCase("ma"))
                            {
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(backImg.getLayoutParams());
                                lp.setMargins(0, 40, 0, 0);
                                backImg.setLayoutParams(lp);
                                mapFragment.getView().setVisibility(View.GONE);
                            }


                        }
                    }

                    double srcLat = Double.parseDouble(StartLattitude);
                    double srcLog = Double.parseDouble(StartLongitude);
                    double dstLat = Double.parseDouble(EndLattitude);
                    double dstLog = Double.parseDouble(EndLongitude);


                    origin = new LatLng(srcLat, srcLog);
                    dest = new LatLng(dstLat, dstLog);


                    mMap.addMarker(options.position(origin).title(empName));
                    mMap.addMarker(options.position(dest).title(empName));

                    build_retrofit_and_get_response("driving" ,dstLat ,dstLog);
                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                final Toast toast = Toast.makeText(ShowEmployeDetailActivity.this, error.getMessage(), Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundResource(R.drawable.button_rounded_shape);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#ffffff"));
                text.setPadding(20, 20, 20, 20);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 2000);

                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",authcode);
                params.put("TExpID",TexpId);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Employ Detail");


    }

}
