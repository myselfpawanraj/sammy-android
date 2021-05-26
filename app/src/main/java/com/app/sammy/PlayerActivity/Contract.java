package com.app.sammy.PlayerActivity;

import com.app.sammy.Models.AudioRequest.AudIdReq;
import com.app.sammy.Models.Finalrequest.IdReq;

public class Contract {
    interface View {
        void onError(String body);

        void onVideoSuccess(IdReq body);

        void onAudioSuccess(AudIdReq body);

        void onSceneSuccess(String body);
    }

    interface Presenter {
        void getVideo(String media_id);

        void getAudioData(String media_id2);

        void getSceneSearch(long time, String media_url, IdReq idReq);
    }
}
