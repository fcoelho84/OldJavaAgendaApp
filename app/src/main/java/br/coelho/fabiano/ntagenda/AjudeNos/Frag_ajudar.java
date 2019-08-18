package br.coelho.fabiano.ntagenda.AjudeNos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class Frag_ajudar extends Fragment {

    private Toast toast;

    public static final String TAG_FRAGMENT = "Frag_Ajudar";

    @BindView(R.id.help_btn) Button btnReward;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ajudar, container, false);

        ButterKnife.bind(this, view);

        toast = Toasty.success(this.getActivity(),
                "Recompensas Adquiridas! Obrigado por ajudar :)",
                Toast.LENGTH_SHORT);

        btnReward.setVisibility(View.GONE);

        return view;
    }


    @OnClick(R.id.help_btn) void onClickBtn(){

        if(!toast.getView().isShown()){
            toast.show();
        }
    }
}
