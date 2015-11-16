package itauamachado.ownpos.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import itauamachado.ownpos.extras.Util;


public class objAcervo implements Parcelable {

    private Long _id;
    private Long _senac;
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
    private String pdico;
    private String serie;
    private String adicionado;
    private boolean disponivel;

    public objAcervo(){}

    public objAcervo(long _id, long _senac, String dstaq, String chamd, String aPrin, String aArtg, String aScdr, String pbcao, String fsico, String rsumo, String notas, String assun, String url, String pdico, String serie, String adicionado) {
        this._id = _id;
        this._senac = _senac;
        this.dstaq = dstaq;
        this.chamd = chamd;
        this.aPrin = aPrin;
        this.aArtg = aArtg;
        this.aScdr = aScdr;
        this.pbcao = pbcao;
        this.fsico = fsico;
        this.rsumo = rsumo;
        this.notas = notas;
        this.assun = assun;
        this.url = url;
        this.pdico = pdico;
        this.serie = serie;
        this.adicionado = adicionado;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_senac() {
        return _senac;
    }

    public void set_senac(long _senac) {
        this._senac = _senac;
    }

    public String getDstaq() {
        return dstaq;
    }

    public void setDstaq(String dstaq) {
        this.dstaq = dstaq;
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

    public String getRsumo() {
        return rsumo;
    }

    public void setRsumo(String rsumo) {
        this.rsumo = rsumo;
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

    public String getAdicionado() {
        return adicionado;
    }

    public void setAdicionado(String adicionado) {
        this.adicionado = adicionado;
    }

    public String getUrlPdf() {
        if(url.contains(";")){
            String[] parts = url.split(";");
            if((url.trim().length()) != 0 ){
                for (String link:parts) {
                    String extensao = link.substring(link.length()-3);
                    if((extensao.equalsIgnoreCase("pdf"))) {
                        return link;
                    }
                }
            }
        }else{
            if(url.trim().length() != 0){
                String extensao = url.substring(url.length()-3);
                //Util.log(extensao);
                if(extensao.equalsIgnoreCase("pdf")){
                    return url;
                }
            }
        }

        return null;
    }

    public String getUrlPhoto() {
        if(url.contains(";")){
            String[] parts = url.split(";");
            if((url.trim().length()) != 0 ){
                for (String link:parts) {
                    String extensao = link.substring(link.length()-3);
                    if((extensao.equalsIgnoreCase("jpg"))||(extensao.equalsIgnoreCase("bmp"))||(extensao.equalsIgnoreCase("png"))) {
                        return link;
                    }
                }
            }
        }else{
            if(url.trim().length() != 0){
                String extensao = url.substring(url.length()-3);
                //Util.log(extensao);
                if((extensao.equalsIgnoreCase("jpg"))||(extensao.equalsIgnoreCase("bmp"))||(extensao.equalsIgnoreCase("png"))) {
                    return url;
                }
            }
        }

        return null;
    }

    public String getUrlLink() {
        if(url.contains(";")){
            String[] parts = url.split(";");
            if((url.trim().length()) != 0 ){
                for (String link:parts) {
                    String extensao = link.substring(link.length()-3);
                    if(!(extensao.equalsIgnoreCase("jpg"))&&
                            !(extensao.equalsIgnoreCase("bmp"))&&
                            !(extensao.equalsIgnoreCase("png"))&&
                            !(extensao.equalsIgnoreCase("pdf"))) {
                        return link;
                    }
                }
            }
        }else{
            if(url.trim().length() != 0){
                String extensao = url.substring(url.length()-3);
                //Util.log(extensao);
                if(!(extensao.equalsIgnoreCase("jpg"))&&
                        !(extensao.equalsIgnoreCase("bmp"))&&
                        !(extensao.equalsIgnoreCase("png"))&&
                        !(extensao.equalsIgnoreCase("pdf"))){
                    return url;
                }
            }
        }
        return null;
    }

    // PARCELABLE
        public objAcervo(Parcel parcel) {
            set_id(parcel.readLong());
            set_senac(parcel.readLong());
            setDstaq(parcel.readString());
            setChamd(parcel.readString());
            setaPrin(parcel.readString());
            setaArtg(parcel.readString());
            setaScdr(parcel.readString());
            setPbcao(parcel.readString());
            setFsico(parcel.readString());
            setRsumo(parcel.readString());
            setNotas(parcel.readString());
            setAssun(parcel.readString());
            setUrl(parcel.readString());
            setPdico(parcel.readString());
            setSerie(parcel.readString());
            setAdicionado(parcel.readString());
        }
        @Override
        public int describeContents() {
            return 0;
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(get_id());
            dest.writeLong(get_senac());
            dest.writeString(getDstaq());
            dest.writeString(getChamd());
            dest.writeString(getaPrin());
            dest.writeString(getaArtg());
            dest.writeString(getaScdr());
            dest.writeString(getPbcao());
            dest.writeString(getFsico());
            dest.writeString(getRsumo());
            dest.writeString(getNotas());
            dest.writeString(getAssun());
            dest.writeString(getUrl());
            dest.writeString(getPdico());
            dest.writeString(getSerie());
            dest.writeString(getAdicionado());
            dest.writeString(getUrlPhoto());
        }
        public static final Creator<objAcervo> CREATOR = new Creator<objAcervo>(){
            @Override
            public objAcervo createFromParcel(Parcel source) {
                return new objAcervo(source);
            }
            @Override
            public objAcervo[] newArray(int size) {
                return new objAcervo[size];
            }
        };
}
