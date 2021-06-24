package app.birdsoft.ecocleaningservicos.dialogo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.adaptador.AdaptadorCidades;
import app.birdsoft.ecocleaningservicos.viewModel.ServicosViewModel;

public class DialogCidades extends AlertDialog {
    public interface OnCidadeClicked {
        void onCidade(String cidade);
    }

    private OnCidadeClicked onCidadeClicked;
    private final Activity activity;
    private final ViewModelStoreOwner owner;
    private final LifecycleOwner owner2;

    public void setOnCidadeClicked(OnCidadeClicked onCidadeClicked) {
        this.onCidadeClicked = onCidadeClicked;
    }

    public DialogCidades(@NonNull Activity activity, ViewModelStoreOwner owner, LifecycleOwner owner2) {
        super(activity);
        this.activity = activity;
        this.owner = owner;
        this.owner2 = owner2;
    }

    @Override
    public void show() {
        super.show();
        iniciarBuscar(activity, owner, owner2);
    }

    private void iniciarBuscar(Activity activity, ViewModelStoreOwner owner, LifecycleOwner owner2) {
        ProgressBar carregando = findViewById(R.id.carregando);
        TextView erro_texto = findViewById(R.id.erro_texto);
        RelativeLayout layout_loading = findViewById(R.id.layout_loading);
        RecyclerView listaCidades = findViewById(R.id.listaCidades);
        assert listaCidades != null;
        listaCidades.setLayoutManager(new LinearLayoutManager(activity));
        Button cancel = findViewById(R.id.cancel);
        TextView titulo = findViewById(R.id.titulo);
        assert carregando != null;
        assert titulo != null;
        assert erro_texto != null;
        assert layout_loading != null;
        assert cancel != null;
        new ViewModelProvider(owner)
                .get(ServicosViewModel.class)
                .getCidades().observe(owner2, (cidades -> {
            if(cidades != null){
                AdaptadorCidades adaptador = new AdaptadorCidades(new ArrayList<>(), activity);
                Collections.sort(cidades);
                listaCidades.setAdapter(adaptador);
                adaptador.inserir(cidades);
                titulo.setText(activity.getString(R.string.somente_maranhao));
                carregando.setVisibility(View.GONE);
                erro_texto.setVisibility(View.GONE);
                layout_loading.setVisibility(View.GONE);
                listaCidades.setVisibility(View.VISIBLE);
                adaptador.setOnCidadeClickedListener(((cidade, position) -> {
                    if(onCidadeClicked != null){
                        onCidadeClicked.onCidade(cidade);
                        dismiss();
                    }
                }));
            }else{
                titulo.setText(activity.getString(R.string.erro));
                carregando.setVisibility(View.GONE);
                erro_texto.setVisibility(View.VISIBLE);
            }
            cancel.setEnabled(true);
        }));
        cancel.setOnClickListener((v -> dismiss()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_selector_cidades);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
