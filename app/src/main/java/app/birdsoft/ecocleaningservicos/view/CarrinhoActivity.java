package app.birdsoft.ecocleaningservicos.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorCarrinho;
import app.birdsoft.ecocleaningservicos.dialogo.DialogAgendamento;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.dialogo.MainDialog;
import app.birdsoft.ecocleaningservicos.login.WelcomeActivity;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.Status;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Cliente;
import app.birdsoft.ecocleaningservicos.model.Endereco;
import app.birdsoft.ecocleaningservicos.model.Pedido;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.settings.Settings;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.tools.Mask;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.tools.Pagamento;
import app.birdsoft.ecocleaningservicos.viewModel.CarrinhoViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ClienteViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.SettingsViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class CarrinhoActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 2;
    private CarrinhoViewModel viewModel;
    private EditText msg_observacao;
    private TextView valorText, endereco_cliente, valor_troco;
    private RadioButton op_card_credito, op_card_debito, op_dinheiro;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean isBloqueado;
    private LatLng minhaLocalizacao;
    private AdaptadorCarrinho adaptador;
    private Cliente cliente;
    private LoadingDialog loading;
    private double valorTroco = 0;
    private Button agenda;
    private String dataAgendamento, horarioAgendamento;
    private String enderecoCliente;
    private double valorTotal = 0;
    private int hora = 0, minuto = 0;
    private LinearLayout listaLayout, layout_conexao_error, lyt_progress_endereco, layout_endereco_visualizador, layout_troco, vazio, lyt_progress, layout_wifi_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        loading = new LoadingDialog(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        viewModel = new ViewModelProvider(this).get(CarrinhoViewModel.class);
        runOnUiThread(()-> viewModel.init(this));
        ClienteViewModel clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        runOnUiThread(()-> clienteViewModel.init(this));
        adaptador = new AdaptadorCarrinho(new ArrayList<>(), this);
        layout_conexao_error  =findViewById(R.id.layout_conexao_error);
        valorText = findViewById(R.id.valorText);
        layout_endereco_visualizador = findViewById(R.id.layout_endereco_visualizador);
        op_dinheiro = findViewById(R.id.op_dinheiro);
        layout_troco = findViewById(R.id.layout_troco);
        endereco_cliente = findViewById(R.id.endereco_cliente);
        lyt_progress_endereco = findViewById(R.id.lyt_progress_endereco);
        op_card_debito = findViewById(R.id.op_card_debito);
        op_card_credito = findViewById(R.id.op_card_credito);
        msg_observacao = findViewById(R.id.msg_observacao);
        valor_troco = findViewById(R.id.valor_troco);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaLayout = findViewById(R.id.listaLayout);
        lyt_progress = findViewById(R.id.lyt_progress);
        agenda = findViewById(R.id.btn_agendar);
        vazio = findViewById(R.id.vazio);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        mRecyclerView.setAdapter(adaptador);
        clienteViewModel.getMutableLiveData().observe(this, (clienteElements -> {
            if(clienteElements.getCliente() != null){
                cliente = clienteElements.getCliente();
                isBloqueado = clienteElements.isBloqueado();
                enderecoCliente = clienteElements.getEndereco();
                endereco_cliente.setText(enderecoCliente);
                lyt_progress_endereco.setVisibility(View.GONE);
                layout_endereco_visualizador.setVisibility(View.VISIBLE);
            }
        }));

        op_dinheiro.setOnCheckedChangeListener((buttonView, isChecked) -> layout_troco.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        viewModel.getMutableLiveData().observe(this, (carrinhoElements -> {
            if(carrinhoElements.isCarrinho()){
                adaptador.insert(carrinhoElements.getCarrinhos());
                adaptador.setOnDeleteItemListener((v, carrinho, position) -> viewModel.delete(carrinho, this).observe(this, (integer -> {
                    switch (integer){
                        case 1:
                            MySnackbar.makeText(this, R.string.removido_carrinho_erro, ModoColor._falha).show();
                            break;
                        case 2:
                            MySnackbar.makeText(this, R.string.removido_carrinho_sucesso, ModoColor._success).show();
                            inputValor();
                            break;
                    }
                })));
            }
            inputValor();
            listaLayout.setVisibility(carrinhoElements.getListaVisibility());
            vazio.setVisibility(carrinhoElements.getVazioVisibility());
            lyt_progress.setVisibility(carrinhoElements.getProgressVisibility());
            layout_conexao_error.setVisibility(carrinhoElements.getLayoutConexao());
            layout_wifi_error.setVisibility(carrinhoElements.getLayoutWifiOffline());
        }));
    }

    private void inputValor(){
        valorTotal = adaptador.getValor();
        hora = adaptador.getHoras();
        minuto = adaptador.getMinutos();
        ((TextView)findViewById(R.id.tempo_servico_total)).setText(HelperManager.getTempoServico(hora, minuto));
        valorText.setText(Mask.formatarValor(valorTotal));
        if(horarioAgendamento != null)
            ((TextView)findViewById(R.id.tempo_servico_total_agenda)).setText(String.format("%s %s", getString(R.string.finalizar), HelperManager.getTempoServicoTotal(hora, minuto, horarioAgendamento)));
    }

    public void onUpdateConnection(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        new ViewModelProvider(this).get(ClienteViewModel.class).update(this);
        viewModel.update(this);
    }

    public void onFinalizarServico(View view) {
        if(HelperManager.getTempoServicoParse(adaptador.getHoras(), adaptador.getMinutos())){
            if(!op_card_credito.isChecked() && !op_card_debito.isChecked() && !op_dinheiro.isChecked()){
                msgErro(R.string.erro_selecione_pagamento);
                return;
            }
            if(agenda.getText().toString().equals(getString(R.string.agendar)) || dataAgendamento == null || horarioAgendamento == null){
                msgErro(R.string.informe_um_agendamento);
                return;
            }
            loading.show();
            onBuscarMinhaLocalizacao();
        }else{
            new DialogMessage(this, R.string.maximo_hr, false, R.string.confirmar).show();
        }
    }

    private void msgErro(@StringRes int msg) {
        MySnackbar.makeText(this, msg, ModoColor._falha).show();
    }

    private void onBuscarMinhaLocalizacao() {
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
                            resolvable.startResolutionForResult(CarrinhoActivity.this,
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
            System.out.println("onComplete");
            try {
                if (task.isSuccessful()) {
                    Location result = task.getResult();
                    minhaLocalizacao = new LatLng(result.getLatitude(), result.getLongitude());
                    enviarPedido();
                }
            } catch (Exception x) {
                DialogMessage
                        message = new DialogMessage(this, getString(R.string.gps_nescessario_erro), false, "");
                message.show();
                loading.dismiss();
            }
        });
    }

    private void enviarPedido() {
        if(cliente.isBlock()){
            Intent intent = new Intent(this, BloqueadoActivity.class);
            intent.putExtra("msg", cliente.getMsgBlock());
            startActivity(intent);
            finish();
            WelcomeActivity.getInstance().finish();
            Conexao.getFirebaseAuth().signOut();
            return;
        }
        Pedido pedido = new Pedido();
        String uid = UUID.randomUUID().toString();
        pedido.setClienteNome(cliente.getNome());
        pedido.setDataServico(System.currentTimeMillis());
        pedido.setDisplayName("Contrato: " + DateTime.toDateString("dd MMM yyyy"));
        pedido.setEndereco(enderecoCliente);
        pedido.setItensServicos(adaptador.getLista());
        pedido.setPagamento(getPagamentos());
        pedido.setObservacao(msg_observacao.getText().toString().trim());
        pedido.setStatusServico(Status.pendente.toString());
        pedido.setTelefone(cliente.getTelefone());
        pedido.setTroco(op_dinheiro.isChecked() && valorTroco != (double) 0);
        pedido.setValorTroco(valorTroco);
        pedido.setValorTotal(valorTotal);
        pedido.setUid_client(cliente.getUuid());
        pedido.setCidade(cliente.getCidadeLocal());
        pedido.setUid(uid);
        pedido.setHora(hora);
        pedido.setMinuto(minuto);
        pedido.setFimDoHorarioTotal(HelperManager.getTempoServicoTotal(hora, minuto, horarioAgendamento));
        pedido.setHorarioAgendamento(horarioAgendamento);
        pedido.setDataAgendamento(dataAgendamento);
        pedido.setCoordenadas(minhaLocalizacao.latitude +", "+ minhaLocalizacao.longitude);
        System.out.println("conectando");
        ServicosViewModel vm = new ViewModelProvider(this).get(ServicosViewModel.class);
        vm.inflateServico(pedido).observe(this, (res) ->{
            DialogMessage
                    message = new DialogMessage(this, getString(res), false, "");
            message.show();
            message.setOnDismissListener((dialog -> {
                if(res == R.string.servico_adicionado){
                    Navigate.activity(this).navigate(ContratosActivity.class);
                    if(SelecionadorServicosItensActivity.getInstance() != null)
                        SelecionadorServicosItensActivity.getInstance().finish();
                    if(SelecionadorServicosUnicoActivity.getInstance() != null)
                        SelecionadorServicosUnicoActivity.getInstance().finish();
                    finish();
                }
            }));

            if(res == R.string.servico_adicionado){
                viewModel.delete(this);
                viewModel.update(this);
                vm.updateContratos(this);
            }
            loading.dismiss();
        });
    }

    private String getPagamentos() {
        String res;
        if(op_dinheiro.isChecked()){
            res = Pagamento.dinheiro.toString();
        }else if(op_card_credito.isChecked()){
            res  = Pagamento.cartao_credito.toString();
        }else{ res = Pagamento.cartao_debido.toString(); }
        return res;
    }

    public void onEditarEndereco(View view) {
        HelperManager.inputEnderecoCliente(this).observe(this, (endereco1 -> {
            if(endereco1 != null){
                LoadingDialog dialog = new LoadingDialog(this);
                dialog.show();

                new ViewModelProvider(this).get(SettingsViewModel.class).trocaEndereco(endereco1, this).observe(this, (sucesso -> {
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
                        this.endereco_cliente.setText(Usuario.getEndereco(this));
                        enderecoCliente = Usuario.getEndereco(this);
                        cliente.setEndereco(Usuario.getEndereco(this));
                        MyToast.makeText(this, R.string.troca_endereco, ModoColor._success).show();
                        new ViewModelProvider(this).get(SettingsViewModel.class).update(this);
                    }else{
                        MyToast.makeText(this, R.string.error_troca_endereco, ModoColor._falha).show();
                    }
                    dialog.dismiss();
                }));
            }
        }));
    }

    public void onBack(View view) { onBackPressed(); }

    public void onInputTroco(View view) {
        MainDialog dialog = new MainDialog();
        dialog.generateCurrencyAmountKeyboardDialog(this, valorTroco);
        dialog.setOnConfirmationClick((valor, real) -> {
            valor_troco.setText(real);
            valorTroco = valor;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onAgendamento(View view) {
        view.setEnabled(false);
        DialogAgendamento agendamento = new DialogAgendamento(this, this, this);
        agendamento.show();
        agendamento.setOnDataSelectedListener(((data, horario) -> {
            dataAgendamento = data;
            horarioAgendamento = horario;
            ((TextView)findViewById(R.id.tempo_servico_total_agenda)).setText(String.format("%s %s", getString(R.string.finalizar), HelperManager.getTempoServicoTotal(hora, minuto, horario)));
            agenda.setText(String.format("%s as %s", data, horario));
        }));
        agendamento.setOnDismissListener((dialog -> view.setEnabled(true)));
    }
}