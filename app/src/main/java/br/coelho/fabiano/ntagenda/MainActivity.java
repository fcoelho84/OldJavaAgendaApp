package br.coelho.fabiano.ntagenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.util.Locale;
import br.coelho.fabiano.ntagenda.AjudeNos.Frag_ajudar;
import br.coelho.fabiano.ntagenda.Auxiliares.Hub_Comunicacao;
import br.coelho.fabiano.ntagenda.Auxiliares.Preferences_IO;
import br.coelho.fabiano.ntagenda.Auxiliares.Receiver_Notification;
import br.coelho.fabiano.ntagenda.Config.Frag_Config;
import br.coelho.fabiano.ntagenda.Inicio.Frag_ini_Container;
import br.coelho.fabiano.ntagenda.InputDados.ActivityAnotacoes;
import br.coelho.fabiano.ntagenda.InputDados.ActivityLembrete;
import br.coelho.fabiano.ntagenda.Tags.Frag_tag_Container;
import br.coelho.fabiano.ntagenda.sobre.Frag_sobre;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static final String TAG_ACTIVITY = "MainActivity",
            RELOAD_FRAGMENT = "RELOAD_FRAGMENT",
            TAG_CONTAINER = "FRAME_CONTAINER",
            GOOGLE_ADMOB = "ca-app-pub-4377694481801798~9014033984",
            ADMOB_REWARDED = "ca-app-pub-4377694481801798/4376128439",
            ADMOB_INTERSTITIAL = "ca-app-pub-4377694481801798/9600534958";

    private Hub_Comunicacao comunicacao = new Hub_Comunicacao();

    private Toast toast;

    private boolean exitApplication;

    private InterstitialAd mAd;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.menu_tags_prefs) FloatingActionButton menuTagPref;
    @BindView(R.id.menu_anotacao) FloatingActionButton menuNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, GOOGLE_ADMOB);

        comunicacao.setContext(this);
        comunicacao.setFM(getFragmentManager());

        toast = Toasty.info(this, getString(R.string.exitApplication), Toast.LENGTH_SHORT);

        Bundle bundle = getIntent().getExtras();

        adInit();

        hideIniciar();

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle
                        (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(
                        new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                menuTagPref.setVisibility(View.GONE);

               Fragment cFragStack = getSupportFragmentManager().findFragmentByTag(TAG_CONTAINER);

                if(cFragStack != null){
                    if(cFragStack.getClass().getSimpleName().equals("Frag_tag_Container")){

                        Preferences_IO prefIO = new Preferences_IO();

                        String aux = prefIO.pegarPreferencias(MainActivity.this.getApplicationContext(),
                                Preferences_IO.SAVE_MYTAG);

                        if(!aux.isEmpty()){
                            menuTagPref.setVisibility(View.VISIBLE);
                        };

                    }
                }


            }
        });

        if(bundle != null){

            if(bundle.getString(Receiver_Notification.PEDING_NOTIFICATION) != null){

                comunicacao.showNotification();


            }else if(bundle.getString(RELOAD_FRAGMENT) != null){
                setReloadFragment(bundle.getString(RELOAD_FRAGMENT));
            }
        }

        setLocale();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            if(exitApplication){

                finish();
                System.exit(0);

            }else{

                super.onBackPressed();
            }

            Fragment cFragStack = getSupportFragmentManager().findFragmentByTag(TAG_CONTAINER);

            if (cFragStack == null) {

                hideIniciar();

                exitApplication = true;

                if(!toast.getView().isShown()){
                    toast.show();
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        exitApplication = false;
                    }
                }, 2000);


            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem ourSearchItem = menu.findItem(R.id.app_bar_search);

        SearchView sv = (SearchView) ourSearchItem.getActionView();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                Fragment fragment = new SearchData();
                Bundle bundle = new Bundle();
                bundle.putString("SEARCH",s);
                fragment.setArguments(bundle);

                setFragment(fragment);

                return true;
            }
        });


        sv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

                onBackPressed();

            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.nav_inicio:
                setFragment(new Frag_ini_Container());
                break;

            case R.id.nav_tag:
                setFragment(new Frag_tag_Container());
                break;

            case R.id.nav_config:
                setFragment(new Frag_Config());
                break;

            case R.id.nav_paytowin:
                setFragment(new Frag_ajudar());
                break;

            case R.id.nav_feedback:
                comunicacao.showFeedback();
                break;

            case R.id.nav_about:
                setFragment(new Frag_sobre());
                break;
        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.menu_lembrete) void abrirLembrete(){
        abrirAcitivty(new Intent(MainActivity.this, ActivityLembrete.class));
    }

    @OnClick(R.id.menu_anotacao) void abrirAnotacao(){
        abrirAcitivty(new Intent(MainActivity.this, ActivityAnotacoes.class));

    }

    @OnClick(R.id.menu_tags_prefs) void abrirTagPrefs(){
        comunicacao.showTagPref();

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, TAG_CONTAINER);
        transaction.addToBackStack(TAG_CONTAINER);
        transaction.commit();

        if(mAd.isLoaded()){
            mAd.show();
        }
    }

    private void abrirAcitivty(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void setReloadFragment(String TAG){

        if(Frag_tag_Container.TAG_FRAGMENT.equals(TAG)){

            setFragment(new Frag_tag_Container());

        }else if(Frag_Config.TAG_FRAGMENT.equals(TAG)){

            setFragment(new Frag_Config());

        }else if(Frag_ajudar.TAG_FRAGMENT.equals(TAG)){

            setFragment(new Frag_ajudar());
        }

    }

    private void hideIniciar(){

        Preferences_IO prefIO = new Preferences_IO();

        boolean list = Boolean.parseBoolean(
                prefIO.pegarPreferencias(this, Preferences_IO.SAVE_CONGIF_LIST)),

                calendar = Boolean.parseBoolean(
                        prefIO.pegarPreferencias(this, Preferences_IO.SAVE_CONGIF_CALENDAR)),

                note = Boolean.parseBoolean(
                        prefIO.pegarPreferencias(this, Preferences_IO.SAVE_CONGIF_NOTE));

        hideItemOnNav(R.id.nav_inicio, true);
        menuNote.setVisibility(View.VISIBLE);

        if(list && calendar && note){
            hideItemOnNav(R.id.nav_inicio, false);

        }

        if (!list || !calendar || !note){
            setFragment(new Frag_ini_Container());
        }else{
            setFragment(new Frag_tag_Container());
        }

        if(note){
            menuNote.setVisibility(View.GONE);
        }
    }

    private void hideItemOnNav(int id, boolean visible){
        Menu menu = navigationView.getMenu();

        menu.findItem(id).setVisible(visible);
    }

    private void adInit(){

        // para testes : ca-app-pub-3940256099942544/1033173712

        mAd = new InterstitialAd(this);
        mAd.setAdUnitId(ADMOB_INTERSTITIAL);
        mAd.loadAd(new AdRequest.Builder().build());

        mAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {}

            @Override
            public void onAdFailedToLoad(int errorCode) {}

            @Override
            public void onAdOpened() {}

            @Override
            public void onAdLeftApplication() {}

            @Override
            public void onAdClosed() {
                mAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void setLocale(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION }, 1);


        }else{

            Locale locale = new Locale(Resources.getSystem().getConfiguration().locale.toString());
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            config.locale = Resources.getSystem().getConfiguration().locale;

            getBaseContext().getResources()
                    .updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
