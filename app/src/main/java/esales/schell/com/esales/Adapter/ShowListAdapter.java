package esales.schell.com.esales.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import esales.schell.com.esales.MainActivity.ShowEmployeDetailActivity;
import esales.schell.com.esales.MainActivity.ShowMapsActivity;
import esales.schell.com.esales.Model.ShowingListModel;
import esales.schell.com.esales.R;

/**
 * Created by Admin on 08-06-2017.
 */

public class ShowListAdapter extends RecyclerView.Adapter<ShowListAdapter.ViewHolder>
{

    public ArrayList<ShowingListModel> list = new ArrayList<>();
    public Context context;

    public ShowListAdapter(Context context , ArrayList<ShowingListModel> list)
    {
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.show_list_item,parent,false);
        return new ShowListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ShowingListModel model = list.get(position);

        holder.dateTxt.setText(model.getDate());
        holder.sourceTxt.setText(model.getSource());
        holder.destinationTxt.setText(model.getDestination());
        holder.expenseTxt.setText(model.getTravelDisatnce()+ "*" + model.getRate() + "="+model.getExpensAmount());
       /* holder.rateTxt.setText(model.getRate());
        holder.travellDisatnceTxt.setText(model.getTravelDisatnce());*/
        holder.vechileTypeTxt.setText(model.getVechileType());

        holder.ownLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ShowEmployeDetailActivity.class);
                i.putExtra("TExpID", model.getTexpId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTxt,sourceTxt,destinationTxt,vechileTypeTxt,travellDisatnceTxt,rateTxt,expenseTxt;
        public LinearLayout ownLay;
        public ViewHolder(View itemView) {
            super(itemView);

            dateTxt = (TextView)itemView.findViewById(R.id.date);
            sourceTxt = (TextView)itemView.findViewById(R.id.source);
            destinationTxt = (TextView)itemView.findViewById(R.id.destination);
            vechileTypeTxt = (TextView)itemView.findViewById(R.id.vechile);
           /* travellDisatnceTxt = (TextView)itemView.findViewById(R.id.travell_ditance);
            rateTxt = (TextView)itemView.findViewById(R.id.rate);*/
            expenseTxt = (TextView)itemView.findViewById(R.id.expense_amount);
            ownLay = (LinearLayout)itemView.findViewById(R.id.own_lay);


        }
    }
}
