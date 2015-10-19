package itauamachado.ownpos.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import itauamachado.ownpos.extras.Util;
import itauamachado.ownpos.fragments.AcervoFragment;
import itauamachado.ownpos.fragments.TarefasFragment;
import itauamachado.ownpos.fragments.CursosEadFragment;
import itauamachado.ownpos.fragments.CursosFragment;
import itauamachado.ownpos.fragments.MainFragment;
import itauamachado.ownpos.fragments.NoticiasFragment;
import itauamachado.ownpos.fragments.AgendaFragment;
import itauamachado.ownpos.fragments.TurmasFragment;

/**
 * Created by viniciusthiengo on 5/18/15.
 */
public class TabsMainAdapter extends FragmentPagerAdapter {

    private Boolean mIconView = false;

    private Context mContext;
    private String[] mtitles;
    //private int[] micons = null;
    //private int heightIcon;
    private String mPerfil;


    public TabsMainAdapter(FragmentManager fm, Context c, String mPerfil) {
        super(fm);
        mContext = c;
        this.mPerfil = mPerfil;
        getItemTitles();
        //double scale = c.getResources().getDisplayMetrics().density;
        //heightIcon = (int)( 24 * scale + 0.5f );
    }

    public void getItemTitles(){
        if(mPerfil.equalsIgnoreCase(Util.PERFIL_VISITANTE)){
            mtitles = Util.TITULOS_TAB_ANONIMO;
        }else{
            mtitles = Util.TITULO_TAB;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        String tab = mtitles[position];

        if(tab == "MENU"){
            frag = new MainFragment();
        }else
        if(tab == "BIBLIOTECA"){
            frag = new AcervoFragment();
        }else
        if(tab=="NOTICIAS"){
            frag = new NoticiasFragment();
        }else
        if(tab=="CURSOS") {
            frag = new CursosFragment();
        }else if(tab=="CURSOS EAD") {
            frag = new CursosEadFragment();
        }else
        if(tab=="TAREFAS"){
            frag = new TarefasFragment();
        }else
        if(tab=="TURMAS"){
            frag = new TurmasFragment();
        }else
        if(tab=="AGENDA"){
            frag = new AgendaFragment();
        }
        return frag;
    }

    @Override
    public int getCount() {
        return mtitles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        /*
        if(mIconView) {

            //TITULO Icone
            Drawable d = mContext.getResources().getDrawable(micons[position]);
            d.setBounds(0, 0, heightIcon, heightIcon);
            ImageSpan is = new ImageSpan(d);

            SpannableString sp = new SpannableString(" ");
            sp.setSpan(is, 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return (sp);
        }else{
            return ( mtitles[position] );
        }
        */
        return ( mtitles[position] );

    }
}
