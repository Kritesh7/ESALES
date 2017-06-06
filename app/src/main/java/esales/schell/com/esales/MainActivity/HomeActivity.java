package esales.schell.com.esales.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.GPSTracker;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

public class HomeActivity extends AppCompatActivity {

    public Button startBtn;
    public GPSTracker gps;
    public Context context;
    public ImageView logoutBtn;
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

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.main_home_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

        context = HomeActivity.this;

        gps = new GPSTracker(context,HomeActivity.this);

        logoutBtn = (ImageView)findViewById(R.id.logoutBtn);
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

                double lat = gps.getLatitude();
                double log = gps.getLongitude();

                Intent i = new Intent(getApplicationContext(),ShowMapsActivity.class);
                i.putExtra("lat" ,lat);
                i.putExtra("log",log);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLat(HomeActivity.this,
                        String.valueOf(lat))));
                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setSourceLog(HomeActivity.this,
                        String.valueOf(log))));

                Toast.makeText(getApplicationContext(), "Your current time  is - \nLat: " + getCurrentTime() , Toast.LENGTH_LONG).show();

                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatusFirstHomePage(HomeActivity.this,
                        "2")));
            }
        });
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    //get current time

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }// en

}
