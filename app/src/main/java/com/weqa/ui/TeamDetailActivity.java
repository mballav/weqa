package com.weqa.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.weqa.model.json.AddTeamMemberInput;
import com.weqa.model.json.CreateTeamInput;
import com.weqa.model.json.TeamDetailInput;
import com.weqa.model.json.TeamDetailResponse;
import com.weqa.model.json.UuidList;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.DatetimeUtil;
import com.weqa.util.SharedPreferencesUtil;
import com.weqa.util.async.AddTeamMemberAsyncTask;
import com.weqa.util.async.CreateTeamAsyncTask;
import com.weqa.util.async.GetTeamDetailAsyncTask;
import com.weqa.widget.CustomQRScannerActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class TeamDetailActivity extends AppCompatActivity implements GetTeamDetailAsyncTask.UpdateUI,
                                                            View.OnClickListener, AddTeamMemberAsyncTask.TeamUpdated,
                                                            View.OnTouchListener {

    private static final String LOG_TAG = "WEQA-LOG";

    private RecyclerView teamMemberList;
    private ProgressBar progressBar;
    private TeamMemberListData teamData;
    private TeamMember2ListAdapter adapter;
    SharedPreferencesUtil util;
    Authentication auth;

    private TextView teamName, orgName, teamPurpose, created, createdBy;
    private int teamId, orgId;

    private List<TeamMemberListItem> newMemberList = new ArrayList<TeamMemberListItem>();

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
        teamId = intent.getIntExtra("TEAM_ID", 0);
        orgId = intent.getIntExtra("ORG_ID", 0);

        teamName = (TextView) findViewById(R.id.teamName);
        orgName = (TextView) findViewById(R.id.orgName);
        teamPurpose = (TextView) findViewById(R.id.teamPurpose);
        created = (TextView) findViewById(R.id.created);
        createdBy = (TextView) findViewById(R.id.createdBy);

        ImageView backArrow = (ImageView) findViewById(R.id.backButton);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TeamDetailActivity.this.finish();
            }
        });

        ImageView addMember = (ImageView) findViewById(R.id.addMember);
        addMember.setOnClickListener(this);

        backArrow.setOnTouchListener(this);
        addMember.setOnTouchListener(this);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newMemberList.size() > 0) {
                    addMembersToTeam();
                }
                else {
                    TeamDetailActivity.this.finish();
                }
            }
        });

        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button b = (Button) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    b.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorTABtextSelected));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    b.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorMENU));
                }
                return false;
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fetchTeamDetail(teamId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageView i = (ImageView) v;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v.getId() == R.id.addMember) {
                i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorTABtext));
            }
            else {
                i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorTABtextSelected));
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (v.getId() == R.id.addMember) {
                i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorTABtextSelected));
            }
            else {
                i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorLightGrey));
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this, CustomQRScannerActivity.class);

        Log.d(LOG_TAG, "Going to add orgId to intent to CustomQRScannerActivity ----- " + orgId);

        intent.putExtra("ORG_ID", orgId);
        intent.putExtra("SCREEN_ID", 2);
        intent.putParcelableArrayListExtra("EXISTING_USERS", teamData.getListData());
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 2)
        {
            ArrayList<TeamMemberListItem> memberList = data.getParcelableArrayListExtra("NEW_USER_LIST");
            teamData.addMembers(memberList);
            newMemberList.addAll(memberList);
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchTeamDetail(int teamId) {

        progressBar.setVisibility(View.VISIBLE);
        teamMemberList.setVisibility(View.GONE);

        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to create new team...");
        GetTeamDetailAsyncTask runner = new GetTeamDetailAsyncTask(retrofit, LOG_TAG, this);

        TeamDetailInput input = new TeamDetailInput();
        input.setTeamId(teamId);
        input.setOrgId(orgId);
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
        String createdOn = DatetimeUtil.getLocalDate(r.getCreationDate());
        created.setText(createdOn);

        if (r.getTeamDescription() == null) {
            teamPurpose.setVisibility(View.GONE);
        }
        else {
            teamPurpose.setText(r.getTeamDescription());
        }
        teamName.setText(r.getTeamName());
        createdBy.setText(getCreatedBy(responses));
        orgName.setText(getOrgName(r.getOrgId()));

        Log.d(LOG_TAG, "------------------------------------------------ OrgId = " + orgId);

        List<String> uuidList = new ArrayList<String>();
        List<TeamMemberListItem> memberList = new ArrayList<TeamMemberListItem>();
        for (TeamDetailResponse rr : responses) {
            if (uuidList.indexOf(rr.getUuid()) == -1) {
                TeamMemberListItem item = new TeamMemberListItem();
                item.setFirstName(rr.getFirstName());
                item.setLastName(rr.getLastName());
                item.setDesignation(rr.getDesignation());
                item.setMobile(rr.getMobileNo());
                item.setUuid(rr.getUuid());
                item.setLocation(rr.getBuildingAddress());
                item.setOrgId(rr.getOrgId());
                item.setFloorLevel(rr.getFloorLevel());
                memberList.add(item);
                uuidList.add(rr.getUuid());
            }
        }
        teamData.addMembers(memberList);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        teamMemberList.setVisibility(View.VISIBLE);
    }

    private String getCreatedBy(List<TeamDetailResponse> responses) {
        for (TeamDetailResponse rr : responses) {
            if (rr.getTeamCreator() != null && rr.getTeamCreator()) {
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

    private void addMembersToTeam() {

        progressBar.setVisibility(View.VISIBLE);
        teamMemberList.setVisibility(View.GONE);

        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to create new team...");
        AddTeamMemberAsyncTask runner = new AddTeamMemberAsyncTask(retrofit, LOG_TAG, this);

        AddTeamMemberInput input = new AddTeamMemberInput();
        input.setTeamId(teamId);
        input.setOrgId(orgId);

        List<UuidList> uuidList = new ArrayList<UuidList>();
        for (TeamMemberListItem item : newMemberList) {
            UuidList uuid = new UuidList();
            uuid.setUUID(item.getUuid());
            uuidList.add(uuid);
        }

        input.setUuidList(uuidList);

        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
        Log.d(LOG_TAG, "Waiting for response...");

    }

    @Override
    public void teamUpdated() {
        Toast.makeText(TeamDetailActivity.this, "Team information saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(10, intent);
        this.finish();
    }
}
