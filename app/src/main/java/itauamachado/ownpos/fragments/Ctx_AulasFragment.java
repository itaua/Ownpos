
package itauamachado.ownpos.fragments;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.MapaActivity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.AulasAdapter;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objAulas;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;


public class Ctx_AulasFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    //itens layout
    private SlidingUpPanelLayout mLayout;

    //Views Layout
    private RecyclerView mRecyclerView;
    private AulasAdapter mAdapter;
    private List<objAulas> mList;




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_turma, container, false);

        initialiseLayout(view);
        return view;
    }


    private void initialiseLayout(View view) {


        TextView tv_titulo = (TextView) view.findViewById(R.id.tv_titulo);
        TextView tv_curso = (TextView) view.findViewById(R.id.tv_curso);
        TextView tv_professor = (TextView) view.findViewById(R.id.tv_prof);
        TextView tv_site = (TextView) view.findViewById(R.id.tv_site);


        String turma = getActivity().getIntent().getStringExtra("turma");
        tv_titulo.setText(getActivity().getIntent().getStringExtra("titulo"));
        tv_curso.setText(getActivity().getIntent().getStringExtra("curso"));
        tv_professor.setText(getActivity().getIntent().getStringExtra("prof"));
        tv_site.setText(getActivity().getIntent().getStringExtra("site"));

        Util.log(getActivity().getIntent().getStringExtra("turma"));
        Util.log(getActivity().getIntent().getStringExtra("titulo"));
        Util.log(getActivity().getIntent().getStringExtra("curso"));
        Util.log(getActivity().getIntent().getStringExtra("prof"));
        Util.log(getActivity().getIntent().getStringExtra("site"));


        mList = new SQLiteConn(getActivity()).getAulas(turma);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(getActivity(), mRecyclerView, this));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new AulasAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);

        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.7f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        posicionaLista();
    }

    public void posicionaLista(){

        try {
            int aux = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String hoje = sdf.format(new Date());
            for(int i = 0; i < mList.size(); i++ ){
                if(sdf.parse(hoje).equals(sdf.parse(mList.get(i).getData()))){
                    mRecyclerView.scrollToPosition(i);
                    aux = i;
                    break;
                }else {
                    if (sdf.parse(hoje).before(sdf.parse(mList.get(i).getData()))) {
                        mRecyclerView.scrollToPosition(i);
                        aux = i;
                        break;
                    } else {
                        Util.log("hoje: " + hoje + " data: " + mList.get(i).getData() + "after");
                    }
                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //RecycleView Methods
    @Override
    public void onClickListener(View view, int position) {

        Intent intent = new Intent(getActivity(), MapaActivity.class);
        intent.putExtra(Util.METHOD, Util.NAVEGACAO);
        intent.putExtra("pontoB", mList.get(position).getIndoor());
        startActivity(intent);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {


    }
}
