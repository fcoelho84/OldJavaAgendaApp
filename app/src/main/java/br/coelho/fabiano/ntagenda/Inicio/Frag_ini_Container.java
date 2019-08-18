package br.coelho.fabiano.ntagenda.Inicio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.coelho.fabiano.ntagenda.Auxiliares.Pager_Adapter;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Frag_ini_Container extends Fragment {

    private Preferences_IO prefIO = new Preferences_IO();
    boolean list, calendar, note;


    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.result_tabs) TabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_modelo_tabs, container, false);
        ButterKnife.bind(this, view);

        list = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(),
                        Preferences_IO.SAVE_CONGIF_LIST));

        calendar = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(),
                                Preferences_IO.SAVE_CONGIF_CALENDAR));

        note = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(),
                                Preferences_IO.SAVE_CONGIF_NOTE));

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Pager_Adapter adapter = new Pager_Adapter(getChildFragmentManager());
        if(!list){
            adapter.addFragment(new Frag_ini_Lembretes(), getString(R.string.ini_tab_lembrete));
        }
        if(!note){
            adapter.addFragment(new Frag_ini_Anotacoes(), getString(R.string.ini_tab_anotacao));
        }
        if(!calendar){
            adapter.addFragment(new Frag_ini_LembretesCalendar(), getString(R.string.ini_tab_calendario));
        }
        viewPager.setAdapter(adapter);
    }

}
