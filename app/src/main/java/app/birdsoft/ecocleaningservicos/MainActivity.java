package app.birdsoft.ecocleaningservicos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorServicos;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.servicos.ServicoSegundoPlano;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.view.CarrinhoActivity;
import app.birdsoft.ecocleaningservicos.view.ConfiguracoesActivity;
import app.birdsoft.ecocleaningservicos.view.NotificacoesActivity;
import app.birdsoft.ecocleaningservicos.viewModel.CarrinhoViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.FavoritosViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.NotificationsViewModel;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;
import app.birdsoft.ecocleaningservicos.widget.MyToast;

public class MainActivity extends AppCompatActivity {

    boolean isNavigationHide = false;
    private BottomNavigationView navigation;
    private NotificationsViewModel notificationsViewModel;
    private ServicosViewModel viewModel;
    private CardView layout_princial;
    private LinearLayout layout_conexao_error, vazio, lyt_progress, layout_wifi_error;
    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }
    private long countClick = 0;

    private void toast(String msg, int x){
        MyToast.makeText(this, msg).show();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit(){
        if(System.currentTimeMillis() - countClick > 2000){
            countClick = System.currentTimeMillis();
            toast(getString(R.string.clique_para_sair), 0);
            return;
        }
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        try{startService(new Intent(getBaseContext(), ServicoSegundoPlano.class));}catch (Exception ignored){}
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        navigation = bottomNavigationView;
        layout_princial = findViewById(R.id.layout_princial);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        notificationsViewModel.init(this);
        TextView contador_carrinho  =findViewById(R.id.contador_carrinho);
        vazio = findViewById(R.id.vazio);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        lyt_progress = findViewById(R.id.lyt_progress);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this).get(ServicosViewModel.class);
        runOnUiThread(() -> viewModel.init(this));
        AdaptadorServicos adaptador = new AdaptadorServicos(new ArrayList<>(), this);
        mRecyclerView.setAdapter(adaptador);
        viewModel.getMutableLiveData().observe(this, (servicoElements -> {
            adaptador.inserir(servicoElements.getServicos());
            layout_princial.setVisibility(servicoElements.getListaVisibility());
            vazio.setVisibility(servicoElements.getVazioVisibility());
            lyt_progress.setVisibility(servicoElements.getProgressVisibility());
            layout_conexao_error.setVisibility(servicoElements.getLayoutConexaoVisibility());
            layout_wifi_error.setVisibility(servicoElements.getLayoutWifiOffline());
        }));
        navigation.setOnNavigationItemSelectedListener((item -> {
            Navigate.activity(this).navigate(item.getItemId());
            return false;
        }));
        ((NestedScrollView)findViewById(R.id.nested_scroll_view)).setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i2, i3, i4) -> {
            if (i2 < i4) {
                animateNavigation(false);
            }
            if (i2 > i4) {
                animateNavigation(true);
            }
        });
        adaptador.setOnFavoritoChanged(((checkBox, servico, position) -> new ViewModelProvider(this)
                .get(FavoritosViewModel.class)
                .favoritar(checkBox, servico, this)
                .observe(this, (sucesso -> {
                    if(sucesso){
                        viewModel.updateServicos(this);
                        MySnackbar.makeText(this, String.format("%s %s aos favoritos", servico.getName(), checkBox.isChecked() ? "adicionado" : "removido"), ModoColor._success).show();
                    }else{
                        checkBox.setChecked(!checkBox.isChecked());
                        MySnackbar.makeText(this, R.string.falha_add_favorito, ModoColor._falha).show();
                    }
                }))));
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
        notificationsViewModel.getMutableLiveData().observe(this, (notificationElements -> findViewById(R.id.icon_notification_view).setVisibility(notificationElements.getLayoutVisibility() == View.VISIBLE && notificationElements.getNumberNaoLida() > 0 ? View.VISIBLE : View.GONE)));
    }

    public void animateNavigation(boolean z) {
        if (this.isNavigationHide && z) {
            return;
        }
        if (this.isNavigationHide || z) {
            this.isNavigationHide = z;
            this.navigation.animate().translationY((float) (z ? this.navigation.getHeight() * 2 : 0)).setStartDelay(100).setDuration(300).start();
        }
    }

    @Override
    protected void onResume() {
        viewModel.updateServicos(this);
        notificationsViewModel.update(this);
        super.onResume();
    }

    public void onGoCtt(View view) {
        switch (Integer.parseInt((String) view.getTag())){
            case 0:
                HelperManager.onZapService(this, this, this);
                break;
            case 1:
                HelperManager.onCallService(this, this, this);
                break;
            case 2:
                HelperManager.onEmailService(this, this, this);
                break;
        }
    }

    public void oncarrinho(View view){
        Navigate.activity(this).navigate(CarrinhoActivity.class);
    }

    public void onUpdateConnection(View view) {
        MyToast.makeText(this, R.string.atualizando).show();
        new ViewModelProvider(this).get(CarrinhoViewModel.class).update(this);
        viewModel.updateServicos(this);
    }

    public void onNotification(View view) {
        Navigate.activity(this).navigate(NotificacoesActivity.class);
    }

    public void onConfig(View view) {
        Navigate.activity(this).navigate(ConfiguracoesActivity.class);
    }

    public void onShare(View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "");
        intent.putExtra("android.intent.extra.TEXT", getPlainText());
        startActivity(Intent.createChooser(intent, "EcoCleaning Serviços"));
    }

    private String getPlainText() {
        return String.format("%s\n\nhttps://play.google.com/store/apps/details?id=app.birdsoft.ecocleaningservicos", getString(R.string.texto_share_app));
    }
}