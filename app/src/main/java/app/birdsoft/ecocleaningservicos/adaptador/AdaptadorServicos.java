package app.birdsoft.ecocleaningservicos.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.HelperManager;
import app.birdsoft.ecocleaningservicos.model.Servico;
import app.birdsoft.ecocleaningservicos.navigation.Navigate;
import app.birdsoft.ecocleaningservicos.tools.ImageUtils;
import app.birdsoft.ecocleaningservicos.tools.ItemAnimation;
import app.birdsoft.ecocleaningservicos.tools.Mask;
import app.birdsoft.ecocleaningservicos.view.SelecionadorServicosItensActivity;
import app.birdsoft.ecocleaningservicos.view.SelecionadorServicosUnicoActivity;

public class AdaptadorServicos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int animation_type = 0;
    public boolean on_attach = true;
    private int lastPosition = -1;
    private List<Servico> str;
    private Context context;
    public boolean loading;
    private OnFavoritoChanged onFavoritoChanged;
    private OnClickItemListener onClickItemListener;

    public void setOnFavoritoChanged(OnFavoritoChanged onFavoritoChanged) {
        this.onFavoritoChanged = onFavoritoChanged;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public void inserir(List<Servico> servicos) {
        this.str = servicos;
        notifyDataSetChanged();
    }

    public interface OnFavoritoChanged{
        void onChanged(CheckBox checkBox, Servico servico, int position);
    }

    public interface OnClickItemListener{
        void onClick(View v, Servico item, int position);
    }

    public AdaptadorServicos(List<Servico> str, Context context) {
        this.str =str;
        this.context =context;
    }

    public void clear() {
        if(str.size() > 0){
            str.clear();
            notifyDataSetChanged();
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.str.add(null);
            notifyItemInserted(getItemCount() - 1);
            this.loading = true;
        }
    }


    public List<Servico> getLista(){
        return str;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView nome_servico, servicos_opcionais, valor, somente_adicionais, tempo, descricao;
        public CardView layout;
        private CheckBox check_favorito;
        private LinearLayout com_adicionais;
        private ImageView image_servico;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            valor = root.findViewById(R.id.valor);
            check_favorito = root.findViewById(R.id.check_favorito);
            com_adicionais = root.findViewById(R.id.com_adicionais);
            somente_adicionais = root.findViewById(R.id.somente_adicionais);
            tempo = root.findViewById(R.id.tempo);
            servicos_opcionais = root.findViewById(R.id.servicos_opcionais);
            image_servico = root.findViewById(R.id.image_servico);
            nome_servico = root.findViewById(R.id.nome_servico);
            descricao = root.findViewById(R.id.descricao);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_servico, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                Servico servico = str.get(position);
                ImageUtils.displayImageFromUrl(context, servico.getImageUrl(), viewHolder.image_servico, context.getResources().getDrawable(R.drawable.image_add_ft));
                viewHolder.nome_servico.setText(servico.getName());
                if(servico.getDescricao() != null){
                    viewHolder.descricao.setText(servico.getDescricao());
                }else viewHolder.descricao.setVisibility(View.GONE);
                viewHolder.check_favorito.setChecked(servico.isFavorito());
                viewHolder.tempo.setText(String.format("Tempo: %s", HelperManager.getTempoServico(servico.getHora(), servico.getMinuto())));
                viewHolder.somente_adicionais.setVisibility(servico.getTipoServico() == 0 ? View.VISIBLE : View.GONE);
                viewHolder.com_adicionais.setVisibility(servico.getTipoServico() != 0 ? View.VISIBLE : View.GONE);
                viewHolder.valor.setText(Mask.formatarValor(servico.getValor()));
                viewHolder.servicos_opcionais.setVisibility(servico.getTipoServico() == 1 ? View.VISIBLE : View.GONE);
                viewHolder.check_favorito.setOnClickListener((clicked -> {
                    if(onFavoritoChanged != null)
                        onFavoritoChanged.onChanged(viewHolder.check_favorito, servico, position);
                }));
                viewHolder.layout.setOnClickListener((v -> {
                    Intent intent;
                    if(servico.getTipoServico() == 2){
                        intent = new Intent(context, SelecionadorServicosUnicoActivity.class);
                    }else{
                        intent = new Intent(context, SelecionadorServicosItensActivity.class);
                    }
                    intent.putExtra("servico", servico);
                    Navigate.context(context).navigate(intent);
                }));
                setAnimation(viewHolder.itemView, position);
            }catch (Exception x){
                new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
            }
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, i);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void setAnimation(View view, int i) {
        if (i > this.lastPosition) {
            ItemAnimation.animate(view, this.on_attach ? i : -1, this.animation_type);
            this.lastPosition = i;
        }
    }

    @Override
    public int getItemCount() {
        return str.size();
    }
}



