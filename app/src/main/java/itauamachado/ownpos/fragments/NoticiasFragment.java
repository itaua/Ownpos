package itauamachado.ownpos.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.NoticiasAdapter;
import itauamachado.ownpos.domain.objNoticias;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;

import static itauamachado.ownpos.extras.Util.log;

public class NoticiasFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {


    private  RecyclerView mRecyclerView;
    private List<objNoticias> mList;
    private android.support.design.widget.FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mPbLoad;
    private Boolean isLastItem;
    private String TAG_LIST = "mListNoticias";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        mList = new ArrayList<>();
        mPbLoad = (ProgressBar) view.findViewById(R.id.pb_load);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition()+1) {
                    NetworkConnection.getInstance(getActivity()).execute(NoticiasFragment.this, NoticiasFragment.class.getName());
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        NoticiasAdapter adapter = new NoticiasAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);
        activateSwipRefresh(view, this, NoticiasFragment.class.getName());

        return view;
    }


    public void activateSwipRefresh(final View view, final Transaction transaction, final String tag){
        // SWIPE REFRESH LAYOUT
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(mPbLoad.getVisibility() != View.VISIBLE){
                    if (Util.verifyConnection(getActivity())) {
                        NetworkConnection.getInstance(getActivity()).execute(transaction, tag);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList(TAG_LIST);
        }
    }


    @Override
    public void onResume() { // HACKCODE TO WORK WHEN JUST COME BACK
        super.onResume();

        if(mList == null || mList.size() == 0){
            callVolleyRequest();
        }
    }


    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, NoticiasFragment.class.getName());
    }



    @Override
    public void onClickListener(View view, int position) {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mList.get(position).getLink()));
        startActivity(intent);
    }


    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onLongPressClickListener(): "+position, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG_LIST, (ArrayList<objNoticias>) mList);
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(NoticiasFragment.class.getName());
    }



    // NETWORK
    @Override
    public WrapObjToNetwork doBefore() {
        mPbLoad.setVisibility((mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) ? View.GONE : View.VISIBLE);

        if( Util.verifyConnection(getActivity()) ){
            objNoticias noticias= new objNoticias();

            if(mSwipeRefreshLayout.isRefreshing()){
                return( new WrapObjToNetwork(
                        noticias,
                        "get-noticias", mList.get(0).getId(), true));
            }
            if(mList.size() != 0){
                return( new WrapObjToNetwork(
                        noticias,
                        "get-noticias",mList.get(mList.size()-1).getId(), false));
            }else{
                return( new WrapObjToNetwork(
                        noticias,
                        "get-noticias",0, false));
            }

        }
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {
        mPbLoad.setVisibility(View.GONE );

        if( jsonArray != null ){
            NoticiasAdapter adapter = (NoticiasAdapter) mRecyclerView.getAdapter();
            Gson gson = new Gson();
            int auxPosition = 0, position;

            if( mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() ){
                mSwipeRefreshLayout.setRefreshing(false);
                auxPosition = 1;
            }

            try{
                for(int i = 0, tamI = jsonArray.length(); i < tamI; i++){
                    objNoticias noticias = gson.fromJson( jsonArray.getJSONObject( i ).toString(), objNoticias.class );
                    position = auxPosition == 0 ? mList.size() : 0;
                    adapter.addListItem(noticias, position);

                    if( auxPosition == 1 ){
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, position);
                    }
                }

                if( jsonArray.length() == 0 && auxPosition == 0 ){
                    isLastItem = true;
                }

            }
            catch(JSONException e){
                log("doAfter(): "+e.getMessage());
            }
        }
        else{
            Toast.makeText(getActivity(), "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
            if( mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing() ){
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

}
