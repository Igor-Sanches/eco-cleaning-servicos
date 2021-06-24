package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;

public class SistemaUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sistema_url);
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        if(getIntent() != null){
            if(getIntent().getAction() != null){
                if(getIntent().getAction().equals(Intent.ACTION_VIEW)){
                    try{
                        Uri linkuri = getIntent().getData();
                        String link = linkuri.toString();
                        int lenght = "https://www.appecocleaningservicos.com/birdsoft/uid?=".length();
                        System.out.println(lenght +", " + link.length() +", " + (link.length() - lenght));
                        String uid = link.substring(lenght);
                        ServicosViewModel viewModel = new ViewModelProvider(this).get(ServicosViewModel.class);
                        if(Conexao.isConnected(this)){
                            viewModel.findItem(uid).observe(this, (servico -> {
                                if(servico != null){
                                    Intent intent;
                                    if(servico.getTipoServico() == 2){
                                        intent = new Intent(this, SelecionadorServicosUnicoActivity.class);
                                    }else{
                                        intent = new Intent(this, SelecionadorServicosItensActivity.class);
                                    }
                                    intent.putExtra("servico", servico);
                                    Navigate.context(this).navigate(intent);
                                    finish();
                                }else{
                                    onErro(loadingDialog);
                                }
                            }));
                        }else{
                            loadingDialog.dismiss();
                            DialogMessage message = new DialogMessage(this, R.string.msg_erro_net, false, R.string.confirmar);
                            message.show();
                            message.setOnDismissListener((dialog -> finish()));
                        }
                    }catch (Exception x){
                        onErro(loadingDialog);
                    }
                }else onErro(loadingDialog);

            }else{
                onErro(loadingDialog);
            }
        }else onErro(loadingDialog);
    }

    private void onErro(LoadingDialog loadingDialog) {
        loadingDialog.dismiss();
        DialogMessage message = new DialogMessage(this, R.string.msg_erro, false, R.string.confirmar);
        message.show();
        message.setOnDismissListener((dialog -> finish()));
    }
}