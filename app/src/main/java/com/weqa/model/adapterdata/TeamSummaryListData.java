package com.weqa.model.adapterdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Manish Ballav on 9/20/2017.
 */

public class TeamSummaryListData {

    private static String[] teamNames = { "Future Transport", "Project X-Files", "System B Automation", "Savage Garden"};

    private static int[] numMems = { 7, 15, 20, 12 };

    private static int[] colocated = { 4, 10, 5, 6 };

    private static int[] distributed = { 3, 3, 15, 5 };

    private static int[] notFound = { 0, 2, 0, 1 };

    private List<TeamSummaryListItem> items = new ArrayList<TeamSummaryListItem>();

    public List<TeamSummaryListItem> getListData() {
/*        for (int i = 0; i < 4; i++) {
            TeamSummaryListItem item = new TeamSummaryListItem();
            item.setTeamName(teamNames[i]);
            item.setNumberOfMembers(numMems[i]);
            item.setCreatedDate(new Date());
            item.setColocated(colocated[i]);
            item.setDistributed(distributed[i]);
            item.setNotFound(notFound[i]);
            items.add(item);
        }*/
        return items;
    }

    public void addItems(List<TeamSummaryListItem> newItems) {
        items.addAll(newItems);
    }
}
