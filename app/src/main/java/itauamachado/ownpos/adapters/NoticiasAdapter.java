package itauamachado.ownpos.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;

import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.objNoticias;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.MyViewHolder> {

    private Context mContext;
    private List<objNoticias> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;


    public NoticiasAdapter(Context c, List<objNoticias> l){
        this(c, l, true);
    }
    public NoticiasAdapter(Context c, List<objNoticias> l, boolean wa){
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

        myViewHolder.tv_titulo.setText(mList.get(position).getDiames()+" :: "+mList.get(position).getTitulo());
        myViewHolder.tv_description.setText(mList.get(position).getDescri());

        ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
            }
        };



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


    public void addListItem(objNoticias noticias, int position){
        mList.add(position, noticias);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_titulo;
        public TextView tv_description;

        public MyViewHolder(View itemView) {
            super(itemView);


            tv_titulo = (TextView) itemView.findViewById(R.id.tv_titulo);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);


        }
    }
}
