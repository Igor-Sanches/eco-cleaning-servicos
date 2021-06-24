package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorServicosItens;
import app.birdsoft.ecocleaningservicos.dialogo.DialogInput;
import app.birdsoft.ecocleaningservicos.dialogo.DialogMessage;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.manager.Usuario;
import app.birdsoft.ecocleaningservicos.model.Carrinho;
import app.birdsoft.ecocleaningservicos.model.ItemServico;
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

public class SelecionadorServicosItensActivity extends AppCompatActivity {

    private TextView valorText;
    private TextView summary, displayName;
    private ImageView app_bar_image;
    private AdaptadorServicosItens adaptador;
    private int tempoX = 0;
    private Servico servico;
    private int hora = 0, minuto = 0, subHora = 0, subMinuto = 0;
    private boolean hr = true;
    private double valor = 0, subValor = 0;
    private static SelecionadorServicosItensActivity instance;

    public static SelecionadorServicosItensActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionador_servicos_itens);
        instance = this;
        servico = (Servico) getIntent().getSerializableExtra("servico");
        if(servico == null){
            MyToast.makeText(this, R.string.error_layout).show();
            finish();
            return;
        }
        adaptador = new AdaptadorServicosItens(new ArrayList<>(), this);
        LinearLayout vazio = findViewById(R.id.vazio);
        LinearLayout layout_wifi_error = findViewById(R.id.layout_wifi_error);
        summary = findViewById(R.id.summary);
        TextView contador_carrinho  =findViewById(R.id.contador_carrinho);
        app_bar_image = findViewById(R.id.app_bar_image);
        CoordinatorLayout lista_layout = findViewById(R.id.layout_principal);
        LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        displayName = findViewById(R.id.displayName);
        valorText = findViewById(R.id.valorText);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adaptador);
        input();
        adaptador = new AdaptadorServicosItens(HelperManager.convert(servico.getServicos()), this);
        mRecyclerView.setAdapter(adaptador);
        adaptador.setOnItemCheckedChanged((v, title, preco, itemServico, position, checkBox, layout, btnRemove, btnAdd, number, maxItensAdicionais) -> {
            try{

                boolean isAdd = false;
                if(layout != null){
                    isAdd = true;
                    btnAdd.setOnClickListener(view -> {
                        if(checkBox.isChecked()){
                            try{
                                int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                                itensAdicionaisCount += 1;
                                number.setText(String.format("%s", itensAdicionaisCount));
                                btnRemove.setEnabled(itensAdicionaisCount > 1);
                                if(maxItensAdicionais > 0){
                                    btnAdd.setEnabled(itensAdicionaisCount < maxItensAdicionais);
                                }
                                itemServico.tempoSomar += 1;
                                itemServico.valor += preco;
                                for (int i = 0; i < itemServico.lista.getContents().size(); i++) {
                                    if(itemServico.lista.getContents().get(i).equals(title) && itemServico.lista.getValores().get(i).equals(preco)){
                                        itemServico.lista.getQuantidate().set(i, itensAdicionaisCount);
                                    }
                                }
                                valorView();
                            }catch (Exception x){
                                // message(x.getMessage());
                            }

                        }
                    });
                    btnRemove.setOnClickListener(view -> {
                        if(checkBox.isChecked()){
                            int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                            itensAdicionaisCount -= 1;
                            number.setText(String.format("%s", itensAdicionaisCount));
                            itemServico.valor -= preco;
                            itemServico.tempoSomar -= 1;
                            for (int i = 0; i < itemServico.lista.getContents().size(); i++) {
                                if(itemServico.lista.getContents().get(i).equals(title) && itemServico.lista.getValores().get(i).equals(preco)){
                                    itemServico.lista.getQuantidate().set(i, itensAdicionaisCount);
                                }
                            }
                            btnRemove.setEnabled(itensAdicionaisCount >= 2);
                            if(maxItensAdicionais != 0){
                                if(itensAdicionaisCount < maxItensAdicionais){
                                    btnAdd.setEnabled(true);
                                }
                            }
                            valorView();
                        }
                    });

                    if(checkBox.isChecked()){
                        int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                        int x = itemServico.tempoSomar;
                        Double y = itemServico.valor;
                        for (int i = 0; i < itensAdicionaisCount; i++) {
                            y += preco;
                            x += 1;
                        }
                        itemServico.tempoSomar = x;
                        itemServico.valor = y;
                        if(maxItensAdicionais > 1){
                            layout.setVisibility(View.VISIBLE);
                        }
                        itemServico.lista.setDisplayName(itemServico.getDispayTitulo());
                        itemServico.lista.getContents().add(title);
                        itemServico.lista.getValores().add(preco);
                        itemServico.lista.getQuantidate().add(itensAdicionaisCount);
                        valorView();
                        layout.setVisibility(maxItensAdicionais > 1 ? View.VISIBLE : View.GONE);

                    }else{
                        int itensAdicionaisCount = Integer.parseInt(number.getText().toString());
                        Double y = itemServico.valor;
                        int x = itemServico.tempoSomar;
                        for (int i = 0; i < itensAdicionaisCount; i++) {
                            y -= preco;
                            x -= 1;
                        }
                        itemServico.tempoSomar = x;
                        itemServico.valor = y;
                        if(maxItensAdicionais > 1){
                            layout.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < itemServico.lista.getContents().size(); i++) {
                            if(itemServico.lista.getContents().get(i).equals(title) && itemServico.lista.getValores().get(i).equals(preco)){
                                itemServico.lista.getValores().remove(i);
                                itemServico.lista.getContents().remove(i);
                                itemServico.lista.getQuantidate().remove(i);
                                if(itemServico.lista.getContents().size() == 0){
                                    itemServico.lista.setDisplayName("");
                                }
                            }
                        }
                        valorView();
                        layout.setVisibility(View.GONE);
                    }
                }

                if (!itemServico.isMultiselect()) {
                    if (checkBox.isChecked()) {
                        itemServico.max.add(checkBox);
                        if (itemServico.max.size() == itemServico.getSelectMax()) {
                            for (int i = 0; i < itemServico.allCheckBox.size(); i++){
                                CheckBox box = itemServico.allCheckBox.get(i);
                                for (int j = 0; j < itemServico.max.size(); j++) {
                                    CheckBox check = itemServico.max.get(j);
                                    if (check != box) {
                                        box.setEnabled(false);
                                    }
                                }
                            }

                            for (CheckBox x : itemServico.max) {
                                x.setEnabled(true);
                            }

                            //bloco.max.get(0).setChecked(false);
                            //bloco.max.remove(0)
                        }
                    } else {
                        for (int i = 0; i < itemServico.max.size(); i++) {
                            if (itemServico.max.get(i) == checkBox) {
                                itemServico.max.remove(i);
                                for (CheckBox check : itemServico.allCheckBox) {
                                    check.setEnabled(true);
                                }
                            }
                        }
                    }

                }

                if(!isAdd){
                    if(checkBox.isChecked()){
                        itemServico.lista.setDisplayName(itemServico.getDispayTitulo());
                        itemServico.lista.getContents().add(title);
                        itemServico.lista.getValores().add(preco);
                        itemServico.lista.getQuantidate().add(1);
                    }else{
                        for (int i = 0; i < itemServico.lista.getContents().size(); i++) {
                            if(itemServico.lista.getContents().get(i).equals(title) && itemServico.lista.getValores().get(i).equals(preco)){
                                itemServico.lista.getValores().remove(i);
                                itemServico.lista.getContents().remove(i);
                                itemServico.lista.getQuantidate().remove(i);
                                if(itemServico.lista.getContents().size() == 0){
                                    itemServico.lista.setDisplayName("");
                                }
                            }
                        }
                    }
                    if(checkBox.isChecked()){
                        itemServico.tempoSomar += 1;
                        if(itemServico.isValorMaior()){
                            if(itemServico.valor < preco) {
                                itemServico.valor = preco;
                            }
                        }else{
                            itemServico.valor += preco;
                        }
                    }else{
                        itemServico.tempoSomar -= 1;
                        if(itemServico.isValorMaior()){
                            double newValor = 0;
                            for (int i = 0; i < itemServico.lista.getValores().size(); i++) {
                                Double x = itemServico.lista.getValores().get(i);
                                if(newValor < x){
                                    newValor = x;
                                }
                            }

                            itemServico.valor = newValor;
                        }else{
                            itemServico.valor -= preco;
                        }
                    }
                }
                valorView();
                itemServico.modificate = itemServico.lista.getContents().size() > 0;
                itemServico.selected_obg = itemServico.modificate;
            }catch (Exception x){
                //message(x.getMessage());
            }
        });
        vazio.setVisibility(View.GONE);
        lista_layout.setVisibility(View.VISIBLE);
        lyt_progress.setVisibility(View.GONE);
        layout_wifi_error.setVisibility(View.GONE);
        findViewById(R.id.adicionar_carrinho).setOnClickListener((v -> {
            if(hr){
                adicionar();
            }else{
                new DialogMessage(this, R.string.maximo_hr, false, R.string.confirmar).show();
            }
        }));
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void adicionar() {
        try {
            boolean isValid = true;
            boolean modificate = false;
            List<ItemServico> lista = adaptador.getLista();
            for (int i = 0; i < lista.size(); i++) {
                ItemServico itemCardapio = lista.get(i);
                if(itemCardapio.modificate){
                    modificate = true;
                }
                if(itemCardapio.isObgdSelect()){
                    if(!itemCardapio.selected_obg){
                        isValid = false;
                        String msg;
                        msg = HelperManager.messageView(itemCardapio, this);
                        new DialogMessage(this, msg, false, "", getString(R.string.inportante)).show();
                        break;
                    }
                }
            }

            if(servico.getTipoServico() != 0){
                modificate = true;
            }

            if(isValid){
                if(modificate){
                    subValor = this.valor;
                    subHora = hora;
                    subMinuto = minuto;
                    onIniciar(Integer.parseInt("1"), lista);
                }else{
                    String msg;
                    msg = getString(R.string.selecione_para_continuar);
                    new DialogMessage(this, msg, false, "", getString(R.string.inportante)).show();

                }
            }
        }catch (Exception x){
            // message(x.getMessage());
        }

    }

    public void oncarrinho(View view){
        Navigate.activity(this).navigate(CarrinhoActivity.class);
    }

    private void onIniciar(int parseInt, List<ItemServico> lista) {
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
        for (int i = 0; i < lista.size(); i++) {
            list.add(lista.get(i).lista);
        }
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
            List<ItemServico> itemCardapioList = adaptador.getLista();
            double x = 0;
            for (int i = 0; i < itemCardapioList.size(); i++) {
                x += itemCardapioList.get(i).valor;
            }

            if(servico.getTipoServico() != 0){
                x += servico.getValor();
            }
            valor = x;
            valorText.setText(Mask.formatarValor(valor));
            tempo();
        }catch (Exception x){
            //message(x.getMessage());
        }
    }

    private void tempo(){
        List<ItemServico> itemCardapioList = adaptador.getLista();
        tempoX = 0;
        for (int i = 0; i < itemCardapioList.size(); i++) {
            tempoX += itemCardapioList.get(i).tempoSomar;
        }
        if(servico.getTipoServico() != 0){
            tempoX += 1;
        }

         hora = tempoX == 0 ? servico.getHora() : servico.getHora() * tempoX;
         minuto = tempoX == 0 ? servico.getMinuto() : servico.getMinuto() * tempoX;
         hr = HelperManager.getTempoServicoParse(hora, minuto);
        ((TextView)findViewById(R.id.tempo)).setText(HelperManager.getTempoServico(hora, minuto));

    }

    public void onBack(View view) {
        onBackPressed();
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