package itauamachado.ownpos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.fragments.Ctx_AulasFragment;
import itauamachado.ownpos.fragments.Ctx_ChatFragment;
import itauamachado.ownpos.fragments.ReservaFragment;


public class TabsContextoAdapter extends FragmentPagerAdapter {


    private String[] mtitles;
    private String mMethodo;


    public TabsContextoAdapter(FragmentManager fm, String mMethodo) {
        super(fm);
        this.mMethodo = mMethodo;
        getItemTitles();
    }

    public void getItemTitles(){
        if(mMethodo.equalsIgnoreCase(Util.TAREFAS)){
            mtitles = Util.TITULOS_TAB_TAREFAS;
        }else{
            mtitles = Util.TITULOS_TAB_AULAS;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        String tab = mtitles[position];

        if(tab.equalsIgnoreCase("TURMA")){
            frag = new Ctx_AulasFragment();
        }else
        if(tab.equalsIgnoreCase("TAREFA")){
            frag = new ReservaFragment();
        }else
        if(tab.equalsIgnoreCase("CHAT")) {
            frag = new Ctx_ChatFragment();
        }
            return frag;
    }

    @Override
    public int getCount() {
        return mtitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ( mtitles[position] );
    }
}
