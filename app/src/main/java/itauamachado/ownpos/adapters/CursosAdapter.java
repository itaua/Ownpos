package itauamachado.ownpos.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.objCursos;
import itauamachado.ownpos.extras.Util;

/**
 * Created by itauafm on 12/09/2015.
 */
public class CursosAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private String[] mGroups = {""};
    private ArrayList<List> mChilds;

    public static final String[] GRUPOS_CURSO_SENAC ={"Livres","Técnicos","Graduação","Pós-Graduação","Extensão","Pronatec"};
    public static final String[] GRUPOS_CURSO_EAD={"Livres","Técnicos","Graduação","Pós-Graduação","Extensão","Aprendizagem","Idiomas"};


    public CursosAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CursosAdapter(Context context, List<objCursos> cursos, boolean ead) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(ead) {
            preencherDadosEad(cursos);
        }else{
            preencherDados(cursos);
        }
    }

    public void preencherDados(List<objCursos> cursos){
        mGroups = GRUPOS_CURSO_SENAC;
        List<objCursos> Child0 = new ArrayList<>();
        List<objCursos> Child1 = new ArrayList<>();
        List<objCursos> Child2 = new ArrayList<>();
        List<objCursos> Child3 = new ArrayList<>();
        List<objCursos> Child4 = new ArrayList<>();
        List<objCursos> Child5 = new ArrayList<>();

        for (objCursos curso: cursos) {
            if (curso.getTipo().equalsIgnoreCase("livres")){
                Child0.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("tecnicos")){
                Child1.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("graduacao")){
                Child2.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("posgraduacao")){
                Child3.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("extensao")){
                Child4.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("pronatec")){
                Child5.add(curso);
            }
        }
        mChilds = new ArrayList<>();
        mChilds.add(Child0);
        mChilds.add(Child1);
        mChilds.add(Child2);
        mChilds.add(Child3);
        mChilds.add(Child4);
        mChilds.add(Child5);
    }

    public void preencherDadosEad(List<objCursos> cursos) {

        mGroups = GRUPOS_CURSO_EAD;

        List<objCursos> Child0 = new ArrayList<>();
        List<objCursos> Child1 = new ArrayList<>();
        List<objCursos> Child2 = new ArrayList<>();
        List<objCursos> Child3 = new ArrayList<>();
        List<objCursos> Child4 = new ArrayList<>();
        List<objCursos> Child5 = new ArrayList<>();
        List<objCursos> Child6 = new ArrayList<>();

        for (objCursos curso: cursos) {
            if (curso.getTipo().equalsIgnoreCase("livres")){
                Child0.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("tecnicos")){
                Child1.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("graduacao")){
                Child2.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("posgraduacao")){
                Child3.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("extensao")){
                Child4.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("aprendizagem")){
                Child5.add(curso);
            }else
            if (curso.getTipo().equalsIgnoreCase("idiomas")) {
                Child6.add(curso);
            }
        }
        mChilds = new ArrayList<>();
        mChilds.add(Child0);
        mChilds.add(Child1);
        mChilds.add(Child2);
        mChilds.add(Child3);
        mChilds.add(Child4);
        mChilds.add(Child5);
        mChilds.add(Child6);
    }

    @Override
    public int getGroupCount() {
        return mGroups.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_curso_group, parent, false);
        }

        //final ImageView image = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_item_image);
        //image.setImageResource(mGroupDrawables[groupPosition]);

        final TextView text = (TextView) convertView.findViewById(R.id.sample_activity_list_group_item_text);
        text.setText(mGroups[groupPosition]);

        //final ImageView expandedImage = (ImageView) convertView.findViewById(R.id.sample_activity_list_group_expanded_image);
        //final int resId = isExpanded ? R.mipmap.ic_bell_ring: R.mipmap.ic_bell_ring;
        //expandedImage.setImageResource(resId);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChilds.get(groupPosition).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  ((objCursos) mChilds.get(groupPosition).get(childPosition)).getCurso();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_cursos, parent, false);
        }
        objCursos curso = (objCursos) mChilds.get(groupPosition).get(childPosition);
        final TextView tv_curso = (TextView) convertView.findViewById(R.id.tv_curso_name);
        final TextView tv_area = (TextView) convertView.findViewById(R.id.tv_curso_area);
        tv_curso.setText(curso.getCurso());
        tv_area.setText((curso.getArea()));

        ImageView iv_curso_psg = (ImageView) convertView.findViewById(R.id.iv_curso_psg);
        ImageView iv_curso_aberto = (ImageView) convertView.findViewById(R.id.iv_curso_aberto);
        ImageView iv_curso_gratuito = (ImageView) convertView.findViewById(R.id.iv_curso_gratuito);
        ImageView iv_curso_novo = (ImageView) convertView.findViewById(R.id.iv_curso_novo);



        iv_curso_aberto.setVisibility(View.INVISIBLE);
        iv_curso_novo.setVisibility(View.INVISIBLE);
        iv_curso_gratuito.setVisibility(View.INVISIBLE);
        iv_curso_psg.setVisibility(View.INVISIBLE);




        String[] imagens = curso.getImg().split(";");
        for (String link: imagens){
            if(link != ""){
                link= link.replace("util/img", "");
                link = link.replace("imagens/", "");
                link = link.replace("/", "");

                if((link.equalsIgnoreCase("btnInscricoesLaranja.png"))|| (link.equalsIgnoreCase("bt_turmas_abertas.png"))){
                    iv_curso_aberto.setVisibility(View.VISIBLE);
                }else
                if(link.equalsIgnoreCase("btnNovoAzul.png")){
                    iv_curso_novo.setVisibility(View.VISIBLE);
                }else
                if(link.equalsIgnoreCase("btnGratuitoCinza.png")){
                    iv_curso_gratuito.setVisibility(View.VISIBLE);
                }else
                if(link.equalsIgnoreCase("rotulo_psg.jpg")){
                    iv_curso_psg.setVisibility(View.VISIBLE);
                }

            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String link = ((objCursos) mChilds.get(groupPosition).get(childPosition)).getUrlbase();
                link = link + ((objCursos) mChilds.get(groupPosition).get(childPosition)).getHref();
                Intent intent = null;
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}