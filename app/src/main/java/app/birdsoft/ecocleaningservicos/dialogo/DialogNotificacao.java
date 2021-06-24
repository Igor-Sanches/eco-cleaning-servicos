package app.birdsoft.ecocleaningservicos.dialogo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.Objects;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.model.Notification;
import app.birdsoft.ecocleaningservicos.model.NotificationType;
import app.birdsoft.ecocleaningservicos.viewModel.NotificationsViewModel;

public class DialogNotificacao extends AlertDialog {

    private OnDeletarListener onDeletarListener;
    private Notification notification;
    private OnResponderAction onResponderAction;
    private OnViewServiceAction onViewServiceAction;
    private ViewModelStoreOwner viewModelStoreOwner;
    public DialogNotificacao(@NonNull Context context, Notification notification, ViewModelStoreOwner viewModelStoreOwner) {
        super(context);
        this.viewModelStoreOwner = viewModelStoreOwner;
        this.notification = notification;
     }

    public void setOnDeletarListener(OnDeletarListener onDeletarListener) {
        this.onDeletarListener = onDeletarListener;
    }

    public void setOnViewServiceAction(OnViewServiceAction onViewServiceAction) {
        this.onViewServiceAction = onViewServiceAction;
    }

    public void setOnResponderAction(OnResponderAction onResponderAction) {
        this.onResponderAction = onResponderAction;
    }

    public interface OnViewServiceAction{
        void onView(Notification notification);
    }

    public interface OnResponderAction{
        void onAction(Notification notification);
    }

    public interface OnDeletarListener{
        void onDeleted();
    }

    @Override
    public void show() {
        super.show();
        TextView titulo = findViewById(R.id.msg_title);
        TextView mensagem = findViewById(R.id.msg);
        assert titulo != null;
        assert mensagem != null;

        if(!notification.isLida()){
            notification.setLida(true);
            new NotificationDAO(getContext()).atualizar(notification);
            new ViewModelProvider(viewModelStoreOwner).get(NotificationsViewModel.class).update(getContext());
        }

        System.out.println(notification.getUidCliente() + "  " + notification.getUidServico());
        findViewById(R.id.responder).setOnClickListener((v) -> {
            dismiss();
            onResponderAction.onAction(notification);
        });
        findViewById(R.id.ver_pedido).setOnClickListener((v) -> {
            dismiss();
            onViewServiceAction.onView(notification);
        });
        findViewById(R.id.responder).setVisibility(notification.isResponder() ? View.VISIBLE : View.GONE);
        findViewById(R.id.layout_pedido).setVisibility(notification.getType().equals(NotificationType.mensagem_pedido.toString()) ? View.VISIBLE : View.GONE);
        titulo.setText(notification.getTitulo());
        mensagem.setText(notification.getMensagem());
        findViewById(R.id.deletar).setOnClickListener((v) -> {
            dismiss();
            DialogMessage dialogMessage = new DialogMessage(getContext(), getContext().getString(R.string.deletar_notificacao), true, getContext().getString(R.string.confirmar));
            dialogMessage.show();
            dialogMessage.setOnPossiveButtonClicked(() -> {
                if(onDeletarListener != null)
                    onDeletarListener.onDeleted();
            });
        });
        findViewById(R.id.cancel).setOnClickListener((v) -> dismiss());
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notification_view);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
