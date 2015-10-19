package itauamachado.ownpos.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.ContextMenuItem;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by itauafm on 26/09/2015.
 */
public class ItemAndarAdapter extends RecyclerView.Adapter<ItemAndarAdapter.MyViewHolder> {
    private Context mContext;
    private List<ItensMapa> mItensMapaList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;;
    private boolean withAnimation;
    private float scale;


    public ItemAndarAdapter(Context c, List<ItensMapa> l){
        this(c, l, true);
    }
    public ItemAndarAdapter(Context c, List<ItensMapa> l, boolean wa){
        mContext = c;
        mItensMapaList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        withAnimation = wa;
        scale = mContext.getResources().getDisplayMetrics().density;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_mapa_itens, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.tv_titulo.setText(mItensMapaList.get(position).getTitulo());
        String description = mItensMapaList.get(position).getDescri() != null ? mItensMapaList.get(position).getDescri() : "";
        myViewHolder.tv_detalhe.setText(description);

        myViewHolder.iv_tipo.setVisibility(View.INVISIBLE);

       // Util.log(mItensMapaList.get(position).getTitulo());

        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Biblioteca")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_biblioteca);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Atendimento")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_atendimento);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Banheiro")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_banheiro);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Cantina")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_cantina);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Rampa")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_acesso);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Informações")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_info);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Auditório")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_auditorio);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Sala de Aula")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_aula);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Escadas")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_escada);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Elevador")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_elevador);
        } else
        if (mItensMapaList.get(position).getTitulo().equalsIgnoreCase("Entrada Principal")) {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_entrada);
        } else {
            myViewHolder.iv_tipo.setVisibility(View.VISIBLE);
            myViewHolder.iv_tipo.setBackgroundResource(R.drawable.mk_atendimento);
        }



       // Util.log(mItensMapaList.get(position).getTitulo()+" -  "+description);


        if(withAnimation){
            try{
                YoYo.with(Techniques.Bounce)
                        .duration(500)
                        .playOn(myViewHolder.itemView);
            }
            catch(Exception e){}
        }
    }

    @Override
    public int getItemCount() {
        return mItensMapaList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(ItensMapa c, int position){
        mItensMapaList.add(position, c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mItensMapaList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView iv_tipo;
        public TextView tv_titulo;
        public TextView tv_detalhe;
        public ImageView ivContextMenu;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            tv_detalhe = (TextView) itemView.findViewById(R.id.tv_resumo);
            ivContextMenu = (ImageView) itemView.findViewById(R.id.iv_context_menu);
            iv_tipo = (ImageView) itemView.findViewById(R.id.iv_tipo);

            if( ivContextMenu != null ){
                ivContextMenu.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {



            List<ContextMenuItem> itens = new ArrayList<>();
            itens.add(new ContextMenuItem(R.mipmap.ic_directions, "chegar até aqui..."));
            itens.add(new ContextMenuItem(R.mipmap.ic_map_marker_radius, "mostrar no mapa..."));
            itens.add( new ContextMenuItem( R.mipmap.ic_bell_ring, "Telefonar" ) );

            ContextMenuAdapter adapter = new ContextMenuAdapter( mContext, itens );

            ListPopupWindow listPopupWindow = new ListPopupWindow(mContext);
            listPopupWindow.setAdapter( adapter );
            listPopupWindow.setAnchorView(ivContextMenu);
            listPopupWindow.setWidth((int) (240 * scale + 0.5f));
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext, position+"", Toast.LENGTH_SHORT).show();
                }
            });
            listPopupWindow.setModal(true);
            listPopupWindow.getBackground().setAlpha(0);
            listPopupWindow.show();
        }
    }
}

