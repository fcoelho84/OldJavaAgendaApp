package br.coelho.fabiano.ntagenda.InputDados;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class ActivityLembrete extends Activity{

    @BindView(R.id.lembrete_text_title) EditText lTitle;
    @BindView(R.id.lembrete_text_tag) EditText lTag;
    @BindView(R.id.lembrete_text_clock) EditText lClock;
    @BindView(R.id.lembrete_text_color) EditText lCor;
    @BindView(R.id.lembrete_text_anex) EditText lAnex;
    @BindView(R.id.lembrete_text_alarm) EditText lAlarm;

    @BindView(R.id.lembrete_layout_title) TextInputLayout lTitleLayout;
    @BindView(R.id.lembrete_layout_clock) TextInputLayout lClockLayout;

    @BindView(R.id.lembrete_btn_anex) ImageView lAnexTrash;
    @BindView(R.id.lembrete_btn_alarm)ImageView lAlarmIcon;
    @BindView(R.id.lembrete_btn_trash) ImageView trash;

    @BindView(R.id.lembrete_anex_id) TextView lAnex_Id;
    @BindView(R.id.lembrete_alarm_id) TextView ALARM;
    @BindView(R.id.lembrete_alarm_long) TextView ALARM_TIME;

    @BindView(R.id.container_color) LinearLayout container_color;

    @BindView(R.id.lembrete_btn_add_edit) Button setBtn;

    private Calendar c = Calendar.getInstance();

    private BD_Database BD = new BD_Database(ActivityLembrete.this);
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private BD_Query bdQuery = new BD_Query();

    private boolean
            auxA = true,
            INSERT_MODE = true,
            TRASH_ANEX,
            ALARM_DELETE;

    private int ID, ID_ANEX, ID_COLOR = Color.TRANSPARENT;

    private long TIME_ALARM;

    private String alarmDesc = "", pDesc, pClock, pTitle, pTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete);
        ButterKnife.bind(this);

        comunicacao.setFM(getFragmentManager());
        comunicacao.setContext(this);
        bdQuery.setContext(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            pTitle = bundle.getString("TITLE");
            lTitle.setText(pTitle);

            pTag = bundle.getString("TAG");
            lTag.setText(pTag);

            pClock = bundle.getString("DATA");
            lClock.setText(pClock);

            ID_COLOR = Integer.parseInt(bundle.getString("IDCOLOR"));
            lCor.setBackgroundColor(ID_COLOR);
            lAnex.setText(bundle.getString("STRANEX"));
            ALARM.setText(bundle.getString("ID_ALARM"));
            ID = Integer.parseInt(bundle.getString("ID"));

            if(bundle.getString("TIME_ALARM") != null){
                try{
                    TIME_ALARM = Long.parseLong(bundle.getString("TIME_ALARM"));
                    if(TIME_ALARM > 10){
                        Calendar c2 = Calendar.getInstance();
                        c2.setTimeInMillis(TIME_ALARM);
                        lAlarm.setText(c2.get(Calendar.DAY_OF_MONTH)
                                +"/"+c2.get(Calendar.MONTH)
                                +"/"+c2.get(Calendar.YEAR)
                                +" "+c2.get(Calendar.HOUR_OF_DAY)
                                +":"+c2.get(Calendar.MINUTE));
                    }
                }catch (Exception e){
                    Log.e("DATA ERROR = ",e.toString());
                }
            }

            INSERT_MODE = false;

            setBtn.setText(getString(R.string.lembrete_btn_edit));

            if(lAlarm.getText().toString().length() > 1){
                ALARM_DELETE = true;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_notification_remove);
                lAlarmIcon.setImageBitmap(bitmap);
            }

            if(pTag.length() > 0){
                pDesc = (pTag+". Hoje as "+pClock.substring(11));
            }else{
                pDesc = ("Hoje as "+pClock.substring(11));
            }

        }else{
            trash.setVisibility(View.GONE);
        }


        if(lAnex.getText().toString().length() > 0){
            TRASH_ANEX = true;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_anex_remove);
            lAnexTrash.setImageBitmap(bitmap);
        }
    }

    @OnClick(R.id.btn_return) void onClickReturn(){
        onBackPressed();
    }

    @OnClick(R.id.lembrete_btn_trash) void onClickDetelete(){

        comunicacao.showAlertDialog().setPositiveButton(
                getString(R.string.dialog_alert_btn_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BD.DROP_TASK(ID);
                        if(ALARM.getText().toString().length() > 0 ){
                            BD.DROP_ALARM(Integer.parseInt(ALARM.getText().toString()));
                        }
                        onBackPressed();
                    }
                }).create().show();
    }

    @OnClick(R.id.lembrete_btn_anex) void onClickAnexRemove(){
        if(TRASH_ANEX){
            lAnex.setText("");
        }else{
            onClickAnex();
        }
    }

    @OnClick(R.id.lembrete_btn_color) void onClickItemColor(){
        onClickColor();
    }

    @OnClick(R.id.lembrete_btn_listTag) void OnClickTag(){
        comunicacao.showTags(lTag);
    }

    @OnClick(R.id.lembrete_btn_alarm) void onClickAlarmIcon(){
        if(ALARM_DELETE && auxA){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_notification);
            lAlarmIcon.setImageBitmap(bitmap);
            lAlarm.setText("");
            auxA = false;
        }else{
            comunicacao.showAlarm(ALARM_TIME, lAlarm);
        }
    }

    @OnClick(R.id.lembrete_btn_clock) void onClickClock(){
        onClickItemClock();
    }

    @OnClick(R.id.lembrete_btn_add_edit) void OnclickAdd(){
        try{
            if(lTitle.getText().toString().equals("")){

                lTitleLayout.setError(getString(R.string.erroCampoNulo));

            }else if(lClock.getText().toString().equals("")){

                lClockLayout.setError(getString(R.string.erroCampoNulo));

            }else{

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                c.setTime(format.parse(lClock.getText().toString()));

                if(lAnex_Id.getText().toString().length() > 0){
                    ID_ANEX = Integer.parseInt(lAnex_Id.getText().toString());
                }else if(lAnex.getText().toString().length() > 0){
                    ID_ANEX = Integer.parseInt(getIntent().getExtras().getString("IDANEX"));
                }


                if(INSERT_MODE){

                    alarmManager();

                    BD.INSERT_TASK(
                            lTitle.getText().toString(),
                            c.getTimeInMillis(),
                            ALARM.getText().toString(),
                            TIME_ALARM,
                            lTag.getText().toString(),
                            comunicacao.getColorId(),
                            ID_ANEX,
                            lClock.getText().toString());

                }else{

                    if(comunicacao.getColorId() != Color.TRANSPARENT){
                        ID_COLOR = comunicacao.getColorId();
                    }

                    if(!auxA){
                        String id = ALARM.getText().toString();

                        if(id.equals(BD_Database.NOTIFICACAO)){
                            BD.UPDATE_TASK_BY_ALARM(BD_Database.NOTIFICACAO,"");

                        }else if(id.length() > 0){
                            BD.DROP_ALARM(Integer.parseInt(id));
                            comunicacao.cancelAlarm(pTitle, pDesc, TIME_ALARM, id);
                        }
                        BD.UPDATE_TASK_BY_ALARM(ALARM.getText().toString(),"");
                    }

                    alarmManager();

                    if(lAlarm.getText().toString().length() < 1){
                        TIME_ALARM = 0;
                    }


                    BD.UPDATE_TASK(
                            lTitle.getText().toString(),
                            c.getTimeInMillis(),
                            ALARM.getText().toString(),
                            TIME_ALARM,
                            lTag.getText().toString(),
                            ID_COLOR,
                            ID_ANEX,
                            lClock.getText().toString(),
                            ID);
                }

                onBackPressed();
                finish();
            }


        }catch(Exception e){
            Log.e("ActivityLembrete", "BTN ERRO = "+e);
        }
    }

    @OnClick(R.id.lembrete_text_color) void onClickColor(){
        comunicacao.showChooseColor(lCor, container_color);

    }

    @OnClick(R.id.lembrete_text_clock) void onClickItemClock(){
        comunicacao.showDateTime(lClock);
    }

    @OnClick(R.id.lembrete_text_anex) void onClickAnex(){
        comunicacao.showSetNote(lAnex,lAnex_Id);
    }

    @OnClick(R.id.lembrete_text_alarm) void onClickAlarm(){
        onClickAlarmIcon();
    }

    @OnFocusChange(R.id.lembrete_text_alarm) void onfocusAlarm(boolean focused){
        if(focused){
            onClickAlarmIcon();
        }
    }

    @OnFocusChange(R.id.lembrete_text_anex) void onFocusAnex(boolean focused){
        if(focused){
            onClickAnex();
        }
    }

    @OnFocusChange(R.id.lembrete_text_clock) void onFocusClock(boolean focused){
        if(focused){
            onClickClock();
        }
    }

    @OnFocusChange(R.id.lembrete_text_color) void onFocusColor(boolean focused){
        if(focused){
            onClickColor();
        }
    }


    private void alarmManager(){
        if(ALARM_TIME.getText().toString().length() > 0){

            BD.INSERT_ALARM(
                    lTitle.getText().toString(),
                    Long.parseLong(ALARM_TIME.getText().toString()),
                    alarmDesc);

            TIME_ALARM = Long.parseLong(ALARM_TIME.getText().toString());

            try{
                ALARM.setText(bdQuery.PegarIdDoAlarmePorData(Long.parseLong(ALARM_TIME.getText().toString())));
            }catch (Exception e){
                Log.e("ALARM","ERRO = "+e);
            }

            if(lTag.getText().toString().length() > 0){
                alarmDesc = (lTag.getText().toString()+". Hoje as "+lClock.getText().toString().substring(11));
            }else{
                alarmDesc = ("Hoje as "+lClock.getText().toString().substring(11));
            }

            comunicacao.setAlarm(lTitle.getText().toString(), alarmDesc,
                    Long.parseLong(ALARM_TIME.getText().toString()), ALARM.getText().toString());

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        BD.close();
        super.onDestroy();
    }
}
