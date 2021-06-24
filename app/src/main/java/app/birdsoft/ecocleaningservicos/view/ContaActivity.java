package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogCidades;
import app.birdsoft.ecocleaningservicos.dialogo.DialogInput;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.login.SplashActivity;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.settings.Settings;
import app.birdsoft.ecocleaningservicos.tools.Mask;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.viewModel.ClienteViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class ContaActivity extends AppCompatActivity {

    private LinearLayout vazio, layout_wifi_error, lyt_progress;
    private NestedScrollView layout_principal;
    private TextView displayName, emila_exib, numero, email, endereco, cidade, situacao;
    private Cliente cliente;
    private LoadingDialog loading;
    private ClienteViewModel viewModel;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        viewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        vazio = findViewById(R.id.vazio);
        loading = new LoadingDialog(this);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        lyt_progress = findViewById(R.id.lyt_progress);
        layout_principal = findViewById(R.id.layout_principal);
        displayName = findViewById(R.id.displayName);
        emila_exib = findViewById(R.id.emila_exib);
        situacao = findViewById(R.id.situacao);
        numero = findViewById(R.id.numero);
        email = findViewById(R.id.email);
        endereco = findViewById(R.id.endereco);
        cidade = findViewById(R.id.cidade);
        uid = Usuario.getUid(this);
        viewModel.init(uid, this);
        viewModel.getMutableLiveDataUID().observe(this, (clientesElements -> {
            cliente = clientesElements.getCliente();
            if(cliente != null){
                displayName.setText(cliente.getNome());
                emila_exib.setText(cliente.getEmail());
                email.setText(cliente.getEmail());
                numero.setText(cliente.getTelefone());
                endereco.setText(cliente.getEndereco());
                cidade.setText(cliente.getCidadeLocal());
                if(cliente.isBlock()){
                    situacao.setText(R.string.bloqueado);
                    situacao.setBackgroundResource(R.drawable.btn_acriligo_vermelho);
                    situacao.setTextColor(getResources().getColor(R.color.acriligo_tint_vermelho));
                }else{
                    situacao.setText(R.string.ativo);
                    situacao.setBackgroundResource(R.drawable.btn_acriligo_verde);
                    situacao.setTextColor(getResources().getColor(R.color.acriligo_tint_verde));
                }
            }
            findViewById(R.id.app_sair).setVisibility(clientesElements.getListaVisibility());
            vazio.setVisibility(clientesElements.getVazioVisibility());
            lyt_progress.setVisibility(clientesElements.getProgressVisibility());
            layout_principal.setVisibility(clientesElements.getListaVisibility());
            layout_wifi_error.setVisibility(clientesElements.getLayoutWifiOffline());
            loading.dismiss();
        }));
    }

    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        viewModel.update(uid, this);
        super.onResume();
    }

    public void onUpdateConnection(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        viewModel.update(uid, this);
    }


    public void onApagarConta(View view) {
        DialogMessage message = new DialogMessage(this, R.string.msg_apagar_conta, true, R.string.ok_continuar);
        message.show();
        message.setOnPossiveButtonClicked(() -> {
            if(isConnected()){
                loading.show();
                viewModel.apagarConta(cliente).observe(this, (result -> {
                    if(result == 1){
                        MySnackbar.makeText(this, R.string.conta_apagada, ModoColor._success).show();
                        Settings.delete(this);
                        new NotificationDAO(this).apagar(0, true);
                        Conexao.getFirebaseAuth().signOut();
                        Navigate.activity(this).navigate(SplashActivity.class);
                    }else{
                        loading.dismiss();
                        new DialogMessage(this, result == 0 ? R.string.conta_apagada_erro : result, false, R.string.confirmar).show();
                    }
                }));
            }
        });
    }

    public void onAlterarCidade(View view) {
        DialogCidades cidades = new DialogCidades(this, this, this);
        cidades.show();
        cidades.setOnCidadeClicked((cidadeSelecionada -> {
            String text = getString(R.string.deseja_alterar_cidade);
            String text_para = getString(R.string.para);
            String formacao = String.format("%s %s %s %s, %s", text, cliente.getCidadeLocal(), text_para, cidadeSelecionada, getString(R.string.atencao_de));
            DialogMessage message = new DialogMessage(this, formacao, true, getString(R.string.confirmar));
            message.show();
            message.setOnPossiveButtonClicked(() -> {
                loading.show();
                viewModel.atualizarCidade(cidadeSelecionada, this).observe(this, (sucesso -> {
                    if(sucesso){
                        MySnackbar.makeText(this, R.string.cidade_alterada, ModoColor._success).show();
                        viewModel.update(uid, this);
                    }else{
                        MySnackbar.makeText(this, R.string.cidade_alterada_erro, ModoColor._falha).show();
                        loading.dismiss();
                    }
                }));
            });
        }));
    }

    public void onEditarEndereco(View view) {
        HelperManager.inputEnderecoCliente(this).observe(this, (endereco1 -> {
            if(endereco1 != null){
               loading.show();
                viewModel.trocaEndereco(endereco1, this).observe(this, (sucesso -> {
                    if(sucesso){
                        Usuario.setEndereco(endereco1.getEndereco(), this);
                        Endereco _endereco = new Endereco();
                        _endereco.setEndereco(endereco1.getEndereco());
                        _endereco.setBairro(endereco1.getBairro());
                        _endereco.setNomeRuaNumero(endereco1.getNomeRuaNumero());
                        _endereco.setNumeroCasa(endereco1.getNumeroCasa());
                        _endereco.setReferencia(endereco1.getReferencia());
                        _endereco.setSn(endereco1.isSn());
                        _endereco.setTipo_lugar(endereco1.getTipo_lugar());
                        _endereco.setComplemento(endereco1.getComplemento());
                        _endereco.setBloco_n_ap(endereco1.getBloco_n_ap());
                        Settings.setEndereco(_endereco, this);
                        cliente.setEndereco(Usuario.getEndereco(this));
                        viewModel.update(uid, this);
                        MyToast.makeText(this, R.string.troca_endereco, ModoColor._success).show();
                    }else{
                        loading.dismiss();
                        MyToast.makeText(this, R.string.error_troca_endereco, ModoColor._falha).show();
                    }
                }));
            }
        }));
    }

    public void onAlterarEmail(View view) {
        @SuppressLint("InflateParams") View root = LayoutInflater.from(this).inflate(R.layout.card_email, null);
        DialogInput input = new DialogInput(this, root, getString(R.string.email));
        TextInputLayout mTextInputLayout = root.findViewById(R.id.user_editor);
        EditText editor = mTextInputLayout.getEditText();
        editor.setText(cliente.getEmail());
        editor.setSelection(cliente.getEmail().length());
        Button cancel = root.findViewById(R.id.cancel);
        Button enter = root.findViewById(R.id.enter);
        cancel.setOnClickListener(v -> input.dismiss());
        enter.setOnClickListener((v -> {
            if(!cliente.getEmail().equals(editor.getText().toString().trim())){
                if(isConnected()){
                    loading.show();
                    String email = editor.getText().toString().trim();
                    if(!email.equals("")){
                        viewModel.trocaEmail(email, this).observe(this, (result ->{
                            if(result == 1){
                                input.dismiss();
                                MySnackbar.makeText(this, R.string.troca_email, ModoColor._success).show();
                                viewModel.update(uid, this);
                            }else{
                                loading.dismiss();
                                input.dismiss();
                                new DialogMessage(this, result == 0 ? R.string.erro_alterar : result, false, R.string.confirmar).show();
                            }
                        }));
                    }else{
                        loading.dismiss();
                        MySnackbar.makeText(this, R.string.digite_o_seu_email, ModoColor._falha).show();
                    }
                }
            }else input.dismiss();
        }));

        input.show();
    }

    public void onAlterarNumero(View view) {
        @SuppressLint("InflateParams") View root = LayoutInflater.from(this).inflate(R.layout.card_contato, null);
        DialogInput input = new DialogInput(this, root, getString(R.string.telefone));
        EditText number = ((TextInputLayout)root.findViewById(R.id.zapUser)).getEditText();
        number.addTextChangedListener(Mask.insertTelefoneCelular(number));
        number.setText(cliente.getTelefone());
        number.setSelection(cliente.getTelefone().length());
        Button cancel = root.findViewById(R.id.cancel);
        Button enter = root.findViewById(R.id.enter);
        cancel.setOnClickListener(v -> input.dismiss());
        enter.setOnClickListener((v -> {
            if(!cliente.getTelefone().equals(number.getText().toString().trim())){
                if(isConnected()){
                    loading.show();
                    String telefone = number.getText().toString().trim();
                    if(!telefone.equals("")){
                        if("(##) ####-####".length() == telefone.length() || "(##) #####-####".length() == telefone.length()){
                            viewModel.update(telefone, "telefone", this).observe(this, (success ->{
                                if(success){
                                    input.dismiss();
                                    MySnackbar.makeText(this, R.string.troca_telefone, ModoColor._success).show();
                                    viewModel.update(uid, this);
                                }else{
                                    MySnackbar.makeText(this, R.string.error_troca_telefone, ModoColor._falha).show();
                                    loading.dismiss();
                                }
                            }));
                        }else{
                            loading.dismiss();
                            MySnackbar.makeText(this, R.string.digite_o_seu_telefone_correto, ModoColor._falha).show();
                        }
                    }else{
                        loading.dismiss();
                        MySnackbar.makeText(this, R.string.digite_o_seu_telefone, ModoColor._falha).show();
                    }
                }
            }else input.dismiss();
        }));
        input.show();
    }

    private boolean isConnected() {
        if(!Conexao.isConnected(this)){
            MySnackbar.makeText(this, R.string.sem_conexao, ModoColor._falha).show();
            return false;
        }
        return true;
    }

    public void onAlterarSenha(View view) {
        @SuppressLint("InflateParams") View root = LayoutInflater.from(this).inflate(R.layout.card_senha, null);
        DialogInput input = new DialogInput(this, root, getString(R.string.senha));
        TextInputLayout mTextInputLayout = root.findViewById(R.id.user_editor);
        EditText editor = mTextInputLayout.getEditText();

        Button cancel = root.findViewById(R.id.cancel);
        Button enter = root.findViewById(R.id.enter);
        cancel.setOnClickListener(v -> input.dismiss());
        enter.setOnClickListener((v -> {
            if(isConnected()){
                loading.show();
                String senha = editor.getText().toString().trim();
                if(!senha.equals("")){
                    viewModel.trocaSenha(senha).observe(this, (result ->{
                        if(result == 1){
                            input.dismiss();
                            MySnackbar.makeText(this, R.string.troca_senha, ModoColor._success).show();
                            viewModel.update(uid, this);
                        }else{
                            loading.dismiss();
                            input.dismiss();
                            new DialogMessage(this, result == 0 ? R.string.erro_alterar : result, false, R.string.confirmar).show();
                        }
                    }));
                }else{
                    loading.dismiss();
                    MySnackbar.makeText(this, R.string.digite_o_seu_email, ModoColor._falha).show();
                }
            }

        }));
        input.show();
    }

    public void onSair(View view) {
        HelperManager.exitApp(this);
    }

    public void onEditarNome(View view) {
        @SuppressLint("InflateParams") View root = LayoutInflater.from(this).inflate(R.layout.card_display_name, null);
        DialogInput input = new DialogInput(this, root, getString(R.string.nome_sobrenome));
        TextInputLayout mTextInputLayout = root.findViewById(R.id.pinUser);
        EditText editor = mTextInputLayout.getEditText();
        editor.setText(cliente.getNome());
        editor.setSelection(cliente.getNome().length());
        Button cancel = root.findViewById(R.id.cancel);
        Button enter = root.findViewById(R.id.enter);
        cancel.setOnClickListener(v -> input.dismiss());
        enter.setOnClickListener((v -> {
            if(!cliente.getNome().equals(editor.getText().toString().trim())){
                if(isConnected()){
                    loading.show();
                    String nome = editor.getText().toString().trim();
                    if(!nome.equals("")){
                        viewModel.update(nome, "nome", this).observe(this, (success ->{
                            if(success){
                                input.dismiss();
                                MySnackbar.makeText(this, R.string.troca_nome, ModoColor._success).show();
                                viewModel.update(uid, this);
                            }else{
                                loading.dismiss();
                                MySnackbar.makeText(this, R.string.error_troca_nome, ModoColor._falha).show();
                            }

                        }));
                    }else{
                        loading.dismiss();
                        MySnackbar.makeText(this, R.string.digite_o_seu_nome, ModoColor._falha).show();
                    }
                }
            }else input.dismiss();

        }));

        input.show();
    }
}