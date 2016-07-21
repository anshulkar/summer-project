package summer.utk.com.summer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Utkarsh on 20-07-2016 with the help of SWAG.
 */
public class StringsDataAdapter extends RecyclerView.Adapter<StringsDataAdapter.MyViewHolder> {
    private ArrayList<String> data;
    public StringsDataAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_prod_name, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name = data.get(position);
        holder.name.setText(name);
        //if(position+1 == data.size())holder.divider.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        //public View divider;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.prod_name_row);
            //divider = (View) view.findViewById(R.id.row_item_divider);
        }
    }
}
