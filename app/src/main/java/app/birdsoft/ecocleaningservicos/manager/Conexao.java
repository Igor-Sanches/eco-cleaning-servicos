package app.birdsoft.ecocleaningservicos.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Conexao {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    public static void signOut(){
        if(firebaseAuth == null){
            getFirebaseAuth();
        }
        firebaseAuth.signOut();
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            carregarAuth();
        }
        return firebaseAuth;
    }

    private static void carregarAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    firebaseUser = user;
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser() {
        if(firebaseUser == null){
            getFirebaseAuth();
        }
        return firebaseUser;
    }

    public static boolean isConnected(Context context) {
        try{
            NetworkInfo info = ((ConnectivityManager) Objects.requireNonNull(context.getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();
            if(info == null){
                return false;
            }
            return info.isConnected();
        }catch (Exception x){
            return false;
        }
    }

}
