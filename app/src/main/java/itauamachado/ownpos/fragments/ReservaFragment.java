package itauamachado.ownpos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.adapters.AcervoAdapter;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objAcervo;
import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.interfaces.RecyclerViewOnClickListenerHack;
import me.drakeet.materialdialog.MaterialDialog;

public class ReservaFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<objAcervo> mList;

    private MaterialDialog mMaterialDialog;

    private FloatingActionMenu fabMenu;
    private com.github.clans.fab.FloatingActionButton fab_tarefa1;
    private com.github.clans.fab.FloatingActionButton fab_tarefa2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tarefas, container, false);

        mList = new ArrayList<>();

        mList = new SQLiteConn(getActivity()).getReserva();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new Util.RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        AcervoAdapter adapter = new AcervoAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);
        createFloatButtons();
        return view;
    }


    @Override
    public void onClickListener(View view, int position) {

    }

    @Override
    public void onLongPressClickListener(View view, final int position) {

        mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(getActivity(), R.style.MyAlertDialog))
                .setTitle("Retirar da reserva")
                .setMessage("Esta ação fará com que o aplicativo não procure pela disponibilidade do Livro.")
                .setPositiveButton("Excluir", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SQLiteConn(getActivity()).delReserva(mList.get(position));
                        Toast.makeText(getActivity(), "Excluído...", Toast.LENGTH_SHORT).show();
                        mList.remove(position);
                        mRecyclerView.removeViewAt(position);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Fechar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }


    public void createFloatButtons(){
        fabMenu = (FloatingActionMenu) getActivity().findViewById(R.id.fab_menu_tarefas);

        fab_tarefa1 = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.fab_tarefa1);
        fab_tarefa2 = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.fab_tarefa2);

        fab_tarefa1.setImageResource(R.mipmap.ic_plus);
        fab_tarefa1.setLabelText("Nova tarefa");
        fab_tarefa1.setLabelVisibility(View.VISIBLE);


        fab_tarefa2.setImageResource(R.mipmap.ic_magnify);
        fab_tarefa2.setLabelText("Procurar tarefa");
        fab_tarefa2.setLabelVisibility(View.VISIBLE);

        fab_tarefa1.setColorNormalResId(R.color.fab);
        fab_tarefa1.setColorPressedResId(R.color.fab_pressed);

        fab_tarefa2.setColorNormalResId(R.color.fab);
        fab_tarefa2.setColorPressedResId(R.color.fab_pressed);

    }


}
