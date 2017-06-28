package esales.schell.com.esales.MainActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import esales.schell.com.esales.Adapter.ShowListAdapter;
import esales.schell.com.esales.Model.ShowingListModel;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.AppController;
import esales.schell.com.esales.Sources.ConnectionDetector;
import esales.schell.com.esales.Sources.SettingConstant;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

import static esales.schell.com.esales.MainActivity.SplashScreen.getConnectivityStatusString;

public class ShowListActivity extends AppCompatActivity {

    public RecyclerView showListRecy;
    public ShowListAdapter adapter;
    public LinearLayout noInternetLay;
    private Snackbar snackbar;
    private boolean internetConnected=true;
    public CoordinatorLayout coordinatorLayout;
    public ConnectionDetector connectionDetector;
    public String userIdString="" , authCodeString = "";
    public String travelListUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppEmployeeTravelExpenseList";
    public ArrayList<ShowingListModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_900));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.show_list_tollbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        // change toolbar back arrow color
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow);
        //  upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // enable back arrow
        if (getSupportActionBar() != null){
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


        connectionDetector = new ConnectionDetector(ShowListActivity.this);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.showlistlay);
        showListRecy = (RecyclerView)findViewById(R.id.showing_list);
        noInternetLay = (LinearLayout)findViewById(R.id.no_internet_layout);

        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(ShowListActivity.this)));
        authCodeString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ShowListActivity.this)));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),NewManuelAddTravelList.class);
                i.putExtra("checked","addNew");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


            }
        });


        adapter = new ShowListAdapter(ShowListActivity.this,list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowListActivity.this);
        showListRecy.setLayoutManager(mLayoutManager);
        showListRecy.setItemAnimator(new DefaultItemAnimator());
        showListRecy.setAdapter(adapter);

        if (connectionDetector.getConnectivityStatus()>0)
        {

            getTravelDetail(userIdString,authCodeString);

           /* noInternetLay.setVisibility(View.GONE);
            showListRecy.setVisibility(View.VISIBLE);*/
        }
        else
        {

           /* noInternetLay.setVisibility(View.VISIBLE);
            showListRecy.setVisibility(View.GONE);*/
            //connectionDetector.showNoInternetAlret();
        }

    }

    public void getTravelDetail(final String userid  ,final String authcode)
    {
        final ProgressDialog pDialog = new ProgressDialog(ShowListActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, travelListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Travel List", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                    if (list.size()>0)
                    {
                        list.clear();
                    }

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            final Toast toast = Toast.makeText(ShowListActivity.this, MsgNotification, Toast.LENGTH_LONG);
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
                        else{

                            String TExpID = jsonObject.getString("TExpID");
                            String VehicleTypeName = jsonObject.getString("VehicleTypeName");
                            String StartAtTimeText = jsonObject.getString("StartAtTimeText");
                            String SourceName = jsonObject.getString("SourceName");
                            String DestinationName = jsonObject.getString("DestinationName");
                            String TravelledDistance = jsonObject.getString("TravelledDistance");
                            String AllowedRate = jsonObject.getString("AllowedRate");
                            String TotalAmount = jsonObject.getString("TotalAmount");

                            list.add(new ShowingListModel(StartAtTimeText,SourceName,DestinationName,VehicleTypeName,
                                    TravelledDistance,AllowedRate,TotalAmount,TExpID));

                        }
                    }
                    adapter.notifyDataSetChanged();
                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Travel List", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                final Toast toast = Toast.makeText(ShowListActivity.this, "Connection Error", Toast.LENGTH_LONG);
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
                params.put("UserID",userid);
                params.put("AuthCode",authcode);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Travel List");


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

                noInternetLay.setVisibility(View.VISIBLE);
                showListRecy.setVisibility(View.GONE);
            }
        }else{
            if(!internetConnected){
                internetConnected=true;
                snackbar.show();
                getTravelDetail(userIdString,authCodeString);

                noInternetLay.setVisibility(View.GONE);
                showListRecy.setVisibility(View.VISIBLE);
            }
        }
    }

}
