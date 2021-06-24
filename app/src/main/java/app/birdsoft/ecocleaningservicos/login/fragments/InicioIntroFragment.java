package app.birdsoft.ecocleaningservicos.login.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.login.LoginActivity;
import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;
import app.birdsoft.ecocleaningservicos.manager.Conexao;

public class InicioIntroFragment extends WelcomeFragment implements  WelcomeActivity.WelcomeContent {

    private Button buttonNext;
    private FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = Conexao.getFirebaseAuth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio_intro, container, false);
        buttonNext =root.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(view -> startActivity(new Intent(getActivity(), LoginActivity.class)));

        return root;
    }

    @Override
    public boolean shouldDisplay(Context context) {
        if(auth == null){
            auth = Conexao.getFirebaseAuth();
        }
        return auth.getCurrentUser() == null;
    }
}