package com.weqa.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weqa.R;
import com.weqa.adapter.TeamMember2ListAdapter;
import com.weqa.adapter.TeamMemberListAdapter;
import com.weqa.model.Authentication;
import com.weqa.model.Org;
import com.weqa.model.adapterdata.TeamMemberListData;
import com.weqa.model.adapterdata.TeamMemberListItem;
import com.weqa.model.json.CreateTeamInput;
import com.weqa.model.json.TeamDetailInput;
import com.weqa.model.json.TeamDetailResponse;
import com.weqa.model.json.UuidList;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.DatetimeUtil;
import com.weqa.util.SharedPreferencesUtil;
import com.weqa.util.async.CreateTeamAsyncTask;
import com.weqa.util.async.GetTeamDetailAsyncTask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class TeamDetailActivity extends AppCompatActivity implements GetTeamDetailAsyncTask.UpdateUI {

    private static final String LOG_TAG = "WEQA-LOG";

    private RecyclerView teamMemberList;
    private TeamMemberListData teamData;
    private TeamMember2ListAdapter adapter;
    SharedPreferencesUtil util;
    Authentication auth;

    private TextView teamName, orgName, teamPurpose, created, createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        util = new SharedPreferencesUtil(this);
        auth = util.getAuthenticationInfo();

        teamMemberList = (RecyclerView) findViewById(R.id.memberList);

        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        teamMemberList.setLayoutManager(layoutManagerNew);

        teamData = new TeamMemberListData();
        adapter = new TeamMember2ListAdapter(teamData, this);
        teamMemberList.setAdapter(adapter);

        Intent intent = getIntent();
        int teamId = intent.getIntExtra("TEAM_ID", 0);

        teamName = (TextView) findViewById(R.id.teamName);
        orgName = (TextView) findViewById(R.id.orgName);
        teamPurpose = (TextView) findViewById(R.id.teamPurpose);
        created = (TextView) findViewById(R.id.created);
        createdBy = (TextView) findViewById(R.id.createdBy);

        fetchTeamDetail(teamId);
    }

    private void fetchTeamDetail(int teamId) {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to create new team...");
        GetTeamDetailAsyncTask runner = new GetTeamDetailAsyncTask(retrofit, LOG_TAG, this);

        TeamDetailInput input = new TeamDetailInput();
        input.setTeamId(teamId);
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
        Log.d(LOG_TAG, "Waiting for response...");
    }

    @Override
    public void updateUI(List<TeamDetailResponse> responses) {
        if (responses == null || responses.size() == 0) {
            Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        TeamDetailResponse r = responses.get(0);
        String createdOn = "Created - " + DatetimeUtil.getLocalDateTime(r.getCreationDate());
        created.setText(createdOn);

        teamPurpose.setText(r.getTeamDescription());
        teamName.setText(r.getTeamName());
        createdBy.setText("Created by - " + getCreatedBy(responses));
        orgName.setText(getOrgName(r.getOrgId()));

        List<TeamMemberListItem> memberList = new ArrayList<TeamMemberListItem>();
        for (TeamDetailResponse rr : responses) {
            TeamMemberListItem item = new TeamMemberListItem();
            item.setFirstName(rr.getFirstName());
            item.setLastName(rr.getLastName());
            item.setDesignation(rr.getDesignation());
            item.setMobile(rr.getMobileNo());
            item.setUuid(rr.getUuid());
            item.setLocation(rr.getBuildingAddress());
            item.setOrgId(rr.getOrgId());
            memberList.add(item);
        }
        teamData.addMembers(memberList);
        adapter.notifyDataSetChanged();
    }

    private String getCreatedBy(List<TeamDetailResponse> responses) {
        for (TeamDetailResponse rr : responses) {
            if (rr.getTeamCreator()) {
                return rr.getFirstName() + " " + rr.getLastName();
            }
        }
        return "";
    }

    private String getOrgName(int orgId) {
        List<Org> orgList = auth.getOrganization();
        for (Org o : orgList) {
            if (o.getOrganizationId() == orgId) {
                return o.getOrganizationName();
            }
        }
        return "";
    }
}
