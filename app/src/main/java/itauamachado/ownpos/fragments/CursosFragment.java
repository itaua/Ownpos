package itauamachado.ownpos.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.CursosAdapter;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objCursos;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;

import static com.google.android.gms.internal.zzhl.runOnUiThread;
import static itauamachado.ownpos.extras.Util.log;


public class CursosFragment extends Fragment implements Transaction{

    private List<objCursos> mList;
    private FloatingGroupExpandableListView mFloatingGroupExpandableListView;
    private CursosAdapter mAdapter;
    private WrapperExpandableListAdapter mWrapperAdapter;
    private String TAG_LIST = "mListCursos";
    private ProgressBar mPbLoad;
    private boolean sincronizarBase = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_cursos, container, false);
        mFloatingGroupExpandableListView = (FloatingGroupExpandableListView) view.findViewById(R.id.sample_activity_list);

        mList = new ArrayList<>();
        mPbLoad = (ProgressBar) view.findViewById(R.id.pb_load);
        mPbLoad.setVisibility(View.VISIBLE);

        mList = new SQLiteConn(getActivity()).getCursos(false);
        if(mList.size() == 0){
            mAdapter = new CursosAdapter(getActivity());
        }else{
            mAdapter = new CursosAdapter(getActivity(), mList,false);
        }

        mWrapperAdapter = new WrapperExpandableListAdapter(mAdapter);
        mFloatingGroupExpandableListView.setAdapter(mWrapperAdapter);
        mPbLoad.setVisibility(View.INVISIBLE);

        activateSwipRefresh(view);
        return view;
    }


    //SwipRefresh
    public void activateSwipRefresh(final View view){
        // SWIPE REFRESH LAYOUT
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPbLoad.getVisibility() == View.INVISIBLE) {
                    if (Util.verifyConnection(getActivity())) {
                        sincronizarBase = true;
                        callVolleyRequest();

                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);

                        android.support.design.widget.Snackbar.make(view, "Sem conexão com Internet. Por favor, verifique seu WiFi ou 3G.", android.support.design.widget.Snackbar.LENGTH_LONG)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                        startActivity(it);
                                    }
                                })
                                .setActionTextColor(getActivity().getResources().getColor(R.color.coloLink))
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mList == null || mList.size() == 0){
            PreencheListaCurso();
        }
    }

    public void PreencheListaCurso(){
        SQLiteConn conn = new SQLiteConn(getActivity());
        Util.log("PreencheListaCurso - buscou da SQLite: " + mList.size() + " registros");
        mList = conn.getCursos(false);
        if(mList.size() == 0){
            sincronizarBase = true;
            callVolleyRequest();
        }else{
            NotifyList();
        }
    }

    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, CursosFragment.class.getName());
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG_LIST, (ArrayList<objCursos>) mList);
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(NoticiasFragment.class.getName());
    }


    @Override
    public WrapObjToNetwork doBefore() {

        if(!(mSwipeRefreshLayout.isRefreshing())){
            mPbLoad.setVisibility(View.VISIBLE);
        }

        if( Util.verifyConnection(getActivity()) ){
            objCursos objCursos = new objCursos();

            return( new WrapObjToNetwork(
                    objCursos,
                    "get-cursosenac"));
        }else{
            Util.log("Sem conexão com internet");
        }
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {
        mPbLoad.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        if( jsonArray != null ){
            Gson gson = new Gson();
            try{
                for(int i = 0, tamI = jsonArray.length(); i < tamI; i++){
                    objCursos objCursos = gson.fromJson( jsonArray.getJSONObject( i ).toString(), objCursos.class );
                    mList.add(objCursos);
                }
            }
            catch(JSONException e){
                log("doAfter(): "+e.getMessage());
            }
            NotifyList();
        }
        else{
            Toast.makeText(getActivity(), "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void NotifyList() {
        Util.log("NotifyList()");
        mAdapter = new CursosAdapter(getActivity(), mList,false);
        mWrapperAdapter = new WrapperExpandableListAdapter(mAdapter);
        mFloatingGroupExpandableListView.setAdapter(mWrapperAdapter);
        if(sincronizarBase){
            Util.log("sincronizarBase Presencial");
            sincronizarBase = false;
            new SQLiteConn(getActivity()).setCursos(mList, false);
        }
    }


}

