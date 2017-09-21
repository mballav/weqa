package com.weqa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.model.adapterdata.TeamMemberListData;
import com.weqa.model.adapterdata.TeamMemberListItem;

/**
 * Created by Manish Ballav on 9/21/2017.
 */
public class TeamMember2ListAdapter extends RecyclerView.Adapter<TeamMember2ListAdapter.TeamMemberItemHolder> {

    private static final String LOG_TAG = "WEQA-LOG";

    class TeamMemberItemHolder extends RecyclerView.ViewHolder {

        private TextView name, designation, mobile, location;

        public TeamMemberItemHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            designation = (TextView)itemView.findViewById(R.id.designation);
            mobile = (TextView)itemView.findViewById(R.id.mobile);
            location = (TextView) itemView.findViewById(R.id.location);
        }

    }

    protected LayoutInflater inflater;
    protected TeamMemberListData itemData;
    protected Context c;

    public TeamMember2ListAdapter(TeamMemberListData itemData, Context c){
        inflater = LayoutInflater.from(c);
        this.itemData = itemData;
        this.c = c;
    }

    public void setItemData(TeamMemberListData itemData) {
        this.itemData = itemData;
    }

    @Override
    public TeamMember2ListAdapter.TeamMemberItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.team_member_list_item2, parent, false);
        return new TeamMember2ListAdapter.TeamMemberItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamMember2ListAdapter.TeamMemberItemHolder holder, int position) {
        final TeamMemberListItem item = this.itemData.getListData().get(position);
        holder.name.setText(item.getFirstName() + " " + item.getLastName());
        holder.designation.setText(item.getDesignation());
        holder.mobile.setText(item.getMobile());
        holder.location.setText(item.getLocation());
    }

    @Override
    public int getItemCount() {
        return this.itemData.getListData().size();
    }
}
