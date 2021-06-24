package app.birdsoft.ecocleaningservicos.repository;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.manager.FirebaseUtils;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.Status;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.ConfigEmpresa;
import app.birdsoft.ecocleaningservicos.model.HorariosFuncionamento;
import app.birdsoft.ecocleaningservicos.model.Pedido;
import app.birdsoft.ecocleaningservicos.model.PedidosElements;
import app.birdsoft.ecocleaningservicos.model.ServiceElements;
import app.birdsoft.ecocleaningservicos.model.Servico;
import app.birdsoft.ecocleaningservicos.tools.DateTime;

public class ServicosRepository {
    private static MutableLiveData<ServiceElements> elementos;
    private static MutableLiveData<PedidosElements> elementosContratos;

    public static MutableLiveData<ServiceElements> getInstance(Context context) {
        if(elementos == null)
            elementos = getElements(context);

        return elementos;
    }

    private synchronized static MutableLiveData<ServiceElements> getElements(Context context) {
        MutableLiveData<ServiceElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    public static void update(MutableLiveData<ServiceElements> data, Context context) {
        getFirebaseData(data, context);
    }

    private static void getFirebaseData(MutableLiveData<ServiceElements> data, Context context) {
        ServiceElements elements = new ServiceElements();
        Query query = FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.SERVICOS);

        query = query.whereEqualTo("disponivel", true);
        query.get()
                .addOnCompleteListener((result -> {
                    if(Conexao.isConnected(context)){
                        if(result.isSuccessful()){
                            if(result.getResult() != null){
                                List<Servico> servicos = new ArrayList<>();
                                for (DocumentSnapshot snapshot : result.getResult()){
                                    Servico servico = snapshot.toObject(Servico.class);
                                    if(servico != null){
                                        Map<String, Object> map = snapshot.getData();
                                        if(map != null){
                                            if(map.get(Usuario.getUid(context)) != null)
                                                servico.setFavorito((boolean)map.get(Usuario.getUid(context)));
                                        }

                                        servicos.add(servico);
                                    }
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    servicos.sort(Comparator.comparingInt(Servico::getPosition));
                                }
                                if(servicos.size() > 0){
                                    elements.setListaVisibility(View.VISIBLE);
                                    elements.setLayoutConexaoVisibility(View.GONE);
                                    elements.setProgressVisibility(View.GONE);
                                    elements.setVazioVisibility(View.GONE);
                                    elements.setLayoutWifiOffline(View.GONE);
                                }else{
                                    elements.setListaVisibility(View.GONE);
                                    elements.setLayoutConexaoVisibility(View.GONE);
                                    elements.setProgressVisibility(View.GONE);
                                    elements.setVazioVisibility(View.VISIBLE);
                                    elements.setLayoutWifiOffline(View.GONE);
                                }
                                elements.setServicos(servicos);
                                data.setValue(elements);
                            }else{
                                elements.setListaVisibility(View.GONE);
                                elements.setProgressVisibility(View.GONE);
                                elements.setVazioVisibility(View.GONE);
                                elements.setLayoutConexaoVisibility(View.VISIBLE);
                                elements.setLayoutWifiOffline(View.GONE);
                                elements.setServicos(null);
                                data.setValue(elements);
                            }


                        }else{
                            elements.setListaVisibility(View.GONE);
                            elements.setProgressVisibility(View.GONE);
                            elements.setVazioVisibility(View.GONE);
                            elements.setLayoutConexaoVisibility(View.VISIBLE);
                            elements.setLayoutWifiOffline(View.GONE);
                            elements.setServicos(null);
                            data.setValue(elements);
                        }
                    }else{
                        elements.setListaVisibility(View.GONE);
                        elements.setLayoutConexaoVisibility(View.GONE);
                        elements.setProgressVisibility(View.GONE);
                        elements.setVazioVisibility(View.GONE);
                        elements.setLayoutWifiOffline(View.VISIBLE);
                        elements.setServicos(null);
                        data.setValue(elements);
                    }
                }));
    }

    public static void getCidade(MutableLiveData<ArrayList<String>> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.EMPRESA)
                .document(Chave.DADOS)
                .get()
                .addOnCompleteListener((result -> {
                    if(result.isSuccessful()){
                        ConfigEmpresa configEmpresa = Objects.requireNonNull(result.getResult()).toObject(ConfigEmpresa.class);
                        if(configEmpresa != null){
                            ArrayList<String> cidades = configEmpresa.getLocaisFucionamentos();
                            data.setValue(cidades);
                        }else data.setValue(null);
                    }else data.setValue(null);
                }));
    }

    public static void onChangedDataBase(MutableLiveData<Boolean> data, Context context) {
        FirebaseUtils
                .getDatabaseRef()
                .child(Chave.CONTRATOS)
                .child(Usuario.getUid(context))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data.setValue(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        data.setValue(false);
                    }
                });
    }

    public static void getHorarios(MutableLiveData<List<HorariosFuncionamento>> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.EMPRESA)
                .document(Chave.DADOS)
                .get()
                .addOnCompleteListener((result -> {
                    if(result.isSuccessful()){
                        ConfigEmpresa configEmpresa = Objects.requireNonNull(result.getResult()).toObject(ConfigEmpresa.class);
                        if(configEmpresa != null){
                            ArrayList<HorariosFuncionamento> horariosFuncionamentos = configEmpresa.getHorariosFuncionamentos();
                            data.setValue(horariosFuncionamentos);
                        }else data.setValue(null);
                    }else data.setValue(null);
                }));
    }

    public static void inflateServico(Pedido pedido, MutableLiveData<Integer> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .whereEqualTo("dataAgendamento", pedido.getDataAgendamento())
                .get().addOnCompleteListener((result) -> {
                    if(result.isSuccessful()){
                        QuerySnapshot _result = result.getResult();
                        if(_result.getDocuments().size() == 0){
                            inflate(pedido, data);
                        }else{
                            Pedido up = null;
                            for(DocumentSnapshot doc : _result.getDocuments()){
                                Pedido old = doc.toObject(Pedido.class);
                                if(old != null){
                                    if(up == null)
                                        up = old;

                                    if(DateTime.isMaiorServicoHorario(old.getFimDoHorarioTotal(), up.getFimDoHorarioTotal(), pedido.getDataAgendamento())){
                                        up = old;
                                    }
                                }
                            }

                            if(up != null){
                                String horario = DateTime.add(up.getFimDoHorarioTotal());
                                if(DateTime.isFechadoServico(horario, pedido.getHorarioAgendamento(), pedido.getDataAgendamento())){
                                    inflate(pedido, data);
                                }else{
                                    data.setValue(R.string.horario_ja_usado);
                                }

                            }else{
                                inflate(pedido, data);
                            }
                        }

                    }else data.setValue(R.string.erro_conexao_servico);
        });
    }

    private static void inflate(Pedido pedido,MutableLiveData<Integer> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .set(pedido)
                .addOnCompleteListener((result) -> {
                   if(result.isSuccessful()){
                       onChanged(pedido.getUid_client());
                       data.setValue(R.string.servico_adicionado);
                   }else{
                       data.setValue(R.string.falha_ao_adicionar);
                   }
                });
    }

    public static void onChanged(String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("changed", DateTime.getTime());
        FirebaseUtils
                .getDatabaseRef()
                .child(Chave.CONTRATOS)
                .child(uid).setValue(map);
    }

    public static MutableLiveData<PedidosElements> getInstanceContratos(Context context) {
        if(elementosContratos == null)
            elementosContratos = getElementsContratos(context);

        return elementosContratos;
    }

    private synchronized static MutableLiveData<PedidosElements> getElementsContratos(Context context) {
        MutableLiveData<PedidosElements> data = new MutableLiveData<>();
        getFirebaseDataContratos(data, context);
        return data;
    }

    private static void getFirebaseDataContratos(MutableLiveData<PedidosElements> data, Context context) {
        PedidosElements pedidosElements = new PedidosElements();
        Query query = FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS);
        query = query.whereEqualTo("uid_client", Usuario.getUid(context));
        query = query.whereNotEqualTo("statusServico", Status.rejeitado.toString());

        query.get().addOnCompleteListener((result -> {
            if(Conexao.isConnected(context)){
                if(result.isSuccessful()){
                    if(result.getResult() != null){
                        List<Pedido> pedidos = new ArrayList<>();
                        for (DocumentSnapshot snapshot : result.getResult()){
                            Pedido pedido = snapshot.toObject(Pedido.class);
                            assert pedido != null;
                            pedidos.add(pedido);

                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            pedidos.sort(Comparator.comparingLong(Pedido::getDataServico).reversed());
                        }
                        if(pedidos.size() > 0){
                            pedidosElements.setLayoutProgressVisibility(View.GONE);
                            pedidosElements.setLayoutVazioVisibility(View.GONE);
                            pedidosElements.setLayoutConnectionVisibility(View.GONE);
                            pedidosElements.setLayoutVisibility(View.VISIBLE);
                            pedidosElements.setLayoutWifiVisibility(View.GONE);
                        }else{
                            pedidosElements.setLayoutVisibility(View.GONE);
                            pedidosElements.setLayoutProgressVisibility(View.GONE);
                            pedidosElements.setLayoutWifiVisibility(View.GONE);
                            pedidosElements.setLayoutConnectionVisibility(View.GONE);
                            pedidosElements.setLayoutVazioVisibility(View.VISIBLE);
                        }
                        pedidosElements.setPedidos(pedidos);
                        data.setValue(pedidosElements);
                    }else{
                        pedidosElements.setLayoutVisibility(View.GONE);
                        pedidosElements.setLayoutProgressVisibility(View.GONE);
                        pedidosElements.setLayoutConnectionVisibility(View.GONE);
                        pedidosElements.setLayoutWifiVisibility(View.GONE);
                        pedidosElements.setLayoutVazioVisibility(View.VISIBLE);
                        data.setValue(pedidosElements);
                    }
                }else{
                    System.out.println(result.getException().getMessage());
                    pedidosElements.setLayoutVisibility(View.GONE);
                    pedidosElements.setLayoutProgressVisibility(View.GONE);
                    pedidosElements.setLayoutConnectionVisibility(View.VISIBLE);
                    pedidosElements.setLayoutVazioVisibility(View.GONE);
                    pedidosElements.setLayoutWifiVisibility(View.GONE);
                    data.setValue(pedidosElements);
                }
            }else{
                pedidosElements.setLayoutVisibility(View.GONE);
                pedidosElements.setLayoutProgressVisibility(View.GONE);
                pedidosElements.setLayoutConnectionVisibility(View.GONE);
                pedidosElements.setLayoutVazioVisibility(View.GONE);
                pedidosElements.setLayoutWifiVisibility(View.VISIBLE);
                data.setValue(pedidosElements);
            }
        }));
    }

    public static void updateContratos(MutableLiveData<PedidosElements> data, Context context) {
        getFirebaseDataContratos(data, context);
    }

    public static void update(Map<String, Object> map, Pedido pedido, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .update(map)
                .addOnCompleteListener((result) ->{
                    if(result.isSuccessful())
                        onChanged(pedido.getUid_client());
                    data.setValue(result.isSuccessful());
                });

    }

    public static void relatarAtraso(Pedido pedido, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .update("atrasado", true)
                .addOnCompleteListener((result) -> {
                    if(result.isSuccessful()) onChanged(pedido.getUid_client());
                    data.setValue(result.isSuccessful());
                });
    }

    public static void cancelamento(Pedido pedido, String mensagem, MutableLiveData<Boolean> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("cancelado", true);
        map.put("msgCancelamento", mensagem);
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .update(map)
                .addOnCompleteListener((result) -> {
                    if(result.isSuccessful()) onChanged(pedido.getUid_client());
                    data.setValue(result.isSuccessful());
                });
    }

    public static void endereco(Pedido pedido, Map<String, Object> map, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .update(map)
                .addOnCompleteListener((result) -> {
                    if(result.isSuccessful()) onChanged(pedido.getUid_client());
                    data.setValue(result.isSuccessful());
                });
    }

    public static void delete(Pedido pedido, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .delete().addOnCompleteListener((result ->
        {if(result.isSuccessful()){
            FirebaseUtils
                    .getDatabaseRef()
                    .child(Chave.CONTRATOS)
                    .child(pedido.getUid_client()).removeValue();
        }
        data.setValue(result.isSuccessful());
        }));
    }

    public static void inflateServico(Pedido pedido, Map<String, Object> map, MutableLiveData<Integer> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CONTRATOS)
                .whereEqualTo("dataAgendamento", pedido.getDataAgendamento())
                .get().addOnCompleteListener((result) -> {
            if(result.isSuccessful()){
                QuerySnapshot _result = result.getResult();
                if(_result.getDocuments().size() == 0){
                    inflate(pedido, map, data);
                }else{
                    Pedido up = null;
                    for(DocumentSnapshot doc : _result.getDocuments()){
                        Pedido old = doc.toObject(Pedido.class);
                        if(!old.getUid().equals(pedido.getUid())){
                            if(old != null){
                                if(up == null)
                                    up = old;

                                if(DateTime.isMaiorServicoHorario(old.getFimDoHorarioTotal(), up.getFimDoHorarioTotal(), pedido.getDataAgendamento())){
                                    up = old;
                                }
                            }
                        }
                    }

                    if(up != null){
                        String horario = DateTime.add(up.getFimDoHorarioTotal());
                        if(DateTime.isFechadoServico(horario, pedido.getHorarioAgendamento(), pedido.getDataAgendamento())){
                            inflate(pedido, map, data);
                        }else{
                            data.setValue(R.string.horario_ja_usado);
                        }

                    }else{
                        inflate(pedido, map, data);
                    }
                }

            }else data.setValue(R.string.erro_conexao_servico);
        });
    }

    private static void inflate(Pedido pedido, Map<String, Object> map, MutableLiveData<Integer> data) {
        FireStoreUtils.getDatabase()
                .collection(Chave.CONTRATOS)
                .document(pedido.getUid())
                .update(map)
                .addOnCompleteListener((result) -> {
                    if(result.isSuccessful()){
                        onChanged(pedido.getUid_client());
                        data.setValue(R.string.servico_adicionado);
                    }else{
                        data.setValue(R.string.falha_ao_adicionar);
                    }
                });
    }

    public static void findItem(String uid, MutableLiveData<Servico> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.SERVICOS)
                .document(uid)
                .get().addOnCompleteListener((result -> {
                    if(result.isSuccessful()){
                        if(result.getResult().exists()){
                            Servico servico = result.getResult().toObject(Servico.class);
                            data.setValue(servico);
                        }else data.setValue(null);
                    }else data.setValue(null);
        }));
    }
}
