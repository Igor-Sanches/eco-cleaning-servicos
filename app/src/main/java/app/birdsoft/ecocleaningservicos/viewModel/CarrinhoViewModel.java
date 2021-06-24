package app.birdsoft.ecocleaningservicos.viewModel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.model.Carrinho;
import app.birdsoft.ecocleaningservicos.model.CarrinhoElements;
import app.birdsoft.ecocleaningservicos.repository.CarrinhoRepository;

public class CarrinhoViewModel extends ViewModel {
    private MutableLiveData<CarrinhoElements> mutableLiveData;

    public void init(Context context){
        if(mutableLiveData == null){
            mutableLiveData = CarrinhoRepository.getInstance(context);
        }
    }

    public MutableLiveData<CarrinhoElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            CarrinhoRepository.update(mutableLiveData, context);
        }
    }

    public void delete(Context context){
        CarrinhoRepository.delete(context);
    }

    public LiveData<Boolean> insert(Carrinho carrinho, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        CarrinhoRepository.insert(carrinho, data, context);
        return data;
    }

    public LiveData<Integer> delete(Carrinho carrinho, Activity context) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        DialogMessage message
                = new DialogMessage(context, context.getString(R.string.msg_deleta_carrinhp), true, context.getString(R.string.confirmar), context.getString(R.string.deleta_carrinho_titulo));
        message.show();
        message.setOnPossiveButtonClicked(() -> CarrinhoRepository.delete(carrinho, data, context));
        return data;
    }
}
