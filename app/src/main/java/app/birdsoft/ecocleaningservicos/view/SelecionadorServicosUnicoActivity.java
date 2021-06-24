package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Carrinho;
import app.birdsoft.ecocleaningservicos.model.ItemServicoLista;
import app.birdsoft.ecocleaningservicos.model.Servico;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.tools.ImageUtils;
import app.birdsoft.ecocleaningservicos.tools.Mask;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.viewModel.CarrinhoViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class SelecionadorServicosUnicoActivity extends AppCompatActivity {

    private TextView valorText;
    private TextView summary, displayName;
    private ImageView app_bar_image;
    private Servico servico;
    private ImageButton btn_add, btn_remove;
    private int tempoX = 1;
    private TextView numero;
    private int hora = 0, minuto = 0, subHora = 0, subMinuto = 0;
    private boolean hr = true;
    private double valor = 0, subValor = 0;
    private static SelecionadorServicosUnicoActivity instance;

    public static SelecionadorServicosUnicoActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionador_servicos_unico);
        instance = this;
        servico = (Servico) getIntent().getSerializableExtra("servico");
        if(servico == null){
            MyToast.makeText(this, R.string.error_layout).show();
            finish();
            return;
        }
        LinearLayout vazio = findViewById(R.id.vazio);
        numero = findViewById(R.id.numero);
        btn_add = findViewById(R.id.btn_adicionar);
        btn_remove= findViewById(R.id.btn_remover);
        LinearLayout layout_wifi_error = findViewById(R.id.layout_wifi_error);
        summary = findViewById(R.id.summary);
        TextView contador_carrinho  =findViewById(R.id.contador_carrinho);
        app_bar_image = findViewById(R.id.app_bar_image);
        CoordinatorLayout lista_layout = findViewById(R.id.layout_principal);
        LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        displayName = findViewById(R.id.displayName);
        valorText = findViewById(R.id.valorText);
        input();
        vazio.setVisibility(View.GONE);
        lista_layout.setVisibility(View.VISIBLE);
        lyt_progress.setVisibility(View.GONE);
        layout_wifi_error.setVisibility(View.GONE);
        findViewById(R.id.adicionar_carrinho).setOnClickListener((v -> adicionar()));
        CarrinhoViewModel viewModel = new ViewModelProvider(this).get(CarrinhoViewModel.class);
        viewModel.init(this);
        viewModel.getMutableLiveData().observe(this, carrinhoElements -> {
            if(carrinhoElements != null){
                if(carrinhoElements.isCarrinho()){
                    if(carrinhoElements.getCount() > 0){
                        contador_carrinho.setText(String.valueOf(carrinhoElements.getCount()));
                        contador_carrinho.setVisibility(View.VISIBLE);
                    }else{
                        contador_carrinho.setText("");
                        contador_carrinho.setVisibility(View.GONE);
                    }
                }else{
                    contador_carrinho.setText("");
                    contador_carrinho.setVisibility(View.GONE);
                }
            }else{
                contador_carrinho.setText("");
                contador_carrinho.setVisibility(View.GONE);
            }
        });
        valorView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void adicionar() {
        if(!hr){
            new DialogMessage(this, R.string.maximo_hr, false, R.string.confirmar).show();
        }else{
            subValor = this.valor;
            subHora = hora;
            subMinuto = minuto;
            onIniciar(Integer.parseInt("1"));
        }
    }

    public void oncarrinho(View view){
        Navigate.activity(this).navigate(CarrinhoActivity.class);
    }

    private void onIniciar(int parseInt) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId_produto(servico.getUid());
        carrinho.setDisplayName(servico.getName());
        carrinho.setUid(UUID.randomUUID().toString());
        carrinho.setValorTotal(subValor);
        carrinho.setHoraTotal(subHora);
        carrinho.setMinutoTotal(subMinuto);
        carrinho.setQuantidade(parseInt);
        carrinho.setUid_client(Usuario.getUid(this));
        carrinho.setData(DateTime.getTime());
        ArrayList<ItemServicoLista> list = new ArrayList<>();
        carrinho.setListas(list);
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        new ViewModelProvider(this).get(CarrinhoViewModel.class).insert(carrinho, this).observe(this, success -> {
            if(success){
                MySnackbar.makeText(this, R.string.adicionado_ao_carrinho, ModoColor._success).show();
            }else{
                MySnackbar.makeText(this, R.string.error_adicionar_carrinho).show();
            }
            loadingDialog.dismiss();
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void input() {
        ImageUtils.displayImageFromUrl(this, servico.getImageUrl(), app_bar_image, getResources().getDrawable(R.drawable.image_add_ft));
        displayName.setText(servico.getName());
        if(servico.getDescricao() != null){
            summary.setText(servico.getDescricao());
        }
        tempo();
        valor = servico.getValor();
        valorText.setText(Mask.formatarValor(valor));
    }

    private void valorView() {
        try{
            double x = 0;
            if(servico.getTipoServico() != 0){
                x += servico.getValor();
            }
            x *= tempoX;
            valor = x;
            valorText.setText(Mask.formatarValor(valor));
            tempo();
        }catch (Exception x){
            //message(x.getMessage());
        }
    }

    private void tempo(){
        hora = tempoX == 0 ? servico.getHora() : servico.getHora() * tempoX;
        minuto = tempoX == 0 ? servico.getMinuto() : servico.getMinuto() * tempoX;
        numero.setText(String.valueOf(tempoX));
        hr = HelperManager.getTempoServicoParse(hora, minuto);
        ((TextView)findViewById(R.id.tempo)).setText(HelperManager.getTempoServico(hora, minuto));
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public void onAdicionar(View view) {
        tempoX += 1;
        btn_add.setEnabled(tempoX < 5);
        btn_remove.setEnabled(tempoX > 1);
        tempo();
        valorView();
    }

    public void onRemover(View view) {
        tempoX -= 1;
        btn_remove.setEnabled(tempoX > 1);
        btn_add.setEnabled(tempoX <= 5);
        tempo();
        valorView();
    }

    public void onShareService(View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "");
        intent.putExtra("android.intent.extra.TEXT", getPlainText());
        startActivity(Intent.createChooser(intent, "EcoCleaning Serviços"));
    }

    private String getPlainText() {
        if(servico.getDescricao() != null){
            return String.format("%s, %s\n\nhttps://www.appecocleaningservicos.com/birdsoft/uid?=%s", servico.getName(), servico.getDescricao(), servico.getUid());
        }else return String.format("%s\n\nhttps://www.appecocleaningservicos.com/birdsoft/uid?=%s", servico.getName(), servico.getUid());
    }
}