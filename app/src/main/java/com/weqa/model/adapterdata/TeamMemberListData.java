package com.weqa.model.adapterdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish Ballav on 9/20/2017.
 */

public class TeamMemberListData {

    String[] fnames = { "Manish", "Anil", "Amarjit"};
    String[] lnames = { "Ballav", "Singh", "Singh"};
    String[] desigs = { "Software Developer", "System Analyst", "Business Analyst"};
    String[] mobile = { "9032018089", "32198908", "31208321"};

    ArrayList<TeamMemberListItem> members = new ArrayList<TeamMemberListItem>();

    public TeamMemberListData() {
        for (int i = 0; i<3; i++) {
            TeamMemberListItem item = new TeamMemberListItem();
            item.setFirstName(fnames[i]);
            item.setLastName(lnames[i]);
            item.setDesignation(desigs[i]);
            item.setMobile(mobile[i]);
            members.add(item);
        }
    }

    public ArrayList<TeamMemberListItem> getListData() {
        return members;
    }

    public void addMembers(List<TeamMemberListItem> newMembers) {
        members.addAll(newMembers);
    }
}
