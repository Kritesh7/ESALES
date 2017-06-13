package esales.schell.com.esales.MainActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.transition.TransitionManager;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import esales.schell.com.esales.Adapter.CustomerListAdapter;
import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.DataBase.VechileTypeTable;
import esales.schell.com.esales.Model.VehicleTypeModel;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.GPSTracker;
import esales.schell.com.esales.Sources.LocationAddress;
import esales.schell.com.esales.Sources.MyAsyncTask;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

public class HomeActivity extends AppCompatActivity {

    public Button startBtn ,voucherBtn;
    public GPSTracker gps;
    public Context context;
    public ImageView logoutBtn , settingBtn;
    public PopupWindow popupWindow;
    public String vechileType = "";
    public   boolean visible;
    public  double lat,log;
    public String startTime="";
    public MasterDataBase masterDataBase;
    public String userIdString = "";
    public ArrayList<VehicleTypeModel> vehicleTypeList = new ArrayList<>();
    public ProgressDialog pDialog;
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    public MyAsyncTask myAsyncTask;
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

        masterDataBase = new MasterDataBase(context);
        gps = new GPSTracker(context,HomeActivity.this);


        logoutBtn = (ImageView)findViewById(R.id.logoutBtn);
        voucherBtn = (Button)findViewById(R.id.voucherBtn);
        settingBtn = (ImageView)findViewById(R.id.setting);
        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(HomeActivity.this)));
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
        startBtn = (Button)findViewById(R.id.startbtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               lat = gps.getLatitude();
               log = gps.getLongitude();

                //get current Time

                if (vehicleTypeList.size()>0)
                {
                    vehicleTypeList.clear();
                }

                // get address
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(lat,log,getApplicationContext(),
                        new GeocoderHandler());



                //getCompleteAddressString(lat,log);

            }
        });


        if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19)
        {

            startBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
            voucherBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
        }
        else
        {
            startBtn.setBackgroundResource(R.drawable.buttonshape);
            voucherBtn.setBackgroundResource(R.drawable.buttonshape);
        }
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
                    Log.e("first Type",vechileType);
                }else if (checkedId == Integer.parseInt(vehicleTypeList.get(1).getVehicleTypeId()))
                {
                    vechileType = vehicleTypeList.get(1).getVehicleTypeId();
                    Log.e("second Type",vechileType);
                }
            }
        });

        // checked add String is null or not then edittext set text
        if (addString.equalsIgnoreCase(""))
        {

        }else
        {
            addTxt.setText(addString);
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
                    addTxt.setError("Please fill the address");
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
                                final Toast toast = Toast.makeText(HomeActivity.this, "Please Try Again lat log is not get", Toast.LENGTH_LONG);
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
                                pDialog.dismiss();
                                startTime = getCurrentTime();
                                Log.e("Start Time is", startTime);
                                Intent i = new Intent(getApplicationContext(), ShowMapsActivity.class);
                                i.putExtra("lat", lat);
                                i.putExtra("log", log);
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

    //get current time
    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }// en



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
            callPopup(locationAddress);

           // Toast.makeText(getApplicationContext(), locationAddress+"", Toast.LENGTH_SHORT).show();
        }
    }

}
