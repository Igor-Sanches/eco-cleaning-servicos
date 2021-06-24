package app.birdsoft.ecocleaningservicos.login.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.regex.Pattern;

import app.birdsoft.ecocleaningservicos.MainActivity;
import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogCidades;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.login.LoginActivity;
import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;
import app.birdsoft.ecocleaningservicos.manager.Chave;
import app.birdsoft.ecocleaningservicos.manager.FireStoreUtils;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.UpdateAllViewModel;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.servicos.SendNotification;
import app.birdsoft.ecocleaningservicos.settings.Settings;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.manager.Email;
import app.birdsoft.ecocleaningservicos.tools.Mask;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;

public class RegistrarFragment extends Fragment {

    private final Pattern nomeRegex = Pattern.compile("[a-zA-ZÇçãõíóáéâ ÃÂÁÉÍÓÚú]+");
    private TextInputLayout nomeUser, emailUser, senhaUser, senhaDouUser, phoneUser;
    private CheckBox checkTermos, checkPrivacy;
    private LoadingDialog dialog;
    private FirebaseAuth auth;
    private Endereco _endereco;
    private TextView endereco, cidade;

    public RegistrarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registrar, container, false);
        dialog = new LoadingDialog(getActivity());
        auth = Conexao.getFirebaseAuth();
        root.findViewById(R.id.btnLogin).setOnClickListener(view -> onLogin());
        checkPrivacy = root.findViewById(R.id.checkPrivacy);
        checkTermos = root.findViewById(R.id.checkTermos);
        nomeUser = root.findViewById(R.id.nomeUser);
        emailUser = root.findViewById(R.id.emailUser);
        senhaUser = root.findViewById(R.id.senhaUser);
        senhaDouUser = root.findViewById(R.id.senhaDouUser);
        cidade = root.findViewById(R.id.cidade_input);
        phoneUser = root.findViewById(R.id.phoneUser);
        endereco = root.findViewById(R.id.endereco_input);
        root.findViewById(R.id.onInputEndereco).setOnClickListener((v -> iniciarInputEndereco()));
        root.findViewById(R.id.onInputCidade).setOnClickListener((v -> iniciarInputCidade()));
        root.findViewById(R.id.buttonRegister).setOnClickListener(view -> onRegister());

        onInput();
        return root;
    }

    private void iniciarInputCidade() {
        DialogCidades dialogCidades = new DialogCidades(requireActivity(), requireActivity(), requireActivity());
        dialogCidades.show();
        dialogCidades.setOnCidadeClicked((cidade1 -> cidade.setText(cidade1)));
    }

    private void iniciarInputEndereco() {
        HelperManager.inputEnderecoCliente(getActivity(), _endereco).observe(requireActivity(), (result -> {
            if(result != null){
                if(_endereco == null) _endereco = new Endereco();
                _endereco.setReferencia(result.getReferencia());
                _endereco.setNumeroCasa(result.getNumeroCasa());
                _endereco.setNomeRuaNumero(result.getNomeRuaNumero());
                _endereco.setBairro(result.getBairro());
                _endereco.setEndereco(result.getEndereco());
                _endereco.setSn(result.isSn());
                _endereco.setTipo_lugar(result.getTipo_lugar());
                _endereco.setComplemento(result.getComplemento());
                _endereco.setBloco_n_ap(result.getBloco_n_ap());
                endereco.setText(result.getEndereco());
                endereco.setSelected(true);
            }
        }));
    }

    private void onRegister() {
        String nome = Objects.requireNonNull(nomeUser.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(emailUser.getEditText()).getText().toString().trim();
        String senha = Objects.requireNonNull(senhaUser.getEditText()).getText().toString().trim();
        String senhaDou = Objects.requireNonNull(senhaDouUser.getEditText()).getText().toString().trim();
        String phone = Objects.requireNonNull(phoneUser.getEditText()).getText().toString().trim();
        String local = endereco.getText().toString().trim();
        String cidade = this.cidade.getText().toString().trim();

        if (!nome.equals("")) {
            if (nomeRegex.matcher(nome).matches()) {
                if (!email.equals("")) {
                    if (Email.validar(email)) {
                        if (!senha.equals("")) {
                            if (!senhaDou.equals("")) {
                                if (senha.equals(senhaDou)) {
                                    if (!phone.equals("")) {
                                        int length = phone.length();
                                        if (length == "(##) #####-####".length() || length == "(##) ####-####".length()) {
                                            if (!local.equals(getString(R.string.endereco))) {
                                                if(!cidade.equals(getString(R.string.cidade))){
                                                    if (checkTermos.isChecked()) {
                                                        if (checkPrivacy.isChecked()) {
                                                            onContinue(new String[]{nome, email, senha, phone, local});
                                                        } else {
                                                            MySnackbar.makeText(getActivity(), getString(R.string.aceite_a_politica), ModoColor._falha).show();
                                                        }
                                                    } else {
                                                        MySnackbar.makeText(getActivity(), getString(R.string.aceite_os_termos), ModoColor._falha).show();
                                                    }
                                                } else {
                                                    MySnackbar.makeText(getActivity(), getString(R.string.digite_o_seu_cidade), ModoColor._falha).show();
                                                }

                                            } else {
                                                MySnackbar.makeText(getActivity(), getString(R.string.digite_o_seu_endereco), ModoColor._falha).show();
                                            }

                                        } else {
                                            onError(phoneUser, getString(R.string.digite_o_seu_telefone_ddd));
                                        }
                                    } else {
                                        onError(phoneUser, getString(R.string.digite_o_seu_telefone));
                                    }

                                } else {
                                    onError(senhaDouUser, getString(R.string.senha_n_igual));
                                }
                            } else {
                                onError(senhaDouUser, getString(R.string.senha_novamente));
                            }
                        } else {
                            onError(senhaUser, getString(R.string.digite_sua_senha));
                        }
                    } else {
                        onError(emailUser, getString(R.string.digite_o_seu_email_valido));
                    }
                } else {
                    onError(emailUser, getString(R.string.digite_o_seu_email));
                }

            } else {
                onError(nomeUser, getString(R.string.digite_nome_valido));
            }

        } else {
            onError(nomeUser, getString(R.string.digite_o_seu_nome));
        }
    }

    private void onContinue(final String[] dados) {
        dialog.show();
        String email = dados[1];
        String senha = dados[2];
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        try{
                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            if(user != null){
                                salvarDadosCriarDb(user, dados);
                            }
                        }catch (Exception ignored){

                        }


                    }else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch (FirebaseAuthWeakPasswordException user){
                            onError(senhaUser, getString(R.string.senha_fraca));
                        }catch (FirebaseAuthInvalidCredentialsException user){
                            onError(senhaUser, getString(R.string.firebaseAuthInvalidCredentialsException));
                        }catch (FirebaseAuthUserCollisionException user){
                            onError(emailUser, getString(R.string.firebaseAuthUserCollisionException));
                        }catch (Exception e) {
                            MySnackbar.makeText(getActivity(), getString(R.string.authException), ModoColor._falha).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
    }

    private void salvarDadosCriarDb(FirebaseUser user, String[] dados) {
        try {
            String nome = dados[0];
            String email = dados[1];
            String phone = dados[3];
            long date = DateTime.getTime();
            String uuid = user.getUid();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profileUpdates);
            Cliente usuario = new Cliente();
            usuario.setData(date);
            usuario.setEmail(email);
            usuario.setNome(nome);
            usuario.setTelefone(phone);
            usuario.setCidadeLocal(cidade.getText().toString());
            usuario.setEndereco(_endereco.getEndereco());
            usuario.setNumeroCasa(_endereco.getNumeroCasa());
            usuario.setNomeRuaNumero(_endereco.getNomeRuaNumero());
            usuario.setBairro(_endereco.getBairro());
            usuario.setReferencia(_endereco.getReferencia());
            usuario.setBloco_n_ap(_endereco.getBloco_n_ap());
            usuario.setComplemento(_endereco.getComplemento());
            usuario.setSn(_endereco.isSn());
            usuario.setTipo_lugar(_endereco.getTipo_lugar());
            usuario.setUuid(uuid);
            Usuario.setUid(user.getUid(), getActivity());
            FireStoreUtils.getDatabase().collection(Chave.CLIENTES).document(uuid).set(usuario);
            onFinish(usuario.getUuid());
            new UpdateAllViewModel(getActivity()).execute(getActivity());
        }catch (Exception x){
            MySnackbar.makeText(getActivity(), x.getMessage(), ModoColor._falha).show();
        }
    }

    private void onFinish(String uid){
        dialog.dismiss();
        Settings.setEndereco(_endereco, getActivity());
        requireActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        Usuario.setEndereco(_endereco.getEndereco(), getActivity());
        SendNotification.UpdateToken(uid);
        requireActivity().finish();
        WelcomeActivity.getInstance().finish();
    }

    private void onLogin() {
        LoginActivity.getInstance().onPage(0);
    }

    private void onError(TextInputLayout input, String error){
        Objects.requireNonNull(input.getEditText()).requestFocus();
        input.setErrorEnabled(true);
        input.setError(error);
    }

    private void onError(TextInputLayout input){
        Objects.requireNonNull(input.getEditText()).requestFocus();
        input.setErrorEnabled(false);
        input.setError("");
    }

    private void onInput(){
        Objects.requireNonNull(nomeUser.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onError(nomeUser);
            }
        });
        Objects.requireNonNull(emailUser.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onError(emailUser);
            }
        });
        Objects.requireNonNull(senhaUser.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onError(senhaUser);
            }
        });
        Objects.requireNonNull(senhaDouUser.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onError(senhaDouUser);
            }
        });
        Objects.requireNonNull(phoneUser.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onError(phoneUser);
            }
        });

        EditText editTextNumero = phoneUser.getEditText();
        assert editTextNumero != null;
        editTextNumero.addTextChangedListener(Mask.insertTelefoneCelular(editTextNumero));

    }

}