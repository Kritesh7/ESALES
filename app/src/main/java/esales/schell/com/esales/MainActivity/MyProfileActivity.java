package esales.schell.com.esales.MainActivity;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import esales.schell.com.esales.DataBase.MasterDataBase;
import esales.schell.com.esales.DataBase.MyProfileTable;
import esales.schell.com.esales.DataBase.VechileTypeTable;
import esales.schell.com.esales.Model.VehicleTypeModel;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.SharedPrefs;
import esales.schell.com.esales.Sources.UtilsMethods;

public class MyProfileActivity extends AppCompatActivity {

    public MasterDataBase masterDataBase;
    public String userIdString = "";
    public TextView nameTxt,emaiIdTxt,phoneTxt,tyepTxt,zoneTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red_900));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_profile_tollbar);
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

        masterDataBase = new MasterDataBase(MyProfileActivity.this);
        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserId(MyProfileActivity.this)));

        nameTxt = (TextView)findViewById(R.id.profile_name);
        emaiIdTxt = (TextView)findViewById(R.id.profile_email);
        phoneTxt = (TextView)findViewById(R.id.profile_phone);
        zoneTxt = (TextView)findViewById(R.id.profile_zone);
        tyepTxt = (TextView)findViewById(R.id.profile_type);

        // data is show to database
        Cursor cursor = masterDataBase.getMyProfileDetail(userIdString);
        if (cursor !=null && cursor.getCount()>0)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    String name = cursor.getString(cursor.getColumnIndex(MyProfileTable.name));
                    String email = cursor.getString(cursor.getColumnIndex(MyProfileTable.emailId));
                    String phoneNo = cursor.getString(cursor.getColumnIndex(MyProfileTable.phoneNo));
                    String zone = cursor.getString(cursor.getColumnIndex(MyProfileTable.zone));
                    String type = cursor.getString(cursor.getColumnIndex(MyProfileTable.type));



                    // checked Data
                    Log.e("name",name);
                    Log.e("email",email);
                    Log.e("phoneNo",phoneNo);
                    Log.e("zone",zone);
                    Log.e("type",type);

                    nameTxt .setText(name);
                    emaiIdTxt.setText(email);
                    phoneTxt.setText(phoneNo);
                    zoneTxt.setText(zone);
                    tyepTxt.setText(type);


                }while (cursor.moveToNext());
            }
        }
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
