package esales.schell.com.esales.MainActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import esales.schell.com.esales.Adapter.ShowListAdapter;
import esales.schell.com.esales.Model.ShowingListModel;
import esales.schell.com.esales.R;

public class ShowListActivity extends AppCompatActivity {

    public RecyclerView showListRecy;
    public ShowListAdapter adapter;
    public ArrayList<ShowingListModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.show_list_tollbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

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


        showListRecy = (RecyclerView)findViewById(R.id.showing_list);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        adapter = new ShowListAdapter(ShowListActivity.this,list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ShowListActivity.this);
        showListRecy.setLayoutManager(mLayoutManager);
        showListRecy.setItemAnimator(new DefaultItemAnimator());
        showListRecy.setAdapter(adapter);

        prepareInsDetails();
    }

    private void prepareInsDetails() {
        ShowingListModel model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);
        model = new ShowingListModel("04-03-2016","Agra",
                "Delhi" ,"Two Vechile",
                "280Km","220Rs","320Rs");
        list.add(model);

        adapter.notifyDataSetChanged();
    }

}
