package esales.schell.com.esales.MainActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.AppController;
import esales.schell.com.esales.Sources.ConnectionDetector;
import esales.schell.com.esales.Sources.SettingConstant;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

public class LoginActivity extends AppCompatActivity {

    public Button logintBtn;
    public String loginUrl = SettingConstant.BASEURL + "LoginSchellService.asmx/AppUserLogin";
    public EditText emailTxt,passTxt;
    public TextView forgetPassTxt;
    public ConnectionDetector conn;
    public MasterDataBase masterDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_900));
        }

        conn = new ConnectionDetector(LoginActivity.this);

        logintBtn = (Button)findViewById(R.id.btn_login);
        emailTxt = (EditText)findViewById(R.id.input_email);
        passTxt = (EditText)findViewById(R.id.input_password);
        forgetPassTxt = (TextView)findViewById(R.id.forget_pass_txt);

        masterDataBase = new MasterDataBase(LoginActivity.this);

        forgetPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        logintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String AuthCode = "";
                // Device model
                String PhoneModel = android.os.Build.MODEL;

                // Android version
                String AndroidVersion = android.os.Build.VERSION.RELEASE;

                long randomNumber = (long) ((Math.random() * 9000000) + 1000000);
                AuthCode = String.valueOf(randomNumber);

                if (emailTxt.getText().toString().equalsIgnoreCase(""))
                {
                    emailTxt.setError("Please enter valid User Name");
                }else if (passTxt.getText().toString().equalsIgnoreCase(""))
                {
                    passTxt.setError("Please Enter Valid Password");
                }else
                    {

                        if (conn.getConnectivityStatus()>0) {
                            Login_Api(emailTxt.getText().toString(), passTxt.getText().toString(), AuthCode, PhoneModel, AndroidVersion);
                        }else
                            {
                                conn.showNoInternetAlret();
                            }
                    }




            }
        });


        Log.e("checking url is ", loginUrl);

        // CHECKED PERMISSION
        if (Build.VERSION.SDK_INT == 16 || Build.VERSION.SDK_INT == 17 ||
                Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19)
        {

            logintBtn.setBackgroundColor(getResources().getColor(R.color.red_700));
        }
        else
            {
                logintBtn.setBackgroundResource(R.drawable.rippileefact);
            }
    }



    public void Login_Api(final String emailinner  , final String passinner, final String authcode ,
                          final String brandName , final String clientVersion) {
        final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    //delete record in database
                    masterDataBase.deleteRecord();
                    masterDataBase.deleteRecordMyProfileDetail();

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            final Toast toast = Toast.makeText(LoginActivity.this, MsgNotification, Toast.LENGTH_LONG);
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

                            String userId = jsonObject.getString("UserID");
                            String authCodeinner = jsonObject.getString("AuthCode");
                            String Name = jsonObject.getString("Name");
                            String Type = jsonObject.getString("RoleName");
                            String EmailID = jsonObject.getString("EmailID");
                            String MobileNo = jsonObject.getString("MobileNo");
                            String ZoneName = jsonObject.getString("ZoneName");

                            //add profile detail in local database
                            masterDataBase.setMyprofileDetail(Name,EmailID,MobileNo,ZoneName,Type,userId);

                            JSONArray vecheleType = jsonObject.getJSONArray("VehicleType");
                            for (int j = 0 ; j<vecheleType.length(); j++)
                            {
                                JSONObject vechelObj = vecheleType.getJSONObject(j);
                                String VehicleTypeID = vechelObj.getString("VehicleTypeID");
                                String AllowedRate = vechelObj.getString("AllowedRate");
                                String VehicleTypeName = vechelObj.getString("VehicleTypeName");
                                Log.e("VehicleTypeID",VehicleTypeID);
                                Log.e("AllowedRate",AllowedRate);

                                // add data in database
                                masterDataBase.setVecheleTypeDate(VehicleTypeID,VehicleTypeName,AllowedRate,userId);

                            }

                            Intent ik = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(ik);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();


                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(LoginActivity.this,
                                    "1")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserId(LoginActivity.this,
                                    userId)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(LoginActivity.this, authCodeinner)));



                        }


                    }

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

                final Toast toast = Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG);
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
                params.put("UserName", emailinner);
                params.put("Password",passinner);
                params.put("AuthCode",authcode);
                params.put("BrandName",brandName);
                params.put("ClientVersion",clientVersion);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");


    }


   /* @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),SplashScreen.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }



}
