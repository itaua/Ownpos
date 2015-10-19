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
import itauamachado.ownpos.domain.objAulas;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

public class TurmasAdapter extends RecyclerView.Adapter<TurmasAdapter.MyViewHolder> {

    private Context mContext;
    private List<objAulas> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;

    public TurmasAdapter(Context c, List<objAulas> l){
        this(c, l, true);
    }
    public TurmasAdapter(Context c, List<objAulas> l, boolean wa){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        withAnimation = wa;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_turma, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        myViewHolder.id_turma.setText(String.valueOf(mList.get(position).getId()));
        myViewHolder.curso.setText(mList.get(position).getCurso());
        myViewHolder.professor.setText(mList.get(position).getProf());
        myViewHolder.nome_turma.setText(mList.get(position).getTurma());


        if(withAnimation){
            try{
                YoYo.with(Techniques.Landing)
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


    public void addListItem(objAulas aulas, int position){
        mList.add(position, aulas);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView id_turma;
        public TextView curso;
        public TextView professor;
        public TextView nome_turma;

        public MyViewHolder(View itemView) {
            super(itemView);
            id_turma = (TextView) itemView.findViewById(R.id.tv_id_turma);
            curso = (TextView) itemView.findViewById(R.id.tv_curso);
            professor = (TextView) itemView.findViewById((R.id.tv_professor));
            nome_turma = (TextView) itemView.findViewById((R.id.tv_nome_turma));
        }
    }
}
