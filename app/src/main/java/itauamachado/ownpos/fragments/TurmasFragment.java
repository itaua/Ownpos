package itauamachado.ownpos.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
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

import itauamachado.ownpos.ContextosActivity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.TurmasAdapter;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.domain.objAulas;
import itauamachado.ownpos.domain.objUsuario;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;
import me.drakeet.materialdialog.MaterialDialog;

public class TurmasFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {


    private  RecyclerView mRecyclerView;
    private List<objAulas> mList;

    private MaterialDialog mMaterialDialog;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_turmas, container, false);

        mList = new ArrayList<>();

        mList = new SQLiteConn(getActivity()).getTurmas();
        if(mList == null || mList.size() == 0){
            callVolleyRequest();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        TurmasAdapter adapter = new TurmasAdapter (getActivity(), mList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }


    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, TurmasFragment.class.getName());
    }

    @Override
    public void onClickListener(View view, int position) {

        Intent intent = new Intent(getActivity(), ContextosActivity.class);
        intent.putExtra(Util.METHOD, Util.AULAS);
        intent.putExtra("turma", String.valueOf(mList.get(position).getId()));
        intent.putExtra("titulo", mList.get(position).getTurma());
        intent.putExtra("curso", mList.get(position).getCurso());
        intent.putExtra("prof", mList.get(position).getProf());
        intent.putExtra("site", "hpt://itauamachado.com.br");
        Util.log(mList.get(position).toString());

        startActivity(intent);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(TurmasFragment.class.getName());
    }

    // NETWORK
    @Override
    public WrapObjToNetwork doBefore() {

        if (Util.verifyConnection(getActivity())) {
            objUsuario usuario = new SQLiteConn(getActivity()).getUserLogged();
            return (new WrapObjToNetwork(Integer.valueOf(usuario.getId_senac()), "get-aulas"));
        } else {
            mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(getActivity(), R.style.MyAlertDialog))
                    .setTitle("Sem conexão com a Internet")
                    .setMessage("Impossivel baixar os dados de turmas")
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
                    objAulas aulas = gson.fromJson(jsonArray.getJSONObject(i).toString(), objAulas.class);
                    mList.add(aulas);
                }
            } catch (JSONException e) {
                Util.log("doAfter(): " + e.getMessage());
            }
            if(mList.size() != 0){
                new SQLiteConn(getActivity()).setAulas(mList);
                NotifyList();
            }
        } else {
            Toast.makeText(getActivity(), "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }


    private void NotifyList() {
        mList = new SQLiteConn(getActivity()).getTurmas();
        TurmasAdapter adapter = new TurmasAdapter (getActivity(), mList);
        mRecyclerView.setAdapter(adapter);
    }

}
