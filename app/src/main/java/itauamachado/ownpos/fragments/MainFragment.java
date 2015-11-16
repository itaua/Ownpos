package itauamachado.ownpos.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import itauamachado.ownpos.MainActivity;
import itauamachado.ownpos.MapaActivity;
import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.SQLiteConn;
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


        Button btn_biblioteca = (Button) view.findViewById(R.id.btn_biblioteca);
        Button btn_turmas = (Button) view.findViewById(R.id.btn_turmas);
        Button btn_noticias = (Button) view.findViewById(R.id.btn_noticias);
        Button btn_mapa = (Button) view.findViewById(R.id.btn_mapa);
        Button btn_cursos = (Button) view.findViewById(R.id.btn_cursos);
        Button btn_outdoor = (Button) view.findViewById(R.id.btn_outdoor);


        btn_biblioteca.setOnClickListener(MainFragment.this);
        btn_turmas.setOnClickListener(MainFragment.this);
        btn_noticias.setOnClickListener(MainFragment.this);
        btn_mapa.setOnClickListener(MainFragment.this);
        btn_cursos.setOnClickListener(MainFragment.this);
        btn_outdoor.setOnClickListener(MainFragment.this);

        if(((MainActivity) getActivity()).getPerfil().equalsIgnoreCase(Util.PERFIL_VISITANTE)){
            btn_turmas.setVisibility(View.GONE); //turmas
        }




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
            ((MainActivity)getActivity()).setTabSelected("BIBLIOTECA");
        }else
        if(t.equalsIgnoreCase("TURMAS")){
            ((MainActivity)getActivity()).setTabSelected("TURMAS");
        }else
        if(t.equalsIgnoreCase("OUTDOOR")){

            String lat = "";
            String lng = "";
            ContentValues contentValues = new SQLiteConn(getActivity()).getLastGeoPosition();
            if(contentValues != null){
                lat = contentValues.getAsString("latitude")+",";
                lng = contentValues.getAsString("longitude")+"&daddr=";
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Util.LINK_OUTDOOR + "?saddr=" +lat+lng+ "-30.0353125,-51.2264457&hl=pt-BR"));
                    startActivity(intent);

        }else
        if(t.equalsIgnoreCase("CURSOS")){
            ((MainActivity)getActivity()).setTabSelected("CURSOS");
        }else
        if(t.equalsIgnoreCase("NOTICIAS")){
            ((MainActivity)getActivity()).setTabSelected("NOTICIAS");
        }
    }



}