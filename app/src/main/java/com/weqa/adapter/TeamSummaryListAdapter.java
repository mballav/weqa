package com.weqa.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.model.adapterdata.TeamSummaryListData;
import com.weqa.model.adapterdata.TeamSummaryListItem;
import com.weqa.ui.TeamDetailActivity;

import java.util.List;

/**
 * Created by Manish Ballav on 9/20/2017.
 */

public class TeamSummaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = "WEQA-LOG";

    class TeamSummaryItemHolder extends RecyclerView.ViewHolder {

        private TextView teamName, teamNumber;
        //private TextView teamDate;
        private TextView colocated;
        private RelativeLayout container;

        public TeamSummaryItemHolder(View itemView) {
            super(itemView);
            teamName = (TextView)itemView.findViewById(R.id.teamName);
            teamNumber = (TextView)itemView.findViewById(R.id.teamNumber);
            //teamDate = (TextView)itemView.findViewById(R.id.teamDate);
            colocated = (TextView) itemView.findViewById(R.id.colocated);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
        }

    }

    class TeamOrgItemHolder extends RecyclerView.ViewHolder {

        private TextView org;

        public TeamOrgItemHolder(View itemView) {
            super(itemView);
            org = (TextView) itemView.findViewById(R.id.org);
        }

    }

    protected LayoutInflater inflater;
    protected TeamSummaryListData itemData;
    protected Activity activity;

    public TeamSummaryListAdapter(TeamSummaryListData itemData, Activity activity){
        inflater = LayoutInflater.from(activity);
        this.itemData = itemData;
        this.activity = activity;
        Log.d(LOG_TAG, "item data size = " + itemData.getListData().size());
        for (TeamSummaryListItem t : itemData.getListData()) {
            Log.d(LOG_TAG, "ORG FLAG : " + t.isOrg());
        }
    }

    public void setItemData(TeamSummaryListData itemData) {
        this.itemData = itemData;
    }

    @Override
    public int getItemViewType(int position) {
        TeamSummaryListItem item = itemData.getListData().get(position);
        if (item.isOrg())
            return 2;
        else
            return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = inflater.inflate(R.layout.team_summary_list_item, parent, false);
                return new TeamSummaryItemHolder(view);

            case 2:
                view = inflater.inflate(R.layout.team_summary_list_org_item, parent, false);
                return new TeamOrgItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final TeamSummaryListItem item = this.itemData.getListData().get(position);
        switch (viewHolder.getItemViewType()) {
            case 1:
                final TeamSummaryItemHolder holder = (TeamSummaryItemHolder) viewHolder;
                holder.teamName.setText(item.getTeamName());
                holder.teamNumber.setText(item.getNumberOfMembers() + " Members");
//                holder.teamDate.setText("Created - " + item.getFormattedDate());
                holder.colocated.setText("" + item.getColocated());
                if (item.getColocated() == 0) {
                    holder.colocated.setBackgroundResource(R.drawable.background_round_avail_red);
                }
                else {
                    holder.colocated.setBackgroundResource(R.drawable.background_round_avail_green);
                }
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), TeamDetailActivity.class);
                        i.putExtra("TEAM_ID", item.getTeamId());
                        i.putExtra("ORG_ID", item.getOrgId());
                        activity.startActivityForResult(i, 10);
                    }
                });

                holder.container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            holder.container.setBackgroundResource(R.drawable.avail_list_rectangle2);
//                            holder.teamName.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorTABtextSelected));
//                            holder.teamNumber.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorTABtextSelected));
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            holder.container.setBackgroundResource(R.drawable.avail_list_rectangle);
//                            holder.teamName.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorMENU));
//                            holder.teamNumber.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorMENU));
                        }
                        return false;
                    }
                });
                break;
            case 2:
                TeamOrgItemHolder holder2 = (TeamOrgItemHolder) viewHolder;
                holder2.org.setText(item.getOrgName());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.itemData.getListData().size();
    }

}
