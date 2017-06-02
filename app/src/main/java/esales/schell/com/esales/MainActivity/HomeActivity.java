package esales.schell.com.esales.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.GPSTracker;

public class HomeActivity extends AppCompatActivity {

    public Button startBtn;
    public GPSTracker gps;
    public Context context;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_home_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        context = HomeActivity.this;

        gps = new GPSTracker(context,HomeActivity.this);

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

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + log, Toast.LENGTH_LONG).show();
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

}
