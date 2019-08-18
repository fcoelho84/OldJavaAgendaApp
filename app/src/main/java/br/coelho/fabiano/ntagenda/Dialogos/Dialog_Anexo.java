package br.coelho.fabiano.ntagenda.Dialogos;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.InputDados.ActivityAnotacoes;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_Anexo extends BaseDialog {

    @BindView(R.id.dListtag) ListView listView;
    @OnClick(R.id.dAnex_Create) void onClick(){
        Intent i = new Intent(this.getActivity(), ActivityAnotacoes.class);
        startActivity(i);
        this.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private BD_Query bdQuery = new BD_Query();

    private EditText editText;
    private TextView editText2;
    private int ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_setar_anexo, container, false);

        ButterKnife.bind(this, view);

        setTheme();

        bdQuery.setContext(this.getActivity());

        setItems();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ID = Integer.parseInt(((TextView) view.findViewById(R.id.Id)).getText().toString());

                ArrayList<String> desc = bdQuery.PegarAsAnotacoesPorAnexo(ID);
                editText.setText(desc.get(1));
                editText2.setText(Integer.toString(ID));

                getDialog().dismiss();

            }
        });

        return view;
    }

    private void setItems(){
        listView.setAdapter(bdQuery.PegarListaDeResumosDeAnotacoes());
    }

    public void setEditText(EditText editText, TextView editText2){
        this.editText = editText;
        this.editText2 = editText2;
    }

    @Override
    public void onStart() {
        setItems();
        super.onStart();
    }
}
