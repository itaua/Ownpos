package itauamachado.ownpos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import itauamachado.ownpos.adapters.TabsContextoAdapter;
import itauamachado.ownpos.extras.SlidingTabLayout;
import itauamachado.ownpos.extras.Util;


public class ContextosActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private FloatingActionMenu fab_menu;

    private String mMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contextos);

        montaLayout();
        montaTab();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }


    protected void montaTab(){

        mMethod = getIntent().getStringExtra(Util.METHOD);
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        mViewPager.setAdapter(new TabsContextoAdapter(getSupportFragmentManager(), mMethod));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        mSlidingTabLayout.setHorizontalFadingEdgeEnabled(true); //coloca faiding na tab que esta saindo da visão da tab
        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /**
                int delay = 400;
                switch (position) {
                    case 0:

                        fab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu_tarefas);
                        fab_menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(ContextosActivity.this, R.anim.jump_from_down));
                        fab_menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(ContextosActivity.this, R.anim.jump_to_down));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fab_menu.showMenu(true);
                            }
                        }, delay);

                        break;
                    case 1:
                        fab_menu.close(true);
                        fab_menu.hideMenu(true);
                        break;
                }
                 **/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    protected void montaLayout(){
        // TOOLBAR
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_left);

    }
}
