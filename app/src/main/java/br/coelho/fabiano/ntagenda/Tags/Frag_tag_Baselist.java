package br.coelho.fabiano.ntagenda.Tags;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import br.coelho.fabiano.ntagenda.R;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class Frag_tag_Baselist extends Fragment {


    private View view;

    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ButterKnife.bind(this, getMyTagView());

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(this.getActivity().getFragmentManager());

        return getMyTagView();
    }

    private View getMyTagView(){

        return view;
    }

    public void setMyTagView(View view){
        this.view = view;

    }

    @OnItemLongClick(R.id.ini_lista_rapida) boolean onItemLongClick(View view){
        comunicacao.openModeEditLembrete(new Intent(this.getActivity(), ActivityLembrete.class), view);

        return true;
    }

    @OnItemClick(R.id.ini_lista_rapida) void onItemClick(View view){
        String id = ((TextView) view.findViewById(R.id.ID_DESC)).getText().toString();
        comunicacao.showDesc(Integer.parseInt(id));

    }

}
