package app.birdsoft.ecocleaningservicos.repository;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.FavoritosElements;
import app.birdsoft.ecocleaningservicos.model.Servico;
import app.birdsoft.ecocleaningservicos.manager.Conexao;

public class FavoritosRepository {
    private static MutableLiveData<FavoritosElements> elementos;
    public static MutableLiveData<FavoritosElements> getInstance(Context context) {
        if(elementos == null){
            elementos = GetElements(context);
        }
        return elementos;
    }

    private synchronized static MutableLiveData<FavoritosElements> GetElements(Context context) {
        MutableLiveData<FavoritosElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<FavoritosElements> data, Context context) {
        FavoritosElements favoritosElements = new FavoritosElements();
        FireStoreUtils.getDatabase()
                .collection(Chave.SERVICOS)
                .whereEqualTo(Usuario.getUid(context), true)
                .get().addOnCompleteListener((result -> {
                    if(Conexao.isConnected(context)){
                        if(result.isSuccessful()){
                            if(result.getResult() != null){
                                List<Servico> servicos = new ArrayList<>();
                                for (DocumentSnapshot snapshot : result.getResult()){
                                    Servico servico = snapshot.toObject(Servico.class);
                                    Map<String, Object> map = snapshot.getData();
                                    if(map.get(Usuario.getUid(context)) != null)
                                        servico.setFavorito((boolean)map.get(Usuario.getUid(context)));

                                    servicos.add(servico);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    servicos.sort(Comparator.comparingLong(Servico::getPosition));
                                }
                                if(servicos.size() > 0){
                                    favoritosElements.setProgressVisibility(View.GONE);
                                    favoritosElements.setVazioVisibility(View.GONE);
                                    favoritosElements.setLayoutConexao(View.GONE);
                                    favoritosElements.setListaVisibility(View.VISIBLE);
                                    favoritosElements.setLayoutWifiOffline(View.GONE);
                                }else{
                                    favoritosElements.setListaVisibility(View.GONE);
                                    favoritosElements.setProgressVisibility(View.GONE);
                                    favoritosElements.setLayoutWifiOffline(View.GONE);
                                    favoritosElements.setLayoutConexao(View.GONE);
                                    favoritosElements.setVazioVisibility(View.VISIBLE);
                                }
                                favoritosElements.setServicos(servicos);
                                data.setValue(favoritosElements);
                            }else{
                                favoritosElements.setListaVisibility(View.GONE);
                                favoritosElements.setProgressVisibility(View.GONE);
                                favoritosElements.setLayoutConexao(View.GONE);
                                favoritosElements.setLayoutWifiOffline(View.GONE);
                                favoritosElements.setVazioVisibility(View.VISIBLE);
                                data.setValue(favoritosElements);
                            }
                        }else{
                            favoritosElements.setListaVisibility(View.GONE);
                            favoritosElements.setProgressVisibility(View.GONE);
                            favoritosElements.setLayoutConexao(View.VISIBLE);
                            favoritosElements.setVazioVisibility(View.GONE);
                            favoritosElements.setLayoutWifiOffline(View.GONE);
                            data.setValue(favoritosElements);
                        }
                    }else{
                        favoritosElements.setListaVisibility(View.GONE);
                        favoritosElements.setProgressVisibility(View.GONE);
                        favoritosElements.setLayoutConexao(View.GONE);
                        favoritosElements.setVazioVisibility(View.GONE);
                        favoritosElements.setLayoutWifiOffline(View.VISIBLE);
                        data.setValue(favoritosElements);
                    }
        }));
    }

    public static void update(MutableLiveData<FavoritosElements> data, Context context) {
        getFirebaseData(data, context);
    }

    public static void favoritar(CheckBox checkBox, Servico servico, Context context, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.SERVICOS)
                .document(servico.getUid())
                .update(Usuario.getUid(context), checkBox.isChecked()).addOnCompleteListener((result -> data.setValue(result.isSuccessful())));
    }
}
