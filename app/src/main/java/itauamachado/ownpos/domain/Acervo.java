package itauamachado.ownpos.domain;

/**
 * Created by Itauá on 21/08/2015.
 */
public class Acervo {

    private String _senac;
    private String dstaq;
    private String chamd;
    private String aPrin;
    private String aArtg;
    private String aScdr;
    private String pbcao;
    private String fsico;
    private String rsumo;
    private String notas;
    private String assun;
    private String url;
    private int photo;
    private String pdico;
    private String serie;


    public Acervo() {
    }

    public Acervo(String dstaq, String rsumo, int photo) {
        this.dstaq = dstaq;
        this.rsumo = rsumo;
        this.photo = photo;
    }

    public String getDstaq() {
        return dstaq;
    }

    public void setDstaq(String dstaq) {
        this.dstaq = dstaq;
    }

    public String getRsumo() {
        return rsumo;
    }

    public void setRsumo(String rsumo) {
        this.rsumo = rsumo;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String get_senac() {
        return _senac;
    }

    public void set_senac(String _senac) {
        this._senac = _senac;
    }

    public String getChamd() {
        return chamd;
    }

    public void setChamd(String chamd) {
        this.chamd = chamd;
    }

    public String getaPrin() {
        return aPrin;
    }

    public void setaPrin(String aPrin) {
        this.aPrin = aPrin;
    }

    public String getaArtg() {
        return aArtg;
    }

    public void setaArtg(String aArtg) {
        this.aArtg = aArtg;
    }

    public String getaScdr() {
        return aScdr;
    }

    public void setaScdr(String aScdr) {
        this.aScdr = aScdr;
    }

    public String getPbcao() {
        return pbcao;
    }

    public void setPbcao(String pbcao) {
        this.pbcao = pbcao;
    }

    public String getFsico() {
        return fsico;
    }

    public void setFsico(String fsico) {
        this.fsico = fsico;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getAssun() {
        return assun;
    }

    public void setAssun(String assun) {
        this.assun = assun;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPdico() {
        return pdico;
    }

    public void setPdico(String pdico) {
        this.pdico = pdico;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }
}
