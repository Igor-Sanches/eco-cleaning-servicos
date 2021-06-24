package app.birdsoft.ecocleaningservicos.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.model.SettingsElements;
import app.birdsoft.ecocleaningservicos.repository.SettingsRepository;

public class SettingsViewModel extends ViewModel {
    private MutableLiveData<SettingsElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = SettingsRepository.getInstance(context);
    }

    public MutableLiveData<SettingsElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            SettingsRepository.update(mutableLiveData, context);
        } else init(context);
    }

    public LiveData<Boolean> trocaEndereco(Endereco endereco, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        SettingsRepository.trocaEndereco(endereco, context, data);
        return data;
    }
}
