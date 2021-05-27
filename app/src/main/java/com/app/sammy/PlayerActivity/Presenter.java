package com.app.sammy.PlayerActivity;

import android.widget.Toast;

import com.app.sammy.Api.Api;
import com.app.sammy.Api.ApiAudio;
import com.app.sammy.Models.AudioRequest.AudIdReq;
import com.app.sammy.Models.Finalrequest.Caption;
import com.app.sammy.Models.Finalrequest.IdReq;
import com.app.sammy.Models.sceneDes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Presenter implements Contract.Presenter {
    Contract.View view;

    public Presenter(Contract.View view) {
        this.view = view;
    }

    public void getVideo(String media_id) {
        Call< IdReq > call;
        call =  Api.getService().getTimestamps(media_id,"sd");
        call.enqueue(new Callback<IdReq>() {
            @Override
            public void onResponse(Call<IdReq> call, Response<IdReq> response) {
                if(response!=null && response.isSuccessful()){
                    view.onVideoSuccess(response.body());
                }
                else{
                    view.onError("Failed getting the Video data");
                }
            }

            @Override
            public void onFailure(Call<IdReq> call, Throwable t) {
                view.onError("Failed getting the Video data");
            }
        });
    }

    public void getAudioData(String media_id2) {
        Call< AudIdReq > call;
        call = ApiAudio.getService().getSubtitles(media_id2);
        call.enqueue(new Callback<AudIdReq>() {
            @Override
            public void onResponse(Call<AudIdReq> call, Response<AudIdReq> response) {
                if(response!=null && response.isSuccessful()){
                    view.onAudioSuccess(response.body());
                }
                else{
                    view.onError("Failed getting the Audio data");
                }
            }

            @Override
            public void onFailure(Call<AudIdReq> call, Throwable t) {
                view.onError("Failed getting the Audio data");
            }
        });
    }

    public void getSceneSearch(long time, String media_url, IdReq idReq) {
        Call< sceneDes > call;
        call = Api.getService().getSceneDescribed(media_url, (time / 1000), "true");
        call.enqueue(new Callback< sceneDes >() {
            @Override
            public void onResponse(Call< sceneDes > call, Response< sceneDes > response) {
                if (response.isSuccessful() && response != null) {
                    String s = response.body().getCaption();
                    if (s.toLowerCase().contains("text") || s.toLowerCase().contains("logo")) {
                        List< Caption > captionList = idReq.getResponseFinal().getCaptions();
                        for (Caption c : captionList) {
                            if (c.getTime() == time) {
                                view.onSceneSuccess(c.getOcr());
                                break;
                            }
                        }
                    } else
                        view.onSceneSuccess(s);
                } else {
                    view.onError("Failed");
                }
            }

            @Override
            public void onFailure(Call< sceneDes > call, Throwable t) {
                view.onError("Failed!");
            }
        });
    }
}
