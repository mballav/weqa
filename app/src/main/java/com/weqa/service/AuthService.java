package com.weqa.service;

import com.weqa.model.AuthInput;
import com.weqa.model.AuthResponse;
import com.weqa.model.Availability;
import com.weqa.model.AvailabilityInput;
import com.weqa.model.FloorplanImage;
import com.weqa.model.FloorplanImageInput;
import com.weqa.model.FloorplanInput;
import com.weqa.model.FloorplanResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Manish Ballav on 8/5/2017.
 */

public interface AuthService {

    @POST("api/security/AuthorizeAuthorizeUser")
    Call<AuthResponse> auth(@Body AuthInput input);

    @POST("api/security/ItemType")
    Call<List<Availability>> availability(@Body AvailabilityInput input);

    @POST("api/security/GetFloorPlanImage")
    Call<FloorplanImage> floorplanImage(@Body FloorplanImageInput input);

    @POST("api/security/FloorInfo")
    Call<FloorplanResponse> floorplan(@Body FloorplanInput input);
}
