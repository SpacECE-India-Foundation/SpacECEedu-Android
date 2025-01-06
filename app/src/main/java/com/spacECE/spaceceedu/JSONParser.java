package com.spacECE.spaceceedu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONParser {

    @GET("api_activity?ano=1")
    Call<List<ActivityData>> getActivites();

}
