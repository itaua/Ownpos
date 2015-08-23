
package itauamachado.ownpos.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import itauamachado.ownpos.R;

/**Created by Itauá on 23/08/2015.*/
public class MapaIndoorFragment extends Fragment {

    private ImageView mapaIndoor;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map_fragment_indoor, container, false);
        mapaIndoor = (ImageView) view.findViewById(R.id.mapaIndoor);
        mapaIndoor.setImageResource(R.mipmap.mapa_terreo);
        Log.i("Ownpos_Log", "onCreateView  mapaIndoorFragment()");
        return view;
    }
}
