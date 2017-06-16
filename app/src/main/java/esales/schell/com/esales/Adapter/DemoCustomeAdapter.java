package esales.schell.com.esales.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import esales.schell.com.esales.Interface.CustomerNameInterface;
import esales.schell.com.esales.Model.CustomerDetailsModel;
import esales.schell.com.esales.R;

/**
 * Created by Admin on 16-06-2017.
 */

public class DemoCustomeAdapter extends BaseAdapter implements Filterable {

    private ArrayList<CustomerDetailsModel> mOriginalValues; // Original Values
    private ArrayList<CustomerDetailsModel> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;
    public CustomerNameInterface anInterface;
    public boolean flag = true;

    public DemoCustomeAdapter(Context context, ArrayList<CustomerDetailsModel> mProductArrayList , CustomerNameInterface anInterface) {
        this.mOriginalValues = mProductArrayList;
        this.mDisplayedValues = mProductArrayList;
        this.anInterface = anInterface;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<CustomerDetailsModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CustomerDetailsModel> FilteredArrList = new ArrayList<CustomerDetailsModel>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<CustomerDetailsModel>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getCustomerName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new CustomerDetailsModel(mOriginalValues.get(i).getCustomerName(),mOriginalValues.get(i).getCustomerId()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView tvName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.customizespinner, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.spiinertext);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mDisplayedValues.get(position).getCustomerName());

        final ViewHolder finalHolder = holder;
        holder.tvName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                /*for (int i = 0; i < mOriginalValues.size(); i++) {
                    if (position == i) {
                        finalHolder.tvName.setBackgroundColor(Color.parseColor("#e0e0e0"));
                    } else {
                        finalHolder.tvName.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }*/
              /*  if (flag)
                {
                    finalHolder.tvName.setBackgroundColor(Color.parseColor("#e0e0e0"));

                    flag = false;
                }else
                    {
                        finalHolder.tvName.setBackgroundColor(Color.parseColor("#ffffff"));
                        flag = true;
                    }*/


                anInterface.getCustomerName(mDisplayedValues.get(position).getCustomerName());
                anInterface.getCustomerId(mDisplayedValues.get(position).getCustomerId());

            }
        });

        return convertView;
    }

   /* @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Product>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Product> FilteredArrList = new ArrayList<Product>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Product>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                *//********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********//*
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).name;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Product(mOriginalValues.get(i).name,mOriginalValues.get(i).price));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }*/
}