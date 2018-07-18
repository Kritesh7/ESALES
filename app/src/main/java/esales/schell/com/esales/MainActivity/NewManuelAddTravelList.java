package esales.schell.com.esales.MainActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import esales.schell.com.esales.Adapter.CustomerNameSpinnerAdapter;
import esales.schell.com.esales.DataBase.AppddlCustomer;
import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.DataBase.VechileTypeTable;
import esales.schell.com.esales.Model.CustomerDetailsModel;
import esales.schell.com.esales.Model.VehicleTypeModel;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.AppController;
import esales.schell.com.esales.Sources.ConnectionDetector;
import esales.schell.com.esales.Sources.SettingConstant;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

import static esales.schell.com.esales.MainActivity.SplashScreen.getConnectivityStatusString;

public class NewManuelAddTravelList extends AppCompatActivity {

    public Button subBtn;
    public RadioGroup travelRadioGruop;
    public com.toptoche.searchablespinnerlibrary.SearchableSpinner selectSpiner;
    public ArrayList<CustomerDetailsModel> custNameList = new ArrayList<>();
    public String userDetailUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppddlCustomer";
    public String authCodeString = "" , userIdString="";
    public CustomerNameSpinnerAdapter adapter;
    public MasterDataBase masterDataBase;
    public ArrayList<VehicleTypeModel> vehicleTypeList = new ArrayList<>();
    public String vechileType = "",custName = "", custId="";
    public ImageView travelDateBtn, startDateBtn, reachedDateBtn;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hh, m;
    private int yy, mm, dd;
    public EditText startEditTxt,reachedEditTxt,travelDateEditTxt,sourceNameTxt,destinationEditTxt,travelDistanceEditTxt,
    amountEditTxt, remarkTxt;
    public LinearLayout amountLay, parentLay;
    public   boolean visible;
    public ConnectionDetector conn;
    public String reachedPointAPIUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppEmployeeTravelExpenseInsUpdt";
    public String checkNavigate = "", sourceAddress = "", sourceTime = ""   ;
    private Snackbar snackbar;
    private boolean internetConnected=true;
    public CoordinatorLayout coordinatorLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_manuel_add_travel_list);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_900));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.manuel_list_tollbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // change toolbar back arrow color
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow);
        //  upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // enable back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                onBackPressed();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });


        final Intent checkInt = getIntent();
        if (checkInt != null)
        {
            checkNavigate = checkInt.getStringExtra("checked");
        }

        masterDataBase = new MasterDataBase(NewManuelAddTravelList.this);
        conn = new ConnectionDetector(NewManuelAddTravelList.this);
        sourceAddress = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceAddress(NewManuelAddTravelList.this)));
        sourceTime = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getSourceTime(NewManuelAddTravelList.this)));
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.newmanuellay);
        subBtn = (Button) findViewById(R.id.btn_manuel_submit);
        travelRadioGruop = (RadioGroup) findViewById(R.id.travel_Type);
        selectSpiner = (com.toptoche.searchablespinnerlibrary.SearchableSpinner) findViewById(R.id.select_customer_name);
        travelDateBtn = (ImageView) findViewById(R.id.travel_date_cal);
        startDateBtn = (ImageView) findViewById(R.id.start_dateView);
        reachedDateBtn = (ImageView) findViewById(R.id.reached_dateview);
        startEditTxt = (EditText) findViewById(R.id.start_date_text);
        reachedEditTxt = (EditText) findViewById(R.id.reacheTimeTxt);
        travelDateEditTxt = (EditText) findViewById(R.id.travelDateTxt);
        amountLay = (LinearLayout) findViewById(R.id.amount_lay);
        parentLay = (LinearLayout) findViewById(R.id.parentlay);
        sourceNameTxt = (EditText) findViewById(R.id.input_sourceName);
        destinationEditTxt = (EditText) findViewById(R.id.input_destinationName);
        travelDistanceEditTxt = (EditText) findViewById(R.id.input_travelDistance);
        amountEditTxt = (EditText) findViewById(R.id.amounttxt);
        remarkTxt = (EditText) findViewById(R.id.remarkEditTxt);


        authCodeString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(NewManuelAddTravelList.this)));
        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(NewManuelAddTravelList.this)));




            if (!sourceAddress.equalsIgnoreCase("null"))
                sourceNameTxt.setText(sourceAddress);

            if (!sourceTime.equalsIgnoreCase("null"))
                startEditTxt.setText(sourceTime);



        ////------------------ case Details-------------------------//
        if (custNameList.size() > 0) {
            custNameList.clear();
        }

        selectSpiner.getBackground().setColorFilter(getResources().getColor(R.color.red_800), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CustomerDetailsModel> adapter =
                new ArrayAdapter<CustomerDetailsModel>(getApplicationContext(), R.layout.spinnertextview, custNameList);
        adapter.setDropDownViewResource(R.layout.spinnertextview);

        selectSpiner.setAdapter(adapter);


        /*adapter = new CustomerNameSpinnerAdapter(NewManuelAddTravelList.this, custNameList);
        selectSpiner.setAdapter(adapter);*/

       /* custNameList.add(new CustomerDetailsModel("Please Select Customer Name",""));*/

        int cnt = masterDataBase.getCustomerDetailCunt(userIdString);

        // cheked data base record is available or not
        if (cnt >= 0) {
            // get the data  to database (Customer Detail)
            Cursor cursor1 = masterDataBase.getCustomerDetail(userIdString);

            if (cursor1 != null && cursor1.getCount() > 0) {
                if (cursor1.moveToFirst()) {
                    do {
                        String CustomerName = cursor1.getString(cursor1.getColumnIndex(AppddlCustomer.CustomerName));
                        String CustomerId = cursor1.getString(cursor1.getColumnIndex(AppddlCustomer.CustomerID));

                        // add data in list
                        custNameList.add(new CustomerDetailsModel(CustomerName, CustomerId));

                        //  adapter.notifyDataSetChanged();

                        // checked Data
                        Log.e("CustomerName", CustomerName);
                        Log.e("CustomerId", CustomerId);

                    } while (cursor1.moveToNext());
                }
            } else {
                //checked Internet connectivity
                if (conn.getConnectivityStatus() > 0) {
                    userDetailApi(userIdString, authCodeString);
                } else {

                    conn.showNoInternetAlret();
                }
            }


            selectSpiner.setPositiveButton("OK");
            selectSpiner.getBackground().setColorFilter(getResources().getColor(R.color.red_800), PorterDuff.Mode.SRC_ATOP);

            selectSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    custName = custNameList.get(position).getCustomerName();
                    custId = custNameList.get(position).getCustomerId();

                    Log.e("customer id is ", custNameList.get(position).getCustomerId() + " null");
                    Log.e("customer Name is ", custNameList.get(position).getCustomerName() + " null");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //Craete Radio Button Dynamicaly
            // add list to public option
            vehicleTypeList.add(new VehicleTypeModel("1", "", "Public Transport"));
            // data is show to database
            Cursor cursor = masterDataBase.getVecheleTypeData(userIdString);

            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String vehcleTypeName = cursor.getString(cursor.getColumnIndex(VechileTypeTable.VechileTypeName));
                        String vechleTypeRate = cursor.getString(cursor.getColumnIndex(VechileTypeTable.vechileRate));
                        String vechleTypeId = cursor.getString(cursor.getColumnIndex(VechileTypeTable.vechileTypeId));

                        // add data in list
                        vehicleTypeList.add(new VehicleTypeModel(vechleTypeId, vechleTypeRate, vehcleTypeName));

                        // checked Data
                        Log.e("vehcleTypeName", vehcleTypeName);
                        Log.e("vechleTypeRate", vechleTypeRate);
                        Log.e("vechleTypeId", vechleTypeId);
                        Log.e("vehicleTypeList size", vehicleTypeList.size() + "");

                    } while (cursor.moveToNext());
                }
            }

            Log.e("first Type", vechileType);

            // dynamically  create radio button
            RadioGroup.LayoutParams rprms;
            RadioButton radioButton = null;
            for (int i = 0; i < vehicleTypeList.size(); i++) {
                radioButton = new RadioButton(this);
                radioButton.setText(vehicleTypeList.get(i).getVehicleTypeName());
                radioButton.setId(Integer.parseInt(vehicleTypeList.get(i).getVehicleTypeId()));
                rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                travelRadioGruop.addView(radioButton, rprms);
            }

            // set checked listner
            //checked alert choose vechile type using home start button only

                if (vehicleTypeList.size() == 1) {
                    ((RadioButton) travelRadioGruop.getChildAt(0)).setChecked(true);
                    vechileType = vehicleTypeList.get(0).getVehicleTypeId();
                } else {
                    ((RadioButton) travelRadioGruop.getChildAt(1)).setChecked(true);
                    vechileType = vehicleTypeList.get(1).getVehicleTypeId();
                }
           /* }else
                {
                    ((RadioButton) travelRadioGruop.getChildAt(Integer.parseInt(postionOfRadio)+1)).setChecked(true);
                }*/

            // checking vechele type to visibile gone to amount edit text
            if (vechileType.equalsIgnoreCase("1")) {
                TransitionManager.beginDelayedTransition(parentLay);
                visible = !visible;
                amountLay.setVisibility(View.VISIBLE);
            }

            Log.e("first Type---------", vechileType);
            travelRadioGruop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                    Log.e("checked id is ", checkedId + " null");

                    if (checkedId == Integer.parseInt(vehicleTypeList.get(0).getVehicleTypeId())) {
                        vechileType = vehicleTypeList.get(0).getVehicleTypeId();
                        Log.e("first Type", vechileType);

                        TransitionManager.beginDelayedTransition(parentLay);
                        visible = !visible;
                        amountLay.setVisibility(View.VISIBLE);
                    } else if (checkedId == Integer.parseInt(vehicleTypeList.get(1).getVehicleTypeId())) {
                        vechileType = vehicleTypeList.get(1).getVehicleTypeId();
                        Log.e("first Type", vechileType);

                        TransitionManager.beginDelayedTransition(parentLay);
                        visible = !visible;
                        amountLay.setVisibility(View.GONE);
                    } else if (checkedId == Integer.parseInt(vehicleTypeList.get(2).getVehicleTypeId())) {
                        vechileType = vehicleTypeList.get(2).getVehicleTypeId();
                        Log.e("second Type", vechileType);

                        TransitionManager.beginDelayedTransition(parentLay);
                        visible = !visible;
                        amountLay.setVisibility(View.GONE);
                    }
                }
            });

            // click on date picker and set date
            startDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(NewManuelAddTravelList.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {


                                    hh = hourOfDay;
                                    m = minute;
                                    // ro = checking + hourOfDay  + minute;


                                    startEditTxt.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();

                }
            });

            reachedDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(NewManuelAddTravelList.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {


                                    hh = hourOfDay;
                                    m = minute;
                                    // ro = checking + hourOfDay  + minute;


                                    reachedEditTxt.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });

            travelDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(NewManuelAddTravelList.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    yy = year;
                                    mm = monthOfYear;
                                    dd = dayOfMonth;

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.MONTH, monthOfYear);
                                    String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.getTime());
                                    sdf = new DateFormatSymbols().getShortMonths()[monthOfYear];

                                    Log.e("checking,............", sdf + " null");
                                    travelDateEditTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
            });

            subBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (travelDateEditTxt.getText().toString().equalsIgnoreCase("")) {

                        final Toast toast = Toast.makeText(NewManuelAddTravelList.this, "Please Select Valid Date", Toast.LENGTH_LONG);
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

                    } else if (vechileType.equalsIgnoreCase("")) {

                        final Toast toast = Toast.makeText(NewManuelAddTravelList.this, "Please Select Vehicle Type", Toast.LENGTH_LONG);
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

                    } else if (startEditTxt.getText().toString().equalsIgnoreCase("")) {

                        final Toast toast = Toast.makeText(NewManuelAddTravelList.this, "Please Select Starting Time", Toast.LENGTH_LONG);
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


                    } else if (sourceNameTxt.getText().toString().equalsIgnoreCase("")) {
                        sourceNameTxt.setError("Please Enter Valid Source Name");

                    } else if (reachedEditTxt.getText().toString().equalsIgnoreCase("")) {

                        final Toast toast = Toast.makeText(NewManuelAddTravelList.this, "Please Select Reached Time", Toast.LENGTH_LONG);
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

                    } else if (custId.equalsIgnoreCase("")) {

                        final Toast toast = Toast.makeText(NewManuelAddTravelList.this, "Please Select Customer Name", Toast.LENGTH_LONG);
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


                    } else if (destinationEditTxt.getText().toString().equalsIgnoreCase("")) {

                        destinationEditTxt.setError("Please Enter valid destination Name");
                    } else if (travelDistanceEditTxt.getText().toString().equalsIgnoreCase("")) {
                        travelDistanceEditTxt.setError("Please Enter Travel Expense");

                    } else {
                        if (vechileType.equalsIgnoreCase("1")) {
                            String amount = amountEditTxt.getText().toString();
                            if (amount.equalsIgnoreCase("")) {
                                amountEditTxt.setError("Please fill the Amount");
                            } else {

                                if (conn.getConnectivityStatus() > 0) {
                                    submitDetails("", userIdString, vechileType,
                                            travelDateEditTxt.getText().toString() + " " + startEditTxt.getText().toString(),
                                            sourceNameTxt.getText().toString(),
                                            travelDateEditTxt.getText().toString() + " " + reachedEditTxt.getText().toString(),
                                            custName + "," + destinationEditTxt.getText().toString(), custId, "0.0", "0.0", "0.0", "0.0",
                                            travelDistanceEditTxt.getText().toString(), remarkTxt.getText().toString(), authCodeString,
                                            amount);
                                } else {
                                    masterDataBase.setInsertTravelRecords("00000000-0000-0000-0000-000000000000", userIdString, vechileType,
                                            travelDateEditTxt.getText().toString() + " " + startEditTxt.getText().toString(),
                                            sourceNameTxt.getText().toString(),
                                            travelDateEditTxt.getText().toString() + " " + reachedEditTxt.getText().toString(),
                                            custName + "," + destinationEditTxt.getText().toString(), custId, "0.0", "0.0", "0.0", "0.0",
                                            travelDistanceEditTxt.getText().toString(), remarkTxt.getText().toString(), authCodeString,
                                            amount);

                                   onBackPressed();

                                }

                            }

                        } else {
                            if (conn.getConnectivityStatus() > 0) {
                                submitDetails("", userIdString, vechileType,
                                        travelDateEditTxt.getText().toString() + " " + startEditTxt.getText().toString(),
                                        sourceNameTxt.getText().toString(),
                                        travelDateEditTxt.getText().toString() + " " + reachedEditTxt.getText().toString(),
                                        custName + " ," + destinationEditTxt.getText().toString(), custId, "0.0", "0.0", "0.0", "0.0",
                                        travelDistanceEditTxt.getText().toString(), remarkTxt.getText().toString(), authCodeString, "0");

                            } else {
                                masterDataBase.setInsertTravelRecords("00000000-0000-0000-0000-000000000000", userIdString, vechileType,
                                        travelDateEditTxt.getText().toString() + " " + startEditTxt.getText().toString(),
                                        sourceNameTxt.getText().toString(),
                                        travelDateEditTxt.getText().toString() + " " + reachedEditTxt.getText().toString(),
                                        custName + " ," +destinationEditTxt.getText().toString(), custId, "0.0", "0.0", "0.0", "0.0",
                                        travelDistanceEditTxt.getText().toString(), remarkTxt.getText().toString(), authCodeString, "0");

                                onBackPressed();

                            }

                        }
                    }
                }
            });


            // CHECKED PERMISSION
            if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                    Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19) {

                subBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
            } else {
                subBtn.setBackgroundResource(R.drawable.rippileefact);
            }

        }
    }

    //Api Work ------------------------------ customer Name  List Api
    public void userDetailApi(final String userId  , final String authCode)
    {
        final ProgressDialog pDialog = new ProgressDialog(NewManuelAddTravelList.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, userDetailUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("User Details", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                   /* // delet all record to save in customerDetail Table
                    masterDataBase.deleteCustomerDetailRecord();*/

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            final Toast toast = Toast.makeText(NewManuelAddTravelList.this, MsgNotification, Toast.LENGTH_LONG);
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

                   // adapter.notifyDataSetChanged();
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

                final Toast toast = Toast.makeText(NewManuelAddTravelList.this, error.getMessage(), Toast.LENGTH_LONG);
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


    // submit all details
    public void submitDetails(final String TExpID  , final String UserID, final String VehicleTypeID, final String StartAtTime,
                              final String SourceName, final String ReachedAtTime, final String DestinationName,
                              final String DestinationCustID, final String StartLattitude, final String StartLongitude,
                              final String EndLattitude, final String EndLongitude, final String TravelledDistance,
                              final String TravelRemark, final String AuthCode, final String amount)
    {
        final ProgressDialog pDialog = new ProgressDialog(NewManuelAddTravelList.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

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
                            final Toast toast = Toast.makeText(NewManuelAddTravelList.this, MsgNotification, Toast.LENGTH_LONG);
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
                        }
                        String  status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success"))
                        {
                            /*// online navigate
                            if (checkNavigate.equalsIgnoreCase("home"))
                            {
                                Intent intent= new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }else
                            {
                                Intent intent= new Intent(getApplicationContext(),ShowListActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }*/

                            onBackPressed();
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

                final Toast toast = Toast.makeText(NewManuelAddTravelList.this, error.getMessage(), Toast.LENGTH_LONG);
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
                params.put("ExpAmount",amount);


                 Log.e("Submit All Data", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Reached Point");

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
            }
        }else{
            if(!internetConnected){
                internetConnected=true;
                snackbar.show();
            }
        }
    }
}
