package itauamachado.ownpos.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.AgendaAdapter;
import itauamachado.ownpos.adapters.NoticiasAdapter;
import itauamachado.ownpos.adapters.TurmasAdapter;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.domain.objAulas;
import itauamachado.ownpos.domain.objEventos;
import itauamachado.ownpos.domain.objNoticias;
import itauamachado.ownpos.domain.objUsuario;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;

import static itauamachado.ownpos.extras.Util.log;

public class AgendaFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {

    private RecyclerView mRecyclerView;
    private List<objEventos> mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        mList = new ArrayList<>();

        mList = new SQLiteConn(getActivity()).getEventos();

        if(mList.size() == 0){
            callVolleyRequest();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(getActivity(), mRecyclerView, this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        AgendaAdapter adapter = new AgendaAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }


    public void callVolleyRequest() {
        NetworkConnection.getInstance(getActivity()).execute(this, AgendaFragment.class.getName());
    }


    @Override
    public void onClickListener(View view, int position) {

    }


    @Override
    public void onLongPressClickListener(View view, int position) {

    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(NoticiasFragment.class.getName());
    }


    // NETWORK
    @Override
    public WrapObjToNetwork doBefore() {

        if (Util.verifyConnection(getActivity())) {

            objUsuario user = new SQLiteConn(getActivity()).getUserLogged();

            return (new WrapObjToNetwork(Integer.valueOf(user.getId_senac()), "get-eventos"));
        }
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {

        if (jsonArray != null) {
            Gson gson = new Gson();
            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {
                    objEventos eventos = gson.fromJson(jsonArray.getJSONObject(i).toString(), objEventos.class);
                    Util.log(eventos.toString());
                    mList.add(eventos);
                }
            } catch (JSONException e) {
                Util.log("doAfter(): " + e.getMessage());
            }
            if (mList.size() != 0) {
                new SQLiteConn(getActivity()).setEventos(mList);
                NotifyList();
            }

        } else {
            Toast.makeText(getActivity(), "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void NotifyList() {
        mList = new SQLiteConn(getActivity()).getEventos();
        AgendaAdapter adapter = new AgendaAdapter (getActivity(), mList);
        mRecyclerView.setAdapter(adapter);
    }
}