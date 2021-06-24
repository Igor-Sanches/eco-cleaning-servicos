package app.birdsoft.ecocleaningservicos.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.UUID;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorNotification;
import app.birdsoft.ecocleaningservicos.dialogo.DialogInput;
import app.birdsoft.ecocleaningservicos.dialogo.DialogNotificacao;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.model.Notification;
import app.birdsoft.ecocleaningservicos.model.NotificationData;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.servicos.SendNotification;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.viewModel.NotificationsViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class NotificacoesActivity extends AppCompatActivity {

    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;
    private LoadingDialog dialog;
    private View sheetBottom;
    private AdaptadorNotification adaptador;
    private LinearLayout lyt_progress, layout_conexao_error, layout_wifi_error, listaLayout, vazio;
    private NotificationsViewModel viewModel;

    private boolean isConnected() {
        if(!Conexao.isConnected(this)){
            MySnackbar.makeText(this, R.string.sem_conexao, ModoColor._falha).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);
        dialog = new LoadingDialog(this);
        sheetBottom = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
        adaptador = new AdaptadorNotification(this, new ArrayList<>());
        lyt_progress = findViewById(R.id.lyt_progress);
        listaLayout = findViewById(R.id.listaLayout);
        vazio = findViewById(R.id.vazio);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adaptador);
        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        runOnUiThread(()-> viewModel.init(this));
        viewModel.getMutableLiveData().observe(this, historicoElements -> {
            adaptador.inserir(historicoElements.getNotifications());
            listaLayout.setVisibility(historicoElements.getLayoutVisibility());
            lyt_progress.setVisibility(historicoElements.getLayoutProgressVisibility());
            vazio.setVisibility(historicoElements.getLayoutVazioVisibility());
            layout_conexao_error.setVisibility(historicoElements.getLayoutConnectionVisibility());
            layout_wifi_error.setVisibility(historicoElements.getLayoutWifiVisibility());
        });
        adaptador.setOnClickListener(((notification) -> {
            DialogNotificacao notificacao = new DialogNotificacao(this, notification, this);
            notificacao.show();
            notificacao.setOnViewServiceAction(this::verServico);
            notificacao.setOnResponderAction(this::responderNotificacao);
            notificacao.setOnDeletarListener(() -> {
                MySnackbar.makeText(this, R.string.deletado_com_sucesso, ModoColor._success).show();
                new NotificationDAO(this).apagar(notification.getId(), false);
                viewModel.update(this);
            });
        }));
    }

    private void verServico(Notification notification) {
        Intent intent = new Intent(this, VisualizarServicoIndivitualActivity.class);
        intent.putExtra("uid", notification.getUidServico());
        Navigate.activity(this).navigate(intent);
    }

    private void responderNotificacao(Notification notification) {
        if(isConnected()){
            @SuppressLint("InflateParams") View layout = LayoutInflater.from(this).inflate(R.layout.dialog_enivar_notificacao_pedido, null);
            DialogInput input = new DialogInput(this, layout, null);
            input.show();
            EditText msgsend = layout.findViewById(R.id.msg);
            layout.findViewById(R.id.title).setVisibility(View.GONE);
            layout.findViewById(R.id.confirmar).setOnClickListener((v -> {
                if(isConnected()){
                    String mensagem = msgsend.getText().toString().trim();
                    if(!mensagem.equals("")){
                        dialog.show();
                        if(notification.isResponder()){
                            notification.setResponder(false);
                            new NotificationDAO(this).atualizar(notification);
                        }
                        NotificationData data = new NotificationData();
                        data.setMensagem(mensagem);
                        data.setTitulo(String.format("%s: %s", getString(R.string.resposta_p), notification.getTitulo().replace(getString(R.string.resposta_p)+":", "").replace(getString(R.string.resposta)+":", "")));
                        data.setType(notification.getType());
                        data.setUidCliente(notification.getUidCliente());
                        data.setUidServico(notification.getUidServico());
                        data.setCliente(notification.getCliente());
                        data.setResponder("true");
                        new SendNotification().onPush(data, this).observe(this, (sucesso) -> {
                            if(sucesso){
                                input.dismiss();
                                Notification novaNotificacao = new Notification();
                                novaNotificacao.setRecebida(false);
                                novaNotificacao.setData(DateTime.getTime());
                                novaNotificacao.setLida(true);
                                novaNotificacao.setCliente(data.getCliente());
                                novaNotificacao.setMensagem(mensagem);
                                novaNotificacao.setType(data.getType());
                                novaNotificacao.setCliente(data.getUidCliente());
                                novaNotificacao.setUidServico(data.getUidServico());
                                novaNotificacao.setTitulo(data.getTitulo());
                                novaNotificacao.setResponder(false);
                                novaNotificacao.setUid(UUID.randomUUID().toString());
                                new NotificationDAO(this).adicionar(novaNotificacao);
                                MySnackbar.makeText(this, R.string.msg_enviada_scs, ModoColor._success).show();
                            viewModel.update(this);
                            }else MyToast.makeText(this, R.string.falha_notificar, ModoColor._falha).show();
                            dialog.dismiss();
                        });
                    }else{
                        MyToast.makeText(this, R.string.digite_msg_send, ModoColor._falha).show();
                    }
                }
            }));
            layout.findViewById(R.id.cancel).setOnClickListener((v -> input.dismiss()));

        }
    }

    @Override
    protected void onResume() {
        onUpdateData();
        super.onResume();
    }

    private void onUpdateData() {
        viewModel.update(this);
    }

    public void onBack(View view) {
        onBackPressed();
    }
}