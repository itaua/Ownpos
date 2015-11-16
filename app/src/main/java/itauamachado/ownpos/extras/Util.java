package itauamachado.ownpos.extras;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.NavigationWiFi;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;


public class Util {

    //Variavel url rota mapaOutdoor
    public static final String LINK_OUTDOOR = "http://maps.google.com/maps";

    private static NavigationWiFi mNavigationWiFi;

    //GCM
        public static final String ServerAPIKey = "AIzaSyDxRNtqHaicUkmOhT6qU8mp0W_fRqdxk38";
        public static final String SenderID = "798906214056";

    //Variaveis Contextos
        public static final String METHOD = "method";
        public static final String AULAS = "aulas";
        public static final String TAREFAS = "tarefas";
        public static final String NAVEGACAO = "navegacao";

        public static final String[] TITULOS_TAB_AULAS = {"TURMA","CHAT"};
        public static final String[] TITULOS_TAB_TAREFAS = {"TAREFA","CHAT"};

    //Variaveis do AtlasIndoor
        public static final String[] FloorPlanId = new String[]{
            "c944fdee-c87c-4800-8dc0-588f814bc221",
            "a9cf9f67-a2a5-4173-959d-ed3833851c36",
            "2ad8900f-429e-48a3-bb6c-b3becef17666",
            "934f7ac6-9686-4545-97eb-1eb83e1c089a",
            "08c102f9-f5b6-4e67-9f62-f558d933cc01",
            "4b7840d4-1192-4f01-a4bc-0ce45dfe4068",
            "512e50c0-ebf3-44ca-83b7-721239eb5ed0",
            "d68544db-c3ca-4692-b188-3041032733a3",
            "f72603af-584f-4bc1-b98a-d0ced3093acd"};


        public static final String[] FloorId = new String[]{
            "ebd17c92-b1bc-4822-9c7a-7edbdbbd2a99",
            "0e7e7037-f5ae-4ba9-9784-64279475c2a2",
            "0da1b660-b5f1-420b-952c-23cfded0279f",
            "d8966111-9f64-47e2-8485-a027753ffb8a",
            "d0998cce-c27c-4e19-82f7-96244e55d671",
            "298cc861-7425-42d6-894c-617f175e6295",
            "602bbf8b-a67e-41d3-844e-92e011764cb6",
            "71d844c4-1951-435a-975a-8fc5419807b4",
            "f22121a3-1644-4677-9470-0c29bfa9f599"
            };
    public static final String ApiKey = "3f55ee6b-a7a1-4cab-8d2f-dec4e0ed34b4";
    public static final String ApiSecret = "Z!Is0QvX4anu3PRiCfDrSx6rYt%cIRIosrKO%7yVug0b6NAy2%1SoOe6)FKnfduEQ49rCEwjPB91X)yr&0cDReuf7A5OH3s!2a4p24x5tGiazalXCww(JlDMWd0etWSP";
    public static final String VenueId = "21380ce8-b17f-470e-b87c-c70571f008c3";


    //Variaveis do sistema
        public static final String WEBSERVER = "http://www.itauamachado.com.br/ownpos/modulos/banco/webservice/package/ctrl/mCtrlObj.php";
        public static final String URL_ACERVO_DISPONIVEL = "http://www.itauamachado.com.br/ownpos/modulos/data_mining/mAcervo_disponivel.php";
        public static final String LOCATION = "location";
        public static final String REDE_SENAC = "SENAC-RS EDUCACIONAL";


    //Variaveis detalhes dos mapas
        public static final String[] andar_numeros  = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        public static final String[] andar_titulos = { "Primeiro Andar","Segundo Andar","Terceiro Andar","Quarto Andar","Quinto Andar","Sexto Andar","Sétimo Andar","Oitavo Andar","Nono Andar"};
        public static final String[] andar_desc = {
                "Atendimento ao público, cantina e salas administrativas",
                "Biblioteca e laboratórios de desenho, confecção e modelagem",
                "Salas de aula",
                "Salas de aula gastronomia",
                "Salas de aula",
                "Salas de aula",
                "Laboratorios de Informática",
                "Estudio de rádio",
                "Auditório principal"};
        public static final String[] AndarMapas = new String[]{
                "mapa_terreo.png",
                "mapa_segundo_andar.png",
                "mapa_terceiro_andar.png",
                "mapa_quarto_andar.png",
                "mapa_quinto_andar.png",
                "mapa_sexto_andar.png",
                "mapa_setimo_andar.png",
                "mapa_oitavo_andar.png",
                "mapa_nono_andar.png"
        };

    //Variaveis de Tabs e Drawers
        public static final String[] TITULOS_DRAWER_ANONIMO =
            {"Principal","Biblioteca","Reservas","Cursos", "Cursos à Distância", "Noticias"};
        public static final String[] TITULOS_TAB_ANONIMO =
            {"MENU","BIBLIOTECA","RESERVAS", "CURSOS", "CURSOS EAD", "NOTICIAS"};
        public static final int[] ICONES_ANONIMO =
            {R.mipmap.ic_view_list,R.mipmap.ic_library,R.mipmap.ic_library, R.mipmap.ic_school, R.mipmap.ic_school,R.mipmap.ic_newspaper};


        public static final String[] TITULO_DRAWER =
                {"Principal", "Turmas", "Compromissos", "Biblioteca", "Reservas", "Cursos","Cursos à Distância", "Noticias"};
        public static final String[] TITULO_TAB=
                {"MENU","TURMAS","AGENDA","BIBLIOTECA", "RESERVAS", "CURSOS","CURSOS EAD", "NOTICIAS"};
        public static final int[] ICONES =
                {R.mipmap.ic_view_list, R.mipmap.ic_book_open, R.mipmap.ic_calendar_clock, R.mipmap.ic_library,
                        R.mipmap.ic_calendar_clock, R.mipmap.ic_school, R.mipmap.ic_school, R.mipmap.ic_newspaper};


    //PERFIL
        public static final String PERFIL_VISITANTE = "Visitante";
        public static final String PERFIL_ALUNO = "Aluno";

    //Verifica a conectividade
        public static boolean verifyConnection(Context context){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        }

    //Verify BroadCast Wi-Fi
        private static WifiManager mMainWifi;
        private static boolean originalState;
        private static WifiReceiver receiverWifi;

        private static class WifiReceiver extends BroadcastReceiver {

            List<ScanResult> wifiList;
            List<NavigationWiFi> navigationWiFiList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            // This method call when number of wifi connections changed
            public void onReceive(Context c, Intent intent) {

                wifiList = mMainWifi.getScanResults();
                //Number Of Wifi connections : wifiList.size()
                /****/


                for(int i = 0; i < wifiList.size(); i++){
                    Util.log("Buscando rede senac");
                    if( wifiList.get(i).SSID.equalsIgnoreCase(REDE_SENAC)){
                        Toast.makeText(c, "dados salvos", Toast.LENGTH_SHORT).show();
                        NavigationWiFi n = new NavigationWiFi();
                        n.setCapabilities(wifiList.get(i).capabilities);
                        n.setFrequency(wifiList.get(i).frequency);
                        n.setLevel(wifiList.get(i).level);
                        n.setSSID(wifiList.get(i).SSID);
                        n.setBSSID(wifiList.get(i).BSSID);
                        navigationWiFiList.add(n);
                    }
                }

                if(navigationWiFiList.size() > 0){
                    PointF Ownpos = new SQLiteConn(c).getNavigationWiFi(navigationWiFiList);
                    MessageEB eb = new MessageEB();
                    eb.setmTag(MessageEB.TAG_WIFI_INFO);
                    eb.setNoSENAC(true);
                    eb.setOwnpos(Ownpos);
                    EventBus.getDefault().post(eb);
                }else{
                    MessageEB eb = new MessageEB();
                    eb.setmTag(MessageEB.TAG_WIFI_INFO);
                    eb.setNoSENAC(false);
                    eb.setOwnpos(null);
                    EventBus.getDefault().post(eb);
                }



                //mMainWifi.setWifiEnabled(originalState);
                unregisterWiFi(c);
            }
        }

        public static void unregisterWiFi(Context context){
            try{
                context.unregisterReceiver(receiverWifi);
            }catch (IllegalArgumentException e){
                Util.log(e.toString());
            }
        }

        public static void getWiFi (Context context){


            //WifiManager mainWifi;
            // Initiate wifi service manager
            mMainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            // Check for wifi is disabled
            originalState = mMainWifi.isWifiEnabled();
            if (originalState) {
                // If wifi disabled then enable it
                // mMainWifi.setWifiEnabled(true);
            // wifi scaned value broadcast receiver
            receiverWifi = new WifiReceiver();
            // Register broadcast receiver
            // Broacast receiver will automatically call when number of wifi connections changed
            context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mMainWifi.startScan();
            }
        }


    //RecycleMethod
        public static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
            private Context mContext;
            private GestureDetector mGestureDetector;
            private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

            public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
                mContext = c;
                mRecyclerViewOnClickListenerHack = rvoclh;
                mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){

                    @Override
                    public void onLongPress(MotionEvent e) {

                        View cv = rv.findChildViewUnder(e.getX(), e.getY());
                        boolean callContextMenuStatus = false;
                        if(cv != null && mRecyclerViewOnClickListenerHack != null && !callContextMenuStatus){
                            mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                    rv.getChildAdapterPosition(cv) );
                        }
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {

                        View cv = rv.findChildViewUnder(e.getX(), e.getY());
                        boolean callContextMenuStatus = false;
                        if(cv != null && mRecyclerViewOnClickListenerHack != null && !callContextMenuStatus){
                            mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                    rv.getChildAdapterPosition(cv) );
                        }

                        return true;
                    }
                });
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                mGestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {}
        }

    //SharedPreference
    public static final String PREF_KEY_NICKNAME = "itauamachado.ownpos.extras.Util.PREF_KEY_NICKNAME";
    public static final String PREF_KEY_ID = "itauamachado.ownpos.extras.Util.PREF_KEY_ID";

    public static final String PREF_KEY_NOTIFICATION_STATUS = "itauamachado.ownpos.extras.Util.PREF_NOTIFICATION_STATUS_KEY_ID";
    public static final String PREF_KEY_NOTIFICATION_STATUS_OLD = "itauamachado.ownpos.extras.Util.PREF_KEY_NOTIFICATION_STATUS_OLD";

    public static void savePrefKeyValue( Context context, String key, String value ){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor e = sp.edit();
            e.putString( key, value );
            e.apply();
        }
        public static String retrievePrefKeyValue( Context context, String key, String... defaultValue ){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context );
            String dValue = defaultValue != null && defaultValue.length > 0 ? defaultValue[0] : "";
            sp.getString(key, dValue);
            return( sp.getString( key, dValue ) );
        }


    //Notification
        public static boolean isMyApplicationTaskOnTop(Context context) {
            String packageName = context.getPackageName();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
            if(recentTasks != null && recentTasks.size() > 0) {
                ActivityManager.RunningTaskInfo t = recentTasks.get(0);
                String pack = t.baseActivity.getPackageName();
                if(pack.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }


    //controle de log do sistema
        public static void log(String msg){
            Log.i("LOG", msg);
        }



}
