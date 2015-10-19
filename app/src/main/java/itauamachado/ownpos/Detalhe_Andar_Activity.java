package itauamachado.ownpos;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import itauamachado.ownpos.adapters.ItemAndarAdapter;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.domain.Andares;
import itauamachado.ownpos.domain.MessageEB;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;


public class Detalhe_Andar_Activity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private List<ItensMapa> mItensAndar;
    private RecyclerView mRecyclerView;
    private ItemAndarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_andar_detalhe);

        int andar = Integer.parseInt(getIntent().getStringExtra("andar"));

        mItensAndar = new ArrayList<>();
        mItensAndar = new SQLiteConn(this).getItensAndar(String.valueOf(andar));


        TextView tv_titulo = (TextView) findViewById(R.id.tv_titulo);
        TextView tv_resumo = (TextView) findViewById(R.id.tv_detalhe);

        tv_titulo.setText(Util.andar_titulos[andar-1]);
        tv_resumo.setText(Util.andar_desc[andar-1]);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(this, mRecyclerView, this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        Util.log("Lista de detalhes do andar: " + mItensAndar.size());
        mAdapter = new ItemAndarAdapter(this, mItensAndar);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onClickListener(View view, int position) {
        MessageEB mb = new MessageEB();
        mb.setmTag(MessageEB.TAG_MAPA_INDOOR);
        mb.setPontoB(mItensAndar.get(position));
        /*
        Util.log("enviado o pontoB no messageBus ("+
                "Titulo: " + mItensAndar.get(position).getTitulo() +" - "+
                "Andar: " + mItensAndar.get(position).getAndar()  +" - "+
                "Point: " + mItensAndar.get(position).getWidth()+", "+
                            mItensAndar.get(position).getHeight()+")");
                            */
        EventBus.getDefault().post(mb);
        finish();
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        //Toast.makeText(this, "Me segurar não irá te levar a lugar algum", Toast.LENGTH_SHORT).show();
    }
}
