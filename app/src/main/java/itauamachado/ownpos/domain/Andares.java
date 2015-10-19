package itauamachado.ownpos.domain;

import java.util.List;

/**
 * Created by itauafm on 26/09/2015.
 */
public class Andares {

    private String andar;
    private String titulo;
    private String descricao;
    private List<ItensMapa> itensMapa;

    private boolean temBanheiro;
    private boolean temAtendimento;
    private boolean temInfo;
    private boolean temComida;
    private boolean temRampa;
    private boolean temBiblioteca;
    private boolean temAuditorio;
    private boolean temAula;



    public Andares() {
        temBanheiro = temAtendimento = temInfo = temComida = temRampa = temBiblioteca = temAuditorio = temAula = false;
    }

    public String getAndar() {
        return andar;
    }

    public void setAndar(String andar) {
        this.andar = andar;
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

    public List<ItensMapa> getItensMapa() {
        return itensMapa;
    }

    public void setItensMapa(List<ItensMapa> listItensMapa) {

        for(ItensMapa item: listItensMapa){

            switch (item.getBuild()) {
                case "BANH":
                    setTemBanheiro(true);
                    break;
                case "INFO":
                    setTemInfo(true);
                    break;
                case "REST":
                    setTemComida(true);
                    break;
                case "ATEN":
                    setTemAtendimento(true);
                    break;
                case "AULA":
                    setTemAula(true);
                    break;
                case "AUDI":
                    setTemAuditorio(true);
                    break;
                case "RAMP":
                    setTemRampa(true);
                    break;
                case "BIBL":
                    setTemBiblioteca(true);
                    break;
            }

        }
        this.itensMapa = listItensMapa;
    }

    public boolean isTemBanheiro() {
        return temBanheiro;
    }

    public void setTemBanheiro(boolean temBanheiro) {
        this.temBanheiro = temBanheiro;
    }

    public boolean isTemAtendimento() {
        return temAtendimento;
    }

    public void setTemAtendimento(boolean temAtendimento) {
        this.temAtendimento = temAtendimento;
    }

    public boolean isTemInfo() {
        return temInfo;
    }

    public void setTemInfo(boolean temInfo) {
        this.temInfo = temInfo;
    }

    public boolean isTemComida() {
        return temComida;
    }

    public void setTemComida(boolean temComida) {
        this.temComida = temComida;
    }

    public boolean isTemRampa() {
        return temRampa;
    }

    public void setTemRampa(boolean temRampa) {
        this.temRampa = temRampa;
    }

    public boolean isTemBiblioteca() {
        return temBiblioteca;
    }

    public void setTemBiblioteca(boolean temBiblioteca) {
        this.temBiblioteca = temBiblioteca;
    }

    public boolean isTemAuditorio() {
        return temAuditorio;
    }

    public void setTemAuditorio(boolean temAuditorio) {
        this.temAuditorio = temAuditorio;
    }

    public boolean isTemAula() {
        return temAula;
    }

    public void setTemAula(boolean temAula) {
        this.temAula = temAula;
    }
}
