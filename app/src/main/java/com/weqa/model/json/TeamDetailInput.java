package com.weqa.model.json;

/**
 * Created by Manish Ballav on 9/21/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamDetailInput {

    @SerializedName("TeamId")
    @Expose
    private Integer teamId;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
}

