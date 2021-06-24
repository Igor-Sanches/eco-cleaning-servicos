package app.birdsoft.ecocleaningservicos.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.ecocleaningservicos.model.NotificationElements;
import app.birdsoft.ecocleaningservicos.repository.NotificationsRepository;

public class NotificationsViewModel extends ViewModel {
    private MutableLiveData<NotificationElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = NotificationsRepository.getInstance(context);
    }

    public MutableLiveData<NotificationElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            NotificationsRepository.update(mutableLiveData, context);
        } else init(context);
    }

}
