package br.coelho.fabiano.ntagenda.Dialogos;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.coelho.fabiano.ntagenda.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_EscolherCor extends BaseDialog {

    private int COLOR;
    private TextView textView;
    private LinearLayout linearLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_escolher_cor, container, false);
        ButterKnife.bind(this, view);


        setTheme();

        return view;
    }

    @OnClick(R.id.color00) void onClick00(){
        setBackgroundColor(getResources().getColor(R.color.color00));
        getDialog().dismiss();
    };

    @OnClick(R.id.color01) void onClick01(){
        setBackgroundColor(getResources().getColor(R.color.color01));
        getDialog().dismiss();
    };

    @OnClick(R.id.color02) void onClick02(){
        setBackgroundColor(getResources().getColor(R.color.color02));
        getDialog().dismiss();
    };

    @OnClick(R.id.color03) void onClick03(){
        setBackgroundColor(getResources().getColor(R.color.color03));
        getDialog().dismiss();
    };

    @OnClick(R.id.color04) void onClick04(){
        setBackgroundColor(getResources().getColor(R.color.color04));
        getDialog().dismiss();
    };

    @OnClick(R.id.color05) void onClick05(){
        setBackgroundColor(getResources().getColor(R.color.color05));
        getDialog().dismiss();
    };

    @OnClick(R.id.color07) void onClick07(){
        setBackgroundColor(getResources().getColor(R.color.color07));
        getDialog().dismiss();
    };
    @OnClick(R.id.color08) void onClick08(){
        setBackgroundColor(getResources().getColor(R.color.color08));
        getDialog().dismiss();
    };

    private void setBackgroundColor(int idColor){
        COLOR = idColor;
        if(linearLayout != null){
            linearLayout.setBackgroundColor(idColor);
            textView.setText("  ");
        }else{
            textView.setBackgroundColor(idColor);
        }
    }

    public void setView(TextView textView, LinearLayout linearLayout){
        this.textView = textView;
        this.linearLayout = linearLayout;
    }

    public  int getSelectedColor(){
        return  COLOR;
    }

}
