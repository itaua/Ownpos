package itauamachado.ownpos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.objEventos;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> {

    private Context mContext;
    private List<objEventos> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;


    public AgendaAdapter(Context c, List<objEventos> l){
        this(c, l, true);
    }
    public AgendaAdapter(Context c, List<objEventos> l, boolean wa){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        withAnimation = wa;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_agenda, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {


        myViewHolder.id_evento.setText(String.valueOf(mList.get(position).getId()));
        myViewHolder.indoor.setText(String.valueOf(mList.get(position).getIndoor()));
        myViewHolder.nome_autor.setText(mList.get(position).getAutor());
        myViewHolder.perfil_autor.setText(mList.get(position).getPerfil());
        myViewHolder.titulo.setText(mList.get(position).getTitulo());
        myViewHolder.descricao.setText(mList.get(position).getDescricao());
        myViewHolder.dt_inicio.setText(mList.get(position).getInicio());
        myViewHolder.dt_fim.setText(mList.get(position).getFim());
        myViewHolder.integrantes.setText(mList.get(position).getParticipantes());



        if(withAnimation){
            try{
                YoYo.with(Techniques.SlideInDown)
                        .duration(700)
                        .playOn(myViewHolder.itemView);
            }
            catch(Exception e){}
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(objEventos evento, int position){
        mList.add(position, evento);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView id_evento;
        public TextView indoor;
        public TextView nome_autor;
        public TextView perfil_autor;
        public TextView titulo;
        public TextView descricao;
        public TextView dt_inicio;
        public TextView dt_fim;
        public TextView integrantes;

        public MyViewHolder(View itemView) {
            super(itemView);

            id_evento = (TextView) itemView.findViewById(R.id.tv_id_evento);
            indoor = (TextView) itemView.findViewById(R.id.tv_indoor);
            nome_autor = (TextView) itemView.findViewById(R.id.tv_nome);
            perfil_autor = (TextView) itemView.findViewById(R.id.tv_perfil);
            titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            descricao = (TextView) itemView.findViewById(R.id.tv_descricao);
            dt_inicio = (TextView) itemView.findViewById(R.id.tv_inicio);
            dt_fim = (TextView) itemView.findViewById(R.id.tv_fim);
            integrantes = (TextView) itemView.findViewById(R.id.tv_integrantes);

        }
    }
}
