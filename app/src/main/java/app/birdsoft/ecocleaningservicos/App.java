package app.birdsoft.ecocleaningservicos;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.Source;

import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;
import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.servicos.SendNotification;
import app.birdsoft.ecocleaningservicos.view.BloqueadoActivity;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        if(Conexao.getFirebaseAuth().getCurrentUser() != null){
            SendNotification.UpdateToken(Conexao.getFirebaseAuth().getCurrentUser().getUid());
        }
    }

    public static void isBloqueado(Activity activity){
        FireStoreUtils
                .getDatabaseOnline()
                .collection(Chave.CLIENTES)
                .document(Usuario.getUid(activity))
                .get().addOnCompleteListener((result -> {
                    if(result.isSuccessful()){
                        if(result.getResult().exists()){
                            Cliente cliente = result.getResult().toObject(Cliente.class);
                            if(cliente.isBlock()){
                                Intent intent = new Intent(activity, BloqueadoActivity.class);
                                intent.putExtra("msg", cliente.getMsgBlock());
                                activity.startActivity(intent);
                                activity.finish();
                                WelcomeActivity.getInstance().finish();
                                Conexao.getFirebaseAuth().signOut();
                            }
                        }

                    }
        }));

    }

}
