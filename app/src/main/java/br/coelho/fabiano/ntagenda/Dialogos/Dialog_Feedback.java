package br.coelho.fabiano.ntagenda.Dialogos;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.MainActivity;
import br.coelho.fabiano.ntagenda.R;
import br.coelho.fabiano.ntagenda.Tags.Frag_tag_Container;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class Dialog_Feedback extends BaseDialog {

    @BindView(R.id.fbDesc) EditText fbDesc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_feedback, container, false);

        ButterKnife.bind(this, view);
        setTheme();

        return view;
    }

    @OnClick(R.id.fbSend) void onClick(){
            String[] para = {getString(R.string.email)};
            String assunto = "Feedback NT";
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, para);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, assunto);
            emailIntent.putExtra(Intent.EXTRA_TEXT, fbDesc.getText().toString()+"  "+Build.VERSION.RELEASE);

            try {

                startActivity(Intent.createChooser(emailIntent, getString(R.string.feedback_send)));

            } catch (Exception e){

                Log.e("Feedback", "error = "+ e.toString());

            }

    }




}
