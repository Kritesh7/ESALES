package esales.schell.com.esales.MainActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import esales.schell.com.esales.Adapter.CustomerListAdapter;
import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.DataBase.VechileTypeTable;
import esales.schell.com.esales.Model.VehicleTypeModel;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.AppController;
import esales.schell.com.esales.Sources.ConnectionDetector;
import esales.schell.com.esales.Sources.GPSTracker;
import esales.schell.com.esales.Sources.LocationAddress;
import esales.schell.com.esales.Sources.MyAsyncTask;
import esales.schell.com.esales.Sources.SettingConstant;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

import static esales.schell.com.esales.MainActivity.SplashScreen.getConnectivityStatusString;

public class HomeActivity extends AppCompatActivity {

    public Button startBtn ,trailListbtn;
    public GPSTracker gps;
    public Context context;
    public ImageView logoutBtn , settingBtn , addBtn;
    public PopupWindow popupWindow;
    public String vechileType = "";
    public   boolean visible;
    public  double lat = 0.0,log = 0.0;
    public String startTime="";
    public MasterDataBase masterDataBase;
    public String userIdString = "", authcodeString = "";
    public ArrayList<VehicleTypeModel> vehicleTypeList = new ArrayList<>();
    public ProgressDialog pDialog;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public ConnectionDetector conn;
    private Snackbar snackbar;
    private boolean internetConnected=true;
    public CoordinatorLayout coordinatorLayout;
    public ProgressDialog progressDialog;
    public String LoginCount = "";
    public String postionRadio = "0";
    public String checkLoginValidateUrl = SettingConstant.BASEURL + "LoginSchellService.asmx/AppLoginStatusCheck";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_900));
        }


        context = HomeActivity.this;

        progressDialog = new ProgressDialog(context);
        masterDataBase = new MasterDataBase(context);
        gps = new GPSTracker(context,HomeActivity.this);
        conn = new ConnectionDetector(context);


        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.home_cordinator);
        logoutBtn = (ImageView)findViewById(R.id.logoutBtn);
        trailListbtn = (Button)findViewById(R.id.trail_listbtn);
        settingBtn = (ImageView)findViewById(R.id.setting);
        addBtn = (ImageView)findViewById(R.id.add_manuely);
        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(HomeActivity.this)));
        authcodeString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(HomeActivity.this)));
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(HomeActivity.this,v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.homepage_menu, popup.getMenu());

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
                           // Toast.makeText(getApplicationContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        }else if (id == R.id.my_profile_menu)
                        {
                            Intent i = new Intent(getApplicationContext(),MyProfileActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                /*
                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusSecondHomePage(HomeActivity.this,
                        "")));*/
                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(HomeActivity.this,
                        "")));
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NewManuelAddTravelList.class);
                i.putExtra("checked","home");
                i.putExtra("new","create");
                i.putExtra("Radio_Postion","");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        // data is show to database
        Cursor cursor = masterDataBase.getVecheleTypeData(userIdString);

        if (cursor !=null && cursor.getCount()>0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    String vehcleTypeName = cursor.getString(cursor.getColumnIndex(VechileTypeTable.VechileTypeName));
                    String vechleTypeRate = cursor.getString(cursor.getColumnIndex(VechileTypeTable.vechileRate));
                    String vechleTypeId = cursor.getString(cursor.getColumnIndex(VechileTypeTable.vechileTypeId));

                    // add data in list
                    vehicleTypeList.add(new VehicleTypeModel(vechleTypeId,vechleTypeRate,vehcleTypeName));

                    // checked Data
                    Log.e("vehcleTypeName",vehcleTypeName);
                    Log.e("vechleTypeRate",vechleTypeRate);
                    Log.e("vechleTypeId",vechleTypeId);
                    Log.e("vehicleTypeList size",vehicleTypeList.size()+"");

                }while (cursor.moveToNext());
            }
        }

        startBtn = (Button)findViewById(R.id.startbtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               lat = gps.getLatitude();
               log = gps.getLongitude();

            //    Toast.makeText(context, "Source Lat" + lat + " Source Log" + log, Toast.LENGTH_SHORT).show();

                //get current Time

                if (conn.getConnectivityStatus()>0) {
                    if (vehicleTypeList.size() == 0) {
                        final Toast toast = Toast.makeText(HomeActivity.this, "Your Vehicle Detail not Configure!", Toast.LENGTH_LONG);
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
                        }, 4000);
                    } else {
                        if (vehicleTypeList.size() > 0) {
                            vehicleTypeList.clear();
                        }

                        // cheked Innternet connection
                        if (conn.getConnectivityStatus() > 0) {
                            getLoginInvalidate(userIdString, authcodeString);
                        } else {
                            if (gps.canGetLocation()) {
                                callPopup("");
                            } else {
                                gps.showSettingsAlert();
                            }
                        }

                    }
                }else
                    {
                        conn.showNoInternetAlret();
                    }

            }
        });

        trailListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lat = gps.getLatitude();
                log = gps.getLongitude();

                Intent i = new Intent(getApplicationContext(), ShowListActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(HomeActivity.this,
                        String.valueOf(lat))));
                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(HomeActivity.this,
                        String.valueOf(log))));
            }
        });


        if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19)
        {

            startBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
            trailListbtn.setBackgroundColor(getResources().getColor(R.color.red_700));
        }
        else
        {
            startBtn.setBackgroundResource(R.drawable.buttonshape);
            trailListbtn.setBackgroundResource(R.drawable.buttonshape);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i =new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);
        finish();
    }



    // call popup button

    private void callPopup(String addString) {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.startup_popupwindow, null);

        Button cancel , save;
        RadioGroup radioGroup;
        final LinearLayout toolTipLay ,addlay;
        final EditText addTxt;
        final TextView homeTxt,officeTxt;


        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationName);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        save = (Button)popupView.findViewById(R.id.first_popup_saveBtn);
        cancel = (Button)popupView.findViewById(R.id.first_popup_cancelbtutton);
        radioGroup = (RadioGroup)popupView.findViewById(R.id.popupradio);
        addTxt = (EditText)popupView.findViewById(R.id.input_add);
        toolTipLay = (LinearLayout)popupView.findViewById(R.id.tooltip_layout);
        addlay = (LinearLayout)popupView.findViewById(R.id.adderass_lay);
        homeTxt = (TextView)popupView.findViewById(R.id.homeTxt);
        officeTxt = (TextView)popupView.findViewById(R.id.officeTxt);



        // data is show to database
        Cursor cursor = masterDataBase.getVecheleTypeData(userIdString);

        if (cursor !=null && cursor.getCount()>0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    String vehcleTypeName = cursor.getString(cursor.getColumnIndex(VechileTypeTable.VechileTypeName));
                    String vechleTypeRate = cursor.getString(cursor.getColumnIndex(VechileTypeTable.vechileRate));
                    String vechleTypeId = cursor.getString(cursor.getColumnIndex(VechileTypeTable.vechileTypeId));

                    // add data in list
                    vehicleTypeList.add(new VehicleTypeModel(vechleTypeId,vechleTypeRate,vehcleTypeName));

                    // checked Data
                    Log.e("vehcleTypeName",vehcleTypeName);
                    Log.e("vechleTypeRate",vechleTypeRate);
                    Log.e("vechleTypeId",vechleTypeId);
                    Log.e("vehicleTypeList size",vehicleTypeList.size()+"");

                }while (cursor.moveToNext());
            }
        }

        //visibile Tooltip then add text is empety
        if (addTxt.getText().toString().equalsIgnoreCase(""))
        {
            TransitionManager.beginDelayedTransition(addlay);
            visible = !visible;
            toolTipLay.setVisibility(View.VISIBLE);
        }else
        {
            TransitionManager.beginDelayedTransition(addlay);
            visible = !visible;
            toolTipLay.setVisibility(View.GONE);
        }


        // dynamically  create radio button
        RadioGroup.LayoutParams rprms;
        RadioButton radioButton = null;
        for (int i=0 ; i<vehicleTypeList.size(); i++)
        {
            radioButton = new RadioButton(this);
            radioButton.setText(vehicleTypeList.get(i).getVehicleTypeName());
            radioButton.setId(Integer.parseInt(vehicleTypeList.get(i).getVehicleTypeId()));
            rprms= new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton,rprms);
        }

        // set checked listner
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
        vechileType = vehicleTypeList.get(0).getVehicleTypeId();
        Log.e("first Type---------",vechileType);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                Log.e("checked id is ", checkedId+" null");

                if (checkedId == Integer.parseInt(vehicleTypeList.get(0).getVehicleTypeId()))
                {
                    vechileType = vehicleTypeList.get(0).getVehicleTypeId();
                    postionRadio = "0";
                    Log.e("first Type",vechileType);
                }else if (checkedId == Integer.parseInt(vehicleTypeList.get(1).getVehicleTypeId()))
                {
                    vechileType = vehicleTypeList.get(1).getVehicleTypeId();
                    Log.e("second Type",vechileType);
                    postionRadio = "1";
                }
            }
        });

        // checked add String is null or not then edittext set text
        if (addString.equalsIgnoreCase(""))
        {

        }else
        {
            addTxt.setText(addString);

            //visibile Tooltip then add text is empety
            if (addTxt.getText().toString().equalsIgnoreCase(""))
            {
                TransitionManager.beginDelayedTransition(addlay);
                visible = !visible;
                toolTipLay.setVisibility(View.VISIBLE);
            }else
            {
                TransitionManager.beginDelayedTransition(addlay);
                visible = !visible;
                toolTipLay.setVisibility(View.GONE);
            }
        }

        // choose manuel and automatic address
        addTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (addTxt.getText().toString().equalsIgnoreCase(""))
                {
                    TransitionManager.beginDelayedTransition(addlay);
                    visible = !visible;
                    toolTipLay.setVisibility(View.VISIBLE);
                }else
                    {
                        TransitionManager.beginDelayedTransition(addlay);
                        visible = !visible;
                        toolTipLay.setVisibility(View.GONE);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // set adderss Edit text
        homeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTxt.setText(homeTxt.getText().toString());
            }
        });

        officeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTxt.setText(officeTxt.getText().toString());
            }
        });


        // checked permission
        if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19)
        {

            save.setBackgroundColor(getResources().getColor(R.color.red_700));
            cancel.setBackgroundColor(getResources().getColor(R.color.red_700));
        }
        else
        {
            save.setBackgroundResource(R.drawable.rippileefact);
            cancel.setBackgroundResource(R.drawable.rippileefact);


        }

        /*((Button) popupView.findViewById(R.id.saveBtn))*/
        save .setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                if (addTxt.getText().toString().equalsIgnoreCase(""))
                {

                    final Toast toast = Toast.makeText(HomeActivity.this, "Please Enter the Address", Toast.LENGTH_LONG);
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
     //               addTxt.setError("Please fill the address");
                }else {

                    pDialog = new ProgressDialog(HomeActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    // checked lat log get or not

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (lat == 0.0)
                            {
                                getAlertBox();
                                pDialog.dismiss();
                            }else
                            {
                                pDialog.dismiss();
                                startTime = getCurrentTime();
                                Log.e("Start Time is", startTime);
                                Intent i = new Intent(getApplicationContext(), ShowMapsActivity.class);
                                i.putExtra("lat", lat);
                                i.putExtra("log", log);
                                i.putExtra("radioPost",postionRadio);
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(HomeActivity.this,
                                        String.valueOf(lat))));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(HomeActivity.this,
                                        String.valueOf(log))));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setVechileType(HomeActivity.this,
                                        String.valueOf(vechileType))));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStartTime(HomeActivity.this,
                                        String.valueOf(startTime))));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceName(HomeActivity.this,
                                        String.valueOf(addTxt.getText().toString()))));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(HomeActivity.this,
                                        "2")));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceAddress(HomeActivity.this,
                                        addTxt.getText().toString())));
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceTime(HomeActivity.this,
                                        getOnlyTime())));
                                popupWindow.dismiss();
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
    }

    public void getAlertBox()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Can Not Get Location");

        // set dialog message
        alertDialogBuilder
                .setMessage("You need to try again or Can add travel expense detail manually")
                .setCancelable(false)
                .setPositiveButton("Add Manually",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                        Intent i= new Intent(getApplicationContext(),NewManuelAddTravelList.class);
                        i.putExtra("checked","");
                        i.putExtra("new","");
                        i.putExtra("Radio_Postion","");
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        // ShowMapsActivity.this.finish();
                    }
                })
                .setNegativeButton("Retry",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }



    //get current time
    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }// en

    public String getOnlyTime()
    {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }



    //checked login invalid or not
    public void getLoginInvalidate(final String userId  , final String authcode) {

        progressDialog.setMessage("Get Address....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, checkLoginValidateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("LoginCount"))
                        {

                            LoginCount = jsonObject.getString("LoginCount");

                            if (LoginCount.equalsIgnoreCase("1"))
                            {
                                if (gps.canGetLocation()) {

                                    // get address
                                    LocationAddress locationAddress = new LocationAddress();
                                    locationAddress.getAddressFromLocation(lat, log, getApplicationContext(),
                                            new GeocoderHandler());
                                }else
                                {
                                    gps.showSettingsAlert();
                                }
                            }else
                                {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();

                                    UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(HomeActivity.this,
                                            "")));


                                    final Toast toast = Toast.makeText(HomeActivity.this, "Login Expired...", Toast.LENGTH_LONG);
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
                                    }, 3000);
                                }

                        }


                        }


                   // pDialog.dismiss();

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

                final Toast toast = Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG);
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

                progressDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID", userId);
                params.put("AuthCode",authcode);
                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "LoginValidate");


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

            // call Popup window
            progressDialog.dismiss();
            callPopup(locationAddress);
        }
    }

    //snackbar show internet not allowed

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);

        Log.e("chcking now is", " checking method is");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();

        Log.e("chcking now is resume", " checking method is");
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

            }
        }else{
            if(!internetConnected){
                internetConnected=true;
                snackbar.show();

            }
        }
    }

}
