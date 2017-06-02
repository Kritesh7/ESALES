package esales.schell.com.esales.MainActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import esales.schell.com.esales.Adapter.CustomerListAdapter;
import esales.schell.com.esales.R;
import esales.schell.com.esales.Sources.RecyclerItemClickListener;

public class ShowMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double lat,log;
    public Spinner custSpiiner;
    public LinearLayout custome_Toolbar;
    public ImageView backBtn;
    public ArrayList<String> custNameList = new ArrayList<>();
    public PopupWindow popupWindow;
    public RecyclerView recyelerCustomerList;
    public CustomerListAdapter adapter;
    public Button rechedBtn;
    public SearchView serchTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        custome_Toolbar = (LinearLayout)findViewById(R.id.custome_bar);
        backBtn = (ImageView)custome_Toolbar.findViewById(R.id.backbtn);
        rechedBtn = (Button)findViewById(R.id.rechecdbtn);
       // custSpiiner = (Spinner)custome_Toolbar.findViewById(R.id.customer_name_spinner);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        if (i!=null)
        {
            lat = i.getDoubleExtra("lat",-34);
            log = i.getDoubleExtra("log",151);
        }


        rechedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup();
            }
        });

        //spinner work

     /*   if (custNameList.size()>0)
        {
            custNameList.clear();
        }



        //change spinner arrow color

        custSpiiner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> updaetTypeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.customizespinner,
                custNameList);
        updaetTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        custSpiiner.setAdapter(updaetTypeAdapter);*/

    }


    private void callPopup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.popup, null);

        popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,  800,
                true);


        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        ((Button) popupView.findViewById(R.id.saveBtn))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        /*Toast.makeText(getApplicationContext(),
                                Name.getText().toString(),Toast.LENGTH_LONG).show();*/

                        popupWindow.dismiss();

                    }

                });

        ((Button) popupView.findViewById(R.id.cancelbtutton))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        popupWindow.dismiss();
                    }
                });

        serchTxt = (SearchView)popupView.findViewById(R.id.editserchview);
         recyelerCustomerList= (RecyclerView)popupView.findViewById(R.id.recyeler_customer_list);


        if (custNameList.size()>0)
        {
            custNameList.clear();
        }
        custNameList.add("Himanshu");
        custNameList.add("Rahul singh updya");
        custNameList.add("Pradeep Jaiswal");
        custNameList.add("Harsh jain");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal");
        custNameList.add("Sunil Jaiswal dffgfdghghgfhgfhffhjfhjhgjgujgjgjhgj");


        adapter = new CustomerListAdapter(ShowMapsActivity.this,custNameList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowMapsActivity.this);
        recyelerCustomerList.setLayoutManager(mLayoutManager);
        recyelerCustomerList.setItemAnimator(new DefaultItemAnimator());
        recyelerCustomerList.setAdapter(adapter);


        SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.e("query",query + "-----------");
                adapter.getFilter().filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.e("Text",newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        serchTxt.setOnQueryTextListener(onQueryTextListener);

      /*  recyelerCustomerList.addOnItemTouchListener(
                new RecyclerItemClickListener(ShowMapsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        Toast.makeText(ShowMapsActivity.this, "Customer Name is "+ custNameList.get(position), Toast.LENGTH_SHORT).show();
                    }
                })
        );*/



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, log);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
