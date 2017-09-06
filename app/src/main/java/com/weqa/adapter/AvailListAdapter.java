package com.weqa.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.model.adapterdata.AvailListItem;

import java.util.List;

/**
 * Created by welcome on 20-06-2017.
 */

public class AvailListAdapter extends RecyclerView.Adapter<AvailListAdapter.AvailItemHolder> {

    public static interface OnAvailListClickListener {
        public void downloadAndUpdateFloorplan(long floorNumber);
    }

    class AvailItemHolder extends RecyclerView.ViewHolder {

        private TextView floorNumber;
        private TextView availability;
        private RelativeLayout availContainer;

        public AvailItemHolder(View itemView) {
            super(itemView);
            floorNumber = (TextView)itemView.findViewById(R.id.floorNumber);
            availability = (TextView)itemView.findViewById(R.id.availability);
            availContainer = (RelativeLayout) itemView.findViewById(R.id.availContainer);
        }
    }

    protected LayoutInflater inflater;
    protected List<AvailListItem> itemData;
    protected OnAvailListClickListener listener;
    protected Context c;

    public AvailListAdapter(List<AvailListItem> itemData, Context c, AvailListAdapter.OnAvailListClickListener listener){
        inflater = LayoutInflater.from(c);
        this.itemData = itemData;
        this.listener = listener;
        this.c = c;
    }

    public void setItemData(List<AvailListItem> itemData) {
        this.itemData = itemData;
    }

    @Override
    public AvailListAdapter.AvailItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.availability_list_item, parent, false);
        return new AvailListAdapter.AvailItemHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailListAdapter.AvailItemHolder holder, int position) {
        final AvailListItem item = this.itemData.get(position);
        holder.floorNumber.setText("Floor " + item.getFloorNumber());
        holder.availability.setText("" + item.getAvailability());
        if (item.getAvailability() == 0) {
            holder.availability.setBackground(this.c.getResources().getDrawable(R.drawable.background_round_avail_red));
        }
        holder.availContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvailListAdapter.this.listener.downloadAndUpdateFloorplan(item.getFloorNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemData.size();
    }
}
