package app.birdsoft.ecocleaningservicos.servicos;

import app.birdsoft.ecocleaningservicos.model.MyResponse;
import app.birdsoft.ecocleaningservicos.model.NotificationSender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAT7iESdk:APA91bEKaJwDApViozlyi_65GwPl3vu9hPTqrjqMx-zhNrmTG_Lidy_x_vv3aq7ImLsUfL1LihF8mgg_tVJAABQCvbzi9_OLA9Cq_gdS57sx-0WO1VNwdfYgfZ9tX1sfuOIvq-aPrSaL"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
