package itauamachado.ownpos.domain;

/**
 * Created by itauafm on 15/11/2015.
 */
public class objGcmNotification {


    private String titulo;
    private String inicio; // Data-hora
    private String fim; //Fim   Data-hora (podendo ser nulo)
    private String desc; //Descrição  Informações gerais sobre o evento
    private String autor; //Autor  Criador do evento
    private String participantes; //Participantes  Integrantes do contexto
    private String tarefa; //Modulo  Tipo de tarefa, para direcionar a Activity de detalhes corretamente dentro do aplicativo móvel
    private String itemMapa; //Local  Item de mapa indoor
    private String alerta; //Alerta  Lança ou não alerta ao usuário (boolean)


    public objGcmNotification(){

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public String getTarefa() {
        return tarefa;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public String getItemMapa() {
        return itemMapa;
    }

    public void setItemMapa(String itemMapa) {
        this.itemMapa = itemMapa;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }
}
