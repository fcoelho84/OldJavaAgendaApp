package br.coelho.fabiano.ntagenda.Dialogos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Dialog_ListaDeTags extends BaseDialog {

    @BindView(R.id.dListtag) ListView listView;

    private BD_Query bdQuery = new BD_Query();
    private EditText editText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.dialog_modelo_lista, container, false);
        ButterKnife.bind(this, view);
        setTheme();

        bdQuery.setContext(this.getActivity());

        listView.setDivider(null);
        listView.setAdapter(bdQuery.PegarTodasAsTagsDosLembretes());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(listView.getItemAtPosition(position).toString());
                getDialog().dismiss();
            }
        });



        return view;
    }

    public void setView(EditText editText){
        this.editText = editText;
    }

}
