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

import esales.schell.com.esales.MainActivity.ShowMapsActivity;
import esales.schell.com.esales.R;

/**
 * Created by Admin on 02-06-2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder>  implements Filterable
{

    public Context context;
    public ArrayList<String> list = new ArrayList<>();
    public ValueFilter valueFilter;
    public ArrayList<String> filterlistlist = new ArrayList<>();

    public CustomerListAdapter(Context context , ArrayList<String> list)
    {
        this.context = context;
        this.list = list;
        this.filterlistlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.customizespinner,parent,false);
        return new CustomerListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.custName.setText(list.get(position));


        holder.custName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Customer Name is "+ list.get(position), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView custName;
        public ViewHolder(View itemView) {
            super(itemView);

            custName = (TextView)itemView.findViewById(R.id.spiinertext);




        }
    }

    private class ValueFilter extends Filter
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
                    if (filterlistlist.get(i)
                            .contains(constraint.toString()))
                    {


                        filterList.add( filterlistlist.get(i));
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

            list = (ArrayList<String>)results.values;
            notifyDataSetChanged();
        }
    }
//
   /* public class CustomeFilter extends Filter
    {
        private CustomerListAdapter adapter;

        public CustomeFilter(CustomerListAdapter adapter)
        {
            super();
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }*/
}
