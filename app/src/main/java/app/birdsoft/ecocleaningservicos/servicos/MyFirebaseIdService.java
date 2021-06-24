package app.birdsoft.ecocleaningservicos.servicos;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import app.birdsoft.ecocleaningservicos.manager.FirebaseUtils;
import app.birdsoft.ecocleaningservicos.model.Token;
import app.birdsoft.ecocleaningservicos.manager.Conexao;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        FirebaseUser firebaseUser= Conexao.getFirebaseAuth().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateToken(refreshToken);
        }
    }
    private void updateToken(String refreshToken){
        Token token1= new Token(refreshToken);
        FirebaseUtils.getDatabaseRef().child("Tokens").child(refreshToken).setValue(token1);
    }
}
