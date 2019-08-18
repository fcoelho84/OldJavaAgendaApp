package br.coelho.fabiano.ntagenda.Auxiliares;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.coelho.fabiano.ntagenda.Dialogos.Dialog_DateTime;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_EscolherCor;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_Feedback;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_ListaDeTags;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_LembretesComNotificacao;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_Alarme;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_MostrarAnexo;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_Anexo;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_TagPref;
import br.coelho.fabiano.ntagenda.R;
import es.dmoral.toasty.Toasty;

public class Hub_Comunicacao {

    private FragmentManager fm;

    private Context context;

    private BD_Query bdQuery = new BD_Query();

    private Dialog_EscolherCor dialog = new Dialog_EscolherCor();

    public void setFM(FragmentManager fm){
        this.fm = fm;
    }

    public void setContext(Context context){
        this.context = context;
        bdQuery.setContext(context);
    }

    public AlertDialog.Builder showAlertDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_alert_title));
        builder.setMessage(context.getString(R.string.dialog_alert_message));
        builder.setNegativeButton(context.getString(R.string.dialog_alert_btn_negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return builder;
    }

    public void showFeedback(){
        Dialog_Feedback dialog = new Dialog_Feedback();
        dialog.show(fm, "DIALOG_FEEDBACK");
    }

    public void showDateTime(EditText editText){
        Dialog_DateTime dialog = new Dialog_DateTime();
        dialog.setEditText(editText);
        dialog.show(fm, "DIALOG_DATETIME");

    }

    public void showChooseColor(EditText editText, LinearLayout linearLayout){
        dialog.setView(editText, linearLayout);
        dialog.show(fm,"DIALOG_COLOR");
    }

    public int getColorId(){
        return dialog.getSelectedColor();
    }

    public void showTags(EditText editText){
        Dialog_ListaDeTags dialog =  new Dialog_ListaDeTags();
        dialog.setView(editText);
        dialog.show(fm, "DIALOG_TAGS");


    }

    public void showTagPref(){
        Dialog_TagPref dialog =  new Dialog_TagPref();
        dialog.show(fm, "DIALOG_TAGS");
    }

    public void showSetNote(EditText editText, TextView textView){
        Dialog_Anexo dialog = new Dialog_Anexo();
        dialog.setEditText(editText, textView);
        dialog.show(fm,"DIALOG_NOTE");
    }

    public void showAlarm(TextView alarmLong, TextView alarmFormat){
        Dialog_Alarme dialog = new Dialog_Alarme();
        dialog.setAlarm(alarmLong, alarmFormat);
        dialog.show(fm, "DIALOG_ALARM");
    }

    public void showDesc(int id){
        try{
            String desc = bdQuery.PegarAsAnotacoesPorAnexo(id).get(0);
            Dialog_MostrarAnexo dialog = Dialog_MostrarAnexo.newInstance(desc);
            dialog.show(fm, "DIALOG_DESC");
        }catch (Exception e){
            Toasty.info(context, context.getString(R.string.erroAnexoNulo), Toast.LENGTH_SHORT).show();
            Log.e("HUB","DESC = "+e);
        }

    }

    public void showNotification(){
        Dialog_LembretesComNotificacao dialog = new Dialog_LembretesComNotificacao();
        dialog.show(fm,"DIALOG_NOTIFICATION");
    }

    public void openModeEditLembrete(Intent i, View v){
        try{
            String ID = ((TextView) v.findViewById(R.id.ID_DESC)).getText().toString();
            i.putExtra("IDANEX", ID);
            i.putExtra("STRANEX", bdQuery.PegarAsAnotacoesPorAnexo(Integer.parseInt(ID)).get(1));
        }catch (Exception e){
            i.putExtra("IDANEX", "");
            i.putExtra("STRANEX", "");
        }

        String data = ((TextView) v.findViewById(R.id.mData_Id)).getText().toString();

        if(data.length() < 6){
            i.putExtra("DATA", ((TextView) v.findViewById(R.id.SAVE_DATE)).getText().toString());
        }else{
            i.putExtra("DATA", data);
        }

        i.putExtra("ID_ALARM", ((TextView) v.findViewById(R.id.ID_ALARME)).getText().toString());
        i.putExtra("ID", ((TextView) v.findViewById(R.id.ID_ID)).getText().toString());
        i.putExtra("IDCOLOR", ((TextView) v.findViewById(R.id.ID_COLOR)).getText().toString());
        i.putExtra("TITLE", ((TextView) v.findViewById(R.id.mTitle_Id)).getText().toString());
        i.putExtra("TAG", ((TextView) v.findViewById(R.id.mTag_Id)).getText().toString());
        i.putExtra("TIME_ALARM", ((TextView) v.findViewById(R.id.TIME_ALARM)).getText().toString());

        context.startActivity(i);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void openModeEditAnotacao(Intent i, View v){
        i.putExtra("ID", ((TextView) v.findViewById(R.id.Id)).getText().toString());
        i.putExtra("COLOR_ID", ((TextView) v.findViewById(R.id.Grau)).getText().toString());
        i.putExtra("NOTES", ((TextView) v.findViewById(R.id.Desc)).getText().toString());
        context.startActivity(i);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void setAlarm(String title, String desc, long time, String id) throws IllegalArgumentException{
        Intent alarmIntent = new Intent(context, Receiver_Notification.class);
        alarmIntent.putExtra("Title",title);
        alarmIntent.putExtra("Desc", desc);
        alarmIntent.putExtra("Id", id);

        int code = Integer.parseInt(Long.toString(time).substring(5));

        try{

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= 23) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= 19) {
                manager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }

        }catch (Exception e){
            Log.e("HUB","setAlarm = "+e);
        }
    }

    public void cancelAlarm(String title, String desc, long time, String id){
        Intent alarmIntent = new Intent(context, Receiver_Notification.class);
        alarmIntent.putExtra("Title",title);
        alarmIntent.putExtra("Desc", desc);
        alarmIntent.putExtra("Id", id);

        int code = Integer.parseInt(Long.toString(time).substring(5));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

        manager.cancel(pendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Integer.parseInt(id));
    }




}
