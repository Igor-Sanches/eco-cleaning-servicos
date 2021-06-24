package app.birdsoft.ecocleaningservicos.servicos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.UUID;

import app.birdsoft.ecocleaningservicos.R;
import app.birdsoft.ecocleaningservicos.login.SplashActivity;
import app.birdsoft.ecocleaningservicos.manager.Conexao;
import app.birdsoft.ecocleaningservicos.manager.NotificationDAO;
import app.birdsoft.ecocleaningservicos.model.NotificationType;
import app.birdsoft.ecocleaningservicos.tools.DateTime;
import app.birdsoft.ecocleaningservicos.view.NotificacoesActivity;
import app.birdsoft.ecocleaningservicos.viewModel.NotificationsViewModel;

public class NotificationSend {

    public static void sendNotification(ViewModelStoreOwner owner, String cliente, String title, String message, String uid_pedido, String state_pedido, String notificationType, String uid_cliente, String responder, Context context) {
        String nomeChannel = "";
        int n_channel = 0;
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "notif_0009");
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setVibrate(new long[]{3000});
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(NotificationType.valueOf(notificationType) == NotificationType.mensagem_pedido){
            app.birdsoft.ecocleaningservicos.model.Notification notification = new app.birdsoft.ecocleaningservicos.model.Notification();
            notification.setData(DateTime.getTime());
            builder.setSmallIcon(R.drawable.ic_action_mensagem);
            notification.setLida(false);
            notification.setMensagem(message);
            notification.setRecebida(true);
            notification.setResponder(responder.equals("true"));
            notification.setTitulo(title);
            notification.setType(notificationType);
            notification.setUidServico(uid_pedido);
            notification.setUidCliente(uid_cliente);
            notification.setCliente(cliente);
            notification.setUid(UUID.randomUUID().toString());
            new NotificationDAO(context).adicionar(notification);
            intent.putExtra("data", new NotificationDAO(context).getNotification(notification));
            intent = new Intent(context, NotificacoesActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            n_channel = 0;
            nomeChannel = context.getString(R.string.mesnagem_pedidos);

        }else if(NotificationType.valueOf(notificationType) == NotificationType.mensagem){
            app.birdsoft.ecocleaningservicos.model.Notification notification = new app.birdsoft.ecocleaningservicos.model.Notification();
            notification.setData(DateTime.getTime());
            builder.setSmallIcon(R.drawable.ic_action_mensagem);
            notification.setLida(false);
            notification.setMensagem(message);
            notification.setRecebida(true);
            notification.setTitulo(title);
            notification.setType(notificationType);
            notification.setUidServico(uid_pedido);
            notification.setUidCliente(uid_cliente);
            notification.setCliente(cliente);
            notification.setResponder(responder.equals("true"));
            notification.setUid(UUID.randomUUID().toString());
            new NotificationDAO(context).adicionar(notification);
            intent.putExtra("data", new NotificationDAO(context).getNotification(notification));
            intent = new Intent(context, NotificacoesActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            n_channel = 0;
            nomeChannel = context.getString(R.string.mesnagem);

        }

        if(owner != null)
            new ViewModelProvider(owner).get(NotificationsViewModel.class).update(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channel_id = "Channel_Delivery_Admin";
            NotificationChannel channel = new NotificationChannel(channel_id, nomeChannel, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }

        manager.notify(n_channel, builder.build());
    }

     /*
    public static void sendAlertaPrazo(String titulo, String texto, Context context){
        try{
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "notif_0010");
            Intent intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : AcompanhamentoPedidoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.ic_action_timer);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentText(texto);
            builder.setAutoCancel(true);
            builder.setContentTitle(titulo);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(new long[]{3000});
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channel_id = "Channel_Delivey_Prazo";
                NotificationChannel channel = new NotificationChannel(channel_id, "Prazo da entrega", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channel_id);
            }

            //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            manager.notify(1, builder.build());

        }catch (Exception x){
            new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
        }
    }

    public static void sendAlterarPedido(String titulo, String texto, Context context){
        try{
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "notif_0011");
            Intent intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : AcompanhamentoPedidoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.ic_action_menu);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentText(texto);
            builder.setAutoCancel(true);
            builder.setContentTitle(titulo);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(new long[]{3000});
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channel_id = "Channel_Delivey_Alterar";
                NotificationChannel channel = new NotificationChannel(channel_id, "Alterar pedido", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channel_id);
            }

            //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            manager.notify(2, builder.build());

        }catch (Exception x){
            new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
        }
    }

    public static void onCupom(String titulo, String texto, Context context){
        try{
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, "notif_0011");
            Intent intent = new Intent(context, Conexao.getFirebaseAuth().getCurrentUser() == null ? SplashActivity.class : CuponsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setSmallIcon(R.drawable.cupom);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(pendingIntent);
            builder.setFullScreenIntent(pendingIntent, true);
            builder.setContentText(texto);
            builder.setAutoCancel(true);
            builder.setContentTitle(titulo);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setVibrate(new long[]{3000});
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(texto));

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String channel_id = "Channel_Delivey_Cupom";
                NotificationChannel channel = new NotificationChannel(channel_id, "Cupons", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
                builder.setChannelId(channel_id);
            }

            //int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            manager.notify(4, builder.build());

        }catch (Exception x){
            new AlertDialog.Builder(context).setMessage(x.getMessage()).show();
        }

    }
    */
}
