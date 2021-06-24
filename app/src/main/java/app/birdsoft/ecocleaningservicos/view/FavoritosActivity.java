package app.birdsoft.ecocleaningservicos.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorServicos;
import app.birdsoft.ecocleaningservicos.dialogo.LoadingDialog;
import app.birdsoft.ecocleaningservicos.tools.ModoColor;
import app.birdsoft.ecocleaningservicos.viewModel.FavoritosViewModel;
import app.birdsoft.ecocleaningservicos.widget.MySnackbar;

public class FavoritosActivity extends AppCompatActivity {

    private FavoritosViewModel viewModel;
    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;
    private LoadingDialog dialog;
    private View sheetBottom;
    private AdaptadorServicos adaptador;
    private LinearLayout lyt_progress, layout_conexao_error, layout_wifi_error, listaLayout, vazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        viewModel = new ViewModelProvider(this).get(FavoritosViewModel.class);
        dialog = new LoadingDialog(this);
        sheetBottom = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
        adaptador = new AdaptadorServicos(new ArrayList<>(), this);
        lyt_progress = findViewById(R.id.lyt_progress);
        listaLayout = findViewById(R.id.listaLayout);
        vazio = findViewById(R.id.vazio);
        layout_conexao_error = findViewById(R.id.layout_conexao_error);
        layout_wifi_error = findViewById(R.id.layout_wifi_error);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adaptador);
        runOnUiThread(()-> viewModel.init(this));

        viewModel.getMutableLiveData().observe(this, (favoritosElements -> {
            adaptador.inserir(favoritosElements.getServicos());
            listaLayout.setVisibility(favoritosElements.getListaVisibility());
            lyt_progress.setVisibility(favoritosElements.getProgressVisibility());
            vazio.setVisibility(favoritosElements.getVazioVisibility());
            layout_conexao_error.setVisibility(favoritosElements.getLayoutConexao());
            layout_wifi_error.setVisibility(favoritosElements.getLayoutWifiOffline());
        }));
        adaptador.setOnFavoritoChanged(((checkBox, servico, position) -> {
            viewModel.favoritar(checkBox, servico, this)
                    .observe(this, (sucesso -> {
                        if(sucesso){
                            viewModel.update(this);
                            MySnackbar.makeText(this, String.format("%s %s aos favoritos", servico.getName(), checkBox.isChecked() ? "adicionado" : "removido"), ModoColor._success).show();
                        }else{
                            checkBox.setChecked(!checkBox.isChecked());
                            MySnackbar.makeText(this, R.string.falha_add_favorito, ModoColor._falha).show();
                        }
                    }));
        }));
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