package app.birdsoft.ecocleaningservicos.servicos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import app.birdsoft.ecocleaningservicos.manager.FirebaseUtils;
import app.birdsoft.ecocleaningservicos.model.MyResponse;
import app.birdsoft.ecocleaningservicos.model.NotificationData;
import app.birdsoft.ecocleaningservicos.model.NotificationSender;
import app.birdsoft.ecocleaningservicos.model.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNotification {
    private APIService apiService;
    public SendNotification(){
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    }

    public static void UpdateToken(String uid){
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseUtils.getDatabaseRef().child("Tokens").child(uid).setValue(token);
    }

    public MutableLiveData<Boolean> onPush(NotificationData data, Context context){
        MutableLiveData<Boolean> data1 = new MutableLiveData<>();
        FirebaseUtils.getDatabaseRef().child("AdminTokens").child("Tokens").child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String usertoken = dataSnapshot.getValue(String.class);
                    sendNotifications(usertoken, data, data1);
                }else data1.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                data1.setValue(false);
            }
        });
        return data1;
    }

    private void sendNotifications(String usertoken, NotificationData data, MutableLiveData<Boolean> data1) {
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    data1.setValue(response.body().success == 1);
                }else{
                    data1.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                data1.setValue(false);
            }
        });
    }

}
