package app.birdsoft.ecocleaningservicos.repository;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.model.ClienteElements;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.model.Pedido;

public class ClienteRepository {
    public static MutableLiveData<ClienteElements> getInstance(Context context) {
        return GetElements(context);
    }

    private synchronized static MutableLiveData<ClienteElements> GetElements(Context context) {
        MutableLiveData<ClienteElements> data = new MutableLiveData<>();
        getFirebaseData(data, context);
        return data;
    }

    private static synchronized void getFirebaseData(MutableLiveData<ClienteElements> data, Context context) {
        ClienteElements clienteElements = new ClienteElements();
        if(Conexao.isConnected(context)) {
            FireStoreUtils
                    .getDatabaseOnline()
                    .collection(Chave.CLIENTES)
                    .document(Usuario.getUid(context))
                    .get().addOnSuccessListener(documentSnapshot -> {
                try {
                    if (documentSnapshot.exists()) {
                        Cliente cliente = documentSnapshot.toObject(Cliente.class);
                        if (cliente != null) {
                            Usuario.setEndereco(cliente.getEndereco(), context);
                            clienteElements.setCliente(cliente);
                            clienteElements.setEndereco(cliente.getEndereco());
                            clienteElements.setNome(cliente.getNome());
                            clienteElements.setBloqueado(cliente.isBlock());
                        } else {
                            clienteElements.setCliente(null);
                            clienteElements.setEndereco(null);
                            clienteElements.setBloqueado(false);
                            clienteElements.setNome(null);
                        }

                    } else {
                        clienteElements.setCliente(null);
                        clienteElements.setEndereco(null);
                        clienteElements.setBloqueado(false);
                        clienteElements.setNome(null);
                    }
                    data.setValue(clienteElements);
                } catch (Exception x) {
                    clienteElements.setCliente(null);
                    clienteElements.setEndereco(null);
                    clienteElements.setBloqueado(false);
                    clienteElements.setNome(null);
                    data.setValue(clienteElements);
                }
            });
        }else{
            clienteElements.setCliente(null);
            clienteElements.setEndereco(null);
            clienteElements.setBloqueado(false);
            clienteElements.setNome(null);
            data.setValue(clienteElements);
        }
    }

    public static void update(MutableLiveData<ClienteElements> data, Context context) {
        getFirebaseData(data, context);
    }

    private synchronized static MutableLiveData<ClienteElements> GetElementsUID(String uid, Context context) {
        MutableLiveData<ClienteElements> data = new MutableLiveData<>();
        getFirebaseDataUID(data, uid, context);
        return data;
    }

    private static void getFirebaseDataUID(MutableLiveData<ClienteElements> data, String uid, Context context) {
        ClienteElements clienteElements = new ClienteElements();
        if(Conexao.isConnected(context)){
            FireStoreUtils.getDatabase()
                    .collection(Chave.CLIENTES)
                    .document(uid).get()
                    .addOnCompleteListener((result -> {
                        if(result.isSuccessful()){
                            if(result.getResult() != null){
                                Cliente cliente = result.getResult().toObject(Cliente.class);
                                if(cliente != null){
                                    clienteElements.setProgressVisibility(View.GONE);
                                    clienteElements.setVazioVisibility(View.GONE);
                                    clienteElements.setListaVisibility(View.VISIBLE);
                                    clienteElements.setLayoutWifiOffline(View.GONE);
                                    clienteElements.setCliente(cliente);
                                }else{
                                    clienteElements.setListaVisibility(View.GONE);
                                    clienteElements.setProgressVisibility(View.GONE);
                                    clienteElements.setLayoutWifiOffline(View.GONE);
                                    clienteElements.setVazioVisibility(View.VISIBLE);
                                }
                            }else{
                                clienteElements.setListaVisibility(View.GONE);
                                clienteElements.setProgressVisibility(View.GONE);
                                clienteElements.setLayoutWifiOffline(View.GONE);
                                clienteElements.setVazioVisibility(View.VISIBLE);
                            }
                        }else{
                            clienteElements.setListaVisibility(View.GONE);
                            clienteElements.setProgressVisibility(View.GONE);
                            clienteElements.setVazioVisibility(View.GONE);
                            clienteElements.setLayoutWifiOffline(View.VISIBLE);
                        }
                        data.setValue(clienteElements);
                    }));
        }else{
            clienteElements.setListaVisibility(View.GONE);
            clienteElements.setProgressVisibility(View.GONE);
            clienteElements.setVazioVisibility(View.GONE);
            clienteElements.setLayoutWifiOffline(View.VISIBLE);
            data.setValue(clienteElements);
        }
    }

    public static void update(String uid, MutableLiveData<ClienteElements> data, Context context) {
        getFirebaseDataUID(data, uid, context);
    }

    public static MutableLiveData<ClienteElements> getInstanceUid(String uid, Context context) {
        return GetElementsUID(uid, context);
    }


    public static void update(String value, String key, Context context, MutableLiveData<Boolean> data) {
        FireStoreUtils.getDatabase()
                .collection(Chave.CLIENTES)
                .document(Usuario.getUid(context))
                .update(key, value).addOnCompleteListener((result -> data.setValue(result.isSuccessful())));

    }

    public static void trocaEmail(String email, Context context, MutableLiveData<Integer> data) {
        Conexao.getFirebaseAuth()
                .getCurrentUser()
                .updateEmail(email)
                .addOnCompleteListener((result) -> {
                    if(result.isSuccessful()){
                        FireStoreUtils.getDatabase()
                                .collection(Chave.CLIENTES)
                                .document(Usuario.getUid(context))
                                .update("email", email).addOnCompleteListener((command -> data.setValue(command.isSuccessful() ? 1 : 0)));
                    }else {

                        if(result.getException().getMessage().equals("This operation is sensitive and requires recent authentication. Log in again before retrying this request.")){
                            data.setValue(R.string.erro_recent_login);
                        }else{
                            try {
                                throw Objects.requireNonNull(result.getException());
                            } catch (FirebaseAuthInvalidUserException user) {
                                data.setValue(R.string.firebaseAuthInvalidUserException);
                            } catch (FirebaseAuthInvalidCredentialsException user) {
                                data.setValue(R.string.firebaseAuthInvalidCredentialsException);
                            } catch (Exception e) {
                                data.setValue(R.string.erro_alterar);
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public static void trocaSenha(String senha, MutableLiveData<Integer> data) {
        Conexao.getFirebaseAuth()
                .getCurrentUser()
                .updatePassword(senha)
                .addOnCompleteListener((result) -> {
                    if(result.isSuccessful()){
                        data.setValue(result.isSuccessful() ? 1 : 0);
                    }else {

                        if(result.getException().getMessage().equals("This operation is sensitive and requires recent authentication. Log in again before retrying this request.")){
                            data.setValue(R.string.erro_recent_login);
                        }else{
                            try {
                                throw Objects.requireNonNull(result.getException());
                            } catch (FirebaseAuthInvalidUserException user) {
                                data.setValue(R.string.firebaseAuthInvalidUserException);
                            } catch (FirebaseAuthWeakPasswordException user){
                                data.setValue(R.string.senha_fraca);
                            } catch (FirebaseAuthInvalidCredentialsException user) {
                                data.setValue(R.string.firebaseAuthInvalidCredentialsException);
                            } catch (FirebaseAuthUserCollisionException user){
                                data.setValue(R.string.firebaseAuthUserCollisionException);
                            }catch (Exception e) {
                                data.setValue(0);
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public static void trocaEndereco(Endereco endereco, Context context, MutableLiveData<Boolean> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("nomeRuaNumero", endereco.getNomeRuaNumero());
        map.put("bairro", endereco.getBairro());
        map.put("referencia", endereco.getReferencia());
        map.put("numeroCasa", endereco.getNumeroCasa());
        map.put("endereco", endereco.getEndereco());
        map.put("tipo_lugar", endereco.getTipo_lugar());
        map.put("complemento", endereco.getComplemento());
        map.put("bloco_n_ap", endereco.getBloco_n_ap());
        map.put("sn", endereco.isSn());
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CLIENTES)
                .document(Usuario.getUid(context))
                .update(map)
                .addOnCompleteListener((command1 -> data.setValue(command1.isSuccessful())));
    }

    public static void atualizarCidade(String cidadeSelecionada, Context context, MutableLiveData<Boolean> data) {
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CLIENTES)
                .document(Usuario.getUid(context))
                .update("cidadeLocal", cidadeSelecionada)
                .addOnCompleteListener((command1 -> {
                    if(command1.isSuccessful()){
                        FireStoreUtils
                                .getDatabaseOnline()
                                .collection(Chave.CONTRATOS)
                                .whereEqualTo("uid_client", Usuario.getUid(context))
                                .get().addOnCompleteListener((result -> {
                            if(result.isSuccessful()){
                                if(result.getResult() != null){
                                    for(DocumentSnapshot doc : result.getResult()){
                                        if(doc != null){
                                            FireStoreUtils
                                                    .getDatabaseOnline()
                                                    .collection(Chave.CONTRATOS)
                                                    .document(doc.toObject(Pedido.class).getUid())
                                                    .update("cidade", cidadeSelecionada);
                                        }
                                    }
                                }
                            }
                        }));
                        ServicosRepository.onChanged(Usuario.getUid(context));
                        data.setValue(true);
                    }else data.setValue(false);
                }));
    }

    public static void apagarConta(Cliente cliente, MutableLiveData<Integer> data) {
        Conexao.getFirebaseAuth()
                .getCurrentUser()
                .delete().addOnCompleteListener((result -> {
            if(result.isSuccessful()){
                FireStoreUtils
                        .getDatabaseOnline()
                        .collection(Chave.CLIENTES)
                        .document(cliente.getUuid())
                        .delete();
                FireStoreUtils
                        .getDatabaseOnline()
                        .collection(Chave.CONTRATOS)
                        .whereEqualTo("uid_client", cliente.getUuid())
                        .get().addOnCompleteListener((task -> {
                    if(task.isSuccessful()){
                        if(task.getResult() != null){
                            for(DocumentSnapshot doc : task.getResult()){
                                if(doc != null){
                                    FireStoreUtils
                                            .getDatabaseOnline()
                                            .collection(Chave.CONTRATOS)
                                            .document(doc.toObject(Pedido.class).getUid())
                                            .delete();
                                }
                            }
                        }
                    }
                }));
                data.setValue(1);
            }else{
                if(result.getException().getMessage().equals("This operation is sensitive and requires recent authentication. Log in again before retrying this request.")){
                    data.setValue(R.string.erro_recent_login);
                }else{
                    data.setValue(0);
                }

            }
        }));
    }
}
