package itauamachado.ownpos.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.iangclifton.android.floatlabel.FloatLabel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import itauamachado.ownpos.Detalhe_Acervo_Activity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.AcervoAdapter;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objAcervo;
import itauamachado.ownpos.domain.WrapObjToNetwork;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;
import itauamachado.ownpos.network.NetworkConnection;
import itauamachado.ownpos.network.Transaction;
import me.drakeet.materialdialog.MaterialDialog;

import static itauamachado.ownpos.extras.Util.log;


public class AcervoFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<objAcervo> mList;

    private ProgressBar mPbLoad;
    private boolean isLastItem;
    private String TAG_LIST = "mListAcervo";
    private MaterialDialog mMaterialDialog;
    private objAcervo mObjAcervo;
    private FloatLabel tv_titulo, tv_autor, tv_assunto, tv_resumo;
    private boolean mTemMais = true;
    private AcervoAdapter mAdapter;

    private FloatingActionMenu fabMenu;
    private FloatingActionButton fab_biblioteca1;
    private FloatingActionButton fab_biblioteca2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_acervo, container, false);

        mList = new ArrayList<>();
        mPbLoad = (ProgressBar) view.findViewById(R.id.pb_load);

        mObjAcervo = new objAcervo();
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

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                if (!isLastItem && mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    if (mTemMais) {
                        NetworkConnection.getInstance(getActivity()).execute(AcervoFragment.this, AcervoFragment.class.getName());
                    }
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AcervoAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);


        tv_titulo = (FloatLabel) view.findViewById(R.id.tv_titulo);
        tv_autor = (FloatLabel) view.findViewById(R.id.tv_autor);
        tv_assunto = (FloatLabel) view.findViewById(R.id.tv_assunto);
        tv_resumo = (FloatLabel) view.findViewById(R.id.tv_resumo);

        tv_titulo.getEditText().setTextColor(getResources().getColor(R.color.primary_text));
        tv_resumo.getEditText().setTextColor(getResources().getColor(R.color.primary_text));
        tv_autor.getEditText().setTextColor(getResources().getColor(R.color.primary_text));
        tv_assunto.getEditText().setTextColor(getResources().getColor(R.color.primary_text));

        if(mList == null || mList.size() == 0) {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }


        createFloatButtons();

        return view;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList(TAG_LIST);
        }
    }


    public void callVolleyRequest(){
        NetworkConnection.getInstance(getActivity()).execute(this, AcervoFragment.class.getName());
    }


    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(getActivity(), Detalhe_Acervo_Activity.class);
        intent.putExtra("acervo", mList.get(position));

        // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

            View iv_capa = view.findViewById(R.id.iv_capa);
            View tv_titulo = view.findViewById(R.id.tv_titulo);
            View tv_chamada = view.findViewById(R.id.tv_chamada);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(iv_capa, "element1"),
                    Pair.create(tv_titulo, "element2"),
                    Pair.create(tv_chamada, "element3"));

            getActivity().startActivity( intent, options.toBundle() );
        }
        else{
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onLongPressClickListener(View view, final int position) {


        mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(getActivity(), R.style.MyAlertDialog))
                .setTitle("Reserva de Livro")
                .setMessage(mList.get(position).getDstaq())
                .setPositiveButton("Reservar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SQLiteConn(getActivity()).setReservaAcervo(mList.get(position));
                        Toast.makeText(getActivity(), "Reservado...", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG_LIST, (ArrayList<objAcervo>) mList);
    }

    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(AcervoFragment.class.getName());
    }


    // NETWORK
        @Override
        public WrapObjToNetwork doBefore() {
            if( Util.verifyConnection(getActivity()) ){
                return( new WrapObjToNetwork(
                        mObjAcervo,
                        "get-biblioteca"));
            }
            return null;
        }

        @Override
        public void doAfter(JSONArray jsonArray) {
            mPbLoad.setVisibility(View.GONE );

            if( jsonArray != null ){
                AcervoAdapter adapter = (AcervoAdapter) mRecyclerView.getAdapter();
                Gson gson = new Gson();
                int auxPosition = 0, position;

                if(jsonArray.length() > 0){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.ll_itensBusca).setVisibility(View.INVISIBLE);
                    mTemMais = (jsonArray.length() > 19);

                }

                try{
                    for(int i = 0, tamI = jsonArray.length(); i < tamI; i++){
                        objAcervo acervo = gson.fromJson( jsonArray.getJSONObject( i ).toString(), objAcervo.class );
                        position = auxPosition == 0 ? mList.size() : 0;
                        adapter.addListItem(acervo, position);

                        //mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, position);
                        mObjAcervo.set_id(acervo.get_id());
                        Util.log("LastID Acervo: "+acervo.get_id());
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
            }
        }

    @Override
    public void onClick(View v) {

        if(v.getId() == fab_biblioteca1.getId()){
            if(mRecyclerView.getVisibility()==View.INVISIBLE){
                if (mList == null || mList.size() == 0){
                    mObjAcervo.setDstaq(String.valueOf(tv_titulo.getEditText().getText()));
                    mObjAcervo.setaArtg(String.valueOf(tv_autor.getEditText().getText()));
                    mObjAcervo.setAssun(String.valueOf(tv_assunto.getEditText().getText()));
                    mObjAcervo.setRsumo(String.valueOf(tv_resumo.getEditText().getText()));
                    mObjAcervo.set_id(0);
                    mTemMais = true;
                    callVolleyRequest();
                }
                fabMenu.close(true);
                Util.log("setOnClickListener( mList > 0 )");

            }else{

                Util.log("setOnClickListener( mList = 0 )");
                mRecyclerView.setVisibility(View.INVISIBLE);
                mList.clear();
                mAdapter = new AcervoAdapter(getActivity(), mList);
                mRecyclerView.setAdapter(mAdapter);
                getActivity().findViewById(R.id.ll_itensBusca).setVisibility(View.VISIBLE);
                fabMenu.close(true);
            }
        }else{
            Toast.makeText(getActivity(), "não foi na busca", Toast.LENGTH_SHORT).show();
        }
    }


    public void createFloatButtons(){
        fabMenu = (FloatingActionMenu) getActivity().findViewById(R.id.fab_menu_biblioteca);

        fab_biblioteca1 = (FloatingActionButton) getActivity().findViewById(R.id.fab_biblioteca1);
        fab_biblioteca2 = (FloatingActionButton) getActivity().findViewById(R.id.fab_biblioteca2);

        fab_biblioteca1.setImageResource(R.mipmap.ic_magnify);

        fab_biblioteca2.setImageResource(R.mipmap.ic_directions);
        fab_biblioteca2.setLabelText("Ir à biblioteca");

        fab_biblioteca1.setColorNormalResId(R.color.fab);
        fab_biblioteca1.setColorPressedResId(R.color.fab_pressed);

        fab_biblioteca2.setColorNormalResId(R.color.fab);
        fab_biblioteca2.setColorPressedResId(R.color.fab_pressed);


        fab_biblioteca1.setOnClickListener(this);
        fab_biblioteca2.setOnClickListener(this);
    }
}
