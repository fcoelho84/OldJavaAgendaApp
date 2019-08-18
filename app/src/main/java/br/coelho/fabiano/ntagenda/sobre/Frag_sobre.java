package br.coelho.fabiano.ntagenda.sobre;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.InputDados.ActivityAnotacoes;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

public class Frag_sobre extends Fragment {

    @BindView(R.id.versionINT) TextView codeVersion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sobre, container, false);

        ButterKnife.bind(this, view);

        PackageInfo pInfo = null;

        try {
            pInfo = this.getActivity().getPackageManager().getPackageInfo(this.getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        codeVersion.setText(pInfo.versionName);



        return view;
    }


}
