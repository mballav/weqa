package com.weqa.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.adapter.AvailListAdapter;
import com.weqa.adapter.TeamSummaryListAdapter;
import com.weqa.model.AuthInput;
import com.weqa.model.CodeConstants;
import com.weqa.model.CollaborationInput;
import com.weqa.model.CollaborationResponse;
import com.weqa.model.adapterdata.AvailListItem;
import com.weqa.model.adapterdata.TeamSummaryListData;
import com.weqa.model.adapterdata.TeamSummaryListItem;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.AuthAsyncTask;
import com.weqa.util.CollaborationAsyncTask;
import com.weqa.util.DatetimeUtil;
import com.weqa.widget.ContinuousCaptureActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class TeamSummaryActivity extends AppCompatActivity implements View.OnClickListener,
        CollaborationAsyncTask.UpdateTeamData {

    private static final String LOG_TAG = "WEQA-LOG";

    private RecyclerView teamSummaryList;
    private CircleImageView profilePicture;

    private TeamSummaryListData teamData;
    private TeamSummaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_summary);

        teamSummaryList = (RecyclerView) findViewById(R.id.teamSummaryList);

        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        teamSummaryList.setLayoutManager(layoutManagerNew);

        teamData = new TeamSummaryListData();
        adapter = new TeamSummaryListAdapter(teamData, this);
        teamSummaryList.setAdapter(adapter);

        profilePicture = (CircleImageView) findViewById(R.id.profilepicture);

        TextView cameraCircle = (TextView) findViewById(R.id.cameracircle);
        cameraCircle.setOnClickListener(this);

        ImageView createTeamArrow = (ImageView) findViewById(R.id.createTeamArrow);
        createTeamArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CreateTeamActivity.class);
                view.getContext().startActivity(i);
            }
        });

        fetchTeamData();
    }

    private void fetchTeamData() {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        Log.d(LOG_TAG, "Calling the API to get collaboration data...");
        CollaborationAsyncTask runner = new CollaborationAsyncTask(retrofit, LOG_TAG, this);
        CollaborationInput input = new CollaborationInput();
        input.setUuid(InstanceIdService.getAppInstanceId(this));
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
        Log.d(LOG_TAG, "Waiting for response...");
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(this, ContinuousCaptureActivity.class);
//        startActivity(intent);
    }

    @Override
    public void updateTeamData(List<CollaborationResponse> responses) {
        List<TeamSummaryListItem> itemList = new ArrayList<TeamSummaryListItem>();
        String previousOrg = "";
        for (CollaborationResponse r : responses) {
            if (!previousOrg.equals(r.getOrgName())) {
                TeamSummaryListItem item = new TeamSummaryListItem();
                item.setOrg(true);
                item.setOrgName(r.getOrgName());
                itemList.add(item);
                previousOrg = r.getOrgName();
            }
            TeamSummaryListItem item = new TeamSummaryListItem();
            item.setTeamName(r.getTeamName());
            item.setNumberOfMembers(r.getTotalMember());
            item.setCreatedDate(DatetimeUtil.getDateFromGMT(r.getCreationDate()));
            item.setColocated(r.getCoLocated());
            item.setOrg(false);
            item.setTeamId(r.getTeamId());
            itemList.add(item);
        }
        teamData.addItems(itemList);
        adapter.notifyDataSetChanged();
    }

}
