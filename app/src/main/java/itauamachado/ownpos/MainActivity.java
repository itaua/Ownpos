package itauamachado.ownpos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.adapters.TabsMainAdapter;
import itauamachado.ownpos.domain.NavigationWiFi;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.objUsuario;
import itauamachado.ownpos.extras.SlidingTabLayout;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.service.JobSchedulerService;
import itauamachado.ownpos.service.LocationIntentService;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


public class MainActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private int mItemDrawerSelected;
    private int mProfileDrawerSelected;

    private List<PrimaryDrawerItem> mListItemDrawerLeft;
    private objUsuario mUsuario;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private FloatingActionMenu fab_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TRANSITIONS
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
                TransitionInflater inflater = TransitionInflater.from(this);
                Transition transition = inflater.inflateTransition( R.transition.transitions );
                getWindow().setSharedElementExitTransition( transition );
            }
        super.onCreate(savedInstanceState);


        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {

            @Override
            public void set(ImageView imageView, Uri uri, Drawable drawable) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(drawable).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context context) {
                return null;
            }
        });



        setContentView(R.layout.activity_main);
        mUsuario = new SQLiteConn(this).getUserLogged();

        if(savedInstanceState != null){
            mItemDrawerSelected = savedInstanceState.getInt("mItemDrawerSelected", 0);
            mProfileDrawerSelected = savedInstanceState.getInt("mProfileDrawerSelected", 0);
        }

        // TOOLBAR
            mToolbar = (Toolbar) findViewById(R.id.tb_main);
            mToolbar.setTitle(R.string.app_name);
            setSupportActionBar(mToolbar);

        montaTab(); //tem que ser antes do drawer, pois o drawer usa itens das tabs.
        montaDrawer(savedInstanceState);

        EventBus.getDefault().register(this);

        setFloatingActionMenu();


        AlarmeLocation();
        AlarmeContext();


        //List<NavigationWiFi> resultados = new SQLiteConn(this).getNavigationWiFi();
        //for (int i = 0; i < resultados.size(); i++){
        //    Log.d("LOG3", resultados.get(i).toString());
        //}
/**
        ContentValues cv = new SQLiteConn(this).getLastGeoPosition();
        if(cv!=null){
            Util.log("latitude"+cv.get("latitude"));
            Util.log("longitude"+cv.get("longitude"));
            Util.log("verificado"+cv.get("verificado"));
            Util.log("accuracy"+cv.get("accuracy"));
        }
**/

    }

    public void setFloatingActionMenu(){
        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_contexto);
        fab_menu.hideMenu(false);
        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_biblioteca);
        fab_menu.hideMenu(false);
        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_tarefas);
        fab_menu.hideMenu(false);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_mapa){
            startActivity(new Intent(this, MapaActivity.class));
        }else if(id == R.id.limparLogin){
            new SQLiteConn(this).delUserLogged();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("mItemDrawerSelected", mItemDrawerSelected);
        outState.putInt("mProfileDrawerSelected", mProfileDrawerSelected);
        outState = navigationDrawerLeft.saveInstanceState(outState);
        outState = headerNavigationLeft.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if(navigationDrawerLeft.isDrawerOpen()){
            navigationDrawerLeft.closeDrawer();
        }

        //colocar as opções de backPressed antes deste if
        else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            super.onBackPressed();
        }
    }

    // ItensMenuPrincipal
    private List<PrimaryDrawerItem> getSetItemDrawerLeft(){

        String[] names = null;
        int[] icons = null;
        int[] iconsSelected = null;

        if(mUsuario.getPerfil().equalsIgnoreCase(Util.PERFIL_VISITANTE)){
            names = Util.TITULOS_DRAWER_ANONIMO;
            icons = Util.ICONES_ANONIMO;
            iconsSelected = Util.ICONES_ANONIMO;
        }else{
            names = Util.TITULO_DRAWER;
            icons = Util.ICONES;
            iconsSelected = Util.ICONES;
        }

        List<PrimaryDrawerItem> list = new ArrayList<>();
        list = new ArrayList<>();
        for(int i = 0; i < names.length; i++){
            PrimaryDrawerItem aux = new PrimaryDrawerItem();
            aux.setName(names[i]);
            aux.setIcon(getResources().getDrawable(icons[i]));
            aux.setTextColor(getResources().getColor(R.color.primary_text));
            aux.setSelectedIcon(getResources().getDrawable(iconsSelected[i]));
            aux.setSelectedTextColor(getResources().getColor(R.color.primary));
            list.add( aux );
        }

        return(list);
    }

    protected void montaTab(){
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);



        mViewPager.setAdapter(new TabsMainAdapter(getSupportFragmentManager(), this, mUsuario.getPerfil()));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        mSlidingTabLayout.setHorizontalFadingEdgeEnabled(true); //coloca faiding na tab que esta saindo da visão da tab
        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                navigationDrawerLeft.setSelection(position);

                if (mUsuario.getPerfil().equalsIgnoreCase(Util.PERFIL_VISITANTE)) {
                    Util.log(Util.TITULOS_TAB_ANONIMO[position]);
                    showFloatActionButton(Util.TITULOS_TAB_ANONIMO[position]);
                } else {
                    Util.log(Util.TITULO_TAB[position]);
                    showFloatActionButton(Util.TITULO_TAB[position]);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Util.log("onPageScrollStateChanged: " + state);
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    public void showFloatActionButton(String tabName){

        if(fab_menu.isOpened())
            fab_menu.close(true);

        fab_menu.hideMenu(true);

        int delay = 400;

        if(tabName == "MENU"){
            fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_contexto);
            fab_menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_from_down));
            fab_menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_to_down));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab_menu.showMenu(true);
                }
            }, delay);

        }else
        if(tabName == "BIBLIOTECA"){
            fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_biblioteca);
            fab_menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_from_down));
            fab_menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_to_down));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab_menu.showMenu(true);
                }
            }, delay);
        }else
        if(tabName=="NOTICIAS"){

        }else
        if(tabName=="CURSOS") {

        }else
        if(tabName=="CURSOS EAD") {

        }else
        if(tabName=="TAREFAS"){
            fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_tarefas);
            fab_menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_from_down));
            fab_menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_to_down));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab_menu.showMenu(true);
                }
            }, delay);

        }else
        if(tabName=="TURMAS"){

        }else
        if(tabName=="AGENDA"){

        }
    }

    protected void montaDrawer(Bundle savedInstanceState){


        // NAVIGATION DRAWER
        // HEADER

        if(mUsuario.getUrlPhoto().trim().length() == 0){

            headerNavigationLeft = new AccountHeader()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withSavedInstance(savedInstanceState)
                    .withThreeSmallProfileImages(true)
                    .addProfiles(new ProfileDrawerItem()
                                    .withName(mUsuario.getPerfil() + ": " + mUsuario.getNome())
                                    .withEmail(mUsuario.getStatus())
                                    .withIcon(getResources().getDrawable(R.mipmap.ic_emoticon))
                    )
                    .withHeaderBackground(R.color.primary)
                    .build();
        }else{
            headerNavigationLeft = new AccountHeader()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withSavedInstance(savedInstanceState)
                    .withThreeSmallProfileImages(true)
                    .addProfiles(new ProfileDrawerItem()
                                    .withName(mUsuario.getPerfil() + ": " + mUsuario.getNome())
                                    .withEmail(mUsuario.getEmail() + "\n" + mUsuario.getStatus())
                                    .withIcon(mUsuario.getUrlPhoto())
                    )
                    .withHeaderBackground(R.color.primary)
                    .build();
        }


       //Util.log("URL PHOTO LOGIN: " + mUsuario.getUrlPhoto());

        // BODY
        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.START)
                .withSavedInstance(savedInstanceState)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        mViewPager.setCurrentItem(i);
                        mToolbar.setSubtitle(((PrimaryDrawerItem) iDrawerItem).getName());
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(MainActivity.this, "onItemLongClick: " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();

        mListItemDrawerLeft = getSetItemDrawerLeft();

        if(mListItemDrawerLeft != null && mListItemDrawerLeft.size() > 0){
            for( int i = 0; i < mListItemDrawerLeft.size(); i++ ){
                navigationDrawerLeft.addItem(mListItemDrawerLeft.get(i));
            }
            navigationDrawerLeft.setSelection(mItemDrawerSelected);
        }

        /**
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Sobre"));
        navigationDrawerLeft.addItem(new DividerDrawerItem());
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Projeto").withIcon(R.mipmap.ic_map_marker_radius));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Desevolvedor").withIcon(R.mipmap.ic_emoticon));
         **/
    }

    public void onEvent(final MessageEB m){
        if(m.getmTag().equalsIgnoreCase(MessageEB.TAG_LOCATION)){
            Util.log("Distancia: "+m.getDistancia());

        }else
            if(m.getmTag().equalsIgnoreCase(MessageEB.TAG_WIFI_INFO)){
                Util.log("Estou no senac: "+ m.isNoSENAC());
            }

    }


    //JOBSCHEDULER
    /**
        public void onJobExecuteLocation(){
            ComponentName cp = new ComponentName(this, JobSchedulerService.class);
            JobInfo jb = new JobInfo.Builder (Util.JOB_LOCATION_CODE, cp)
                    .setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR)
                            //.setExtras(b)
                    .setPersisted(true)
                    .setPeriodic(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .build();
            JobScheduler js = JobScheduler.getInstance(this);
            js.schedule(jb);
        }

        public void onJobExecuteContext(){
            ComponentName cp = new ComponentName(this, JobSchedulerService.class);
            JobInfo jb = new JobInfo.Builder (Util.JOB_CONTEXT_CODE, cp)
                    .setBackoffCriteria(5000, JobInfo.BACKOFF_POLICY_LINEAR)
                            //.setExtras(b)
                    .setPersisted(true)
                    .setPeriodic(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setRequiresCharging(false)
                    .setRequiresDeviceIdle(false)
                    .build();
            JobScheduler js = JobScheduler.getInstance(this);
            js.schedule(jb);
        }


    public void onCancel(int id){
        JobScheduler js = JobScheduler.getInstance(this);
        js.cancel(id);
    }

    public void callIntentService(){
        Intent it = new Intent(this, LocationIntentService.class);
        it.putExtra(Util.LOCATION, mLastLocation);
        startService(it);
    }
    **/

    //ALARME
    public void AlarmeLocation(){

        //boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARME_DISPARADO"), PendingIntent.FLAG_NO_CREATE) == null);

        //if(alarmeAtivo){
        //    Log.i("Script", "Novo alarme");

            Intent intent = new Intent("ALARME_LOCATION");
            PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 3);

            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (60*1000*5), p);
        //}
        //else{
        //    Log.i("Script", "Alarme já ativo");
        //}
    }

    public void AlarmeContext(){

        boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARME_CONTEXT"), PendingIntent.FLAG_NO_CREATE) == null);

        if(alarmeAtivo){
            Log.i("Script", "Novo alarme");

            Intent intent = new Intent("ALARME_CONTEXT");
            PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 3);

            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (60000*5), p);
            alarme.cancel(p);
        }else{
            Log.i("Script", "Alarme já ativo");
        }
    }

}
