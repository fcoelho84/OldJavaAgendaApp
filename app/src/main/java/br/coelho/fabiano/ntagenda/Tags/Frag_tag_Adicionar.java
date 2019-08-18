package br.coelho.fabiano.ntagenda.Tags;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import br.coelho.fabiano.ntagenda.AjudeNos.Frag_ajudar;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Database;
import br.coelho.fabiano.ntagenda.Auxiliares.BD_Query;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.MainActivity;
import br.coelho.fabiano.ntagenda.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class Frag_tag_Adicionar extends Fragment implements RewardedVideoAdListener {

    public static final String TAG_FRAGMENT = "Frag_tag_Adicionar";

    public static final int TAGS_CURRENT = 8, TAGS_MAX = 10;


    private RewardedVideoAd mAd;
    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();
    private BD_Database database;
    private BD_Query bdQuery = new BD_Query();
    private Preferences_IO prefIO = new Preferences_IO();

    private int lenTags = 0, tagSlot = 0;
    private boolean fullSlot;

    private Toast toast;


    @BindView(R.id.maxDisplay) TextView display;
    @BindView(R.id.tags_tag_display) EditText tagText;
    @BindView(R.id.layout_tag) TextInputLayout layoutTag;
    @BindView(R.id.tags_btn_reward) Button btnReward;
    @BindView(R.id.tags_desc_reward) TextView descReward;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_tag_adicionar, container, false);

        ButterKnife.bind(this, view);

        comunicacao.setContext(this.getActivity());
        comunicacao.setFM(this.getActivity().getFragmentManager());

        bdQuery.setContext(this.getActivity());

        database = new BD_Database(this.getActivity());

        lenTags = bdQuery.PegarMinhasTags().size();

        toast = Toasty.info(this.getActivity(), getString(R.string.adRewardError), Toast.LENGTH_SHORT);

        String aux = prefIO.pegarPreferencias(this.getActivity(), Preferences_IO.SAVE_PREF_MYTAG_TAB_CREATE);

        if(aux.length() > 0 ){
            tagSlot = Integer.parseInt(aux);
        }

        int currentSlot = TAGS_CURRENT + tagSlot;

        display.setText(Integer.toString(lenTags)+"/"+currentSlot);

        fullSlot = currentSlot >= TAGS_MAX;

        if(fullSlot){
            btnReward.setText(getString(R.string.tags_slot_btn_full));
            descReward.setText(getString(R.string.tags_slot__desc_full));
        }else{

            mAd = MobileAds.getRewardedVideoAdInstance(this.getActivity());
            mAd.setRewardedVideoAdListener(this);

            loadRewardedVideoAd();

        }

        return view;
    }

    @OnClick(R.id.tags_btn_listTag) void onClickTagIcon(){
        onClickTagDisplay();
    }

    @OnClick(R.id.tags_tag_display) void onClickTagDisplay(){
        comunicacao.showTags(tagText);

    }

    @OnClick(R.id.tags_add) void onClickAdd(){

        String auxTag = tagText.getText().toString();


        if(auxTag.equals("")){

            layoutTag.setError(getString(R.string.erroCampoNulo));

        }else if(lenTags < TAGS_CURRENT){

            database.INSERT_TAGS(auxTag);

            reloadFragment();

        }else{
            Toasty.info(this.getActivity(), getString(R.string.erroMaxTags), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.tags_btn_reset) void onClickReset(){

        comunicacao.showAlertDialog().setPositiveButton(
                getString(R.string.dialog_alert_btn_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.DELETE_TABLE(BD_Database.TABLE_TAGS);

                reloadFragment();
            }
        }).create().show();
    }

    @OnClick(R.id.tags_btn_reward) void onClickReward(){
        if(mAd.isLoaded()){
            mAd.show();
        }else{
            if(!toast.getView().isShown()){
                toast.show();
            }
        }

        if(fullSlot){
            Intent i = new Intent(this.getActivity(), MainActivity.class);
            i.putExtra(MainActivity.RELOAD_FRAGMENT, Frag_ajudar.TAG_FRAGMENT);
            startActivity(i);
        }
    }

    private void loadRewardedVideoAd() {
        // para testes : ca-app-pub-3940256099942544/5224354917
        mAd.loadAd(MainActivity.ADMOB_REWARDED, new AdRequest.Builder().build());
    }

    private void reloadFragment(){
        Intent i = new Intent(this.getActivity(), MainActivity.class);
        i.putExtra(MainActivity.RELOAD_FRAGMENT, Frag_ajudar.TAG_FRAGMENT);
        startActivity(i);
    }

    @Override
    public void onRewardedVideoAdLoaded() {}

    @Override
    public void onRewardedVideoAdOpened() {}

    @Override
    public void onRewardedVideoStarted() {}

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {}

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {}

    @Override
    public void onRewardedVideoCompleted() {
        Toasty.success(this.getActivity(),
                getString(R.string.rewardTagSlot), Toast.LENGTH_SHORT).show();

        int aux = tagSlot+1;

        prefIO.salvarPreferencias(
                this.getActivity(),
                Preferences_IO.SAVE_PREF_MYTAG_TAB_CREATE,
                Integer.toString(aux));

        reloadFragment();


    }
}
