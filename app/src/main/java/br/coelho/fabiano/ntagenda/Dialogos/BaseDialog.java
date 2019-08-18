package br.coelho.fabiano.ntagenda.Dialogos;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import br.coelho.fabiano.ntagenda.R;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BaseDialog extends DialogFragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setTheme(){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AnimationFade;
    }

    @OnClick(R.id.dBtnClose) void onClickClose(){
        getDialog().dismiss();
    }

}
