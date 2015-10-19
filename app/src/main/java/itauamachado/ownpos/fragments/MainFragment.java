package itauamachado.ownpos.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import itauamachado.ownpos.MapaActivity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.extras.Util;


public class MainFragment extends Fragment implements View.OnClickListener{


    private FloatingActionButton fab_btn1;
    private FloatingActionButton fab_btn2;
    private FloatingActionButton fab_btn3;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button btn_b = (Button) view.findViewById(R.id.btn_biblioteca);
        Button btn_c = (Button) view.findViewById(R.id.btn_cursos);
        Button btn_n = (Button) view.findViewById(R.id.btn_noticias);
        Button btn_m = (Button) view.findViewById(R.id.btn_mapa);

        btn_b.setOnClickListener(MainFragment.this);
        btn_c.setOnClickListener(MainFragment.this);
        btn_n.setOnClickListener(MainFragment.this);
        btn_m.setOnClickListener(MainFragment.this);

        fab_btn1 = (FloatingActionButton) getActivity().findViewById(R.id.fab_contexto1);
        fab_btn2 = (FloatingActionButton) getActivity().findViewById(R.id.fab_contexto2);
        fab_btn3 = (FloatingActionButton) getActivity().findViewById(R.id.fab_contexto3);
        fab_btn1.setLabelText("Compromisso agendado");
        fab_btn2.setLabelText("Livro disponível");
        fab_btn3.setLabelText("Sala de aula");


        fab_btn1.setImageResource(R.mipmap.ic_calendar_clock);
        fab_btn2.setImageResource(R.mipmap.ic_book_open);
        fab_btn3.setImageResource(R.mipmap.ic_school);

        fab_btn1.setColorNormalResId(R.color.fab);
        fab_btn1.setColorPressedResId(R.color.fab_pressed);
        fab_btn1.setColorRippleResId(R.color.fab_pressed);

        fab_btn2.setColorNormalResId(R.color.fab);
        fab_btn2.setColorPressedResId(R.color.fab_pressed);
        fab_btn2.setColorRippleResId(R.color.fab_pressed);

        fab_btn3.setColorNormalResId(R.color.fab);
        fab_btn3.setColorPressedResId(R.color.fab_pressed);
        fab_btn3.setColorRippleResId(R.color.fab_pressed);

        return view;
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(getActivity(), ((Button) v).getText(), Toast.LENGTH_SHORT).show();
        String t = (String) ((Button) v).getText();

        if (t.equalsIgnoreCase("MAPA")){
            Intent intent = new Intent(getActivity(), MapaActivity.class);
            startActivity(intent);
        }else
        if(t.equalsIgnoreCase("BIBLIOTECA")){
                //Util.getWiFi(getActivity());
        }else
        if(t.equalsIgnoreCase("TURMAS")){
            Intent intent = new Intent(getActivity(), Ctx_AulasFragment.class);
            startActivity(intent);
        }

    }
}