package itauamachado.ownpos.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objAulas;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;

public class AulasAdapter extends RecyclerView.Adapter<AulasAdapter.MyViewHolder> {

    private Context mContext;
    private List<objAulas> mList;
    private LayoutInflater mLayoutInflater;
    private ContentValues salas;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private boolean withAnimation;
    private boolean isNext = false;
    private int positionNext = -1;


    public AulasAdapter(Context c, List<objAulas> l){
        this(c, l, true);
    }
    public AulasAdapter(Context c, List<objAulas> l, boolean wa){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        withAnimation = wa;
        salas = new ContentValues();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = mLayoutInflater.inflate(R.layout.item_aula, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        //myViewHolder.id_turma.setText(String.valueOf(mList.get(position).getId()));
        myViewHolder.indoor.setText(String.valueOf(mList.get(position).getIndoor()));

        Util.log("id indoor " + String.valueOf(mList.get(position).getIndoor()));
        if( salas.containsKey(String.valueOf(mList.get(position).getIndoor()))){
            myViewHolder.tv_sala.setText(salas.getAsString(String.valueOf(mList.get(position).getIndoor())));
        }else{
            ItensMapa sala = new SQLiteConn(mContext).getDescSala(mList.get(position).getIndoor());
            salas.put(String.valueOf(mList.get(position).getIndoor()), sala.getAndar()+"- "+sala.getDescri());
            myViewHolder.tv_sala.setText(salas.getAsString(String.valueOf(mList.get(position).getIndoor())));
        }

        Util.log(""+mList.get(position).getIndoor());

        //myViewHolder.curso.setText(mList.get(position).getCurso());
        //myViewHolder.professor.setText(mList.get(position).getProf());
        //myViewHolder.nome_turma.setText(mList.get(position).getTurma());
        myViewHolder.dt_aula.setText("Dia: " + mList.get(position).getData());
        myViewHolder.hr_inicio.setText("Horário: "+ mList.get(position).getHr_inicio());
        myViewHolder.hr_fim.setText(" até "+ mList.get(position).getHr_fim());



        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String hoje = sdf.format(new Date());

            if(sdf.parse(hoje).equals(sdf.parse(mList.get(position).getData()))) {
                isNext = true;
                positionNext = position;
                myViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_selected));
                Util.log("datas iguais");
            }else{
                if (sdf.parse(hoje).before(sdf.parse(mList.get(position).getData()))) {
                    if (isNext) {
                        if (position == (positionNext)) {
                            myViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_selected));
                        } else {
                            myViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_future));
                        }
                    } else {
                        isNext = true;
                        positionNext = position;
                        myViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_selected));
                    }
                } else {
                    myViewHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.list_pass));

                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



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

        //public TextView id_turma;
        public TextView indoor;
        public TextView curso;
        //public TextView professor;
        //public TextView nome_turma;
        public TextView tv_sala;
        public TextView dt_aula;
        public TextView hr_inicio;
        public TextView hr_fim;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_list_aula);
            //id_turma = (TextView) itemView.findViewById(R.id.tv_id_turma);
            indoor = (TextView) itemView.findViewById(R.id.tv_indoor);
            //curso = (TextView) itemView.findViewById(R.id.tv_curso);
            //professor = (TextView) itemView.findViewById((R.id.tv_professor));
            //nome_turma = (TextView) itemView.findViewById((R.id.tv_nome_turma));
            dt_aula = (TextView) itemView.findViewById((R.id.tv_dt_aula));
            hr_inicio = (TextView) itemView.findViewById((R.id.tv_hr_inicio));
            hr_fim = (TextView) itemView.findViewById((R.id.tv_hr_fim));
            tv_sala = (TextView) itemView.findViewById(R.id.tv_sala);


        }
    }
}
