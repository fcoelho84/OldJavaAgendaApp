package br.coelho.fabiano.ntagenda.Config;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class Frag_Config extends Fragment {


    public static final String TAG_FRAGMENT = "Frag_Config";

    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private Preferences_IO prefIO = new Preferences_IO();
    private  BD_Database database;
    private BD_Query query = new BD_Query();
    private Toast toast;

    @BindView(R.id.config_sLembrete) Switch List;
    @BindView(R.id.config_sCalendar) Switch Calendar;
    @BindView(R.id.config_sNote) Switch Note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_config, container, false);

        ButterKnife.bind(this, view);

        toast = Toasty.info(this.getActivity(), getString(R.string.reloadApp), Toast.LENGTH_LONG);

        database = new BD_Database(this.getActivity());

        query.setContext(this.getActivity());

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(getActivity().getFragmentManager());

        boolean list = Boolean.parseBoolean(
                prefIO.pegarPreferencias(
                        this.getActivity(),
                        Preferences_IO.SAVE_CONGIF_LIST)),
                calendar = Boolean.parseBoolean(
                        prefIO.pegarPreferencias(
                                this.getActivity(),
                                Preferences_IO.SAVE_CONGIF_CALENDAR)),
                note = Boolean.parseBoolean(
                        prefIO.pegarPreferencias(
                                this.getActivity(),
                                Preferences_IO.SAVE_CONGIF_NOTE));

        List.setChecked(list);
        Calendar.setChecked(calendar);
        Note.setChecked(note);

        return view;
    }

    @OnClick(R.id.config_resetDB) void onClickreset(){

        comunicacao.showAlertDialog().setPositiveButton(
                getString(R.string.dialog_alert_btn_positive),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Frag_Config.this.getActivity().deleteDatabase(BD_Database.DATABASE_NAME);
                    }
                }).create().show();
    }

    @OnCheckedChanged(R.id.config_sLembrete) void onChangedList(boolean checked){
        prefIO.salvarPreferencias(
                this.getActivity(),
                Preferences_IO.SAVE_CONGIF_LIST,
                Boolean.toString(checked));

        showToast(checked);

    }

    @OnCheckedChanged(R.id.config_sCalendar) void onChangedCalendar(boolean checked){
        prefIO.salvarPreferencias(
                this.getActivity(),
                Preferences_IO.SAVE_CONGIF_CALENDAR,
                Boolean.toString(checked));

        showToast(checked);

    }

    @OnCheckedChanged(R.id.config_sNote) void onChangedNote(boolean checked){
        prefIO.salvarPreferencias(
                this.getActivity(),
                Preferences_IO.SAVE_CONGIF_NOTE,
                Boolean.toString(checked));

        showToast(checked);

    }

    @OnCheckedChanged(R.id.config_sDataLembrete) void onChangedDataOld(boolean checked){
        if(checked){
            comunicacao.showAlertDialog().setPositiveButton(
                    getString(R.string.dialog_alert_btn_positive),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> DataAndId = query.PegarOsIdsDasDatasDosLembretesAntigos();

                            for(int i=0; i < DataAndId.size() ; i++){

                                String[] aux = DataAndId.get(i).split(";");

                                database.DROP_TASK(Integer.parseInt(aux[0]));

                                if(aux[1] != null){
                                    String intAux = query.PegarIdDoAlarmePorData(Long.parseLong(aux[1]));
                                    database.DROP_ALARM(Integer.parseInt(intAux));
                                }

                            }
                        }
                    }).create().show();

        }
    }

    @OnCheckedChanged(R.id.config_sDataNote) void onChangedDataNote(boolean checked){
        if(checked){

            comunicacao.showAlertDialog().setPositiveButton(
                    getString(R.string.dialog_alert_btn_positive),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<Integer> Ids = query.PegarOsIdsDasAnotacoes();

                            for(int i=0; i < Ids.size() ; i++){

                                database.DROP_NOTE(Ids.get(i));
                            }
                        }
                    }).create().show();

        }
    }

    private void showToast(boolean checked){
        if(!toast.getView().isShown()){
            if(checked){
                toast.show();
            }
        }
    }


}
