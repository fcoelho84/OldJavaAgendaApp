package br.coelho.fabiano.ntagenda.Dialogos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Dialog_MostrarAnexo extends BaseDialog {

    public static Dialog_MostrarAnexo newInstance(String Desc){
        Dialog_MostrarAnexo frag = new Dialog_MostrarAnexo();
        Bundle args = new Bundle();
        args.putString("desc", Desc);
        frag.setArguments(args);
        return frag;
    }


    @BindView(R.id.dShowDesc) TextView desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_mostrar_anexo, container, false);
        setTheme();
        ButterKnife.bind(this, view);
        desc.setText(getArguments().getString("desc"));

        return view;
    }
}
