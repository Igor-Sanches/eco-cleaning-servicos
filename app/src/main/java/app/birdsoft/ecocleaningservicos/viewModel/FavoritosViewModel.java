package app.birdsoft.ecocleaningservicos.viewModel;

import android.content.Context;
import android.widget.CheckBox;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.ecocleaningservicos.MainActivity;
import app.birdsoft.ecocleaningservicos.model.FavoritosElements;
import app.birdsoft.ecocleaningservicos.model.Servico;
import app.birdsoft.ecocleaningservicos.repository.FavoritosRepository;

public class FavoritosViewModel extends ViewModel {
    private MutableLiveData<FavoritosElements> mutableLiveData;

    public void init(Context contextx){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = FavoritosRepository.getInstance(contextx);
    }

    public MutableLiveData<FavoritosElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            FavoritosRepository.update(mutableLiveData, context);
        }

    }

    public LiveData<Boolean> favoritar(CheckBox checkBox, Servico servico, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        FavoritosRepository.favoritar(checkBox, servico, context, data);
        return data;
    }
}
