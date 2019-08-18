package br.coelho.fabiano.ntagenda;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import br.coelho.fabiano.ntagenda.Auxiliares.Aux_SetHeightList;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;


public class SearchData extends Fragment {

    @BindView(R.id.search_list_lembrete) ListView lListaLembrete;

    @BindView(R.id.search_list_anotacao) ListView lListaAnotacao;

    private BD_Query query = new BD_Query();
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private Aux_SetHeightList heightList = new Aux_SetHeightList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_modelo_search_data, container, false);
        ButterKnife.bind(this, view);

        query.setContext(this.getActivity());
        comunicacao.setContext(this.getActivity());
        comunicacao.setFM((getActivity().getFragmentManager()));

        lListaLembrete.setDivider(null);
        lListaAnotacao.setDivider(null);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            try{
                String search = bundle.getString("SEARCH");

                if(search.length() > 0){
                    lListaAnotacao.setAdapter(query.PegarAsAnotacoes(search, true));
                    lListaLembrete.setAdapter(query.PegarOsLembretes(search, true));
                }else{
                    lListaAnotacao.setAdapter(query.PegarAsAnotacoes("", false));
                    lListaLembrete.setAdapter(query.PegarOsLembretes("", false));
                }

                heightList.setListViewHeightBasedOnChildren(lListaAnotacao);
                heightList.setListViewHeightBasedOnChildren(lListaLembrete);

            }catch (Exception e){
                Log.e("SEARCH_DATA","ERRO = "+e);
            }
        }


        return view;
    }


    @OnItemClick(R.id.search_list_lembrete) void onItemClickLembrete(View view){
        String id = ((TextView) view.findViewById(R.id.ID_DESC)).getText().toString();
        comunicacao.showDesc(Integer.parseInt(id));
    }

    @OnItemLongClick(R.id.search_list_lembrete) boolean onItemLongClick(View view){
        comunicacao.openModeEditLembrete(new Intent(this.getActivity(), ActivityLembrete.class), view);
        return true;
    }

    @OnItemClick(R.id.search_list_anotacao) void onItemClickAnotacao(View view){
        String id = ((TextView) view.findViewById(R.id.ID_DESC)).getText().toString();
        comunicacao.showDesc(Integer.parseInt(id));
    }

}
