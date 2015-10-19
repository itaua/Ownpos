
package itauamachado.ownpos;


import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.AnimationBuilder;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.adapters.MapaAdapter;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.domain.Andares;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.NavigationWiFi;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.extras.mapa.views.PinView;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;
import itauamachado.ownpos.service.IndoorMap;
import me.drakeet.materialdialog.MaterialDialog;


public class MapaActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack, Transaction {
    //itens layout
    private SlidingUpPanelLayout mLayout;

    //Views Layout
    private MaterialDialog mMaterialDialog;
    private RecyclerView mRecyclerView;
    private MapaAdapter mAdapter;

    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabIndoorAtlas;
    private FloatingActionButton mFabWiFi;



    //iten de configuração

    private int mMapSelected;
    private List<Andares> mList;
    private List<ItensMapa> mItensMapaList;
    private boolean sincronizarBase = false;
    private boolean newPos = false;
    private PointF mOwnpos;
    private ItensMapa mPointA;
    private ItensMapa mPointB;
    private PinView mImageView;
    private Handler handler;
    //indoor
    private boolean isIndoorActived = false;
    private IndoorMap indoorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        handler = new Handler();
        mImageView = (PinView) findViewById(R.id.imageView);
        mItensMapaList = new ArrayList<>();

        mMapSelected = 0;
        mList = new ArrayList<>();

        int andares = new SQLiteConn(this).InfoMapas();

        if(andares == 0){
            callVolleyRequest();
        }else{
            for( int i = 1; i <= andares; i++){
                Andares andar = new Andares();
                andar.setAndar(Util.andar_numeros[i-1]);
                andar.setTitulo(Util.andar_titulos[i - 1]);
                andar.setDescricao(Util.andar_desc[i - 1]);
                andar.setItensMapa(new SQLiteConn(this).getItensAndar(String.valueOf(i)));
                mList.add(andar);
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(this, mRecyclerView, this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new MapaAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        initialiseLayout();

        if (getIntent() != null && getIntent().getExtras() != null) {
            mPointB = new SQLiteConn(this).getDescSala(getIntent().getExtras().getInt("pontoB"));
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        initialiseImage();
        EventBus.getDefault().register(this);



    }

    public void onEvent(final MessageEB m){

        if (m.getmTag().equalsIgnoreCase(MessageEB.TAG_INDOOR_ATLAS)){
            mOwnpos = m.getOwnpos();
            newPos = true;
            indoorMap.stopPositioning();
            isIndoorActived = false;
            Util.log("Veio resposta de posição");
        }else
        if (m.getmTag().equalsIgnoreCase(MessageEB.TAG_WIFI_INFO)){
            Util.log("TAG_WIFI_INFO");
            mOwnpos = m.getOwnpos();
            newPos = true;

        }else
        if (m.getmTag().equalsIgnoreCase(MessageEB.TAG_MAPA_INDOOR)){
            mPointB  = m.getPontoB();
            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                            mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {

                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                initialiseImage();
            }
        }
    }

    public void buscaPosicaoIndoor(){
        //IndoorAtlas
        newPos = false;
        indoorMap = new IndoorMap(this, Util.FloorId[mMapSelected], Util.FloorPlanId[mMapSelected]);
        indoorMap.startPositioning();
        isIndoorActived = true;
    }
    public void buscaPosicaoWiFi(){
        //Wi-Fi BroadCast
        Util.getWiFi(MapaActivity.this);
    }

    public void callVolleyRequest() {
        NetworkConnection.getInstance(this).execute(this, MapaActivity.class.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            if(isIndoorActived){
                Util.unregisterWiFi(this);
            }
            super.onBackPressed();
        }
    }

    private void initialiseImage() {

        mImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER);
        mImageView.configPath(true);

        mImageView.setImage(ImageSource.asset(Util.AndarMapas[mMapSelected]));

        Util.log("initialiseImage()");
        if(mPointB != null){
            mImageView.setPontoB(mPointB);
        }

        //mImageView.setImage(ImageSource.resource(mAndarImagens.get(mMapSelected)));
        //Util.log(mAndarImagens.get(mMapSelected));
        mImageView.setPointsPath(mMapSelected);

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mImageView.isReady()) {

                    PointF sCoord = mImageView.viewToSourceCoord(e.getX(), e.getY());
                    final ItensMapa item = mImageView.getInfoMarker(sCoord, 1);
                    if (item != null) {

                        float scale = 1f;
                        AnimationBuilder animationBuilder = mImageView.animateScaleAndCenter(scale, sCoord);
                        animationBuilder.withDuration(700).withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start();

                        mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog))
                                .setTitle(item.getTitulo())
                                .setMessage(item.getDescri())
                                .setPositiveButton("Chegar até aqui", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mImageView.setPontoB(item);
                                        mMaterialDialog.dismiss();
                                    }
                                })
                                .setNegativeButton("Fechar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                });
                        mMaterialDialog.show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mImageView.isReady()) {

                } else {
                    Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mImageView.isReady()) {
                    PointF sCoord = mImageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Double tap: " + ((int) sCoord.x) + ", " + ((int) sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Double tap: Image not ready", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        mImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    //Segundo Layout
    private void initialiseLayout() {
        //setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.7f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelExpanded(View panel) {
            }

            @Override
            public void onPanelCollapsed(View panel) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFabMenu.showMenu(true);
                    }
                }, 700);

            }

            @Override
            public void onPanelAnchored(View panel) {
                mFabMenu.hideMenu(false);
            }

            @Override
            public void onPanelHidden(View panel) {
                mFabMenu.hideMenu(false);
            }
        });

        TextView t = (TextView) findViewById(R.id.name);
        t.setText("Escolha um outro andar se desejar...");


        mFabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);


        mFabMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(MapaActivity.this, R.anim.scale_up));
        mFabMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(MapaActivity.this, R.anim.scale_down));
        mFabMenu.hideMenu(false);


        mFabIndoorAtlas = (FloatingActionButton) findViewById(R.id.fab_location);
        mFabIndoorAtlas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFabMenu.close(true);
                if (isIndoorActived) {
                    indoorMap.tearDown();
                    Util.unregisterWiFi(MapaActivity.this);
                    Toast.makeText(MapaActivity.this, "parando busca de local", Toast.LENGTH_SHORT).show();
                    isIndoorActived = false;

                } else {
                    Toast.makeText(MapaActivity.this, "iniciando busca de local", Toast.LENGTH_SHORT).show();
                    buscaPosicaoIndoor();
                    mImageView.setOwnpos(); // thread pra desenhar
                    setOwnposIndoorAtlas(); // thread pra perguntar se aceita posição
                }
            }
        });


        mFabWiFi = (FloatingActionButton) findViewById(R.id.fab_wifi);
        mFabWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFabMenu.close(true);
                Toast.makeText(MapaActivity.this, "parando busca de local wifi", Toast.LENGTH_SHORT).show();
                buscaPosicaoWiFi();
                mImageView.setOwnpos(); // thread pra desenhar
                setOwnposWiFi();        // thread pra perguntar se aceita posição
            }
        });
    }


    @Override
    public WrapObjToNetwork doBefore() {

        if (Util.verifyConnection(this)) {
            return (new WrapObjToNetwork("get-mapa"));
        } else {

            mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog))
                    .setTitle("Sem conexão com a Internet")
                    .setMessage("Impossivel baixar os dados do mapa")
                    .setPositiveButton("ligar dados moveis", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(it);
                        }
                    })
                    .setNegativeButton("Voltar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });
            mMaterialDialog.show();
        }
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {
        if (jsonArray != null) {

            Gson gson = new Gson();

            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {
                    ItensMapa mapa = gson.fromJson(jsonArray.getJSONObject(i).toString(), ItensMapa.class);

                    Util.log(mapa.toString());

                    mItensMapaList.add(mapa);

                    sincronizarBase = true;
                }
            } catch (JSONException e) {
                Util.log("doAfter(): " + e.getMessage());
            }
            NotifyList();
        } else {
            Toast.makeText(this, "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void NotifyList() {
        Util.log("NotifyList: ");

        if (sincronizarBase) {
            sincronizarBase = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Util.log("runOnUiThread(new Runnable(): ");

                    new SQLiteConn(MapaActivity.this).saveItensMapas(mItensMapaList);

                    for (int i = 1; i <= 9; i++) {
                        Andares andar = new Andares();
                        andar.setAndar(Util.andar_numeros[i - 1]);
                        andar.setTitulo(Util.andar_titulos[i - 1]);
                        andar.setDescricao(Util.andar_desc[i - 1]);
                        andar.setItensMapa(new SQLiteConn(MapaActivity.this).getItensAndar(String.valueOf(i)));
                        mList.add(andar);
                        mAdapter = new MapaAdapter(MapaActivity.this, mList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            });
        }
    }

    //RecycleView Methods
    @Override
    public void onClickListener(View view, int position) {
        mMapSelected = position;
        initialiseImage();
        if(isIndoorActived){
            indoorMap.stopPositioning();
            indoorMap = new IndoorMap(this, Util.FloorId[mMapSelected], Util.FloorPlanId[mMapSelected]);
            indoorMap.startPositioning();
        }

        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }

    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Intent intent = new Intent(this, Detalhe_Andar_Activity.class);
        intent.putExtra("andar", mList.get(position).getAndar());
        Util.log(mList.get(position).getAndar());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if(isIndoorActived){
            indoorMap.tearDown();
        }
        super.onDestroy();
    }

    public void setOwnposIndoorAtlas(){

        handler = new Handler();
        new Thread(){
            public void run(){
                while (!newPos) {} //segura até ter resposta do EventBus

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.centralizarPoint(mOwnpos);
                        final ItensMapa itensMapa = mImageView.getNearPoint(mOwnpos);
                        mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog))
                            .setTitle("Posição sugerida...")
                                .setMessage(itensMapa.getDescri())
                                .setPositiveButton("Aceitar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mImageView.setPontoA(itensMapa);
                                        mMaterialDialog.dismiss();
                                    }
                                })
                                .setNegativeButton("rejeitar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                        final MaterialDialog n = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog));
                                        n.setTitle("Posição sugerida...");
                                        n.setMessage("Navegar a partir da entrada");
                                        n.setPositiveButton("Aceitar", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mImageView.setPontoA(null);
                                                n.dismiss();
                                            }
                                        });
                                        n.setNegativeButton("rejeitar", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                n.dismiss();
                                            }
                                        });
                                        n.show();
                                    }
                            });
                        mMaterialDialog.show();
                    }
                });
            }
            }.start();
    }

    public void setOwnposWiFi(){
        handler = new Handler();
        new Thread(){
            public void run(){
                while (!newPos) {} //segura até ter resposta do EventBus

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Util.log("X: "+mOwnpos.x + "Y: "+mOwnpos.y);
                        if(mOwnpos !=null){
                            mImageView.centralizarPoint(mOwnpos);

                            final ItensMapa itensMapa = mImageView.getNearPoint(mOwnpos);
                            mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog))
                                .setTitle("Posição sugerida...")
                                .setMessage(itensMapa.getDescri())
                                .setPositiveButton("Aceitar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mImageView.setPontoA(itensMapa);
                                        mMaterialDialog.dismiss();
                                    }
                                })
                                .setNegativeButton("rejeitar", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                        final MaterialDialog n = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog));
                                        n.setTitle("Posição sugerida...");
                                        n.setMessage("Navegar a partir da entrada");
                                        n.setPositiveButton("Aceitar", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mImageView.setPontoA(null);
                                                n.dismiss();
                                            }
                                        });
                                        n.setNegativeButton("rejeitar", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                n.dismiss();
                                            }
                                        });
                                        n.show();
                                    }
                                });
                            mMaterialDialog.show();
                        }else {

                            mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(MapaActivity.this, R.style.MyAlertDialog))
                                    .setTitle("Não foi possivel determinar posição.")
                                    .setMessage("Utilizar ponto de partida como sendo a porta de acessp principal?")
                                    .setPositiveButton("Aceitar", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mImageView.setPontoA(null);
                                            mMaterialDialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("rejeitar", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mMaterialDialog.dismiss();
                                        }
                                    });
                            mMaterialDialog.show();
                        }
                    }
                });
            }
        }.start();
    }
}
