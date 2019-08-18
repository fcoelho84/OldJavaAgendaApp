package br.coelho.fabiano.ntagenda.InputDados;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.Dialogos.Dialog_EscolherCor;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityAnotacoes extends Activity {

    @BindView(R.id.note_desc) EditText desc;
    @BindView(R.id.note_trash) ImageView trash;


    private Dialog_EscolherCor dialogColor = new Dialog_EscolherCor();
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private BD_Database bd = new BD_Database(this);
    private int ID, ID_COLOR;
    private boolean INSERT_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacoes);
        ButterKnife.bind(this);

        comunicacao.setContext(this);
        comunicacao.setFM(getFragmentManager());


        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            desc.setText(bundle.getString("NOTES"));

            ID_COLOR = Integer.parseInt(getIntent().getExtras().getString("COLOR_ID"));
            desc.setBackgroundColor(ID_COLOR);

            ID = Integer.parseInt(getIntent().getExtras().getString("ID"));

            INSERT_MODE = false;
        }else{

            trash.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.note_done) void onClickSaveNote(){
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEE. MMM. yyyy HH:mm");

        if(dialogColor.getSelectedColor() > 0){
            ID_COLOR = dialogColor.getSelectedColor();
        }


        if(INSERT_MODE){

            if(dialogColor.getSelectedColor() == 0){
                ID_COLOR = getResources().getColor(R.color.color00);
            }

            bd.INSERT_NOTE(
                    format.format(currentDate),
                    dialogColor.getSelectedColor(),
                    desc.getText().toString());

        }else{

            bd.UPDATE_NOTE(
                    format.format(currentDate),
                    ID_COLOR,
                    desc.getText().toString(),
                    ID);
        }

        onBackPressed();

    }

    @OnClick(R.id.note_btn_insertColor) void onClickSetColor(){
            dialogColor.setView(desc, null);
            dialogColor.show(getFragmentManager(),"DialogChooseColor");

    }

    @OnClick(R.id.note_btn_back) void onClickBack(){
        onBackPressed();
    }

    @OnClick(R.id.note_trash) void onClickTrash(){

        comunicacao.showAlertDialog().setPositiveButton(
                getString(R.string.dialog_alert_btn_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bd.REMOVE_NOTE_ID_LEMBRETES(ID);
                        bd.DROP_NOTE(ID);
                        onBackPressed();

                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
