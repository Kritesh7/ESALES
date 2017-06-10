package esales.schell.com.esales.MainActivity;

import android.content.Context;
import android.content.Intent;
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
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.GPSTracker;
import esales.schell.com.esales.Sources.LocationAddress;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

public class HomeActivity extends AppCompatActivity {

    public Button startBtn ,voucherBtn;
    public GPSTracker gps;
    public Context context;
    public ImageView logoutBtn , settingBtn;
    public PopupWindow popupWindow;
    public String vechileType ="2";
    public   boolean visible;
    public  double lat,log;
    public String startTime="";
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
        gps = new GPSTracker(context,HomeActivity.this);

        logoutBtn = (ImageView)findViewById(R.id.logoutBtn);
        voucherBtn = (Button)findViewById(R.id.voucherBtn);
        settingBtn = (ImageView)findViewById(R.id.setting);
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
        RadioButton firstBtn,secondBtn;
        final LinearLayout toolTipLay ,addlay,mailay;
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
        firstBtn = (RadioButton)popupView.findViewById(R.id.two_wheeler);
        secondBtn = (RadioButton)popupView.findViewById(R.id.four_wheeler);
        addTxt = (EditText)popupView.findViewById(R.id.input_add);
        toolTipLay = (LinearLayout)popupView.findViewById(R.id.tooltip_layout);
        addlay = (LinearLayout)popupView.findViewById(R.id.adderass_lay);
        homeTxt = (TextView)popupView.findViewById(R.id.homeTxt);
        officeTxt = (TextView)popupView.findViewById(R.id.officeTxt);
        mailay = (LinearLayout)popupView.findViewById(R.id.main_pop_up_layout);

        // click btn and find vechile Type
        firstBtn.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.two_wheeler)
                {
                    vechileType = "2";
                    Log.e("first Type","2");
                }else if (checkedId == R.id.four_wheeler)
                {
                    vechileType = "4";
                    Log.e("second Type","4");
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
                    toolTipLay.setVisibility(View.GONE);
                }else
                    {
                        TransitionManager.beginDelayedTransition(addlay);
                        visible = !visible;
                        toolTipLay.setVisibility(View.VISIBLE);
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
                    startTime = getCurrentTime();
                    Intent i = new Intent(getApplicationContext(), ShowMapsActivity.class);
                    i.putExtra("lat",lat);
                    i.putExtra("log",log);
                    i.putExtra("vechile_Type",vechileType);
                    i.putExtra("Source_Name", addTxt.getText().toString());
                    i.putExtra("start_Time",startTime);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                    UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(HomeActivity.this,
                            String.valueOf(lat))));
                    UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(HomeActivity.this,
                            String.valueOf(log))));

                 /*   Toast.makeText(getApplicationContext(), "Your current time  is - \nLat: " + getCurrentTime(), Toast.LENGTH_LONG).show();
*/
                    UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(HomeActivity.this,
                            "2")));
                }
                popupWindow.dismiss();
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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

            Toast.makeText(getApplicationContext(), locationAddress+"", Toast.LENGTH_SHORT).show();
        }
    }

}
