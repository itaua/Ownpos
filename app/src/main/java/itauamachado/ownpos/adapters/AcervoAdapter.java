package itauamachado.ownpos.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.objAcervo;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by viniciusthiengo on 4/5/15.
 */
public class AcervoAdapter extends RecyclerView.Adapter<AcervoAdapter.MyViewHolder> {
    private Context mContext;
    private List<objAcervo> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;


    public AcervoAdapter(Context c, List<objAcervo> l){
        this(c, l, true, true);
    }
    public AcervoAdapter(Context c, List<objAcervo> l, boolean wa, boolean wcl){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        withAnimation = wa;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_biblioteca_card, viewGroup, false);

        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        String capa = mList.get(position).getUrlPhoto();
        String pdf = mList.get(position).getUrlPdf();
        String link = mList.get(position).getUrlLink();
/*
        Util.log("Capa: "+capa);
        Util.log("Pdf: "+pdf);
        Util.log("Link: " + link);
        Util.log(mList.get(position).getUrl());
*/
        myViewHolder.tv_titulo.setText(mList.get(position).getDstaq());
        myViewHolder.tv_chamada.setText(mList.get(position).getChamd());
        String autor = mList.get(position).getaArtg();
        autor = autor + mList.get(position).getaPrin();
        myViewHolder.tv_autor.setText(autor);
        myViewHolder.tv_coautor.setText(mList.get(position).getaScdr());
        myViewHolder.tv_publicacao.setText(mList.get(position).getPbcao());
        myViewHolder.tv_resumo.setText(mList.get(position).getRsumo());



        if(capa!=null){
            Picasso.with(mContext).load(capa).into(myViewHolder.iv_capa);
            //Util.log("image load com picasso: "+capa);
        }else{
            myViewHolder.iv_capa.setBackgroundResource(R.mipmap.ic_file);
            //Util.log("ic_file: "+capa);
        }



        if(withAnimation){
            try{
                YoYo.with(Techniques.FadeIn)
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


    public void addListItem(objAcervo ownAcervo, int position){
        mList.add(position, ownAcervo);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        //ocultos
        public TextView tvo_senac;
        public TextView tvo_fsico;
        public TextView tvo_notas;
        public TextView tvo_assun;
        public TextView tvo_pdico;
        public TextView tvo_serie;

        public ImageView iv_capa;
        public TextView tv_titulo;
        public TextView tv_chamada;
        public TextView tv_autor;
        public TextView tv_coautor;
        public TextView tv_publicacao;
        public TextView tv_resumo;
        //public ImageView ivContextMenu;

        public MyViewHolder(View itemView) {
            super(itemView);


            iv_capa = (ImageView) itemView.findViewById(R.id.iv_capa);
            tvo_senac = (TextView) itemView.findViewById(R.id.tv_senac);
            tvo_fsico = (TextView) itemView.findViewById(R.id.tv_fsico);
            tvo_notas = (TextView) itemView.findViewById(R.id.tv_notas);
            tvo_assun = (TextView) itemView.findViewById(R.id.tv_assun);
            tvo_pdico = (TextView) itemView.findViewById(R.id.tv_pdico);
            tvo_serie = (TextView) itemView.findViewById(R.id.tv_serie);

            tv_titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            tv_chamada = (TextView) itemView.findViewById(R.id.tv_chamada);
            tv_autor = (TextView) itemView.findViewById(R.id.tv_autor);
            tv_coautor = (TextView) itemView.findViewById(R.id.tv_coautor);
            tv_publicacao = (TextView) itemView.findViewById(R.id.tv_publicacao);
            tv_resumo = (TextView) itemView.findViewById(R.id.tv_resumo);

        }

    }
}
