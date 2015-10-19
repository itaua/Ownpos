package itauamachado.ownpos.domain;

/**
 * Created by itauafm on 07/10/2015.
 */
public class objEventos {
    private int id;
    private int indoor;
    private String autor;
    private String perfil;
    private String titulo;
    private String descricao;
    private String inicio;
    private String fim;
    private String participantes;

    public objEventos() {
    }

    public String getParticipantes() {
        return participantes;
    }
    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIndoor() {
        return indoor;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }


    @Override
    public String toString() {

        String result = "";
        result = result + "id:"+id;
        result = result + " indoor:"+indoor;
        result = result + " autor:"+ autor;
        result = result + " perfil:"+perfil;
        result = result + " titulo:"+titulo;
        result = result + " descricao:"+descricao;
        result = result + " inicio:"+inicio;
        result = result + " fim:"+fim;
        result = result + " participantes:"+participantes;
        return result;
    }
}
