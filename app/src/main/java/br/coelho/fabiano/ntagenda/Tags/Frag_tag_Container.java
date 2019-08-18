package br.coelho.fabiano.ntagenda.Tags;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.util.ArrayList;
import java.util.Calendar;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Calendar_FormatView;
import br.coelho.fabiano.ntagenda.Auxiliares.Pager_Adapter;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.MainActivity;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class Frag_tag_Container extends Fragment {


    public static final String TAG_FRAGMENT = "Frag_tag_Container";

    private static final String BASE_PAGER = "Minhas Tags";

    private BD_Query bdQuery = new BD_Query();

    private ArrayList<String> tags;

    private Preferences_IO prefIO = new Preferences_IO();

    private Calendar_FormatView CalendarFormat = new Calendar_FormatView();


    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.result_tabs) TabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_modelo_tabs, container, false);

        ButterKnife.bind(this, view);

        bdQuery.setContext(this.getActivity());
        CalendarFormat.setContext(this.getActivity());

        tags = bdQuery.PegarMinhasTags();

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

        if(tabs.getTabCount() > 1){
            saveTag(tags.get(0)+";0");
        }else{
            saveTag("");

        }



        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String TAB_NAME = tab.getText().toString();

                if(TAB_NAME != BASE_PAGER){
                    saveTag(tags.get(tab.getPosition())+";"+tab.getPosition());
                }else{
                    saveTag("");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Pager_Adapter adapter = new Pager_Adapter(getChildFragmentManager());

        if(tags.size() > 0){
            for(int i=0; i<tags.size(); i++){

                String ID_PREF_QUERY = i+Preferences_IO.SAVE_PREF_MYTAG_DB_QUERY_PENDING,
                       ID_PREF_CALENDAR = i+Preferences_IO.SAVE_PREF_MYTAG_CALENDAR_VIEW;


                String[] auxTags = tags.get(i).split(";");

                Boolean sQuery = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(), ID_PREF_QUERY)),
                        sView = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(), ID_PREF_CALENDAR));

                if(sView){
                    adapter.addFragment(createFragmentCalendar(auxTags[1], sQuery), auxTags[1]);
                }else{
                    adapter.addFragment(createFragmentList(auxTags[1], sQuery), auxTags[1]);

                }

            }
        }

        adapter.addFragment(new Frag_tag_Adicionar(), BASE_PAGER);

        viewPager.setAdapter(adapter);
    }

    private Fragment createFragmentList(String tag, Boolean sQuery){
        Frag_tag_Baselist baseList = new Frag_tag_Baselist();

        View viewList = getLayoutInflater().inflate(R.layout.layout_modelo_lista, null);

        ListView myList = viewList.findViewById(R.id.ini_lista_rapida);

        myList.setAdapter(bdQuery.PegarOsLembretesPorTag(tag, sQuery));

        myList.setDivider(null);

        baseList.setMyTagView(viewList);

        return baseList;
    }

    private Fragment createFragmentCalendar(String tag, Boolean sQuery){

        Frag_tag_Basecalendar baseCalendar = new Frag_tag_Basecalendar();

        View view = getLayoutInflater().inflate(R.layout.frag_ini_lembrete_calendario, null);

        ListView myList = view.findViewById(R.id.lContainer);
        CompactCalendarView myCalendar = view.findViewById(R.id.lCalendar);

        CalendarFormat.setListView(myList);

        CalendarFormat.setListItems(bdQuery.PegarAsDatasDosLembretesPorTag(tag, sQuery, false));
        CalendarFormat.setCalendarEvent(bdQuery.PegarAsDatasDosLembretesPorTag(tag, sQuery, true), myCalendar);


        myList.setDivider(null);
        myCalendar.setUseThreeLetterAbbreviation(true);

        baseCalendar.setMyTagView(view);

        return baseCalendar;
    }


    private void saveTag(String info){
        prefIO.salvarPreferencias(
                Frag_tag_Container.this.getActivity(),
                Preferences_IO.SAVE_MYTAG,
                info);
    }


}
