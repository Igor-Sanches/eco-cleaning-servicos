package app.birdsoft.ecocleaningservicos.login.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import app.birdsoft.ecocleaningservicos.MainActivity;
import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.login.LoginActivity;
import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;
import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.manager.UpdateAllViewModel;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.servicos.SendNotification;
import app.birdsoft.ecocleaningservicos.settings.Settings;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.Email;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.view.BloqueadoActivity;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;

public class LoginFragment extends Fragment {

    private View root;
    private FirebaseAuth auth;
    private TextInputLayout emailUser, senhaUser;
    private LoadingDialog dialog;
    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            root = inflater.inflate(R.layout.fragment_login, container, false);
            auth = Conexao.getFirebaseAuth();
            dialog = new LoadingDialog(getActivity());
            View sheetBottom = root.findViewById(R.id.bottom_sheet);
            sheetBehavior = BottomSheetBehavior.from(sheetBottom);
            senhaUser = root.findViewById(R.id.senhaUser);
            Objects.requireNonNull(senhaUser.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    senhaUser.setError("");
                    senhaUser.setErrorEnabled(false);
                }
            });
            emailUser = root.findViewById(R.id.emailUser);
            Objects.requireNonNull(emailUser.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    emailUser.setError("");
                    emailUser.setErrorEnabled(false);
                }
            });
            root.findViewById(R.id.btnRedefinirSenha).setOnClickListener(view -> onRedefinir());
            root.findViewById(R.id.btnLogin).setOnClickListener(view -> onLogin());
            root.findViewById(R.id.btnRegister).setOnClickListener(view -> goRegister());
        }catch (Exception x){
            MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
        }
        return root;
    }

    private void onRedefinir() {
        try{
            if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.dialog_redefinir_senha, null);

            final TextInputLayout emailInput = _view.findViewById(R.id.emailUser);
            Objects.requireNonNull(emailInput.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    emailInput.setError("");
                    emailInput.setErrorEnabled(false);
                }
            });
            FrameLayout btnRec= _view.findViewById(R.id.buttonGo);
            btnRec.setOnClickListener(v -> {
                try {
                    final String _emailB2 = emailInput.getEditText().getText().toString();
                    if (Conexao.isConnected(getActivity())) {
                        if (!_emailB2.equals("")) {

                            if (Email.validar(_emailB2)) {
                                onRecuperar(emailInput);
                            } else {
                                emailInput.setErrorEnabled(true);
                                emailInput.setError(getString(R.string.digite_o_seu_email_valido));
                                emailInput.getEditText().requestFocus();
                            }

                        } else {
                            emailInput.setErrorEnabled(true);
                            emailInput.setError(getString(R.string.digite_o_seu_email));
                            emailInput.getEditText().requestFocus();
                        }


                    } else {
                        MySnackbar.makeText(getActivity(), getString(R.string.sem_conexao), ModoColor._falha).show();

                    }
                }catch (Exception x){
                    MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
                }
            });

            sheetDialog = new BottomSheetDialog(requireActivity());
            sheetDialog.setContentView(_view);
            if(Build.VERSION.SDK_INT >= 21){
                sheetDialog.getWindow().addFlags(67108864);
            }
            sheetDialog.show();
        }catch (Exception x){
            MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
        }
    }

    private void onRecuperar(final TextInputLayout emailInput){
        sheetDialog.dismiss();
        MySnackbar.makeText(getActivity(), getString(R.string.verificando_email)).show();
        dialog.show();
        String email = Objects.requireNonNull(emailInput.getEditText()).getText().toString().trim();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        MySnackbar.makeText(getActivity(), getString(R.string.verifique_sua_caixa_de_email), ModoColor._success).show();
                    }else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (FirebaseAuthInvalidUserException user) {
                            emailInput.getEditText().requestFocus();
                            emailInput.setErrorEnabled(true);
                            emailInput.setError(getString(R.string.firebaseAuthInvalidUserException));
                        }catch (Exception e) {
                            MySnackbar.makeText(getActivity(), getString(R.string.authException), ModoColor._falha).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        sheetDialog.show();
                    }
                });
    }

    private void onLogin() {
        String email = Objects.requireNonNull(emailUser.getEditText()).getText().toString().trim();
        final String senha = Objects.requireNonNull(senhaUser.getEditText()).getText().toString().trim();
        if(Conexao.isConnected(getActivity())) {
            if (!email.equals("")) {
                if (Email.validar(email)) {
                    if (!senha.equals("")) {
                        dialog.show();
                        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
                            try{
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                                    if (user != null) {
                                        salvarDados(user);
                                    }

                                } else {
                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    } catch (FirebaseAuthInvalidUserException user) {
                                        emailUser.getEditText().requestFocus();
                                        emailUser.setError(getString(R.string.firebaseAuthInvalidUserException));
                                        emailUser.setErrorEnabled(true);
                                    } catch (FirebaseAuthInvalidCredentialsException user) {
                                        senhaUser.getEditText().requestFocus();
                                        senhaUser.setError(getString(R.string.firebaseAuthInvalidCredentialsException));
                                        senhaUser.setErrorEnabled(true);
                                    } catch (Exception e) {
                                        MySnackbar.makeText(getActivity(), getString(R.string.authException), ModoColor._falha).show();
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                }
                            }catch (Exception x){
                                MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
                            }
                        });
                    } else {
                        senhaUser.getEditText().requestFocus();
                        senhaUser.setErrorEnabled(true);
                        senhaUser.setError(getString(R.string.digite_sua_senha));
                    }
                } else {
                    emailUser.getEditText().requestFocus();
                    emailUser.setErrorEnabled(true);
                    emailUser.setError(getString(R.string.digite_o_seu_email_valido));
                }
            } else {
                emailUser.getEditText().requestFocus();
                emailUser.setErrorEnabled(true);
                emailUser.setError(getString(R.string.digite_o_seu_email));
            }
        }else{
            MySnackbar.makeText(getActivity(), getString(R.string.sem_conexao), ModoColor._falha).show();
        }

    }

    private void salvarDados(FirebaseUser user) {
        try{
            FireStoreUtils.getDatabase().collection(Chave.CLIENTES).document(user.getUid())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        try{
                            if(documentSnapshot.exists()) {
                                Cliente cliente = documentSnapshot.toObject(Cliente.class);
                                assert cliente != null;
                                if(cliente.isBlock()){
                                    Intent intent = new Intent(getActivity(), BloqueadoActivity.class);
                                    intent.putExtra("msg", cliente.getMsgBlock());
                                    requireActivity().startActivity(intent);
                                    requireActivity().finish();
                                    WelcomeActivity.getInstance().finish();
                                    auth.signOut();
                                    return;
                                }
                                Endereco endereco = new Endereco();
                               endereco.setReferencia(cliente.getReferencia());
                                endereco.setNumeroCasa(cliente.getNumeroCasa());
                                endereco.setNomeRuaNumero(cliente.getNomeRuaNumero());
                                endereco.setBairro(cliente.getBairro());
                                ////endereco.setCidade(cliente.getCidadeLocal());
                                endereco.setBloco_n_ap(cliente.getBloco_n_ap());
                                endereco.setComplemento(cliente.getComplemento());
                                endereco.setSn(cliente.isSn());
                                endereco.setTipo_lugar(cliente.getTipo_lugar());
                                endereco.setEndereco(cliente.getEndereco());
                                Settings.setEndereco(endereco, getActivity());
                                Usuario.setEndereco(cliente.getEndereco(), getActivity());
                                Usuario.setUid(user.getUid(), getActivity());
                                requireActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                SendNotification.UpdateToken(cliente.getUuid());
                                requireActivity().finish();
                                WelcomeActivity.getInstance().finish();
                                new UpdateAllViewModel(getActivity()).execute(getActivity());
                            }else{
                                auth.signOut();
                                new DialogMessage(requireActivity(), getString(R.string.msg_conta_not_auter), false, "").show();
                            }
                        }catch (Exception x){
                            MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
                        }
                    });
        }catch (Exception x){
            MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
        }
    }

    private void goRegister() {
        LoginActivity.getInstance().onPage(1);
    }
}