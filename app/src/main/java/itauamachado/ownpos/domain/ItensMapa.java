package itauamachado.ownpos.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itauafm on 26/09/2015.
 */
public class ItensMapa {

    private String indoor;
    private String andar;
    private String width;
    private String height;
    private String build;
    private String pathw;
    private String pathh;
    private String titulo;
    private String descri;
    private String telefone;

    public ItensMapa(){
    }

    public String getAndar() {
        return andar;
    }

    public void setAndar(String andar) {
        this.andar = andar;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getPathw() {
        return pathw;
    }

    public void setPathw(String pathw) {
        this.pathw = pathw;
    }

    public String getPathh() {
        return pathh;
    }

    public void setPathh(String pathh) {
        this.pathh = pathh;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIndoor() {
        return indoor;
    }

    public void setIndoor(String indoor) {
        this.indoor = indoor;
    }


    @Override
    public String toString(){
        String result = "";
        result  = "indoor: "+indoor;
        result  = result  +" andar: "+andar;
        result  = result  +" width: "+width;
        result  = result  +" height: "+height;
        result  = result  +" build: "+build;
        result  = result  +" pathw: "+pathw;
        result  = result  +" pathh: "+pathh;
        result  = result  +" titulo: "+titulo;
        result  = result  +" descri: "+descri;
        result  = result  +" telefone: "+telefone;

        return result;
    }


    /**
    // PARCELABLE
    public ItensMapa(Parcel parcel) {

        setTitulo(parcel.readString());
        setHeight(parcel.readString());
        setAndar(parcel.readString());
        setTelefone(parcel.readString());
        setDescri(parcel.readString());
        setWidth(parcel.readString());
        setBuild(parcel.readString());
        setPathh(parcel.readString());
        setPathw(parcel.readString());

    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitulo());
        dest.writeString(getHeight());
        dest.writeString(getAndar());
        dest.writeString(getTelefone());
        dest.writeString(getDescri());
        dest.writeString(getWidth());
        dest.writeString(getBuild());
        dest.writeString(getPathh());
        dest.writeString(getPathw());
    }
    public static final Parcelable.Creator<ItensMapa> CREATOR = new Parcelable.Creator<ItensMapa>(){
        @Override
        public ItensMapa createFromParcel(Parcel source) {
            return new ItensMapa(source);
        }
        @Override
        public ItensMapa[] newArray(int size) {
            return new ItensMapa[size];
        }
    };
    **/
}
