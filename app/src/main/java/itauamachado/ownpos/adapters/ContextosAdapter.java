package itauamachado.ownpos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.objContextos;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

public class ContextosAdapter extends RecyclerView.Adapter<ContextosAdapter.MyViewHolder> {

    private Context mContext;
    private List<objContextos> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;


    public ContextosAdapter(Context c, List<objContextos> l){
        this(c, l, true);
    }
    public ContextosAdapter(Context c, List<objContextos> l, boolean wa){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        withAnimation = wa;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_noticias, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {


        myViewHolder.tv_id.setText(mList.get(position).get_id());
        myViewHolder.tv_chave.setText(mList.get(position).getChave());
        myViewHolder.tv_titulo.setText(mList.get(position).getTitulo());
        myViewHolder.tv_autor.setText(mList.get(position).getAutor());
        myViewHolder.tv_local.setText(mList.get(position).getLocal().getAndar()+"::"+mList.get(position).getLocal().getBuild());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        myViewHolder.tv_inicio.setText(sdf.format(mList.get(position).getInicio()));
        myViewHolder.tv_termino.setText(sdf.format(mList.get(position).getTermino()));
        myViewHolder.tv_obs.setText(mList.get(position).getObs());

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


    public void addListItem(objContextos contextos, int position){
        mList.add(position, contextos);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        public TextView tv_id;
        public TextView tv_chave;
        public TextView tv_titulo;
        public TextView tv_autor;
        public TextView tv_local;
        public TextView tv_inicio;
        public TextView tv_termino;
        public TextView tv_obs;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_chave = (TextView) itemView.findViewById(R.id.tv_chave);
            tv_titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            tv_autor = (TextView) itemView.findViewById(R.id.tv_autor);
            tv_local = (TextView) itemView.findViewById(R.id.tv_ItensMapa);
            tv_inicio = (TextView) itemView.findViewById(R.id.tv_inicio);
            tv_termino = (TextView) itemView.findViewById(R.id.tv_termino);
            tv_obs = (TextView) itemView.findViewById(R.id.tv_obs);
        }
    }
}
