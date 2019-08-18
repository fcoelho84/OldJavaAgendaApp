package br.coelho.fabiano.ntagenda.Auxiliares;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import br.coelho.fabiano.ntagenda.R;

import br.coelho.fabiano.ntagenda.MainActivity;

public class Receiver_Notification extends BroadcastReceiver {


    public static final String PEDING_NOTIFICATION = "PEDING_NOTIFICATION";

    private BD_Database database;

    private String idChannel   = "canal_notificação_nt",
                   name        = "Notificações do NT Agenda",
                   description = "Canal usado para os serviços de notifcações do NT Agenda.";

    @Override
    public void onReceive(Context context, Intent i) {
        try{
            showNotification(context, i);

            String id = i.getExtras().getString("Id");

            database = new BD_Database(context);

            database.DROP_ALARM(Integer.parseInt(id));

            database.UPDATE_TASK_BY_ALARM(id,BD_Database.NOTIFICACAO);

            context.getApplicationContext().registerReceiver(new Receiver_Notification(), new IntentFilter("ALARM_RECEIVER"));

            Log.e("NOTIFICATION","myID = "+id);

        }catch(Exception e){
            Log.e("NOTIFICATION","ERRO = "+e);
        }

    }

    private void showNotification(Context context, Intent i) {
        int id = Integer.parseInt(i.getExtras().getString("Id"));
        Intent onClickOnNoticiation = new Intent(context, MainActivity.class);

        onClickOnNoticiation.putExtra(PEDING_NOTIFICATION,i.getExtras().getString("Id"));

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                onClickOnNoticiation, 0);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(idChannel, name, importance);

            mChannel.setDescription(description);

            mNotificationManager.createNotificationChannel(mChannel);

            Notification notification =
                    new NotificationCompat.Builder(context, idChannel)
                    .setSmallIcon(R.drawable.icon_notification_active)
                    .setContentTitle(i.getExtras().getString("Title"))
                    .setContentText(i.getExtras().getString("Desc"))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setChannelId(idChannel)
                    .build();

            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(id , notification);

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, soundUri);
            ringtone.play();



        }else{

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.icon_notification_active)
                            .setContentTitle(i.getExtras().getString("Title"))
                            .setContentText(i.getExtras().getString("Desc"))
                            .setContentIntent(contentIntent)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setAutoCancel(true);

            notificationManager.notify(id, mBuilder.build());

        }
    }

}
