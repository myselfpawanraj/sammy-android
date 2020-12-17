package com.app.sammy.Api;

import com.app.sammy.Models.AudioRequest.AudIdReq;
import com.app.sammy.Models.Finalrequest.IdReq;
import com.app.sammy.Models.Responce;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiAudio {
    private static final String BASE_URL = "https://sammy-audio.herokuapp.com/";

    public static PostService postService = null;
    public static PostService getService()
    {
        if(postService==null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService=retrofit.create(PostService.class);
        }
        return postService;

    }

    public interface PostService {
        @GET("job/{id}/status")
        Call<AudIdReq> getSubtitles(
                @Path( "id" ) String id
        );
        @GET("job/{id}/logs")
        Call<String> getLogs(
                @Path( "id" ) String id
        );
    }

}
