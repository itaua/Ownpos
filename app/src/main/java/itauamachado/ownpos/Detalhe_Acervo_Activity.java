package itauamachado.ownpos;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;


import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.domain.objAcervo;
import itauamachado.ownpos.extras.Util;
import me.drakeet.materialdialog.MaterialDialog;


public class Detalhe_Acervo_Activity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private objAcervo acervo;
    private MaterialDialog mMaterialDialog;
    private TextView tvResumo;
    private ViewGroup mRoot;
    private boolean isUsingTransition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TRANSITIONS
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

                TransitionInflater inflater = TransitionInflater.from( this );
                Transition transition = inflater.inflateTransition( R.transition.transitions );
                getWindow().setSharedElementEnterTransition(transition);

                Transition transition1 = getWindow().getSharedElementEnterTransition();
                transition1.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        isUsingTransition = true;
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            TransitionManager.beginDelayedTransition(mRoot, new Slide());
                        }
                        tvResumo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
            }

        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_acervo_detalhe);

        if(savedInstanceState != null){
            acervo = savedInstanceState.getParcelable("acervo");
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getParcelable("acervo") != null) {
                acervo = getIntent().getExtras().getParcelable("acervo");
            }
            else {
                Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(acervo.getDstaq());

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle(acervo.getDstaq());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_left);


        mRoot = (ViewGroup) findViewById(R.id.ll_tv_description);
        tvResumo = (TextView) findViewById(R.id.tv_resumo);
        SimpleDraweeView ivCapa = (SimpleDraweeView) findViewById(R.id.iv_capa);
        final TextView tvModel = (TextView) findViewById(R.id.tv_titulo);
        TextView tvBrand = (TextView) findViewById(R.id.tv_chamada);

        String capa = acervo.getUrlPhoto();

        if(capa!=null){
            Uri uri = Uri.parse(acervo.getUrlPhoto());
            DraweeController dc = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .setOldController( ivCapa.getController() )
                    .build();
            ivCapa.setController(dc);
        }




        tvModel.setText(acervo.getDstaq());
        tvBrand.setText(acervo.getChamd());
        tvResumo.setText(acervo.getRsumo());
        tvResumo.setVisibility(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || savedInstanceState != null || !isUsingTransition ? View.VISIBLE : View.INVISIBLE);

        ImageView iv_pdf = (ImageView) findViewById(R.id.iv_pdf);
        final String link_pdf = acervo.getUrlPdf();
        if(link_pdf!=null){

            iv_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link_pdf));
                    startActivity(intent);
                }
            });

        }else{
            iv_pdf.setVisibility(View.INVISIBLE);
        }


        ImageView iv_link = (ImageView) findViewById(R.id.iv_link);
        final String link_link = acervo.getUrlLink();
        if(link_link!=null){

            iv_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link_link));
                    startActivity(intent);
                }
            });

        }else{
            iv_pdf.setVisibility(View.INVISIBLE);
        }

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(Detalhe_Acervo_Activity.this, R.style.MyAlertDialog))
                        .setTitle("Reserva de Livro")
                        .setMessage(acervo.getDstaq())
                        .setPositiveButton("marcar interesse", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SQLiteConn(Detalhe_Acervo_Activity.this).setReservaAcervo(acervo);
                                Toast.makeText(Detalhe_Acervo_Activity.this, "Reservado..", Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("acervo", acervo);
    }

    @Override
    public void onBackPressed() {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionManager.beginDelayedTransition(mRoot, new Slide());
            tvResumo.setVisibility(View.INVISIBLE);
        }

        super.onBackPressed();
    }
}
