package itauamachado.ownpos.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.location.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itauamachado.ownpos.extras.SqliteHelper;
import itauamachado.ownpos.extras.Util;

public class SQLiteConn {

    private SQLiteDatabase bd;
    private Context mContext;
    private SqliteHelper auxBd;

    public SQLiteConn(Context context){
        mContext = context;
        auxBd = new SqliteHelper(context);
    }

    //USUARIOS
        public objUsuario getUserLogged(){
            bd = auxBd.getReadableDatabase();
            objUsuario usuario = new objUsuario();
            String[] colunas = new String[]{
                    "id_senac",
                    "nome",
                    "email",
                    "social",
                    "urlPhoto",
                    "urlSite",
                    "perfil",
                    "status" };

            Cursor cursor = bd.query(SqliteHelper.TABLE_PROFILE, colunas, null, null, null, null, "nome ASC");

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                usuario.setId_senac(cursor.getString(0));
                usuario.setNome(cursor.getString(1));
                usuario.setEmail(cursor.getString(2));
                usuario.setLoginSocial(cursor.getString(3));
                usuario.setUrlPhoto(cursor.getString(4));
                usuario.setUrlSocial(cursor.getString(5));
                usuario.setPerfil(cursor.getString(6));
                usuario.setStatus(cursor.getString(7));
            }else{
                usuario = null;
            }

            bd.close();
            return usuario;
        }
        public boolean setUserLogged(objUsuario usuario){
            delUserLogged();
            bd = auxBd.getWritableDatabase();
            //bd.execSQL("delete * from "+SqliteHelper.TABLE_PROFILE);
            //bd.delete(SqliteHelper.TABLE_PROFILE, "1", null);
            ContentValues valores = new ContentValues();
            valores.put("nome", usuario.getNome());
            valores.put("email", usuario.getEmail());
            valores.put("social", usuario.getLoginSocial());
            valores.put("urlPhoto", usuario.getUrlPhoto());
            valores.put("urlSite", usuario.getUrlSocial());
            valores.put("perfil", usuario.getPerfil());
            valores.put("status", usuario.getStatus());
            valores.put("id_senac", usuario.getId_senac());
            bd.insert(SqliteHelper.TABLE_PROFILE, null, valores);

            bd.close();
            return false;
        }
        public void delUserLogged(){
            bd = auxBd.getWritableDatabase();
            bd.delete(SqliteHelper.TABLE_PROFILE, "1", null);
            bd.close();
        }

    //CURSOS PRESENCIAL E EAD
        public List<objCursos> getCursos(boolean isCursoEAD){
            //Util.log("getCursos : isCursoEad? " + isCursoEAD);
            bd = auxBd.getReadableDatabase();
            List<objCursos> objCursosList = new ArrayList<>();
            String[] colunas = new String[]{"_id, tipo, area, curso, img, href"};
            String tabela = (isCursoEAD)? SqliteHelper.TABLE_CURSO_EAD : SqliteHelper.TABLE_CURSO;

            Cursor cursor = bd.query(tabela, colunas, null, null, null, null, "area ASC");

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                   objCursos curso = new objCursos();
                    curso.setId(cursor.getLong(0));
                    curso.setTipo(cursor.getString(1));
                    curso.setArea(cursor.getString(2));
                    curso.setCurso(cursor.getString(3));
                    curso.setImg(cursor.getString(4));
                    curso.setHref(cursor.getString(5));
                    curso.setUrlbase((isCursoEAD)? "http://www.ead.senac.br/" : "http://www.senacrs.com.br/");
                    objCursosList.add(curso);
                }while(cursor.moveToNext());
            }
            bd.close();
            return objCursosList;
        }
        public void setCursos(List<objCursos> cursos, boolean isCursoEAD){
            //Util.log("setCursos : Quantos na lista: "+ cursos.size()+" isCursoEad? "+isCursoEAD);

            bd = auxBd.getWritableDatabase();
            String tabela = (isCursoEAD) ? SqliteHelper.TABLE_CURSO_EAD : SqliteHelper.TABLE_CURSO;
            bd.delete(tabela, "1", null);
            for (objCursos curso: cursos) {
                ContentValues valores = new ContentValues();
                valores.put("acervo", curso.getId());
                valores.put("tipo", curso.getTipo());
                valores.put("area", curso.getArea());
                valores.put("curso", curso.getCurso());
                valores.put("img", curso.getImg());
                valores.put("href", curso.getHref());
                bd.insert(tabela, null, valores);
            }
            bd.close();
        }

    //LOCATION
        public void savePosition(Location location){
        bd = auxBd.getWritableDatabase();
        if(location != null){
            bd.delete(SqliteHelper.TABLE_LOCATION, "1", null);
            ContentValues valores = new ContentValues();
            valores.put("latitude", location.getLatitude());
            valores.put("longitude", location.getLongitude());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            valores.put("verificado", sdf.format(new Date()));
            valores.put("accuracy", location.getAccuracy());
            bd.insert(SqliteHelper.TABLE_LOCATION, null, valores);
        }
        bd.close();
    }
        public ContentValues getLastGeoPosition(){
        bd = auxBd.getReadableDatabase();
        String[] colunas = new String[]{
                "latitude",
                "longitude",
                "verificado",
                "accuracy"};
        Cursor cursor = bd.query(SqliteHelper.TABLE_LOCATION, colunas, null, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            ContentValues valores = new ContentValues();
            do {
                valores.put("latitude", cursor.getString(0));
                valores.put("longitude", cursor.getString(1));
                valores.put("verificado", cursor.getString(2));
                valores.put("accuracy", cursor.getString(3));
            } while (cursor.moveToNext());
            bd.close();
            return valores;
        }
        bd.close();
        return null;
    }

    //MAPAS
        public void saveItensMapas(List<ItensMapa> itensMapaList){
            bd = auxBd.getWritableDatabase();
            String tabela = SqliteHelper.TABLE_POINTS;
            for (ItensMapa itens: itensMapaList) {
                ContentValues valores = new ContentValues();
                valores.put("indoor", itens.getIndoor());
                valores.put("andar", itens.getAndar());
                valores.put("width", itens.getWidth());
                valores.put("heigth", itens.getHeight());
                valores.put("build", itens.getBuild());
                valores.put("pathw", itens.getPathw());
                valores.put("pathh", itens.getPathh());
                valores.put("titulo", itens.getTitulo());
                valores.put("descrip", itens.getDescri());
                valores.put("telefone", itens.getTelefone());
                bd.insert(tabela, null, valores);
            }
            bd.close();
        }
        public List<ItensMapa> getItensAndar(String andar){
            bd = auxBd.getReadableDatabase();
            List<ItensMapa> itensMapaArrayList = new ArrayList<>();

            String[] colunas = new String[]{
                    "andar",
                    "width",
                    "heigth",
                    "build",
                    "pathw",
                    "pathh",
                    "titulo",
                    "descrip",
                    "telefone"};
            String tabela = SqliteHelper.TABLE_POINTS;
            Cursor cursor = bd.query(true, tabela, colunas, "titulo != 'path' and andar ="+andar, null, null, null, null , null);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    ItensMapa item = new ItensMapa();
                    item.setAndar(String.valueOf(cursor.getInt(0)));
                    item.setWidth(String.valueOf(cursor.getInt(1)));
                    item.setHeight(String.valueOf(cursor.getInt(2)));
                    item.setBuild(cursor.getString(3));
                    item.setPathw(String.valueOf(cursor.getInt(4)));
                    item.setPathh(String.valueOf(cursor.getInt(5)));
                    item.setTitulo(cursor.getString(6));
                    item.setDescri(cursor.getString(7));
                    item.setTelefone(String.valueOf(cursor.getInt(8)));
                    itensMapaArrayList.add(item);
                    //Util.log("(getItensAndar) Andar: " + item.getAndar() + "Descrição: " + item.getDescri());
                }while(cursor.moveToNext());
            }
            bd.close();
            return itensMapaArrayList;
        }
        public List<PointF> getPathAndar(int andar){
            bd = auxBd.getReadableDatabase();
            List<PointF> result = new ArrayList<>();

            String[] colunas = new String[]{
                    "width",
                    "heigth"};
            String tabela = SqliteHelper.TABLE_POINTS;
            Cursor cursor = bd.query(true, tabela, colunas, "titulo = 'path' and andar ="+andar, null, null, null, "telefone asc" , null);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    PointF p = new PointF();
                    p.set(Float.valueOf(cursor.getString(0)), Float.valueOf(cursor.getString(1)));
                    result.add(p);
                   // Util.log("(getPathAndar) Width: " + p.x + "Heigth: " + p.y);
                }while(cursor.moveToNext());
            }

            bd.close();
            return result;
        }
        public int InfoMapas(){
            bd = auxBd.getReadableDatabase();
            String tabela = SqliteHelper.TABLE_POINTS;
            String[] colunas = new String[]{"andar"};
            Cursor cursor = bd.query(true, tabela, colunas, null, null, null, null, null , null);
            return cursor.getCount();
        }
        public ItensMapa getDescSala(int id){
            bd = auxBd.getReadableDatabase();
            ItensMapa result = new ItensMapa();
            String[] colunas = new String[]{
                    "indoor",
                    "andar",
                    "width",
                    "heigth",
                    "build",
                    "pathw",
                    "pathh",
                    "titulo",
                    "descrip",
                    "telefone"
            };
            String tabela = SqliteHelper.TABLE_POINTS;
            Cursor cursor = bd.query(true, tabela, colunas, "indoor ="+id, null, null, null, null , null);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    result.setIndoor(String.valueOf(cursor.getInt(0)));
                    result.setAndar(String.valueOf(cursor.getInt(1)));
                    result.setWidth(String.valueOf(cursor.getInt(2)));
                    result.setHeight(String.valueOf(cursor.getInt(3)));
                    result.setBuild(cursor.getString(4));
                    result.setPathw(String.valueOf(cursor.getInt(5)));
                    result.setPathh(String.valueOf(cursor.getInt(6)));
                    result.setTitulo(cursor.getString(7));
                    result.setDescri(cursor.getString(8));
                    result.setTelefone(String.valueOf(cursor.getInt(9)));
                }while(cursor.moveToNext());
            }
            bd.close();
            return result;
        }

    //BIBLIOTECA
        public void setReservaAcervo(objAcervo livro){
            bd = auxBd.getWritableDatabase();
            String tabela = SqliteHelper.TABLE_RESERVAS;
                ContentValues valores = new ContentValues();
                valores.put("_senac", livro.get_senac());
                valores.put("dstaq", livro.getDstaq());
                valores.put("chamd", livro.getChamd());
                valores.put("aPrin", livro.getaPrin());
                valores.put("aArtg", livro.getaArtg());
                valores.put("aScdr", livro.getaScdr());
                valores.put("pbcao", livro.getPbcao());
                valores.put("fsico", livro.getFsico());
                valores.put("rsumo", livro.getRsumo());
                valores.put("notas", livro.getNotas());
                valores.put("assun", livro.getAssun());
                valores.put("url", livro.getUrl());
                valores.put("pdico", livro.getPdico());
                valores.put("serie", livro.getSerie());

                bd.insert(tabela, null, valores);
            bd.close();
        }
        public List<objAcervo> getReserva(){
            bd = auxBd.getReadableDatabase();
            List<objAcervo> acervoArrayList= new ArrayList<>();

            String[] colunas = new String[]{
                    "_senac",
                    "dstaq",
                    "chamd",
                    "aPrin",
                    "aArtg",
                    "aScdr",
                    "pbcao",
                    "fsico",
                    "rsumo",
                    "notas",
                    "assun",
                    "url",
                    "pdico",
                    "serie"};
            String tabela = SqliteHelper.TABLE_RESERVAS;
            Cursor cursor = bd.query(true, tabela, colunas, null , null, null, null, null , null);


            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    objAcervo item = new objAcervo();
                    item.set_senac(cursor.getLong(0));
                    item.setDstaq(cursor.getString(1));
                    item.setChamd(cursor.getString(2));
                    item.setaPrin(cursor.getString(3));
                    item.setaArtg(cursor.getString(4));
                    item.setaScdr(cursor.getString(5));
                    item.setPbcao(cursor.getString(6));
                    item.setFsico(cursor.getString(7));
                    item.setRsumo(cursor.getString(8));
                    item.setNotas(cursor.getString(9));
                    item.setAssun(cursor.getString(10));
                    item.setUrl(cursor.getString(11));
                    item.setPdico(cursor.getString(12));
                    item.setSerie(cursor.getString(13));
                    acervoArrayList.add(item);
                }while(cursor.moveToNext());
            }
            bd.close();
            return acervoArrayList;
        }
        public void delReserva(objAcervo livro){
            bd = auxBd.getWritableDatabase();
            bd.delete(SqliteHelper.TABLE_RESERVAS, "_senac = "+livro.get_senac(), null);
            bd.close();
        }

    //EVENTOS
        public void setEventos(List<objEventos> eventosList){
            bd = auxBd.getWritableDatabase();
            String tabela = SqliteHelper.TABLE_EVENTOS;
            for (objEventos evento: eventosList) {
                ContentValues valores = new ContentValues();
                valores.put("id_evento", evento.getId());
                valores.put("indoor", evento.getIndoor());
                valores.put("nome_autor", evento.getAutor());
                valores.put("perfil_autor", evento.getPerfil());
                valores.put("titulo", evento.getTitulo());
                valores.put("descricao", evento.getDescricao());
                valores.put("dt_inicio", evento.getInicio());
                valores.put("dt_fim", evento.getFim());
                valores.put("integrantes", evento.getParticipantes());
                bd.insert(tabela, null, valores);
            }
            bd.close();
        }
        public List<objEventos> getEventos(){
            bd = auxBd.getReadableDatabase();

            List<objEventos> eventosList = new ArrayList<>();

            String[] colunas = new String[]{
                    "id_evento",
                    "indoor",
                    "nome_autor",
                    "perfil_autor",
                    "titulo",
                    "descricao",
                    "dt_inicio",
                    "dt_fim",
                    "integrantes"};
            String tabela = SqliteHelper.TABLE_EVENTOS;
            Cursor cursor = bd.query(true, tabela, colunas, null , null, null, null, null , null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    objEventos item = new objEventos();
                    item.setId(cursor.getInt(0));
                    item.setIndoor(cursor.getInt(1));
                    item.setAutor(cursor.getString(2));
                    item.setPerfil(cursor.getString(3));
                    item.setTitulo(cursor.getString(4));
                    item.setDescricao(cursor.getString(5));
                    item.setInicio(cursor.getString(6));
                    item.setFim(cursor.getString(7));
                    item.setParticipantes(cursor.getString(8));
                    eventosList.add(item);
                }while(cursor.moveToNext());
            }
            bd.close();
            return eventosList;
        }

    //AULAS
        public void setAulas(List<objAulas> aulasList){
            bd = auxBd.getWritableDatabase();
            String tabela = SqliteHelper.TABLE_AULAS;

            for (objAulas aula: aulasList) {
                ContentValues valores = new ContentValues();
                valores.put("id_turma", aula.getId());
                valores.put("indoor", aula.getIndoor());
                valores.put("curso", aula.getCurso());
                valores.put("professor", aula.getProf());
                valores.put("nome_turma", aula.getTurma());
                valores.put("dt_aula", aula.getData());
                valores.put("hr_inicio", aula.getHr_inicio());
                valores.put("hr_fim", aula.getHr_fim());
                bd.insert(tabela, null, valores);
            }
            bd.close();
        }
        public List<objAulas> getAulas(String turma){
            bd = auxBd.getReadableDatabase();

            List<objAulas> aulasList = new ArrayList<>();

            String[] colunas = new String[]{
                    "id_turma",
                    "indoor",
                    "dt_aula",
                    "hr_inicio",
                    "hr_fim"};
            String tabela = SqliteHelper.TABLE_AULAS;
            Cursor cursor = bd.query(true, tabela, colunas, "id_turma = "+Integer.valueOf(turma), null, null, null, "dt_aula asc" , null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    objAulas item = new objAulas();
                    item.setId(cursor.getInt(0));
                    item.setIndoor(cursor.getInt(1));
                    item.setData(cursor.getString(2));
                    item.setHr_inicio(cursor.getString(3));
                    item.setHr_fim(cursor.getString(4));
                    aulasList.add(item);
                }while(cursor.moveToNext());
            }
            bd.close();
            return aulasList;
        }
        public List<objAulas> getTurmas(){
            bd = auxBd.getReadableDatabase();
            List<objAulas> turmaList = new ArrayList<>();

            String[] colunas = new String[]{
                    "id_turma",
                    "curso",
                    "professor",
                    "nome_turma"};
            String tabela = SqliteHelper.TABLE_AULAS;
            Cursor cursor = bd.query(true, tabela, colunas, null , null, null, null, null , null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    objAulas item = new objAulas();
                    item.setId(cursor.getInt(0));
                    item.setCurso(cursor.getString(1));
                    item.setProf(cursor.getString(2));
                    item.setTurma(cursor.getString(3));
                    turmaList.add(item);
                }while(cursor.moveToNext());
            }
            bd.close();
            return turmaList;
        }


    //TAREFAS
        public List<objContextos> getTarefas(){
            bd = auxBd.getReadableDatabase();
            List<objContextos> contextosArrayList = new ArrayList<>();

            String[] colunas = new String[]{
                    "chave",
                    "titulo",
                    "autor",
                    "andar",
                    "local",
                    "inicio",
                    "termino",
                    "obs"};

            String tabela = SqliteHelper.TABLE_CONTEXTOS;
            Cursor cursor = bd.query(true, tabela, colunas, null , null, null, null, null , null);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ItensMapa itensMapa = new ItensMapa();
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    objContextos item = new objContextos();
                    item.setChave(cursor.getString(0));
                    item.setTitulo(cursor.getString(1));
                    item.setAutor(cursor.getString(2));
                    itensMapa.setAndar(cursor.getString(3));
                    itensMapa.setBuild(cursor.getString(4));
                    item.setLocal(itensMapa);
                    try {
                        item.setInicio(sdf.parse(cursor.getString(4)));
                        if(cursor.getString(5) != null){
                            item.setTermino(sdf.parse(cursor.getString(5)));
                        }else{
                            item.setTermino(null);
                        }

                    }catch (ParseException e){
                        Util.log(e.toString());
                    }
                    item.setObs(cursor.getString(6));
                    contextosArrayList.add(item);
                }while(cursor.moveToNext());
            }
            bd.close();
            return contextosArrayList;
        }

    //CONTEXTOS
        public List<objContextos> getContextos(){
            bd = auxBd.getReadableDatabase();
            List<objContextos> contextosArrayList = new ArrayList<>();

            String[] colunas = new String[]{
                    "chave",
                    "titulo",
                    "autor",
                    "andar",
                    "local",
                    "inicio",
                    "termino",
                    "obs"};

            String tabela = SqliteHelper.TABLE_CONTEXTOS;
            Cursor cursor = bd.query(true, tabela, colunas, null , null, null, null, null , null);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ItensMapa itensMapa = new ItensMapa();
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    objContextos item = new objContextos();
                    item.setChave(cursor.getString(0));
                    item.setTitulo(cursor.getString(1));
                    item.setAutor(cursor.getString(2));
                    itensMapa.setAndar(cursor.getString(3));
                    itensMapa.setBuild(cursor.getString(4));
                    item.setLocal(itensMapa);
                    try {
                        item.setInicio(sdf.parse(cursor.getString(4)));
                        if(cursor.getString(5) != null){
                            item.setTermino(sdf.parse(cursor.getString(5)));
                        }else{
                            item.setTermino(null);
                        }

                    }catch (ParseException e){
                        Util.log(e.toString());
                    }
                    item.setObs(cursor.getString(6));
                    contextosArrayList.add(item);
                }while(cursor.moveToNext());
            }
            bd.close();
            return contextosArrayList;
        }
        public void CursosxContexto(double distancia){
            Util.log("Entrou na busca por contextos com a distanca " + distancia + " metros");
        }

    //NAVIGATION WIFI
        public void setData(NavigationWiFi dados){
            bd = auxBd.getWritableDatabase();
            String tabela = SqliteHelper.TABLE_NAVIGATION;

            ContentValues valores = new ContentValues();
            valores.put("capabilities", dados.getCapabilities());
            valores.put("Frequency", dados.getFrequency());
            valores.put("Level", dados.getLevel());
            valores.put("SSID", dados.getSSID());
            valores.put("BSSID", dados.getBSSID());
            valores.put("With", dados.getWith());
            valores.put("Height", dados.getHeight());

            bd.insert(tabela, null, valores);

            bd.close();
        }
        public PointF getNavigationWiFi(List<NavigationWiFi> dados){
            bd = auxBd.getReadableDatabase();
            PointF p = new PointF();
            String tabela = SqliteHelper.TABLE_NAVIGATION;

           //String[] colunas = new String[]{"With", "Height"};
            String[] joins = new String[]{"Z", "A", "B", "C", "D", "E", "F", "G"};

            String query = "";
            for(int i = 0; i < dados.size(); i++){

                if(query.equalsIgnoreCase("")) {
                    query = "SELECT "+joins[0]+".With, "+joins[0]+".Height from " + SqliteHelper.TABLE_NAVIGATION +" "+joins[0];
                }else{
                    query = query + " join (SELECT With, Height from "+
                            SqliteHelper.TABLE_NAVIGATION +" where BSSID = '"+dados.get(i).getBSSID()+"') "+
                            joins[i] +" on "+joins[i]+".With = Z.With and "+joins[i]+".Height = Z.Height";
                }
                if(i==6){break;}
            }
            query  = query + " where BSSID = '"+dados.get(0).getBSSID()+"';";

           // Util.log(query);

            Cursor cursor = bd.rawQuery(query, null);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    p.set (Float.parseFloat(""+cursor.getInt(0)), Float.parseFloat(""+cursor.getInt(1)));
                }while(cursor.moveToNext());
            }
            bd.close();
            return p;
        }

    //NOTFICATION
        public List<String> getNotifications(){
            List<String> result = new ArrayList<>();
            bd = auxBd.getReadableDatabase();

            //String[] colunas = new String[]{ "tipo", "dia"};

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String tabela = SqliteHelper.TABLE_CONF_NOTIFICATION;
            //Cursor cursor = bd.query(true, tabela, colunas, "dia = " + sdf.format(new Date()), null, null, null, null, null);
            String query = "SELECT tipo, dia from "+ tabela +" WHERE  dia = '"+sdf.format(new Date())+"'";


            Cursor cursor = bd.rawQuery(query, null);
            //Util.log(query);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    result.add(cursor.getString(0));
                }while(cursor.moveToNext());
            }

            bd.close();
            return result;
        }

        public void setNotification(String tipo){
            bd = auxBd.getWritableDatabase();
            String tabela = SqliteHelper.TABLE_CONF_NOTIFICATION;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ContentValues valores = new ContentValues();
            valores.put("tipo", tipo);
            valores.put("dia", sdf.format(new Date()));
            bd.insert(tabela, null, valores);
            Util.log("Tipo:"+tipo + " Data: "+valores.getAsString("dia"));
            bd.close();
        }

        public void cleardNotifications(){
            List<String> result = new ArrayList<>();
            bd = auxBd.getReadableDatabase();


            //String[] colunas = new String[]{ "tipo", "dia"};

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String tabela = SqliteHelper.TABLE_CONF_NOTIFICATION;

            int delete = bd.delete(SqliteHelper.TABLE_CONF_NOTIFICATION, "dia < '" + sdf.format(new Date())+"'", null);
            Util.log("cleardNotifications("+delete+") dia: " + sdf.format(new Date()));

            delete = bd.delete(SqliteHelper.TABLE_CONF_NOTIFICATION, "dia = '" + sdf.format(new Date())+"'", null);
            Util.log("cleardNotifications("+delete+") dia: " + sdf.format(new Date()));


            bd.close();
        }
}