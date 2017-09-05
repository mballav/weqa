package com.weqa.model.adapterdata;

import android.content.Context;

import com.weqa.model.Authorization;
import com.weqa.model.FloorPlanDetailV2;
import com.weqa.model.FloorplanResponseV2;
import com.weqa.model.ItemTypeDetailV2;
import com.weqa.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome on 20-06-2017.
 */

public class AvailListData {

    private FloorplanResponseV2 response;
    private SharedPreferencesUtil util;
    private List<Authorization> authList;
    private long buildingId;

    public AvailListData(FloorplanResponseV2 r, SharedPreferencesUtil util, long selectedBuildingId) {
        this.response = r;
        this.util = util;
        this.buildingId = selectedBuildingId;
        this.authList = this.util.getAuthorizationInfo(selectedBuildingId);
    }

    public void update(FloorplanResponseV2 rUpdate) {
        List<FloorPlanDetailV2> fList = rUpdate.getFloorPlanDetails();
        for (FloorPlanDetailV2 f : fList) {
            List<ItemTypeDetailV2> iList = f.getItemTypeDetail();
            for (ItemTypeDetailV2 i : iList) {
                setItemCount(f.getFloorPlanId(), i.getItemTypeId(), i.getItemCount());
            }
        }
    }

    private void setItemCount(Integer floorplanId, Integer itemTypeId, Integer itemCount) {
        List<FloorPlanDetailV2> fList = this.response.getFloorPlanDetails();
        for (FloorPlanDetailV2 f : fList) {
            if (f.getFloorPlanId().equals(floorplanId)) {
                List<ItemTypeDetailV2> iList = f.getItemTypeDetail();
                for (ItemTypeDetailV2 i : iList) {
                    if (i.getItemTypeId().equals(itemTypeId)) {
                        i.setItemCount(itemCount);
                    }
                }
            }
        }
    }

    public List<AvailListItem> getListData(int itemTypeId) {
        List<AvailListItem> data = new ArrayList<>();
        List<FloorPlanDetailV2> fList = response.getFloorPlanDetails();
        for (FloorPlanDetailV2 f : fList) {
            AvailListItem item = new AvailListItem();
            item.setFloorNumber(getFloorLevel(this.buildingId, f.getFloorPlanId()));
            item.setAvailability(0);
            List<ItemTypeDetailV2> iList = f.getItemTypeDetail();
            for (ItemTypeDetailV2 i : iList) {
                if (i.getItemTypeId() == itemTypeId) {
                    item.setAvailability(i.getItemCount());
                    break;
                }
            }
            data.add(item);
        }
        return data;
    }

    public int getFloorLevel(long buildingId, long floorplanId) {
        for (Authorization a : this.authList) {
            if (Long.parseLong(a.getFloorPlanId()) == floorplanId) {
                return Integer.parseInt(a.getFloorLevel());
            }
        }
        return 0;
    }
}
