package com.app.sammy.Api;

import android.provider.MediaStore;

import com.app.sammy.Models.AudioRequest.AudIdReq;
import com.app.sammy.Models.Finalrequest.IdReq;
import com.app.sammy.Models.Responce;
import com.app.sammy.Models.TimeStamps;
import com.app.sammy.Models.sceneDes;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class Api {
    private static final String BASE_URL = "https://sm-web2.herokuapp.com/";

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
        Call<IdReq> getTimestamps(
                @Path( "id" ) String id,
                @Query("mode") String mode
        );
        @GET("/vid/single")
        Call<sceneDes> getSceneDescribed(
                @Query("url") String url,
                @Query("timestamp") long timestamp,
                @Query("onlyString") String str
        );
        @GET("job/{id}/logs")
        Call<String> getLogs(
                @Path( "id" ) String id,
                @Query("mode") String updn
        );
        @POST("vid/sd")
        Call<Responce> uploadByLink(
                @Query("url") String url,
                @Query("audio") boolean audio
        );
//        @Multipart
//        @POST("vid/up")
//        Call<Responce> uploadByVid(
//                @PartMap Map<String, RequestBody> map
//        );
    }

}
