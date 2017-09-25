package com.weqa.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.weqa.util.ui.KeyboardUtil;
import com.weqa.widget.CustomQRScannerActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class CreateTeamActivity extends AppCompatActivity implements View.OnClickListener,
                                            CreateTeamAsyncTask.UpdateUI, View.OnTouchListener {

    private static final String LOG_TAG = "WEQA-LOG";

    private Spinner spinner;
    private RecyclerView teamMemberList;
    private ProgressBar progressBar;
    private RelativeLayout container;
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
                R.layout.spinner_item2, orgNameList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item2);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);

        teamMemberList = (RecyclerView) findViewById(R.id.memberList);

        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        teamMemberList.setLayoutManager(layoutManagerNew);

        teamData = new TeamMemberListData();
        adapter = new TeamMemberListAdapter(teamData, this);
        teamMemberList.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                teamData.getListData().remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
        });

        itemTouchHelper.attachToRecyclerView(teamMemberList);

        ImageView addMember = (ImageView) findViewById(R.id.addMember);
        addMember.setOnClickListener(this);

        ImageView backArrow = (ImageView) findViewById(R.id.backButton);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateTeamActivity.this.finish();
            }
        });

        backArrow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView i = (ImageView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorTABtextSelected));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorLightGrey));
                }
                return false;
            }
        });

        addMember.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView i = (ImageView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorTABtextSelected));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    i.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.colorDarkGrey));
                }
                return false;
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTeam();
            }
        });
        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Button i = (Button) view;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    i.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorTABtextSelected));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    i.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorMENU));
                }
                return false;
            }
        });

        teamName = (EditText) findViewById(R.id.teamName);
        teamPurpose = (EditText) findViewById(R.id.teamPurpose);

        container = (RelativeLayout) findViewById(R.id.container);
        container.setOnTouchListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    private void createTeam() {

        if (teamName.getText() == null || teamName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Team name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        teamMemberList.setVisibility(View.GONE);

        String purpose;
        if (teamPurpose.getText() == null || teamPurpose.getText().toString().trim().equals("")) {
            purpose = "";
        }
        else {
            purpose = teamPurpose.getText().toString();
        }

        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to create new team...");
        CreateTeamAsyncTask runner = new CreateTeamAsyncTask(retrofit, LOG_TAG, this);

        String creatorUUID = InstanceIdService.getAppInstanceId(this);

        CreateTeamInput input = new CreateTeamInput();
        input.setTeamName(teamName.getText().toString());
        input.setTeamDescription(purpose);
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

        Log.d(LOG_TAG, "-------------------------------------------- ORG ID = " + getOrgId(position));

        intent.putExtra("SCREEN_ID", 1);
        intent.putParcelableArrayListExtra("EXISTING_USERS", teamData.getListData());
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() != R.id.teamName && v.getId() != R.id.teamPurpose) {
            KeyboardUtil.hideSoftKeyboard(this);
        }
        return false;
    }

    private int getOrgId(int position) {
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

        Intent intent = new Intent();
        setResult(5, intent);
        this.finish();
    }
}
