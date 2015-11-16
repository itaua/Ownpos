

package itauamachado.ownpos.extras.mapa.views;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

import itauamachado.ownpos.R;
import itauamachado.ownpos.domain.ItensMapa;
import itauamachado.ownpos.domain.SQLiteConn;
import itauamachado.ownpos.extras.Util;


public class PinView extends SubsamplingScaleImageView {

    //private PointF sPin;
    private Bitmap pin;
    private Bitmap dotPath;

    private  ItensMapa pontoEntrada = null;
    private ItensMapa pontoA = null;
    private ItensMapa pontoB = null;

    private int mAndar;

    //marcação no mapa
    private Bitmap origem, destino;
    private Bitmap atendimento, info, entrada, elevador, banheiro, escada, aula, rampa, cantina, ajuda;
    private Bitmap tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9, base;
    private PointF ownpos;

    private List<PointF> mPointsPath = new ArrayList<>();
    private List<PointF> mPointMapa = new ArrayList<>();
    private List<ItensMapa> itensMapaList = new ArrayList<>();

    private boolean desenharPath = false;

    //dados do ponto pressionado.
    private static double RAIO_CLICK = 50.0;


    public void setPointsPath(int andar){
        andar++;
        mAndar = andar;
        itensMapaList = new ArrayList<>();
        mPointMapa = new ArrayList<>();
        mPointsPath = new ArrayList<>();

        itensMapaList = new SQLiteConn(getContext()).getItensAndar(String.valueOf(andar));
        mPointsPath = new SQLiteConn(getContext()).getPathAndar(andar);

        for(ItensMapa item: itensMapaList){
            PointF pointF = new PointF(Float.valueOf(item.getWidth()), Float.valueOf(item.getHeight()));
            mPointMapa.add(pointF);
            if(item.getTitulo().equalsIgnoreCase("Entrada Principal")){
                pontoEntrada = item;
            }
        }
    }

    public PinView(Context context) {
        this(context, null);

    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void configPath(boolean path){
        desenharPath = path;
    }

    private Bitmap AjusteDensity(Bitmap bmp, boolean isText) {
        if (isText) {
            float density = getResources().getDisplayMetrics().densityDpi;
            float w = (density / 520f) * bmp.getWidth();
            float h = (density / 520f) * bmp.getHeight();
            return Bitmap.createScaledBitmap(bmp, (int) w, (int) h, true);
        }else{
            float density = getResources().getDisplayMetrics().densityDpi;
            float w = (density / 620f) * bmp.getWidth();
            float h = (density / 620f) * bmp.getHeight();
            return Bitmap.createScaledBitmap(bmp, (int) w, (int) h, true);
        }
    }

    private void initialise() {

        Util.log("initialise()");

        tt1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_primeiro_andar);
        tt1 = AjusteDensity(tt1, false);
        tt2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_segundo_andar);
        tt2 = AjusteDensity(tt2, false);
        tt3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_terceiro_andar);
        tt3 = AjusteDensity(tt3, false);
        tt4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_quarto_andar);
        tt4 = AjusteDensity(tt4, false);
        tt5 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_quinto_andar);
        tt5 = AjusteDensity(tt5, false);
        tt6 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_sexto_andar);
        tt6 = AjusteDensity(tt6, false);
        tt7 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_setimo_andar);
        tt7 = AjusteDensity(tt7, false);
        tt8 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_oitavo_andar);
        tt8 = AjusteDensity(tt8, false);
        tt9 = BitmapFactory.decodeResource(this.getResources(), R.drawable.titulo_nono_andar);
        tt9 = AjusteDensity(tt9, false);
        base = BitmapFactory.decodeResource(this.getResources(), R.drawable.zoon_minimo_base);
        base = AjusteDensity(base, true);

        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_point_vermelho);
        pin = AjusteDensity(pin, false);
        dotPath = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_dot);
        dotPath = AjusteDensity(dotPath, false);
        atendimento = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_atendimento);
        atendimento = AjusteDensity(atendimento, false);
        info = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_info);
        info = AjusteDensity(info, false);
        entrada= BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_entrada);
        entrada = AjusteDensity(entrada, false);
        elevador= BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_elevador);
        elevador = AjusteDensity(elevador, false);
        banheiro = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_banheiro);
        banheiro = AjusteDensity(banheiro, false);
        escada= BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_escada);
        escada = AjusteDensity(escada, false);
        aula = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_aula);
        aula = AjusteDensity(aula, false);
        rampa = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_acesso);
        rampa = AjusteDensity(rampa, false);
        cantina = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_cantina);
        cantina = AjusteDensity(cantina, false);
        ajuda = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_help);
        ajuda = AjusteDensity(ajuda, false);

        origem = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_point_verde);
        origem = AjusteDensity(origem, false);
        destino = BitmapFactory.decodeResource(this.getResources(), R.drawable.mk_point_vermelho);
        destino = AjusteDensity(destino, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if(getScale() > 0.33f){
            setPontosDoMapa(canvas, paint);
        }else{
            setTextMap(canvas, paint);
        }

        //Util.log( "A: "+(pontoA !=null) +" B: "+ (pontoB !=null) );
        if(pontoA != null){
            if(pontoB!=null) {
                setPath(pontoA, pontoB, canvas, paint);
            }
        }

        if(pontoA !=null){
            try{
                PointF vPin = sourceToViewCoord(new PointF(Float.parseFloat(pontoA.getWidth()), Float.parseFloat(pontoA.getHeight())));
                float vX = vPin.x - (origem.getWidth() / 2);
                float vY = vPin.y - (origem.getHeight());
                canvas.drawBitmap(origem, vX, vY, paint);
            }catch (Exception e){
                Util.log(e.toString());
            }

        }

        if(pontoB != null){
            try{
                PointF vPin = sourceToViewCoord(new PointF(Float.parseFloat(pontoB.getWidth()), Float.parseFloat(pontoB.getHeight())));
                float vX = vPin.x - (destino.getWidth() / 2);
                float vY = vPin.y - (destino.getHeight());
                canvas.drawBitmap(destino, vX, vY, paint);
            }catch (Exception e){
                Util.log(e.toString());
            }
        }

    }

    private double distancia(PointF origem, PointF dest){

        double distancia = 0;
        float eixoX;
        float eixoY;
        float QuadY;
        float QuadX;

        try {
            if(origem.x==dest.x){
                distancia = origem.y - dest.y;
                distancia = distancia > 0.0 ? distancia : distancia*-1;
                return (distancia);
            }else if(origem.y == dest.y){
                distancia = origem.x - dest.x;
                distancia = distancia > 0.0 ? distancia : distancia*-1;
                return (distancia);
            }

            if(origem.x > dest.x) {
                eixoX = origem.x;
            }else{
                eixoX = dest.x;
            }
            if(origem.y > dest.y){
                eixoY = origem.y;
            }else{
                eixoY = dest.y;
            }

            if(eixoX==origem.x){
                QuadX = (eixoX - dest.x)*(eixoX - dest.x);
            }else{
                QuadX = (eixoX - origem.x)*(eixoX - origem.x);
            }

            if(eixoY==origem.y){
                QuadY = (eixoY - dest.y)*(eixoY - dest.y);
            }else{
                QuadY = (eixoY - origem.y)*(eixoY - origem.y);
            }
            distancia = QuadX+QuadY;
            distancia = Math.sqrt(distancia);
        }catch (NullPointerException e){
            Util.log(e.toString());
        }
        return (distancia);
    }

    private void setPath(ItensMapa pontoA, ItensMapa pontoB, Canvas canvas, Paint paint){

        try {


            List<PointF> caminho = new ArrayList<>();

            //inicio
            caminho.add(new PointF(Float.valueOf(pontoA.getWidth()), Float.valueOf(pontoA.getHeight())));
            //conector
            caminho.add(new PointF(Float.valueOf(pontoA.getPathw()), Float.valueOf(pontoA.getPathh())));

            PointF destino = new PointF(Float.valueOf(pontoB.getWidth()), Float.valueOf(pontoB.getHeight()));
            PointF conecdestino = new PointF(Float.valueOf(pontoB.getPathw()), Float.valueOf(pontoB.getPathh()));
            int pos_pontoA = -1;
            int pos_pontoB = -1;

            for (int i = 0; i < mPointsPath.size(); i++) {

                if (pos_pontoA < 0) {
                    if ((Float.valueOf(pontoA.getPathw()) == mPointsPath.get(i).x) && (Float.valueOf(pontoA.getPathh()) == mPointsPath.get(i).y))
                        pos_pontoA = i;
                }
                if (pos_pontoB < 0) {
                    if ((Float.valueOf(pontoB.getPathw()) == mPointsPath.get(i).x) && (Float.valueOf(pontoB.getPathh()) == mPointsPath.get(i).y))
                        pos_pontoB = i;
                }
            }

            if (pos_pontoA < pos_pontoB) {
                for (int i = pos_pontoA; i < pos_pontoB; i++) {
                    caminho.add(mPointsPath.get(i));
                }
            } else {
                for (int i = pos_pontoA; i > pos_pontoB; i--) {
                    caminho.add(mPointsPath.get(i));
                }
            }
            caminho.add(conecdestino);
            caminho.add(destino);

            List<PointF> resultado = new ArrayList<>();
            for (int i = 1; i < caminho.size(); i++) {
                //pontos entre pontos
                PointF A = caminho.get(i - 1);
                PointF B = caminho.get(i);
                resultado.add(A);

                double pDist = distancia(A, B);

                pDist = pDist > 0.0 ? pDist : pDist * (-1);

                if (pDist != (0)) {
                    int partes = (int) pDist / 20;
                    double fragX = Math.abs(A.x - B.x);
                    double fragY = Math.abs(A.y - B.y);
                    double parteX = fragX / partes;
                    double parteY = fragY / partes;
                    Boolean aMinX = A.x < B.x;
                    Boolean aMinY = A.y < B.y;

                    for (int j = 1; j < partes; j++) {
                        float x = (aMinX) ? (float) (A.x + (parteX * j)) : (float) (A.x - (parteX * j));
                        float y = (aMinY) ? (float) (A.y + (parteY * j)) : (float) (A.y - (parteY * j));
                        resultado.add(new PointF(x, y));
                    }
                    resultado.add(B);
                }
            }


            for (int i = 1; i < resultado.size(); i++) {
                PointF vPin = sourceToViewCoord(resultado.get(i));
                float vX = vPin.x - (dotPath.getWidth() / 2);
                float vY = vPin.y - (dotPath.getHeight() / 2);
                canvas.drawBitmap(dotPath, vX, vY, paint);
            }
        }catch (Exception e){
            Util.log("setPath() Exception: "+e.toString());
        }
    }


    private void setPontosDoMapa(Canvas canvas, Paint paint){

        if(ownpos != null){
            PointF vPin = sourceToViewCoord(ownpos);
            float vX = vPin.x - (pin.getWidth() / 2);
            float vY = vPin.y - (pin.getHeight());
            canvas.drawBitmap(pin, vX, vY, paint);
        }

        if((mPointMapa  != null) && !(mPointMapa.size() == 0)){
            //Coloca no mapa os pontos de interesse
            for(int i = 0; i < mPointMapa.size(); i++) {
                PointF vPin = sourceToViewCoord(mPointMapa.get(i));

                String itemNome = itensMapaList.get(i).getTitulo();
                if ((itemNome.equalsIgnoreCase("Atendimento")) ||
                        (itemNome.equalsIgnoreCase("Monitoria")) ||
                        (itemNome.equalsIgnoreCase("Pedagogia")) ||
                        (itemNome.equalsIgnoreCase("Secretaria")) ||
                        (itemNome.equalsIgnoreCase("Coordenadoria Informática")) ||
                        (itemNome.equalsIgnoreCase("Fotocópias")) ||
                        (itemNome.equalsIgnoreCase("Monitoria")) ){

                    float vX = vPin.x - (atendimento.getWidth() / 2);
                    float vY = vPin.y - (atendimento.getHeight()/ 2);
                    canvas.drawBitmap(atendimento, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Informações")){
                    float vX = vPin.x - (info.getWidth() / 2);
                    float vY = vPin.y - (info.getHeight()/ 2);
                    canvas.drawBitmap(info, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Entrada Principal")){
                    float vX = vPin.x - (entrada.getWidth() / 2);
                    float vY = vPin.y - (entrada.getHeight()/2);
                    canvas.drawBitmap(entrada, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Elevador")){
                    float vX = vPin.x - (elevador.getWidth() / 2);
                    float vY = vPin.y - (elevador.getHeight()/2);
                    canvas.drawBitmap(elevador, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Banheiro")){
                    float vX = vPin.x - (banheiro.getWidth() / 2);
                    float vY = vPin.y - (banheiro.getHeight()/2);
                    canvas.drawBitmap(banheiro, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Escadas")){
                    float vX = vPin.x - (escada.getWidth() / 2);
                    float vY = vPin.y - (escada.getHeight()/2);
                    canvas.drawBitmap(escada, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Sala de Aula")){
                    float vX = vPin.x - (aula.getWidth() / 2);
                    float vY = vPin.y - (aula.getHeight()/2);
                    canvas.drawBitmap(aula, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Rampa")){
                    float vX = vPin.x - (rampa.getWidth() / 2);
                    float vY = vPin.y - (rampa.getHeight()/2);
                    canvas.drawBitmap(rampa, vX, vY, paint);
                }else
                if(itemNome.equalsIgnoreCase("Cantina")){
                    float vX = vPin.x - (cantina.getWidth() / 2);
                    float vY = vPin.y - (cantina.getHeight()/2);
                    canvas.drawBitmap(cantina, vX, vY, paint);
                }else{
                    float vX = vPin.x - (ajuda.getWidth() / 2);
                    float vY = vPin.y - (ajuda.getHeight() /2);
                    canvas.drawBitmap(ajuda, vX, vY, paint);
                }
            }
        }
    }

    private void setTextMap(Canvas canvas, Paint paint){

        float x = 1508.0f;
        float y = 0.0f;
        float yb = 901.0f;


        PointF vPin = sourceToViewCoord(new PointF(x,y));
        if (mAndar == 1){
            float vX =  vPin.x - (tt1.getWidth() / 2);
            float vY = vPin.y - tt1.getHeight();
            canvas.drawBitmap(tt1, vX, vY, paint);
        }else
        if(mAndar == 2){
            float vX = vPin.x - (tt2.getWidth() / 2);
            float vY = vPin.y - (tt2.getHeight());
            canvas.drawBitmap(tt2, vX, vY, paint);
        }else
        if(mAndar == 3){
            float vX = vPin.x - (tt3.getWidth() / 2);
            float vY = vPin.y - (tt3.getHeight()/2);
            canvas.drawBitmap(tt3, vX, vY, paint);
        }else
        if(mAndar ==4){
            float vX = vPin.x - (tt4.getWidth() / 2);
            float vY = vPin.y - (tt4.getHeight()/2);
            canvas.drawBitmap(tt4, vX, vY, paint);
        }else
        if(mAndar == 5){
            float vX = vPin.x - (tt5.getWidth() / 2);
            float vY = vPin.y - (tt5.getHeight()/2);
            canvas.drawBitmap(tt5, vX, vY, paint);
        }else
        if(mAndar == 6){
            float vX = vPin.x - (tt6.getWidth() / 2);
            float vY = vPin.y - (tt6.getHeight()/2);
            canvas.drawBitmap(tt6, vX, vY, paint);
        }else
        if(mAndar == 7){
            float vX = vPin.x - (tt7.getWidth() / 2);
            float vY = vPin.y - (tt7.getHeight()/2);
            canvas.drawBitmap(tt7, vX, vY, paint);
        }else
        if(mAndar == 8){
            float vX = vPin.x - (tt8.getWidth() / 2);
            float vY = vPin.y - (tt8.getHeight()/2);
            canvas.drawBitmap(tt8, vX, vY, paint);
        }else
        if(mAndar == 9){
            float vX = vPin.x - (tt9.getWidth() / 2);
            float vY = vPin.y - (tt9.getHeight()/2);
            canvas.drawBitmap(tt9, vX, vY, paint);
        }

        vPin = sourceToViewCoord(new PointF(x,yb));
        float vX = vPin.x - (base.getWidth() / 2);
        float vY = vPin.y + (base.getHeight());
        canvas.drawBitmap(base, vX, vY, paint);
    }

    public ItensMapa getInfoMarker(PointF point, int Andar){

        for(ItensMapa marker: itensMapaList){
            PointF markerPoint = new PointF( Float.valueOf( marker.getWidth()), Float.valueOf(marker.getHeight()));
            if(distancia(point, markerPoint) < RAIO_CLICK ) {
                return marker;
            }
        }
        return null;
    }

    public void setPontoB(ItensMapa itensMapa){
        pontoB = itensMapa;
        initialise();
    }

    public void setPontoA(ItensMapa itensMapa){
        if(itensMapa == null){
            pontoA = pontoEntrada;
        }else{
            pontoA = itensMapa;
        }
        initialise();
    }

    public void treadMostraPath(){
        new Thread(){
            public void run(){
                if (!isReady()) {
                    treadMostraPath();
                }else {
                    if ((pontoB != null) && (pontoA != null)) {
                        try{
                            PointF pointB = new PointF(Float.valueOf(pontoB.getWidth()), Float.valueOf(pontoB.getHeight()));
                            AnimationBuilder animationBuilder = animateScaleAndCenter(1f, pointB);
                            animationBuilder.withDuration(700).withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start();
                        }catch (Exception e) {
                            Util.log(e.toString());
                        }
                    } else {
                        treadMostraPath();
                    }
                }
            }
        }.start();
    }

    public void centralizarPoint(final PointF pointF){
        new Thread(){
            public void run(){
                super.run();
                do{
                    if(!isReady()){
                        try{

                            AnimationBuilder animationBuilder = animateScaleAndCenter(1f, pointF);
                            animationBuilder.withDuration(700).withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start();
                        }catch (Exception e){
                            Util.log(e.toString());
                        }
                        break;
                    }
                }while(true);
            }
        }.start();
    }

    public ItensMapa getNearPoint(PointF point){

        double RAIO_OWNPOS = 10.0;
        ItensMapa result = null;
        do{
            for(ItensMapa marker: itensMapaList){
                PointF markerPoint = new PointF( Float.valueOf( marker.getWidth()), Float.valueOf(marker.getHeight()));
                if(distancia(point, markerPoint) < RAIO_OWNPOS ) {
                    result = marker;
                }
            }
            RAIO_OWNPOS = RAIO_OWNPOS + 5.0;
        }while(result==null);

        return result;
    }

    //BUSCANDO MEU PONTO
        public void setOwnpos(){
            pontoA = null;
            new Thread(){
                public void run(){
                    do{
                        if(!isReady() && pontoA!=null){
                            try{
                                float x = Float.parseFloat(pontoA.getWidth());
                                float y = Float.parseFloat(pontoA.getHeight());
                                AnimationBuilder animationBuilder = animateScaleAndCenter(1f, new PointF(x, y));
                                animationBuilder.withDuration(700).withEasing(SubsamplingScaleImageView.EASE_OUT_QUAD).withInterruptible(false).start();
                            }catch (Exception e){
                                Util.log(e.toString());
                            }

                            break;
                        }
                    }while(true);
                }
            }.start();
        }
}