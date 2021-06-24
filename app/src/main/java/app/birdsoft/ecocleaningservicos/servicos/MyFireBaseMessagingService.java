package app.birdsoft.ecocleaningservicos.servicos;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.birdsoft.ecocleaningservicos.MainActivity;
import app.birdsoft.ecocleaningservicos.model.NotificationType;
import app.birdsoft.ecocleaningservicos.settings.Settings;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private String titulo, cliente, mensagem, uidCliente, uidServico, type, responder, statusServico;  @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(Settings.isNotification(this)){
            titulo=remoteMessage.getData().get("titulo");
            mensagem=remoteMessage.getData().get("mensagem");
            uidCliente=remoteMessage.getData().get("uidCliente");
            uidServico=remoteMessage.getData().get("uidServico");
            responder=remoteMessage.getData().get("responder");
            type=remoteMessage.getData().get("type");
            cliente=remoteMessage.getData().get("cliente");
            statusServico = remoteMessage.getData().get("statusServico");
            if(MainActivity.getInstance() != null){
                MainActivity.getInstance().runOnUiThread(() -> NotificationSend.sendNotification(MainActivity.getInstance(), cliente, titulo, mensagem, uidServico, statusServico, type, uidCliente, responder, this));
            }
            else NotificationSend.sendNotification(null, cliente, titulo, mensagem, uidServico, statusServico, type, uidCliente, responder, this);

        }
    }

}
