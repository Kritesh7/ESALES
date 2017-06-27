package esales.schell.com.esales.Sources;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import esales.schell.com.esales.DataBase.AppEmployeeTravelExpenseInsUpdt;
import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.Interface.RetrofitMaps;
import esales.schell.com.esales.MainActivity.NewManuelAddTravelList;
import esales.schell.com.esales.Model.MapRouteModel.Example;
import esales.schell.com.esales.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Admin on 22-06-2017.
 */

public class BackEndProcessActivity extends BroadcastReceiver {

    public MasterDataBase masterdabase ;
    public String userId = "" , travelDistance = "" , TExpID = "",VehicleTypeID = "",StartAtTime = "",SourceName = "",
            ReachedAtTime = "",DestinationName = "",DestinationCustID = "",StartLattitude = "",
            StartLongitude = "",EndLattitude = "",EndLongitude = "",TravelRemark = "",AuthCode = "",Amount = "",
            MsgNotification = "";
    public LatLng origin;
    public LatLng dest;
    public ConnectionDetector conn ;
    public int pos ;
    public String submitAPIUrl = SettingConstant.BASEURL + "ExpenseWebService.asmx/AppEmployeeTravelExpenseInsUpdt";
    @Override
    public void onReceive(Context context, Intent intent) {

        masterdabase = new MasterDataBase(context);
        conn = new ConnectionDetector(context);

        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(context)));
        AuthCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));

        int count = masterdabase.getInsertTravelRecordsCunt(userId);
        pos = count;

        Log.e("Counting database" , count + " null");


        //checked Internet Connectivity
        if (conn.getConnectivityStatus()>0) {
            // data is show to database
            if (count > 0) {
                Cursor cursor = masterdabase.getInsertTravelRecords(userId);
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToLast()) {
                        do {
                            TExpID = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.TExpID));
                            VehicleTypeID = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.VehicleTypeID));
                            StartAtTime = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.StartAtTime));
                            SourceName = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.SourceName));
                            ReachedAtTime = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.ReachedAtTime));
                            DestinationName = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.DestinationName));
                            DestinationCustID = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.DestinationCustID));
                            StartLattitude = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.StartLattitude));
                            StartLongitude = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.StartLongitude));
                            EndLattitude = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.EndLattitude));
                            EndLongitude = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.EndLongitude));
                            travelDistance = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.TravelledDistance));
                            TravelRemark = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.TravelRemark));
                            //AuthCode = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.AuthCode));
                            Amount = cursor.getString(cursor.getColumnIndex(AppEmployeeTravelExpenseInsUpdt.Amount));


                            if (travelDistance.equalsIgnoreCase("0")) {

                                double srcLat = Double.parseDouble(StartLattitude);
                                double srcLog = Double.parseDouble(StartLongitude);
                                double dstLat = Double.parseDouble(EndLattitude);
                                double dstLog = Double.parseDouble(EndLongitude);

                                origin = new LatLng(srcLat, srcLog);
                                dest = new LatLng(dstLat, dstLog);

                                build_retrofit_and_get_response("driving",TExpID,userId,VehicleTypeID,StartAtTime,SourceName,
                                        ReachedAtTime,DestinationName,DestinationCustID,StartLattitude,StartLongitude,
                                        EndLattitude,EndLongitude,TravelRemark,AuthCode,Amount,context,pos);
                            }else
                                {
                                    submitDetails(TExpID,userId,VehicleTypeID,StartAtTime,SourceName,ReachedAtTime,DestinationName,DestinationCustID,
                                            StartLattitude,StartLongitude,EndLattitude,EndLongitude,travelDistance,TravelRemark,
                                            AuthCode,Amount,context,pos);

                                }

                            // checked Data
                            Log.e("TExpID", TExpID + " null");
                            Log.e("VehicleTypeID", VehicleTypeID);
                            Log.e("StartAtTime", StartAtTime);
                            Log.e("zone", SourceName);
                            Log.e("ReachedAtTime", ReachedAtTime);
                            Log.e("DestinationName", DestinationName);
                            Log.e("DestinationCustID", DestinationCustID);
                            Log.e("StartLattitude", StartLattitude);
                            Log.e("StartLongitude", StartLongitude);
                            Log.e("EndLattitude", EndLattitude);
                            Log.e("EndLongitude", EndLongitude);
                            Log.e("travelDistance", travelDistance);
                            Log.e("TravelRemark", TravelRemark);
                            Log.e("AuthCode", AuthCode);
                            Log.e("Amount", Amount);


                        } while (cursor.moveToPrevious());
                    }
                }
            } else {
                Log.e("Data not", "Data is empty in database");
            }
        }else
            {
                Log.e("Internet Connectivity", "Internet is not avilable");
            }


        //Toast.makeText(context, "Hello Harsh", Toast.LENGTH_SHORT).show();
    }


    // Calculate Distance
    private void build_retrofit_and_get_response(String type, final String TExpID  , final String UserID, final String VehicleTypeID,
                                                 final String StartAtTime, final String SourceName, final String ReachedAtTime,
                                                 final String DestinationName, final String DestinationCustID, final String StartLattitude,
                                                 final String StartLongitude, final String EndLattitude, final String EndLongitude,
                                                 final String TravelRemark, final String AuthCode, final String amount,
                                                 final Context context, final int postion) {

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

                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();

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

                        submitDetails(TExpID,UserID,VehicleTypeID,StartAtTime,SourceName,ReachedAtTime,DestinationName,DestinationCustID,
                                StartLattitude,StartLongitude,EndLattitude,EndLongitude,travelDistance,TravelRemark,
                                AuthCode,amount,context,postion);





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

    //submit all detail to server
    public void submitDetails(final String TExpID  , final String UserID, final String VehicleTypeID, final String StartAtTime,
                              final String SourceName, final String ReachedAtTime, final String DestinationName,
                              final String DestinationCustID, final String StartLattitude, final String StartLongitude,
                              final String EndLattitude, final String EndLongitude, final String TravelledDistance,
                              final String TravelRemark, final String AuthCode, final String amount, final Context context,
                              final int postion)
    {

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, submitAPIUrl, new com.android.volley.Response.Listener<String>() {
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
                            MsgNotification = jsonObject.getString("MsgNotification");

                            //testing purpose

                           /* final Toast toast = Toast.makeText(context, MsgNotification, Toast.LENGTH_LONG);
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
                            }, 6000);*/

                            if (MsgNotification.equalsIgnoreCase("Requested update already exist in database, Please review your request"))
                            {
                                // delete record rowvise in database
                                masterdabase.deleteInsertTravelRecords();

                            /*pos--;
                            Log.e("delete all records",pos + " null");*/

                            }
                        }
                        String  status = jsonObject.getString("status");
                       /* if (status.equalsIgnoreCase("failed"))
                        {

                        }
*/
                    }

                    /*pDialog.dismiss();*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());

                final Toast toast = Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG);
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
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TExpID", TExpID);
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
}
