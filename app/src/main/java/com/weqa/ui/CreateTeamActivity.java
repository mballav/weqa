package com.weqa.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weqa.R;
import com.weqa.adapter.TeamMemberListAdapter;
import com.weqa.adapter.TeamSummaryListAdapter;
import com.weqa.model.Authentication;
import com.weqa.model.CollaborationInput;
import com.weqa.model.Org;
import com.weqa.model.adapterdata.TeamMemberListData;
import com.weqa.model.adapterdata.TeamMemberListItem;
import com.weqa.model.adapterdata.TeamSummaryListData;
import com.weqa.model.adapterdata.TeamSummaryListItem;
import com.weqa.model.json.CreateTeamInput;
import com.weqa.model.json.UuidList;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.CollaborationAsyncTask;
import com.weqa.util.SharedPreferencesUtil;
import com.weqa.util.async.CreateTeamAsyncTask;
import com.weqa.widget.CustomQRScannerActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class CreateTeamActivity extends AppCompatActivity implements View.OnClickListener,
                                            CreateTeamAsyncTask.UpdateUI {

    private static final String LOG_TAG = "WEQA-LOG";

    private Spinner spinner;
    private RecyclerView teamMemberList;
    private EditText teamName, teamPurpose;
    SharedPreferencesUtil util;
    private TeamMemberListData teamData;
    private TeamMemberListAdapter adapter;
    private List<Org> orgList;
    private List<String> orgNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        util = new SharedPreferencesUtil(this);
        orgNameList = util.getOrganizationNameList();

        Authentication auth = util.getAuthenticationInfo();
        orgList = auth.getOrganization();

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, orgNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);

        teamMemberList = (RecyclerView) findViewById(R.id.memberList);

        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        teamMemberList.setLayoutManager(layoutManagerNew);

        teamData = new TeamMemberListData();
        adapter = new TeamMemberListAdapter(teamData, this);
        teamMemberList.setAdapter(adapter);

        TextView addMember = (TextView) findViewById(R.id.addMember);
        addMember.setOnClickListener(this);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTeam();
            }
        });

        teamName = (EditText) findViewById(R.id.teamName);
        teamPurpose = (EditText) findViewById(R.id.teamPurpose);
    }

    private void createTeam() {

        if (teamName.getText() == null || teamName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Team name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (teamPurpose.getText() == null || teamPurpose.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Purpose cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to create new team...");
        CreateTeamAsyncTask runner = new CreateTeamAsyncTask(retrofit, LOG_TAG, this);

        String creatorUUID = InstanceIdService.getAppInstanceId(this);

        CreateTeamInput input = new CreateTeamInput();
        input.setTeamName(teamName.getText().toString());
        input.setTeamDescription(teamPurpose.getText().toString());
        input.setOrgid((int) getOrgId(spinner.getSelectedItemPosition()));
        input.setCreatedByUUID(creatorUUID);

        List<UuidList> uuidList = new ArrayList<UuidList>();
        for (TeamMemberListItem item : teamData.getListData()) {
            UuidList uuid = new UuidList();
            uuid.setUUID(item.getUuid());
            uuidList.add(uuid);
        }
        UuidList uuid = new UuidList();
        uuid.setUUID(creatorUUID);
        uuidList.add(uuid);

        input.setUuidList(uuidList);

        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
        Log.d(LOG_TAG, "Waiting for response...");

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this, CustomQRScannerActivity.class);
        int position = spinner.getSelectedItemPosition();
        intent.putExtra("ORG_ID", getOrgId(position));
        intent.putParcelableArrayListExtra("EXISTING_USERS", teamData.getListData());
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    private long getOrgId(int position) {
        String orgName = orgNameList.get(position);
        for (Org o : orgList) {
            if (orgName.equals(o.getOrganizationName())) {
                return o.getOrganizationId();
            }
        }
        return 0;
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
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateUI() {
        Toast.makeText(this, "Team created", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
