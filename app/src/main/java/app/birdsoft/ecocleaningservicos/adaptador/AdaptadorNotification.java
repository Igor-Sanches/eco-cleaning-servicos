package app.birdsoft.ecocleaningservicos.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.model.Notification;
import app.birdsoft.ecocleaningservicos.model.NotificationType;
import app.birdsoft.ecocleaningservicos.tools.DateTime;

public class AdaptadorNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notification> notifications;
    private Context context;
    public boolean on_atach = true;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onCliked(Notification notification);
    }

    public void inserir(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    public AdaptadorNotification(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, status, data;
        private ImageView icon;
        public FrameLayout layout;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            data = root.findViewById(R.id.data);
            titulo = root.findViewById(R.id.displayName);
            icon = root.findViewById(R.id.icon_notification);
            status = root.findViewById(R.id.status);
            layout = root.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                Notification notification = notifications.get(position);
                viewHolder.layout.setOnClickListener(view -> {
                    if(onClickListener != null){
                        onClickListener.onCliked(notification);
                    }
                });  viewHolder.titulo.setText(notification.getTitulo());
                viewHolder.data.setText(DateTime.toDateString("dd/MM/yyyy HH:mm", notification.getData()));
                viewHolder.layout.setBackgroundColor(notification.isLida() ? context.getResources().getColor(R.color.branco) : context.getResources().getColor(R.color.card_n));
                viewHolder.icon.setImageResource(getIcone(notification.getType()));
                if(notification.isRecebida()){
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.acriligo_tint_laranja));
                    viewHolder.status.setBackgroundResource(R.drawable.btn_acriligo_laranja);
                }else{
                    viewHolder.status.setTextColor(context.getResources().getColor(R.color.acriligo_tint_verde));
                    viewHolder.status.setBackgroundResource(R.drawable.btn_acriligo_verde);
                }
                viewHolder.status.setText(notification.isRecebida() ? R.string.recebida : R.string.enviada);


            }catch (Exception x){
                Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private @DrawableRes
    int getIcone(String type) {
        int res = 0;
        switch (NotificationType.valueOf(type)){
            case mensagem_pedido:
            case mensagem:
                res = R.drawable.ic_action_mensagem;
                break;
        }
        return res;
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
        return notifications.size();
    }

}




