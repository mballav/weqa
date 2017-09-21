package com.weqa.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        private TextView teamName, teamNumber, teamDate;
        private TextView colocated;
        private ImageView teamDetailArrow;

        public TeamSummaryItemHolder(View itemView) {
            super(itemView);
            teamName = (TextView)itemView.findViewById(R.id.teamName);
            teamNumber = (TextView)itemView.findViewById(R.id.teamNumber);
            teamDate = (TextView)itemView.findViewById(R.id.teamDate);
            teamDetailArrow = (ImageView) itemView.findViewById(R.id.teamDetailArrow);
            colocated = (TextView) itemView.findViewById(R.id.colocated);

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
    protected Context c;

    public TeamSummaryListAdapter(TeamSummaryListData itemData, Context c){
        inflater = LayoutInflater.from(c);
        this.itemData = itemData;
        this.c = c;
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
                TeamSummaryItemHolder holder = (TeamSummaryItemHolder) viewHolder;
                holder.teamName.setText(item.getTeamName());
                holder.teamNumber.setText(item.getNumberOfMembers() + " Members");
                holder.teamDate.setText("Created - " + item.getFormattedDate());
                holder.colocated.setText("" + item.getColocated());
                if (item.getColocated() == 0) {
                    holder.colocated.setBackgroundResource(R.drawable.circle_grey);
                }
                else {
                    holder.colocated.setBackgroundResource(R.drawable.circle_green);
                }
                holder.teamDetailArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), TeamDetailActivity.class);
                        i.putExtra("TEAM_ID", item.getTeamId());
                        view.getContext().startActivity(i);
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
