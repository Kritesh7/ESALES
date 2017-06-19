package esales.schell.com.esales.MainActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import esales.schell.com.esales.Adapter.CustomerListAdapter;
import esales.schell.com.esales.Adapter.DemoCustomeAdapter;
import esales.schell.com.esales.DataBase.AppddlCustomer;
import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.Interface.CustomerNameInterface;
import esales.schell.com.esales.Interface.RetrofitMaps;
import esales.schell.com.esales.Model.CustomerDetailsModel;
import esales.schell.com.esales.Model.MapRouteModel.Example;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.AppController;
import esales.schell.com.esales.Sources.ConnectionDetector;
import esales.schell.com.esales.Sources.GPSTracker;
import esales.schell.com.esales.Sources.LocationAddress;
import esales.schell.com.esales.Sources.RecyclerItemClickListener;
import esales.schell.com.esales.Sources.SettingConstant;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static esales.schell.com.esales.MainActivity.SplashScreen.getConnectivityStatusString;

public class ShowMapsActivity extends FragmentActivity implements OnMapReadyCallback,CustomerNameInterface {

    private GoogleMap mMap;
    public double lat,log;
    public String vechileType="",startTime = "", sourceName = "";
    public LinearLayout custome_Toolbar;
    public ArrayList<CustomerDetailsModel> custNameList = new ArrayList<>();
    public PopupWindow popupWindow;
    public ListView serchListData;
    public DemoCustomeAdapter adapter;
    public Button rechedBtn;
    public EditText searchData;
    public Context context;
    public boolean flag = true;
    private Snackbar snackbar;
    private boolean internetConnected=true;
    public GPSTracker gpsTracker;
    public String sourceLat="";
    public String sourceLog="", destinationName="", customerId = "", travelDistance = "";
    public  String authCodeString = "", userIdString = "";
    public LatLng origin;
    public LatLng dest;
    public LatLng point;
    public ArrayList<LatLng> MarkerPoints;
    public Polyline line;
    public String ShowDistanceDuration = "";
    public MarkerOptions options;
    public Button dayEndBtn , showListBtn;
    public ImageView menuItemImg;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public ProgressDialog pDialog;
    public String loactionDstAdd = "";
    public ConnectionDetector conn;
    public MasterDataBase masterDataBase;
    double errordouble = 0;
    public CoordinatorLayout coordinatorLayout;
    public ImageView img ;
    public  SupportMapFragment mapFragment;
    public String userDetailUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppddlCustomer";
    public String reachedPointAPIUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppEmployeeTravelExpenseInsUpdt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = ShowMapsActivity.this;
        conn =new ConnectionDetector(context);
        masterDataBase = new MasterDataBase(context);
        MarkerPoints = new ArrayList<>();
        gpsTracker = new GPSTracker(context,ShowMapsActivity.this);
        custome_Toolbar = (LinearLayout)findViewById(R.id.custome_bar);
        menuItemImg = (ImageView)custome_Toolbar.findViewById(R.id.menu_item);
        rechedBtn = (Button)findViewById(R.id.rechecdbtn);
      //  dayEndBtn = (Button)findViewById(R.id.day_endBtn);
        showListBtn = (Button)findViewById(R.id.showListBtn);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.maps_cordinate);
        img = (ImageView)findViewById(R.id.net_off);


        // menu item popup
        menuItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ShowMapsActivity.this,v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.showmapmenu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                      /*  Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;*/
                      int id  = item.getItemId();

                        if (id == R.id.action_menu_change_pass)
                        {
                            Intent i = new Intent(getApplicationContext(),ChnagePasswordActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }else if(id == R.id.show_logout)
                        {
                            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(ShowMapsActivity.this,
                                    "")));
                        }
                      return true;
                    }
                });

                popup.show();//showing popup menu

        }
        });

        // check android version
        if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19)
        {

          //  dayEndBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
            showListBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
            rechedBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
        }
        else
        {
//            dayEndBtn.setBackgroundResource(R.drawable.rippileefact);
            showListBtn.setBackgroundResource(R.drawable.rippileefact);
            rechedBtn.setBackgroundResource(R.drawable.rippileefact);

            //show error dialog if Google Play Services not available
            if (!isGooglePlayServicesAvailable()) {
                Log.d("onCreate", "Google Play Services not available. Ending Test case.");
                finish();
            }
            else {
                Log.d("onCreate", "Google Play Services available. Continuing.");
            }
        }


        showListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),ShowListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

       /* dayEndBtn.setOnClickListener(new View.OnClickListener() {
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
*/
        sourceLat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
        sourceLog =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));

        // all details start postion
        /*lat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
        log = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));*/
        vechileType = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getVechileType(ShowMapsActivity.this)));
        startTime = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getStartTime(ShowMapsActivity.this)));
        sourceName = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceName(ShowMapsActivity.this)));
        authCodeString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ShowMapsActivity.this)));
        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(ShowMapsActivity.this)));

        // checkin data


        Log.e("vechileType",vechileType);
        Log.e("startTime",startTime);
        Log.e("sourceName",sourceName);
        Log.e("authCodeString",authCodeString);
        Log.e("userIdString",userIdString);

        Intent i = getIntent();
        if (i!=null)
        {
            lat = i.getDoubleExtra("lat",-34);
            log = i.getDoubleExtra("log",151);
          /*  mysynclat = i.getDoubleExtra("calculated_Lat",-28.54545);
            mysynclog = i.getDoubleExtra("calculated_Lon", -77.4546);*/
           /* vechileType = i.getStringExtra("vechile_Type");
            startTime = i.getStringExtra("start_Time");
            sourceName = i.getStringExtra("Source_Name");

            Log.e("intent lat",lat + "null");
            Log.e("intent log",log + "null");
            Log.e("intent start Time",startTime + "null");
            Log.e("intent Source Name",sourceName + "null");*/

           /* Log.e("mysynclat",mysynclat + "null");
            Log.e("mysynclog",mysynclog + "null");
*/
        }

    }


    private void callPopup() {

        final double dstLat = gpsTracker.getLatitude();
        final double dstLog = gpsTracker.getLongitude();

         /*final double dstLat = 	28.9845;
         final double dstLog =  77.7064;*/

        sourceLat = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLet(ShowMapsActivity.this)));
        sourceLog = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceLog(ShowMapsActivity.this)));

        //Log.e("checking error lat---------------------------->>>>>>>>>>", errordouble + " null");
        Log.e("checking error log", sourceLog + " null");

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        Button cancel, save;
        popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationName);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        save = (Button) popupView.findViewById(R.id.saveBtn);
        cancel = (Button) popupView.findViewById(R.id.cancelbtutton);

        // checked permission
        if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19) {

            save.setBackgroundColor(getResources().getColor(R.color.red_700));
            cancel.setBackgroundColor(getResources().getColor(R.color.red_700));
        } else {
            save.setBackgroundResource(R.drawable.rippileefact);
            cancel.setBackgroundResource(R.drawable.rippileefact);


        }

        save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                errordouble += .0373;

                rechedBtn.setText("Restart");
                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(ShowMapsActivity.this,
                        "1")));

                // Required second Point
                       /* lat = gpsTracker.getLatitude();
                        log = gpsTracker.getLongitude();*/
                flag = false;


                // get address
                if (conn.getConnectivityStatus()>0)
                {
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(dstLat, dstLog, getApplicationContext(),
                            new GeocoderHandler());
                }

                final double srcLat = Double.parseDouble(sourceLat);
                final double srcLog = Double.parseDouble(sourceLog);

                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(ShowMapsActivity.this, String.valueOf(dstLat))));
                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(ShowMapsActivity.this, String.valueOf(dstLog))));

                Log.e("checking srclat is :", srcLat + " null");
                Log.e("checking srcLog is :", srcLog + " null");
                Log.e("checking dstLat is :", dstLat + " null");
                Log.e("checking dstLog is :", dstLog + " null");


                origin = new LatLng(srcLat, srcLog);
                dest = new LatLng(dstLat, dstLog);

                point = new LatLng(dstLat, dstLog);
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


                // wait until to get lat log
                if (destinationName.equalsIgnoreCase("")) {
                    final Toast toast = Toast.makeText(ShowMapsActivity.this, "Please Select Customer", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundResource(R.drawable.button_rounded_shape);
                    TextView text = (TextView) view.findViewById(android.R.id.message);
                    text.setTextColor(Color.parseColor("#ffffff"));
                    text.setPadding(20, 20, 20, 20);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 2000);

                } else {
                    pDialog = new ProgressDialog(ShowMapsActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();

                    // checked lat log is get or not
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (dstLat == 0.0) {


                                if (gpsTracker.canGetLocation()) {

                                    final Toast toast = Toast.makeText(ShowMapsActivity.this, "Please Try Again lat log is not get", Toast.LENGTH_LONG);
                                    View view = toast.getView();
                                    view.setBackgroundResource(R.drawable.button_rounded_shape);
                                    TextView text = (TextView) view.findViewById(android.R.id.message);
                                    text.setTextColor(Color.parseColor("#ffffff"));
                                    text.setPadding(20, 20, 20, 20);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 5000);

                                    pDialog.dismiss();

                                }else
                                {
                                    gpsTracker.showSettingsAlert();
                                }

                            } else {

                                if (conn.getConnectivityStatus()>0) {
                                    build_retrofit_and_get_response("driving", String.valueOf(srcLat), String.valueOf(srcLog),
                                            String.valueOf(dstLat), String.valueOf(dstLog), loactionDstAdd);
                                }else {

                                     pDialog.dismiss();
                                    String reachedTime = getCurrentTime();
                                    if (destinationName.equalsIgnoreCase(""))
                                    {
                                        final Toast toast = Toast.makeText(ShowMapsActivity.this, "Please Select Customer", Toast.LENGTH_LONG);
                                        View view = toast.getView();
                                        view.setBackgroundResource(R.drawable.button_rounded_shape);
                                        TextView text = (TextView) view.findViewById(android.R.id.message);
                                        text.setTextColor(Color.parseColor("#ffffff"));
                                        text.setPadding(20, 20, 20, 20);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toast.cancel();
                                            }
                                        }, 2000);
                                    }else
                                    {

                                        //insert data in local database
                                        masterDataBase.setInsertTravelRecords("", userIdString, vechileType, startTime, sourceName, reachedTime, destinationName,
                                                customerId, sourceLat, sourceLog,
                                                String.valueOf(dstLat), String.valueOf(dstLog), "0", "", authCodeString,"0");

                                        //pDialog.dismiss();
                                        popupWindow.dismiss();

                                    }
                                   }
                            }

                        }
                    }, SPLASH_DISPLAY_LENGTH);

                }
            }

        });


        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                popupWindow.dismiss();
            }
        });

        searchData = (EditText) popupView.findViewById(R.id.editTextsearching);
        serchListData = (ListView) popupView.findViewById(R.id.listview_customer_list);


        adapter = new DemoCustomeAdapter(context, custNameList, ShowMapsActivity.this);
        serchListData.setAdapter(adapter);


       int cnt = masterDataBase.getCustomerDetailCunt(userIdString);

        // cheked data base record is available or not
        if (cnt>=0)
        {
            // get the data  to database (Customer Detail)
            Cursor cursor = masterDataBase.getCustomerDetail(userIdString);

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String CustomerName = cursor.getString(cursor.getColumnIndex(AppddlCustomer.CustomerName));
                        String CustomerId = cursor.getString(cursor.getColumnIndex(AppddlCustomer.CustomerID));

                        // add data in list
                        custNameList.add(new CustomerDetailsModel(CustomerName, CustomerId));

                        //  adapter.notifyDataSetChanged();

                        // checked Data
                        Log.e("CustomerName", CustomerName);
                        Log.e("CustomerId", CustomerId);

                    } while (cursor.moveToNext());

                }
        }
        else
            {
                  //checked Internet connectivity
                   if (conn.getConnectivityStatus() > 0) {
                        userDetailApi(userIdString, authCodeString);
                    } else {

                        conn.showNoInternetAlret();
                    }
            }


            serchListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    for (int i = 0; i < serchListData.getChildCount(); i++) {
                        if (position == i) {
                            serchListData.getChildAt(i).setBackgroundColor(Color.parseColor("#e0e0e0"));
                        } else {
                            serchListData.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                        }
                    }
                }
            });

            searchData.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    adapter.getFilter().filter(s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    //get current time
    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status,false);
        }
    };

    private void setSnackbarMessage(String status,boolean showBar) {
        String internetStatus="";
        if(status.equalsIgnoreCase("Wifi enabled")||status.equalsIgnoreCase("Mobile data enabled")){
            internetStatus="Internet Connected";
        }else {
            internetStatus="Lost Internet Connection";
        }
        snackbar = Snackbar
                .make(coordinatorLayout, internetStatus, Snackbar.LENGTH_LONG)
                .setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(getResources().getColor(R.color.red_900));
        snackbar.setDuration(5000);
        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.red_900));
        textView.setTypeface(null, Typeface.BOLD);
        if(internetStatus.equalsIgnoreCase("Lost Internet Connection")){
            if(internetConnected){
                snackbar.show();
                internetConnected=false;

                img.setVisibility(View.VISIBLE);
                mapFragment.getView().setVisibility(View.GONE);
            }
        }else{
            if(!internetConnected){
                internetConnected=true;
                snackbar.show();


                img.setVisibility(View.GONE);
                mapFragment.getView().setVisibility(View.VISIBLE);
            }
        }
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


                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                    UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(ShowMapsActivity.this,
                            "1")));



                   /* flag = true;
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
                    mMap.animateCamera(cameraUpdate);*/
                }
            }
        });
    }

    private void build_retrofit_and_get_response(String type, final String innersrclat, final String innersrclog,
                                                 final String innerdstlat, final String innerdstlog, final String locationdstAdd) {

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

                    //Log.e("checking clicking btn--------------------------", response.body().getRoutes().size()+ " null");

                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        ShowDistanceDuration = distance ;

                        // distance.replaceAll("\\D+","")

                        Log.e("Travel Distance>>>>>>>", distance + " null");
                        String MyString = distance.replaceAll("[^a-z,]","");

                        //checked distance is m and KM
                        if (MyString.equalsIgnoreCase("m"))
                        {
                            int distanceConvInteger = Integer.parseInt(distance.replaceAll("\\D+",""));
                            double distanceInKm = distanceConvInteger/1000;
                            travelDistance = String.valueOf(distanceInKm);
                            Log.e("Travel Distance in M", travelDistance + " null");
                        }
                        else
                            {
                                travelDistance = distance.replaceAll("[^0-9\\.]+", "");
                                Log.e("Travel Distance in KM>>", travelDistance + " null");
                            }




                        if (rechedBtn.getText().toString().equalsIgnoreCase("Restart"))
                        {

                            Log.e("checking clicking btn", "Restart");

                            String reachedTime = getCurrentTime();

                            Log.e("REACHED TIME", reachedTime);

                            //Log.e("destinationName+locationdstAdd", destinationName+"," +locationdstAdd + " null");

                            submitDetails("", userIdString, vechileType, startTime, sourceName, reachedTime, destinationName+"," +locationdstAdd,
                                        customerId, innersrclat, innersrclog,
                                        innerdstlat, innerdstlog, travelDistance, "", authCodeString);

                                destinationName = "";

                        }else
                        {
                            Log.e("checking clicking btn", "Reached");
                        }

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
                    Log.e("onResponse",e.getMessage());
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

    // Api Work-----  User Detail Api ----------->>>>>>>.
    public void userDetailApi(final String userId  , final String authCode)
    {
        final ProgressDialog pDialog = new ProgressDialog(ShowMapsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, userDetailUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("User Details", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    if (custNameList.size()>0)
                    {
                        custNameList.clear();
                    }

                   /* // delet all record to save in customerDetail Table
                    masterDataBase.deleteCustomerDetailRecord();*/

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            final Toast toast = Toast.makeText(ShowMapsActivity.this, MsgNotification, Toast.LENGTH_LONG);
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

                        }

                        String CustomerID = "" , CustomerName = "";
                        if (jsonObject.has("CustomerID"))
                        {

                            CustomerID = jsonObject.getString("CustomerID");

                            Log.e("checking CustomerID", CustomerID);
                        }
                        if (jsonObject.has("CustomerName"))
                        {
                            CustomerName = jsonObject.getString("CustomerName");
                            Log.e("checking CustomerName", CustomerName);
                        }

                        // insert record in CustomerDetail Table
                        masterDataBase.setCustomerDetail(CustomerID,CustomerName,userId);
                        custNameList.add(new CustomerDetailsModel(CustomerName,CustomerID));
                    }

                    adapter.notifyDataSetChanged();
                    pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                final Toast toast = Toast.makeText(ShowMapsActivity.this, error.getMessage(), Toast.LENGTH_LONG);
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
                params.put("UserID", userId);
                params.put("AuthCode",authCode);


                // Log.e(TAG, "auth_key");
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "User Details");


    }


    // Hit Api is Reached Point
    public void submitDetails(final String TExpID  , final String UserID, final String VehicleTypeID, final String StartAtTime,
                              final String SourceName, final String ReachedAtTime, final String DestinationName,
                              final String DestinationCustID, final String StartLattitude, final String StartLongitude,
                              final String EndLattitude, final String EndLongitude, final String TravelledDistance,
                              final String TravelRemark, final String AuthCode)
    {
        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, reachedPointAPIUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Reached Point", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            final Toast toast = Toast.makeText(ShowMapsActivity.this, MsgNotification, Toast.LENGTH_LONG);
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
                            }, 6000);

                            popupWindow.dismiss();

                        }
                    }

                    pDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                final Toast toast = Toast.makeText(ShowMapsActivity.this, error.getMessage(), Toast.LENGTH_LONG);
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
                params.put("TExpID", "00000000-0000-0000-0000-000000000000");
                params.put("UserID",UserID);
                params.put("VehicleTypeID",VehicleTypeID);
                params.put("StartAtTime",StartAtTime);
                params.put("SourceName",SourceName);
                params.put("ReachedAtTime",ReachedAtTime);
                params.put("DestinationName",DestinationName);
                params.put("DestinationCustID",DestinationCustID);
                params.put("StartLattitude",StartLattitude);
                params.put("StartLongitude",StartLongitude);
                params.put("EndLattitude",EndLattitude);
                params.put("EndLongitude",EndLongitude);
                params.put("TravelledDistance",TravelledDistance);
                params.put("TravelRemark",TravelRemark);
                params.put("AuthCode",AuthCode);
                params.put("ExpAmount","0");


                 Log.e("Show Maps Lat>>>>>", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Reached Point");


    }

    @Override
    public void getCustomerName(String name) {
        //Toast.makeText(context, "Customer Name is "+ name, Toast.LENGTH_SHORT).show();

        destinationName = name;
        final Toast toast = Toast.makeText(ShowMapsActivity.this, destinationName, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.button_rounded_shape);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#ffffff"));
        text.setPadding(20, 20, 20, 20);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 2000);

    }

    @Override
    public void getCustomerId(String cusId) {

        customerId = cusId;

      //  Toast.makeText(context, customerId, Toast.LENGTH_SHORT).show();
    }

    // get address with the help of lat log
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            loactionDstAdd = locationAddress;
        }
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        Intent i =new Intent(getApplicationContext(),ShowMapsActivity.class);
        startActivity(i);
        finish();
    }*/
}
