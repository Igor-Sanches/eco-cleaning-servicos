package app.birdsoft.ecocleaningservicos.repository;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.model.ConfigEmpresa;
import app.birdsoft.ecocleaningservicos.model.ConfigEmpresaElements;
public class ConfigEmpresaRepository {
    private static MutableLiveData<ConfigEmpresaElements> elementos;
    public static MutableLiveData<ConfigEmpresaElements> getInstance(Context context) {
        if(elementos == null)
            elementos = getElements(context);

        return elementos;
    }

    private synchronized static MutableLiveData<ConfigEmpresaElements> getElements(Context context) {
        MutableLiveData<ConfigEmpresaElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static void getFirebaseData(MutableLiveData<ConfigEmpresaElements> data, Context context) {
        ConfigEmpresaElements elements = new ConfigEmpresaElements();
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.EMPRESA)
                .document(Chave.DADOS)
                .get()
                .addOnCompleteListener((result -> {
                    if(Conexao.isConnected(context)){
                        if(result.isSuccessful()){
                            ConfigEmpresa configEmpresa = result.getResult().toObject(ConfigEmpresa.class);
                            if(configEmpresa != null){
                                elements.setConfigEmpresa(configEmpresa);
                                elements.setAberto(configEmpresa.isAberto());
                                elements.setLayoutPrincipal(View.VISIBLE);
                                elements.setLayoutProgress(View.GONE);
                                elements.setLayoutConexao(View.GONE);
                                elements.setLayoutVazio(View.GONE);
                                elements.setLayoutWifiOffline(View.GONE);
                                data.setValue(elements);
                            }else{
                                elements.setConfigEmpresa(null);
                                elements.setLayoutPrincipal(View.GONE);
                                elements.setLayoutProgress(View.GONE);
                                elements.setLayoutConexao(View.GONE);
                                elements.setLayoutVazio(View.VISIBLE);
                                elements.setLayoutWifiOffline(View.GONE);
                                data.setValue(elements);
                            }
                        }else{
                            elements.setConfigEmpresa(null);
                            elements.setLayoutPrincipal(View.GONE);
                            elements.setLayoutProgress(View.GONE);
                            elements.setLayoutVazio(View.GONE);
                            elements.setLayoutConexao(View.VISIBLE);
                            elements.setLayoutWifiOffline(View.GONE);
                            data.setValue(elements);
                        }
                    }else{
                        elements.setConfigEmpresa(null);
                        elements.setLayoutPrincipal(View.GONE);
                        elements.setLayoutProgress(View.GONE);
                        elements.setLayoutConexao(View.GONE);
                        elements.setLayoutVazio(View.GONE);
                        elements.setLayoutWifiOffline(View.VISIBLE);
                        data.setValue(elements);
                    }
                }));
    }

}
