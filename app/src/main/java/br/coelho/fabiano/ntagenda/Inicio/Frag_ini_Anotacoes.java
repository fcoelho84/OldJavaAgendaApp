package br.coelho.fabiano.ntagenda.Inicio;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityAnotacoes;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

public class Frag_ini_Anotacoes extends Fragment {

    @BindView(R.id.ini_lista_rapida) ListView lLista;

    private BD_Query bdQuery = new BD_Query();
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_modelo_lista, container, false);
        ButterKnife.bind(this, view);

        bdQuery.setContext(this.getActivity());
        comunicacao.setContext(this.getActivity());
        setItems();
        lLista.setDivider(null);

        return view;
    }

    @OnItemLongClick(R.id.ini_lista_rapida) boolean onLongClick(View v){
        comunicacao.openModeEditAnotacao(new Intent(Frag_ini_Anotacoes.this.getActivity(), ActivityAnotacoes.class), v);

        return true;
    }

    @Override
    public void onStart() {
        setItems();
        super.onStart();
    }

    private void setItems(){
        lLista.setAdapter(bdQuery.PegarAsAnotacoes(null, false));
    }

}
