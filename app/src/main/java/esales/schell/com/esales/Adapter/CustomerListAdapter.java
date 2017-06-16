package esales.schell.com.esales.Adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import esales.schell.com.esales.Interface.CustomerNameInterface;
import esales.schell.com.esales.MainActivity.ShowMapsActivity;
import esales.schell.com.esales.Model.CustomerDetailsModel;
import esales.schell.com.esales.R;

/**
 * Created by Admin on 02-06-2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder>  implements Filterable {

    public Context context;
    public ArrayList<CustomerDetailsModel> list = new ArrayList<>();
    public ValueFilter valueFilter;
    public ArrayList<CustomerDetailsModel> filterlistlist = new ArrayList<>();
    public CustomerNameInterface anInterface;

    public CustomerListAdapter(Context context, ArrayList<CustomerDetailsModel> list, CustomerNameInterface anInterface) {
        this.context = context;
        this.list = list;
        this.filterlistlist = list;
        this.anInterface = anInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customizespinner, parent, false);
        return new CustomerListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        final CustomerDetailsModel model = list.get(position);
        holder.custName.setText(model.getCustomerName());
        holder.custName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anInterface.getCustomerName(model.getCustomerName());
                anInterface.getCustomerId(model.getCustomerId());


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   /* @Override
    public Filter getFilter() {
        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }*/

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView custName;

        public ViewHolder(View itemView) {
            super(itemView);

            custName = (TextView) itemView.findViewById(R.id.spiinertext);


        }
    }

    //Returns a filter that can be used to constrain data with a filtering pattern.
    @Override
    public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<String> filterList = new ArrayList<String>();

                for (int i = 0; i < filterlistlist.size(); i++) {

                    if (filterlistlist.get(i).getCustomerName().contains(constraint)) {

                        filterList.add(filterlistlist.get(i).getCustomerName());

                    }
                }


                results.count = filterList.size();

                results.values = filterList;

            } else {

                results.count = filterlistlist.size();

                results.values = filterlistlist;

            }

            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            filterlistlist = (ArrayList<CustomerDetailsModel>) results.values;

            notifyDataSetChanged();


        }


   /* private class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String str = constraint.toString();
            Log.e("constraint string",str);

            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length()>0)
            {
                ArrayList<String> filterList = new ArrayList<String>();

                for (int i =0 ; i<filterlistlist.size(); i++)
                {
                    if (filterlistlist.get(i).getCustomerName()
                            .contains(constraint.toString()))
                    {

                        filterList.add( filterlistlist.get(i).getCustomerName());
                    }
                }
                filterResults.count = filterList.size();
                filterResults.values = filterList;

            }else
            {
                filterResults.count =  filterlistlist.size();
                filterResults.values = filterlistlist;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            list = (ArrayList<CustomerDetailsModel>)results.values;
            notifyDataSetChanged();
        }
    }*/

    }
}
