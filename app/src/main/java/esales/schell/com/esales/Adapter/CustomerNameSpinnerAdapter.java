package esales.schell.com.esales.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esales.schell.com.esales.Model.CustomerDetailsModel;
import esales.schell.com.esales.R;

/**
 * Created by Admin on 14-06-2017.
 */

public class CustomerNameSpinnerAdapter extends BaseAdapter {

    Context c;
    ArrayList<CustomerDetailsModel> objects;

    public CustomerNameSpinnerAdapter(Context context, ArrayList<CustomerDetailsModel> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CustomerDetailsModel cur_obj = objects.get(position);
        TextView textView = (TextView) View.inflate(c, android.R.layout.simple_spinner_item, null);
        textView.setText(cur_obj.getCustomerName());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(objects.get(position).getCustomerName());
        return convertView;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /* @Override
    public String toString() {
        return objects.;
    }*/
}
