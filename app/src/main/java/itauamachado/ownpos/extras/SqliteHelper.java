package itauamachado.ownpos.extras;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by itauafm on 15/09/2015.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String NOME_BD = "ownpos_db";
    private static final int VERSAO_BD = 3;

    //tabelas
    public static final String TABLE_PROFILE = "usuarios";
    public static final String TABLE_CONTEXTOS = "contextos";
    public static final String TABLE_CURSO_EAD = "cursosead";
    public static final String TABLE_CURSO = "cursos";
    public static final String TABLE_POINTS = "mapas";
    public static final String TABLE_COMPROMISSOS = "compromissos";
    public static final String TABLE_RESERVAS = "livros";
    public static final String TABLE_LOCATION = "locations";
    public static final String TABLE_CONF_NOTIFICATION = "notification";
    public static final String TABLE_AULAS = "aulas";
    public static final String TABLE_EVENTOS = "eventos";
    public static final String TABLE_NAVIGATION = "wifi";


    public SqliteHelper(Context ctx){
        super(ctx, NOME_BD, null, VERSAO_BD);
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {
        setTableProfile(bd);
        setTableContextos(bd);
        setTableCursoEad(bd);
        setTableCursos(bd);
        setTableMapaPoints(bd);
        setTableCompromissos(bd);
        setTableReservas(bd);
        setTableLocation(bd);
        setTableAulas(bd);
        setTableEventos(bd);
        setTableNavigation(bd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
        bd.execSQL("drop table " + TABLE_PROFILE + ";");
        bd.execSQL("drop table " + TABLE_CONTEXTOS + ";");
        bd.execSQL("drop table " + TABLE_CURSO_EAD + ";");
        bd.execSQL("drop table " + TABLE_CURSO + ";");
        bd.execSQL("drop table " + TABLE_POINTS + ";");
        bd.execSQL("drop table " + TABLE_COMPROMISSOS + ";");
        bd.execSQL("drop table " + TABLE_RESERVAS + ";");
        bd.execSQL("drop table " + TABLE_AULAS + ";");
        bd.execSQL("drop table " + TABLE_EVENTOS + ";");
        bd.execSQL("drop table " + TABLE_NAVIGATION + ";");

        onCreate(bd);
    }

    public void setTableProfile(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+TABLE_PROFILE;
        String colunas = "( " +
                "_id      INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "id_senac TEXT NOT NULL, " +
                "nome     TEXT NOT NULL, " +
                "email    TEXT NOT NULL, " +
                "social   TEXT NOT NULL, " +
                "urlPhoto TEXT, " +
                "urlSite TEXT, " +
                "perfil TEXT," +
                "status TEXT );";
        bd.execSQL(create_table + colunas);
    }

    public void setTableContextos(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+TABLE_CONTEXTOS;
        String colunas = "( " +
                "    _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    chave  TEXT    NOT NULL, " +
                "    titulo TEXT    NOT NULL, " +
                "    autor  TEXT    NOT NULL, " +
                "    andar  TEXT    NOT NULL, " +
                "    local  TEXT    NOT NULL, " +
                "    inicio DATE    NOT NULL, " +
                "    termino DATE, " +
                "    obs    TEXT" +
                ");";
        bd.execSQL(create_table+colunas);
    }

    public void setTableCursos(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+ TABLE_CURSO;
        String colunas = "( " +
                "    _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    acervo  INTEGER, "+
                "    tipo   TEXT    NOT NULL, " +
                "    area   TEXT    NOT NULL, " +
                "    curso  TEXT    NOT NULL, " +
                "    img    TEXT, " +
                "    href   TEXT    NOT NULL " +
                ");";
        bd.execSQL(create_table+colunas);
    }

    public void setTableCursoEad(SQLiteDatabase bd){

        String create_table = "CREATE TABLE "+ TABLE_CURSO_EAD;
        String colunas = "( " +
                "    _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    acervo  INTEGER, "+
                "    tipo   TEXT    NOT NULL, " +
                "    area   TEXT    NOT NULL, " +
                "    curso  TEXT    NOT NULL, " +
                "    img    TEXT, " +
                "    href   TEXT    NOT NULL " +
                ");";
        bd.execSQL(create_table+colunas);
    }

    public void setTableMapaPoints(SQLiteDatabase bd){

        String create_table = "CREATE TABLE "+ TABLE_POINTS;
        String colunas = "( " +
                "    _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "   indoor INTEGER NOT NULL, " +
                "   andar   INTEGER NOT NULL, " +
                "   width    INTEGER NOT NULL, " +
                "   heigth  INTEGER NOT NULL, " +
                "   build   TEXT, " +
                "   pathw   INTEGER," +
                "   pathh   INTEGER," +
                "   titulo  TEXT, " +
                "   descrip TEXT, " +
                "   telefone INTEGER"+
                ");";
        bd.execSQL(create_table+colunas);
    }

    public void setTableCompromissos(SQLiteDatabase bd){

        String create_table = "CREATE TABLE "+ TABLE_COMPROMISSOS;
        String colunas = "( " +
                "    _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    chave  TEXT    NOT NULL, " +
                "    titulo TEXT    NOT NULL, " +
                "    autor  TEXT    NOT NULL, " +
                "    indoor TEXT    NOT NULL, " +
                "    inicio DATE    NOT NULL, " +
                "    termino DATE, " +
                "    descricao  TEXT" +
                ");";
        bd.execSQL(create_table+colunas);
    }

    public void setTableReservas(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+ TABLE_RESERVAS;
        String colunas = "( " +
                "    _id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    _senac INTEGER NOT NULL, " +
                "    dstaq  TEXT, " +
                "    chamd  TEXT, " +
                "    aPrin  TEXT, " +
                "    aArtg  TEXT, " +
                "    aScdr  TEXT, " +
                "    pbcao  TEXT, " +
                "    fsico  TEXT, " +
                "    rsumo  TEXT, " +
                "    notas  TEXT, " +
                "    assun  TEXT, " +
                "    url    TEXT, " +
                "    pdico  TEXT, " +
                "    serie  TEXT " +
        ");";
        bd.execSQL(create_table+colunas);
    }

    public void setTableLocation(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+ TABLE_LOCATION;
        String colunas = "( " +
                "    _id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    latitude  DOUBLE NOT NULL, " +
                "    longitude DOUBLE NOT NULL, " +
                "    verificado DATE NOT NULL, " +
                "   accuracy FLOAT );";
        bd.execSQL(create_table+colunas);
    }

    public void setTableAulas(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+ TABLE_AULAS;
        String colunas = "( " +
                " _id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " id_turma INTEGER NOT NULL, "+
                " indoor INTEGER, "+
                " curso TEXT, " +
                " professor TEXT, " +
                " nome_turma TEXT, " +
                " dt_aula TEXT, " +
                "hr_inicio TEXT, " +
                "hr_fim TEXT);";
        bd.execSQL(create_table+colunas);
    }

    public void setTableEventos(SQLiteDatabase bd){
        String create_table = "CREATE TABLE "+ TABLE_EVENTOS;
        String colunas = "( " +
                " _id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " id_evento INTEGER NOT NULL, "+
                " indoor INTEGER, "+
                " nome_autor TEXT, " +
                " perfil_autor TEXT, " +
                " titulo TEXT, " +
                " descricao TEXT, " +
                " dt_inicio DATE, " +
                " dt_fim DATE, " +
                " integrantes TEXT);";
        bd.execSQL(create_table+colunas);
    }

    public void setTableConfig(SQLiteDatabase bd){

        String create_table = "CREATE TABLE "+ TABLE_CONF_NOTIFICATION;
        String colunas = "( " +
                "    _id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    Push DATE, " +
                "    NCursos DATE, " +
                "    NCursosNave DATE, " +
                "    NNoticias DATE);";
        bd.execSQL(create_table+colunas);
    }

    public void setTableNavigation(SQLiteDatabase bd){

        String create_table = "CREATE TABLE "+ TABLE_NAVIGATION;
        String colunas = "( " +
                "    _id    INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "    capabilities TEXT, " +
                "    Frequency INTEGER, " +
                "    Level INTEGER, " +
                "    SSID TEXT, " +
                "    BSSID TEXT, " +
                "    With int, " +
                "    Height int);";
        bd.execSQL(create_table+colunas);
    }


}
