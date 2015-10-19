package itauamachado.ownpos.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by itauafm on 22/09/2015.
 */
public class objContextos {

    private int _id;
    private String chave;
    private String titulo;
    private String autor;
    private Date inicio;
    private Date termino;
    private String obs;
    private ItensMapa local;



    public objContextos(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public ItensMapa getLocal() {
        return local;
    }

    public void setLocal(ItensMapa local) {
        this.local = local;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
