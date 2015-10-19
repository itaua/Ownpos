package itauamachado.ownpos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.Andares;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

public class MapaAdapter extends RecyclerView.Adapter<MapaAdapter.MyViewHolder> {

    private Context mContext;
    private List<Andares> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;

    public MapaAdapter(Context c, List<Andares> l){
        this(c, l, true);
    }
    public MapaAdapter(Context c, List<Andares> l, boolean wa){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        withAnimation = wa;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_mapa_andares, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        //Util.log(mList.get(position).getAndar());

        myViewHolder.andar.setText(mList.get(position).getAndar());
        myViewHolder.titulo.setText(mList.get(position).getTitulo());
        myViewHolder.descri.setText(mList.get(position).getDescricao());

        //for(ItensMapa itensMapa: mList.get(position).getItensMapa()){
/*
        Util.log("Andar:" +mList.get(position).getAndar());
        Util.log("Titulo:" +mList.get(position).getTitulo());
        Util.log("Descricao:" +mList.get(position).getDescricao());
        Util.log("ItemMapa tamanho: " + mList.get(position).getItensMapa().size());
        Util.log("banheiro:" +mList.get(position).isTemBanheiro());
        Util.log("Atendimento:" +mList.get(position).isTemAtendimento());
        Util.log("Info:" +mList.get(position).isTemInfo());
        Util.log("Comida:" +mList.get(position).isTemComida());
        Util.log("Rampa:" +mList.get(position).isTemRampa());
        Util.log("Biblioteca:" +mList.get(position).isTemBiblioteca());
        Util.log("Auditoio:" +mList.get(position).isTemAuditorio());
        Util.log("Aula:" +mList.get(position).isTemAula());
*/
            if(mList.get(position).isTemBanheiro()){
                myViewHolder.banheiros.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemAtendimento()){
                myViewHolder.atendimento.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemInfo()){
                myViewHolder.info.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemComida()){
                myViewHolder.comida.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemRampa()){
                myViewHolder.rampa.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemBiblioteca()){
                myViewHolder.biblioteca.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemAuditorio()){
                myViewHolder.auditorio.setVisibility(View.VISIBLE);
            }
            if(mList.get(position).isTemAula()) {
                myViewHolder.aula.setVisibility(View.VISIBLE);
            }

        //}

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


    public void addListItem(Andares mapas, int position){
        mList.add(position, mapas);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView andar;
        public TextView titulo;
        public TextView descri;
        public ImageView banheiros;
        public ImageView atendimento;
        public ImageView info;
        public ImageView comida;
        public ImageView rampa;
        public ImageView biblioteca;
        public ImageView auditorio;
        public ImageView aula;

        public MyViewHolder(View itemView) {
            super(itemView);
            andar = (TextView) itemView.findViewById(R.id.tv_andar);
            titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            descri = (TextView) itemView.findViewById(R.id.tv_description);
            banheiros = (ImageView) itemView.findViewById((R.id.iv_banheiro));
            atendimento = (ImageView) itemView.findViewById((R.id.iv_atendimento));
            info = (ImageView) itemView.findViewById((R.id.iv_info));
            comida = (ImageView) itemView.findViewById((R.id.iv_comida));
            rampa = (ImageView) itemView.findViewById((R.id.iv_rampa));
            biblioteca = (ImageView) itemView.findViewById((R.id.iv_biblioteca));
            auditorio = (ImageView) itemView.findViewById((R.id.iv_auditorio));
            aula = (ImageView) itemView.findViewById((R.id.iv_aula));

        }
    }
}
