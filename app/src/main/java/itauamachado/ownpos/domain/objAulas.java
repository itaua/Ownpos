package itauamachado.ownpos.domain;

/**
 * Created by itauafm on 07/10/2015.
 */
public class objAulas {
    private int id;
    private int indoor;
    private String curso;
    private String prof;
    private String turma;
    private String data;
    private String hr_inicio;
    private String hr_fim;


    public objAulas() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndoor() {
        return indoor;
    }

    public void setIndoor(int indoor) {
        this.indoor = indoor;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getData() {
        return data;
    }

    public void setData(String dt_aula) {
        this.data = dt_aula;
    }

    public String getHr_inicio() {
        return hr_inicio;
    }

    public void setHr_inicio(String hr_inicio) {
        this.hr_inicio = hr_inicio;
    }

    public String getHr_fim() {
        return hr_fim;
    }

    public void setHr_fim(String hr_fim) {
        this.hr_fim = hr_fim;
    }

    @Override
    public String toString(){

        String result = "id: "+this.id;
        result = result + ",indoor: "+ this.indoor;
        result = result + ",curso: "+ this.curso;
        result = result + ",prof: "+ this.prof;
        result = result + ",turma: "+ this.turma;
        result = result + ",dt_aula: "+ this.data;
        result = result + ",hr_inicio: "+ this.hr_inicio;
        result = result + ",hr_fim: "+ this.hr_fim;
        return result;
    }
}
