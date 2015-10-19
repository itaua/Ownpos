package itauamachado.ownpos.domain;

/**
 * Created by viniciusthiengo on 7/26/15.
 */
public class WrapObjToNetwork {

    private objAcervo biblioteca;
    private objNoticias noticias;
    private objCursos objCursos;
    private String autor;
    private String titulo;
    private String resumo;
    private String assunto;

    private Long lastId;
    private String method;
    private boolean isNewer;
    private String term;


    //login callVolley
    private String usuario_nome;
    private String usuario_mail;
    private String usuario_login;

    //tarefas, reservas e aulas
    private int id;

    public WrapObjToNetwork(objNoticias noticias, String method, long lastId, boolean isNewer) {
        this.noticias = noticias;
        this.method = method;
        this.lastId = lastId;
        this.isNewer = isNewer;
    }
    public WrapObjToNetwork(objAcervo object, String method) {
        this.method = method;
        this.autor = object.getaArtg();
        this.titulo = object.getDstaq();
        this.resumo = object.getRsumo();
        this.assunto = object.getAssun();
        this.lastId = object.get_id();
        this.biblioteca = null;
    }
    public WrapObjToNetwork(objCursos object, String method) {
        this.method = method;
        this.objCursos = object;
    }
    public WrapObjToNetwork(objUsuario object, String method) {
        this.method = method;
        this.usuario_nome = object.getNome();
        this.usuario_mail = object.getEmail();
        this.usuario_login = object.getLoginSocial();
    }
    public WrapObjToNetwork(String method){
        this.method = method;
    }

    public WrapObjToNetwork(int id, String method){
        this.method = method;
        this.id = id;
    }


    public String getUsuario_nome() {
        return usuario_nome;
    }

    public void setUsuario_nome(String usuario_nome) {
        this.usuario_nome = usuario_nome;
    }

    public String getUsuario_mail() {
        return usuario_mail;
    }

    public void setUsuario_mail(String usuario_mail) {
        this.usuario_mail = usuario_mail;
    }

    public String getUsuario_login() {
        return usuario_login;
    }

    public void setUsuario_login(String usuario_login) {
        this.usuario_login = usuario_login;
    }

    public objAcervo getBiblioteca() {
        return biblioteca;
    }

    public objNoticias getNoticias() {
        return noticias;
    }

    public Long getLastId() {
        return lastId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isNewer() {
        return isNewer;
    }

    public void setIsNewer(boolean isNewer) {
        this.isNewer = isNewer;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }


}
