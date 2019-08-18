package br.coelho.fabiano.ntagenda.Inicio;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class Frag_ini_Lembretes extends Fragment {

    @BindView(R.id.ini_lista_rapida) ListView lLista;
    @BindView(R.id.ini_total_int) TextView total;

    private BD_Query bdQuery = new BD_Query();
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ini_lembretes, container, false);
        ButterKnife.bind(this, view);

        bdQuery.setContext(this.getActivity());

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM((getActivity().getFragmentManager()));

        setItems();

        lLista.setDivider(null);


        return view;
    }

    @OnItemLongClick(R.id.ini_lista_rapida) boolean onItemLongClick(View view){
        comunicacao.openModeEditLembrete(new Intent(this.getActivity(), ActivityLembrete.class), view);
        return true;
    }

    @OnItemClick(R.id.ini_lista_rapida) void onItemClick(View view){
        String id = ((TextView) view.findViewById(R.id.ID_DESC)).getText().toString();
        comunicacao.showDesc(Integer.parseInt(id));
    }

    @Override
    public void onStart() {
        setItems();
        super.onStart();
    }

    private void setItems(){
        lLista.setAdapter(bdQuery.PegarOsLembretesPendentes());
        String size = Integer.toString(lLista.getCount());
        total.setText(size);
    }

}
