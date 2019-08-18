package br.coelho.fabiano.ntagenda.Auxiliares;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;

public class Receiver_Boot extends BroadcastReceiver {

    private BD_Query bdQuery = new BD_Query();

    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();



    // adb shell am broadcast -a android.intent.action.REBOOT

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("BOOT","INICIALIZADO");

        comunicacao.setContext(context.getApplicationContext());
        bdQuery.setContext(context.getApplicationContext());

        try{

            ArrayList<String> myAlarms = bdQuery.PegarOsDadosDoAlarme();

            Log.e("BOOT","DATA  = "+myAlarms.get(0));

            for(int i = 0;i<myAlarms.size();i++){

                Log.e("BOOT","DATA  = "+myAlarms.get(i));

                String[] array = myAlarms.get(i).split(";");

                comunicacao.setAlarm(array[0],array[1],Long.parseLong(array[2]),array[3]);

            }


        }catch(Exception e){
            Log.e("BOOT","ERRO = "+e.toString());
        }

    }
};