package app.birdsoft.ecocleaningservicos.repository;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.model.Notification;
import app.birdsoft.ecocleaningservicos.model.NotificationElements;

public class NotificationsRepository {
    private static MutableLiveData<NotificationElements> elementos;
    public static MutableLiveData<NotificationElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<NotificationElements> GetElements(Context context) {
        MutableLiveData<NotificationElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<NotificationElements> data, Context context) {
        NotificationElements notificationElements = new NotificationElements();
        List<Notification> notifications = new NotificationDAO(context).lista();
        if(notifications != null){
            if(notifications.size() > 0){
                int numberNl = 0;
                for(Notification notification : notifications){
                    if(!notification.isLida())
                        numberNl += 1;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    notifications.sort(Comparator.comparingLong(Notification::getData).reversed());
                }

                notificationElements.setNumberNaoLida(numberNl);
                notificationElements.setNotifications(notifications);
                notificationElements.setLayoutConnectionVisibility(View.GONE);
                notificationElements.setLayoutProgressVisibility(View.GONE);
                notificationElements.setLayoutVazioVisibility(View.GONE);
                notificationElements.setLayoutVisibility(View.VISIBLE);
                notificationElements.setLayoutWifiVisibility(View.GONE);
            }else{
                notificationElements.setNotifications(new ArrayList<>());
                notificationElements.setLayoutConnectionVisibility(View.GONE);
                notificationElements.setLayoutProgressVisibility(View.GONE);
                notificationElements.setLayoutVazioVisibility(View.VISIBLE);
                notificationElements.setLayoutVisibility(View.GONE);
                notificationElements.setLayoutWifiVisibility(View.GONE);
            }
            data.setValue(notificationElements);
        }else{
            notificationElements.setNotifications(new ArrayList<>());
            notificationElements.setLayoutConnectionVisibility(View.GONE);
            notificationElements.setLayoutProgressVisibility(View.GONE);
            notificationElements.setLayoutVazioVisibility(View.VISIBLE);
            notificationElements.setLayoutVisibility(View.GONE);
            notificationElements.setLayoutWifiVisibility(View.GONE);
            data.setValue(notificationElements);
        }
    }

    public static void update(MutableLiveData<NotificationElements> data, Context context) {
        getFirebaseData(data, context);
    }

}
