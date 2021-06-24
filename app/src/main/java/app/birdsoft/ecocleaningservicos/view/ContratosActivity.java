package app.birdsoft.ecocleaningservicos.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import app.birdsoft.ecocleaningservicos.App;
import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorContratos;
import app.birdsoft.ecocleaningservicos.dialogo.DialogAgendamento;
import app.birdsoft.ecocleaningservicos.dialogo.DialogAtrasado;
import app.birdsoft.ecocleaningservicos.dialogo.DialogInput;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.DialogNaoAtrasado;
import app.birdsoft.ecocleaningservicos.dialogo.DialogObservacao;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.manager.Status;
import app.birdsoft.ecocleaningservicos.model.Notification;
import app.birdsoft.ecocleaningservicos.model.NotificationData;
import app.birdsoft.ecocleaningservicos.model.NotificationType;
import app.birdsoft.ecocleaningservicos.model.Pedido;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.servicos.SendNotification;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.viewModel.ClienteViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.NotificationsViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class ContratosActivity extends AppCompatActivity {

    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng minhaLocalizacao;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private String dataAgendamento, horarioAgendamento;
    private String endereco;
    private Pedido pedido;
    private LoadingDialog loading;
    private AdaptadorContratos adaptador;
    private LinearLayout lyt_progress, layout_conexao_error, layout_wifi_error, listaLayout, vazio;
    private ServicosViewModel viewModel;

    @Override
    protected void onResume() {
        viewModel.updateContratos(this);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contratos);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        View sheetBottom = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
        sheetDialog = new BottomSheetDialog(this);
        loading = new LoadingDialog(this);
        adaptador = new AdaptadorContratos(new ArrayList<>(), this);
        lyt_progress = findViewById(R.id.lyt_progress);
        listaLayout = findViewById(R.id.listaLayout);
        vazio = findViewById(R.id.vazio);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adaptador);
        viewModel = new ViewModelProvider(this).get(ServicosViewModel.class);
        runOnUiThread(()-> viewModel.initContratos(this));
        viewModel.getMutableLiveDataContratos().observe(this, pedidosElements -> {
            adaptador.inserir(pedidosElements.getPedidos());
            listaLayout.setVisibility(pedidosElements.getLayoutVisibility());
            lyt_progress.setVisibility(pedidosElements.getLayoutProgressVisibility());
            vazio.setVisibility(pedidosElements.getLayoutVazioVisibility());
            layout_conexao_error.setVisibility(pedidosElements.getLayoutConnectionVisibility());
            layout_wifi_error.setVisibility(pedidosElements.getLayoutWifiVisibility());
        });

        viewModel.onChangedDataBase(this).observe(this, (changed) -> {
            if(changed){
                 viewModel.updateContratos(this);
                App.isBloqueado(this);
            }
        });
        adaptador.setOnCallServicoAction(() -> HelperManager.onCallService(this, this, this));
        adaptador.setOnZapServicoAction(() -> HelperManager.onZapService(this, this, this));
        adaptador.setOnEditarObservacaoAction((this::onEditarObservacao));
        adaptador.setOnMenuOptionsAction((this::onShowMenuView));
        adaptador.setOnVerObersavacaoAction((this::dialogObservacao));
        adaptador.setOnMotivoCancelamentoAction((this::omMotivoCancelamento));
        adaptador.setOnConfirmarCancelamentoAction(this::confirmarCancelamento);
        adaptador.setOnAceitarAlteracaoAction(this::aceitarAlteracao);
        adaptador.setOnRejeitarAlteracaoAction(this::rejeitarAlteracao);
        adaptador.setOnContraProposta(this::contraProposta);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void contraProposta(Pedido pedido) {
        DialogMessage message = new DialogMessage(this, R.string.mensagem_alterar_agendamento, true, R.string.sim_desejo);
        message.show();
        message.setOnPossiveButtonClicked(() -> {
            @SuppressLint("InflateParams") View root = LayoutInflater.from(this).inflate(R.layout.dialog_novo_agedamento, null);
            DialogInput input = new DialogInput(this, root, null);
            input.show();
            TextView tempo_servico_total = root.findViewById(R.id.tempo_servico_total);
            TextView btn_agendar = root.findViewById(R.id.btn_agendar);
            TextView tempo_servico_total_agenda = root.findViewById(R.id.tempo_servico_total_agenda);
            tempo_servico_total.setText(HelperManager.getTempoServico(pedido.getHora(), pedido.getMinuto()));
            btn_agendar.setOnClickListener((v) -> {
                DialogAgendamento agendamento = new DialogAgendamento(this, this, this);
                agendamento.show();
                agendamento.setOnDataSelectedListener(((data, horario) -> {
                    dataAgendamento = data;
                    horarioAgendamento = horario;
                    tempo_servico_total_agenda.setText(String.format("%s %s", getString(R.string.finalizar), HelperManager.getTempoServicoTotal(pedido.getHora(), pedido.getMinuto(), horario)));
                    btn_agendar.setText(String.format("%s as %s", data, horario));
                }));
            });

            root.findViewById(R.id.confirmar).setOnClickListener((v) -> {
                if(isConnected()){
                    if(btn_agendar.getText().toString().equals(getString(R.string.agendar)) || dataAgendamento == null || horarioAgendamento == null){
                        MySnackbar.makeText(this, R.string.informe_dados, ModoColor._falha).show();
                        return;
                    }
                    loading.show();
                    ServicosViewModel vm = new ViewModelProvider(this).get(ServicosViewModel.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("dataAgendamento", dataAgendamento);
                    map.put("statusServico", Status.contraProposta.toString());
                    map.put("horarioAgendamento", horarioAgendamento);
                    map.put("fimDoHorarioTotal", HelperManager.getTempoServicoTotal(pedido.getHora(), pedido.getMinuto(), horarioAgendamento));
                    vm.inflateServico(pedido, map).observe(this, (result -> {
                        if(result == R.string.servico_adicionado){
                            new DialogMessage(this, R.string.sua_proposta_enviada, false, R.string.confirmar).show();
                            input.dismiss();
                        }else MyToast.makeText(this, result, ModoColor._falha).show();
                        loading.dismiss();
                    }));
                }
            });
            root.findViewById(R.id.cancel).setOnClickListener((v) -> input.dismiss());
        });
    }

    private void rejeitarAlteracao(Pedido pedido) {
        DialogMessage message
                = new DialogMessage(this, R.string.rejeitar_alteracao_msg, true);
        message.show();
        message.setOnPossiveButtonClicked(() -> {
            loading.show();
            Map<String, Object> map = new HashMap<>();
            map.put("contraProposta", false);
            map.put("statusServico", Status.rejeitado.toString());
            viewModel.update(map, pedido).observe(this, (sucesso -> {
                if(sucesso){
                    MySnackbar.makeText(this, R.string.rejeitado_agendamento_confirmado, ModoColor._success).show();
                    viewModel.updateContratos(this);
                }else MySnackbar.makeText(this, R.string.rejeitado_agendamento_confirmado_falha, ModoColor._falha).show();

                loading.dismiss();
            }));
        });
    }

    private void aceitarAlteracao(Pedido pedido) {
        loading.show();
        Map<String, Object> map = new HashMap<>();
        map.put("contraProposta", false);
        map.put("statusServico", Status.aceito.toString());
        viewModel.update(map, pedido).observe(this, (sucesso -> {
            if(sucesso){
                MySnackbar.makeText(this, R.string.novo_agendamento_confirmado, ModoColor._success).show();
                viewModel.updateContratos(this);
            }else MySnackbar.makeText(this, R.string.novo_agendamento_confirmado_falha, ModoColor._falha).show();

            loading.dismiss();
        }));

    }

    private void confirmarCancelamento(Pedido pedido, int position) {
        if(isConnected()){
            loading.show();
            viewModel.delete(pedido).observe(this, (sucesso -> {
                if(sucesso){
                    MySnackbar.makeText(this, R.string.confirmado_cancel, ModoColor._success).show();
                    adaptador.remover(position);
                }else MySnackbar.makeText(this, R.string.confirmado_cancel_erro, ModoColor._falha).show();

                loading.dismiss();
            }));
        }
    }

    private void onEditarObservacao(Pedido pedido) {
        DialogObservacao observacao = new DialogObservacao(this);
        observacao.show();
        observacao.setOnClickecPositive((observacaoMsg -> {
            if(Conexao.isConnected(this)){
                loading.show();
                Map<String, Object> map = new HashMap<>();
                map.put("editadoObservacao", true);
                map.put("observacao", observacaoMsg);
                viewModel.update(map, pedido).observe(this, (sucesso) -> {
                    viewModel.updateContratos(this);
                    MySnackbar.makeText(this, sucesso ? R.string.observacao_update_success : R.string.observacao_update_erro, sucesso ? ModoColor._success : ModoColor._falha).show();
                });
            }else MySnackbar.makeText(this, R.string.sem_conexao, ModoColor._falha).show();
            loading.dismiss();
        }));
    }

    private void omMotivoCancelamento(String motivo) {
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.fragment_mensagem_dialog_pedido, null);
        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(_view);
        TextView mensagem = _view.findViewById(R.id.mensagem);
        ((TextView)_view.findViewById(R.id.name_toolbar)).setText(getString(R.string.motivo_cancelamento));
        mensagem.setText(motivo);
        _view.findViewById(R.id.bt_close).setOnClickListener(view -> sheetDialog.dismiss());

        if(Build.VERSION.SDK_INT >= 21){
            sheetDialog.getWindow().addFlags(67108864);
        }

        sheetDialog.show();
    }

    private void onShowMenuView(Pedido pedido) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.dialog_relatar_problemas, null);
        DialogInput input = new DialogInput(this, view, null);
        input.show();
        Button btn_pedido_n_chegou = view.findViewById(R.id.btn_pedido_n_chegou);
        Button btn_pedido_cancelar = view.findViewById(R.id.btn_pedido_cancelar);
        Button btn_pedido_alterar_endereco = view.findViewById(R.id.btn_pedido_alterar_endereco);
        Button btn_pedido_enviar_notificacao = view.findViewById(R.id.btn_pedido_enviar_notificacao);
        assert btn_pedido_n_chegou != null;
        assert btn_pedido_cancelar != null;
        assert btn_pedido_alterar_endereco != null;
        assert btn_pedido_enviar_notificacao != null;
        btn_pedido_cancelar.setEnabled(!pedido.isCancelado() || pedido.getStatusServico().equals(Status.pendente.toString()) || pedido.getStatusServico().equals(Status.aceito.toString()));

        btn_pedido_enviar_notificacao.setOnClickListener((v) -> {
            input.dismiss();
            if(isConnected()){
                enviarNotificacao(pedido);
            }
        });
        btn_pedido_alterar_endereco.setOnClickListener((v) -> {
            input.dismiss();
            if(isConnected()){
                onChangedEndereco(pedido);
            }
        });
        btn_pedido_cancelar.setOnClickListener((v) -> {
            input.dismiss();
            onDialogCancelar(pedido);
        });
        btn_pedido_n_chegou.setOnClickListener((v) -> {
            input.dismiss();
            if(!pedido.isAtrasado())
                onDialogRelatarAtraso(pedido);
            else{
                new DialogMessage(this, getString(R.string.msg_atraso_one_time), false, null).show();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener((v) -> input.dismiss());
    }

    private void enviarNotificacao(Pedido pedido) {
        if(isConnected()){
            @SuppressLint("InflateParams") View layout = LayoutInflater.from(this).inflate(R.layout.dialog_enivar_notificacao_pedido, null);
            DialogInput input = new DialogInput(this, layout, null);
            input.show();
            EditText msgsend = layout.findViewById(R.id.msg);
            EditText titlesend = layout.findViewById(R.id.title);
            layout.findViewById(R.id.confirmar).setOnClickListener((v -> {
                if(isConnected()){
                    String mensagem = msgsend.getText().toString().trim();
                    String title = titlesend.getText().toString().trim();
                    if(!title.equals("")){
                        if(!mensagem.equals("")){
                            loading.show();
                            NotificationData data = new NotificationData();
                            data.setMensagem(mensagem);
                            data.setTitulo(title);
                            data.setCliente(pedido.getClienteNome());
                            data.setType(NotificationType.mensagem_pedido.toString());
                            data.setStatusServico(pedido.getStatusServico());
                            data.setUidCliente(pedido.getUid_client());
                            data.setUidServico(pedido.getUid());
                            data.setResponder("true");
                            new SendNotification().onPush(data, this).observe(this, (sucesso) -> {
                                if(sucesso){
                                    input.dismiss();
                                    Notification notification = new Notification();
                                    notification.setRecebida(false);
                                    notification.setData(DateTime.getTime());
                                    notification.setLida(true);
                                    notification.setMensagem(mensagem);
                                    notification.setTitulo(title);
                                    notification.setResponder(false);
                                    notification.setType(NotificationType.mensagem_pedido.toString());
                                    notification.setUidServico(pedido.getUid());
                                    notification.setUidCliente(pedido.getUid_client());
                                    notification.setCliente(pedido.getClienteNome());
                                    notification.setUid(UUID.randomUUID().toString());
                                    new NotificationDAO(this).adicionar(notification);
                                    new ViewModelProvider(this).get(NotificationsViewModel.class).update(this);
                                    DialogMessage message = new DialogMessage(this, getString(R.string.msg_enviada), true, getString(R.string.sim_desejo));
                                    message.show();
                                    message.setOnPossiveButtonClicked(() -> Navigate.activity(this).navigate(NotificacoesActivity.class));
                                }else MyToast.makeText(this, R.string.falha_notificar, ModoColor._falha).show();
                                loading.dismiss();
                            });
                        }else{
                            MyToast.makeText(this, R.string.digite_msg_send, ModoColor._falha).show();
                        }

                    }else{
                        MyToast.makeText(this, R.string.digite_title_send, ModoColor._falha).show();
                    }
                }
            }));
            layout.findViewById(R.id.cancel).setOnClickListener((v -> input.dismiss()));

        }
    }

    private void onChangedEndereco(Pedido pedido) {
        DialogMessage inicio = new DialogMessage(this, getString(R.string.msg_inicio_endereco), true, getString(R.string.continuar));
        inicio.show();
        inicio.setOnPossiveButtonClicked(() -> HelperManager.inputEnderecoCliente(this).observe(this, (endereco -> {
            DialogMessage geoPoint = new DialogMessage(this, getString(R.string.geo_localizacao_changed), true, getString(R.string.sim_desejo));
            geoPoint.show();
            geoPoint.setOnNegativeButtonClicked(() -> {
                this.pedido = pedido;
                this.endereco = endereco.getEndereco();
                salvarNovoEndereco(null);
            });
            geoPoint.setOnPossiveButtonClicked(() -> {
                this.pedido = pedido;
                this.endereco = endereco.getEndereco();
                iniciarGeolocalizacao();
            });
        })));
    }

    private void salvarNovoEndereco(String geoPoint) {
        loading.show();
        Map<String, Object> map = new HashMap<>();
        map.put("endereco", endereco);
        map.put("editadoEndereco", true);
        if(geoPoint != null)
            map.put("coordenadas", geoPoint);

        viewModel.endereco(map, pedido).observe(this, (sucesso -> {
            if(sucesso){
                MySnackbar.makeText(this, R.string.novo_endereco_salvo, ModoColor._success).show();
                viewModel.updateContratos(this);
            }else MySnackbar.makeText(this, R.string.novo_endereco_salvo_erro, ModoColor._falha).show();

            loading.dismiss();
        }));
    }

    private void iniciarGeolocalizacao() {
        if (Build.VERSION.SDK_INT < 23) {
            onIniciarGeolocalizacao();
            return;
        }
        try {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } catch (Exception x) {
            onIniciarGeolocalizacao();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onIniciarGeolocalizacao();
            }
        } catch (Exception x) {
            DialogMessage
                    message = new DialogMessage(this, getString(R.string.gps_nescessario_erro), false, "");
            message.show();
            loading.dismiss();
        }
    }

    private synchronized void onIniciarGeolocalizacao() {
        if(Conexao.isConnected(this)){
            final LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder =
                    new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);

            SettingsClient client = LocationServices.getSettingsClient(this);

            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(this,
                    locationSettingsResponse -> onIniciarBuscar());

            task.addOnFailureListener(this, e -> {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(ContratosActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                            onIniciarBuscar();
                        } catch (IntentSender.SendIntentException sendEx) {
                            DialogMessage
                                    message = new DialogMessage(this, getString(R.string.gps_nescessario), false, "");
                            message.show();
                            loading.dismiss();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        onIniciarBuscar();
                        break;
                }
            });
        }else{
            DialogMessage
                    message = new DialogMessage(this, getString(R.string.gps_nescessario_erro), false, "");
            message.show();
            loading.dismiss();
        }
    }

    private void onIniciarBuscar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    Location result = task.getResult();
                    minhaLocalizacao = new LatLng(result.getLatitude(), result.getLongitude());
                    String geoPoint = String.format("%s, %s", minhaLocalizacao.latitude, minhaLocalizacao.longitude);
                    salvarNovoEndereco(geoPoint);
                }
            } catch (Exception x) {
                DialogMessage
                        message = new DialogMessage(this, getString(R.string.gps_nescessario_erro), false, "");
                message.show();
                loading.dismiss();
            }
        });
    }

    private void onDialogCancelar(Pedido pedido) {
        if(isConnected()){
            @SuppressLint("InflateParams") View layout = LayoutInflater.from(this).inflate(R.layout.dialog_cancelar_pedido, null);
            DialogInput input = new DialogInput(this, layout, null);
            input.show();
            EditText messageCancel = layout.findViewById(R.id.message_cancel);
            layout.findViewById(R.id.btn_comfirmar_cancelamento).setOnClickListener((v -> {
                if(isConnected()){
                    String mensagem = messageCancel.getText().toString().trim();
                    if(!mensagem.equals("")){
                        loading.show();
                        viewModel.cancelamento(pedido, mensagem).observe(this, (a ->{
                            if(a){
                                MySnackbar.makeText(this, R.string.send_cancelamento, ModoColor._success).show();

                                viewModel.updateContratos(this);
                                input.dismiss();
                            }else{
                                MySnackbar.makeText(this, R.string.erro_cancelamento, ModoColor._falha).show();
                            }
                            loading.dismiss();
                        }));
                    }else{
                        MyToast.makeText(this, R.string.digite_msg_cancelamento, ModoColor._falha).show();
                    }
                }
            }));
            layout.findViewById(R.id.btn_cancelar).setOnClickListener((v -> input.dismiss()));

        }
    }

    private void onDialogRelatarAtraso(Pedido pedido) {
        if(isConnected()){
            if(HelperManager.isAtrasado(pedido)) {
                DialogAtrasado atrasado = new DialogAtrasado(this);
                atrasado.show();
                atrasado.setOnClickListener(()-> {
                    loading.show();
                    viewModel.relatarAtraso(pedido).observe(this, (a -> {
                        if (a) {
                            MySnackbar.makeText(this, R.string.send_relata_atraso, ModoColor._success).show();
                            viewModel.updateContratos(this);
                        } else {
                            MySnackbar.makeText(this, R.string.erro_relata_atraso, ModoColor._falha).show();
                        }
                        loading.dismiss();
                    }));
                });

            }else{
                new DialogNaoAtrasado(this).show();
            }
        }

    }

    private boolean isConnected() {
        if(!Conexao.isConnected(this)){
            MySnackbar.makeText(this, R.string.sem_conexao, ModoColor._falha).show();
            return false;
        }
        return true;
    }

    private void dialogObservacao(String observacao) {
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        @SuppressLint("InflateParams") View _view = getLayoutInflater().inflate(R.layout.fragment_mensagem_dialog_pedido, null);
        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(_view);
        TextView mensagem = _view.findViewById(R.id.mensagem);
        ((TextView)_view.findViewById(R.id.name_toolbar)).setText(getString(R.string.observacao));
        mensagem.setText(observacao);
        _view.findViewById(R.id.bt_close).setOnClickListener(view -> sheetDialog.dismiss());

        if(Build.VERSION.SDK_INT >= 21){
            sheetDialog.getWindow().addFlags(67108864);
        }

        sheetDialog.show();
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onUpdateConnection(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        viewModel.updateContratos(this);
    }
}