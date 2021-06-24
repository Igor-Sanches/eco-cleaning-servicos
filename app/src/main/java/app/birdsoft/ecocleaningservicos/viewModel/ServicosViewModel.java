package app.birdsoft.ecocleaningservicos.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.birdsoft.ecocleaningservicos.model.HorariosFuncionamento;
import app.birdsoft.ecocleaningservicos.model.Pedido;
import app.birdsoft.ecocleaningservicos.model.PedidosElements;
import app.birdsoft.ecocleaningservicos.model.ServiceElements;
import app.birdsoft.ecocleaningservicos.model.Servico;
import app.birdsoft.ecocleaningservicos.repository.ServicosRepository;

public class ServicosViewModel extends ViewModel {
    private MutableLiveData<ServiceElements> mutableLiveData;
    private MutableLiveData<PedidosElements> contratosLiveData;

    public void initContratos(Context contextx){
        if(contratosLiveData != null){
            return;
        }
        contratosLiveData = ServicosRepository.getInstanceContratos(contextx);
    }

    public void init(Context context){
        if(mutableLiveData != null){
            return;
        }
        mutableLiveData = ServicosRepository.getInstance(context);
    }

    public MutableLiveData<ServiceElements> getMutableLiveData(){
        return mutableLiveData;
    }

    public MutableLiveData<PedidosElements> getMutableLiveDataContratos(){
        return contratosLiveData;
    }

    public void updateContratos(Context context) {
        if(contratosLiveData != null){
            ServicosRepository.updateContratos(contratosLiveData, context);
        }
    }

    public LiveData<Boolean> onChangedDataBase(Context context) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ServicosRepository.onChangedDataBase(data, context);
        return data;
    }

    public void updateServicos(Context context) {
        if(mutableLiveData != null){
            ServicosRepository.update(mutableLiveData, context);
        }
    }

    public LiveData<ArrayList<String>> getCidades() {
        MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
        ServicosRepository.getCidade(data);
        return data;
    }

    public LiveData<List<HorariosFuncionamento>> getHorarios() {
        MutableLiveData<List<HorariosFuncionamento>> data = new MutableLiveData<>();
        ServicosRepository.getHorarios(data);
        return data;
    }

    public LiveData<Integer> inflateServico(Pedido pedido) {
       MutableLiveData<Integer> data = new MutableLiveData<>();
       ServicosRepository.inflateServico(pedido, data);
       return data;
    }

    public LiveData<Boolean> update(Map<String, Object> map, Pedido pedido) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ServicosRepository.update(map, pedido, data);
        return data;
    }

    public LiveData<Boolean> relatarAtraso(Pedido pedido) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ServicosRepository.relatarAtraso(pedido, data);
        return data;
    }

    public LiveData<Boolean> cancelamento(Pedido pedido, String mensagem) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ServicosRepository.cancelamento(pedido, mensagem, data);
        return data;
    }

    public LiveData<Boolean> endereco(Map<String, Object> map, Pedido pedido) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ServicosRepository.endereco(pedido, map, data);
        return data;
    }

    public LiveData<Boolean> delete(Pedido pedido) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        ServicosRepository.delete(pedido, data);
        return data;
    }


    public LiveData<Integer> inflateServico(Pedido pedido, Map<String, Object> map) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        ServicosRepository.inflateServico(pedido, map, data);
        return data;
    }

    public LiveData<Servico> findItem(String uid) {
        MutableLiveData<Servico> data = new MutableLiveData<>();
        ServicosRepository.findItem(uid, data);
        return data;
    }
}
