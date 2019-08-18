package br.coelho.fabiano.ntagenda.Dialogos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.MainActivity;
import br.coelho.fabiano.ntagenda.R;
import br.coelho.fabiano.ntagenda.Tags.Frag_tag_Container;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class Dialog_TagPref extends BaseDialog {

    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private BD_Database database;
    private BD_Query bdQuery = new BD_Query();
    private Preferences_IO prefIO = new Preferences_IO();
    private String tag, ID_PREF_CALENDAR, ID_PREF_QUERY, tagPos;
    private int id;

    @BindView(R.id.dialog_tag_display) EditText tagText;

    @BindView(R.id.layout_tag) TextInputLayout layoutTag;

    @BindView(R.id.sPending) Switch sPQuery;

    @BindView(R.id.sCalendar) Switch vMode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_tagpref, container, false);

        ButterKnife.bind(this, view);

        String IdAndTag = prefIO.pegarPreferencias(this.getActivity(), Preferences_IO.SAVE_MYTAG);

        String[] aux = IdAndTag.split(";");

        id = Integer.parseInt(aux[0]);
        tag = aux[1];
        tagPos = aux[2];

        tagText.setText(tag);

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(this.getActivity().getFragmentManager());

        bdQuery.setContext(this.getActivity());

        database = new BD_Database(this.getActivity());

        setTheme();

        ID_PREF_QUERY = tagPos+Preferences_IO.SAVE_PREF_MYTAG_DB_QUERY_PENDING;
        ID_PREF_CALENDAR = tagPos+Preferences_IO.SAVE_PREF_MYTAG_CALENDAR_VIEW;

        boolean sQuery = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(), ID_PREF_QUERY)),
                sView = Boolean.parseBoolean(prefIO.pegarPreferencias(this.getActivity(), ID_PREF_CALENDAR));


        sPQuery.setChecked(sQuery);

        vMode.setChecked(sView);


        return view;
    }


    @OnClick(R.id.dialog_btn_listTag) void onClickTagIcon(){
        onClickTagDisplay();
    }

    @OnClick(R.id.dialog_tag_display) void onClickTagDisplay(){
        comunicacao.showTags(tagText);

    }

    @OnClick(R.id.tags_add) void onClickAdd(){

        String auxTag = tagText.getText().toString();


        if(auxTag.equals("")){
            layoutTag.setError(getString(R.string.erroCampoNulo));

        }else{

            database.UPDATE_TAGS(auxTag, id);

            reloadFragment();

        }

    }

    @OnClick(R.id.dialog_tagpref_delete) void onClickTrash(){

        database.DROP_TAGS(id);

        prefIO.salvarPreferencias(this.getActivity(),
                ID_PREF_CALENDAR,
                "false");

        prefIO.salvarPreferencias(this.getActivity(),
                ID_PREF_QUERY,
                "false");

        reloadFragment();

    }

    @OnCheckedChanged(R.id.sPending) void onCheckedChangedQuery(boolean checked){
        if(checked){
            prefIO.salvarPreferencias(this.getActivity(),
                    ID_PREF_QUERY,
                    "true");

        }else{
            prefIO.salvarPreferencias(this.getActivity(),
                    ID_PREF_QUERY,
                    "false");
        }
    }

    @OnCheckedChanged(R.id.sCalendar) void onCheckedChangedView(boolean checked){
        if(checked){
            prefIO.salvarPreferencias(this.getActivity(),
                    ID_PREF_CALENDAR,
                    "true");

        }else{
            prefIO.salvarPreferencias(this.getActivity(),
                    ID_PREF_CALENDAR,
                    "false");
        }
    }

    private void reloadFragment(){
        Intent i = new Intent(this.getActivity(), MainActivity.class);
        i.putExtra(MainActivity.RELOAD_FRAGMENT, Frag_tag_Container.TAG_FRAGMENT);
        startActivity(i);
    }


}
