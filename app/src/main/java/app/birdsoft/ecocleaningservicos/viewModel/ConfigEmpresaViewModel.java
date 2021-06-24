package app.birdsoft.ecocleaningservicos.viewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.ecocleaningservicos.model.ConfigEmpresaElements;
import app.birdsoft.ecocleaningservicos.repository.ConfigEmpresaRepository;

public class ConfigEmpresaViewModel extends ViewModel {
    private MutableLiveData<ConfigEmpresaElements> configEmpresaElements;

    public void init(Context context){
        configEmpresaElements = ConfigEmpresaRepository.getInstance(context);
    }

    public MutableLiveData<ConfigEmpresaElements> getMutableLiveData(){
        return configEmpresaElements;
    }

}
