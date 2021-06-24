package app.birdsoft.ecocleaningservicos.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.model.ClienteElements;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.repository.ClienteRepository;

public class ClienteViewModel extends ViewModel {
    private MutableLiveData<ClienteElements> mutableLiveData, clienteUid;

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = ClienteRepository.getInstance(context);
    }

    public void init(String uid, Context context){
        clienteUid = ClienteRepository.getInstanceUid(uid, context);
    }

    public MutableLiveData<ClienteElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public MutableLiveData<ClienteElements> getMutableLiveDataUID(){
        return clienteUid;
    }

    public void update(Context context) {
        if(mutableLiveData != null){
            ClienteRepository.update(mutableLiveData, context);
        }
    }

    public void update(String uid, Context context) {
        if(uid != null){
            if(clienteUid != null){
                ClienteRepository.update(uid, clienteUid, context);
            }
        }
    }

    public LiveData<Boolean> update(String value, String key, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ClienteRepository.update(value, key, context, data);
        return data;
    }

    public LiveData<Integer> trocaEmail(String email, Context context) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        ClienteRepository.trocaEmail(email, context, data);
        return data;
    }

    public LiveData<Integer> trocaSenha(String senha) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        ClienteRepository.trocaSenha(senha, data);
        return data;
    }

    public LiveData<Boolean> trocaEndereco(Endereco endereco, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ClienteRepository.trocaEndereco(endereco, context, data);
        return data;
    }

    public LiveData<Boolean> atualizarCidade(String cidadeSelecionada, Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ClienteRepository.atualizarCidade(cidadeSelecionada, context, data);
        return data;
    }

    public LiveData<Integer> apagarConta(Cliente cliente) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        ClienteRepository.apagarConta(cliente, data);
        return data;
    }
}
