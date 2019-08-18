package br.coelho.fabiano.ntagenda.Dialogos;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class Dialog_LembretesComNotificacao extends BaseDialog {

    @BindView(R.id.dListtag) ListView listView;
    @BindView(R.id.DialogTitle) TextView title;

    private BD_Database bd = new BD_Database(this.getActivity());
    private BD_Query bdQuery = new BD_Query();
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_modelo_lista, container, false);
        ButterKnife.bind(this, view);
        setTheme();

        bdQuery.setContext(this.getActivity());
        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(getFragmentManager());

        title.setText(getString(R.string.dialog_alarme_title));

        ViewGroup.LayoutParams lp = listView.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        listView.setLayoutParams(lp);
        listView.setDivider(null);
        listView.setAdapter(bdQuery.PegarOsLembretesComNotificacao());

        remove();

        return view;
    }

    @OnItemLongClick(R.id.dListtag) boolean onItemLongClick(View view){
        comunicacao.openModeEditLembrete(new Intent(this.getActivity(), ActivityLembrete.class), view);
        return true;
    }

    @OnItemClick(R.id.dListtag) void onItemClick(View view){
        String id = ((TextView) view.findViewById(R.id.ID_DESC)).getText().toString();
        comunicacao.showDesc(Integer.parseInt(id));
    }



    private void remove(){
        bd = new BD_Database(this.getActivity());
        bd.UPDATE_TASK_BY_ALARM(BD_Database.NOTIFICACAO,"");
    }

}
