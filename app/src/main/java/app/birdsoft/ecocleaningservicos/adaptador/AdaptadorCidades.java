package app.birdsoft.ecocleaningservicos.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.birdsoft.ecocleaningservicos.R;

public class AdaptadorCidades extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> cidades;
    private Context context;
    private int animation = 2, lastPosition = -1;
    public boolean on_atach = true;
    private OnCidadeClickedListener onCidadeClickedListener;

    public void setOnCidadeClickedListener(OnCidadeClickedListener onCidadeClickedListener) {
        this.onCidadeClickedListener = onCidadeClickedListener;
    }

    public interface OnCidadeClickedListener {
        void onDelete(String cidade, int position);
    }

    public AdaptadorCidades(List<String> cidades, Context context) {
        this.cidades =cidades;
        this.context =context;
    }

    public void inserir(List<String> cidades) {
        this.cidades = cidades;
        notifyDataSetChanged();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView cidade;
        public View layout;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            cidade = root.findViewById(R.id.cidade);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cidade, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                String cidade = cidades.get(position);
                viewHolder.cidade.setText(cidade);
                viewHolder.layout.setOnClickListener((v -> {
                    if(onCidadeClickedListener != null)
                        onCidadeClickedListener.onDelete(cidade, position);
                }));
            }catch (Exception x){
                new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_atach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return cidades.size();
    }
}



